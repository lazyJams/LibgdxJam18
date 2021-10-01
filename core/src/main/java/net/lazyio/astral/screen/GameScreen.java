package net.lazyio.astral.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.gempukku.libgdx.lib.camera2d.FocusCameraController;
import com.gempukku.libgdx.lib.camera2d.constraint.LockedToCameraConstraint;
import com.gempukku.libgdx.lib.camera2d.constraint.SceneCameraConstraint;
import com.gempukku.libgdx.lib.camera2d.focus.EntityFocus;
import net.lazyio.astral.Assets;
import net.lazyio.astral.Astral;
import net.lazyio.astral.GameManager;
import net.lazyio.astral.event.Event;
import net.lazyio.astral.event.EventManager;
import net.lazyio.astral.event.IEventListener;
import net.lazyio.astral.event.events.ShakeCamera;
import net.lazyio.astral.particle.ParticleSystem;
import net.lazyio.astral.util.CameraShakeConstraint;
import net.lazyio.astral.util.TaskCompletion;
import net.lazyio.astral.util.fixes.FixedTmxLoader;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.Arrays;

import static com.badlogic.gdx.Input.Keys.*;
import static net.lazyio.astral.util.gdx.*;

public class GameScreen extends ScreenAdapter implements IEventListener {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Stage stage;

    private FocusCameraController cameraController;
    private final CameraShakeConstraint shakeConstraint = new CameraShakeConstraint();

    private GameManager manager;

    private ShapeDrawer shapeDrawer;

    private void addEventListeners() {
        EventManager evtManager = Astral.INST.evtManager;
        evtManager.addListener(ShakeCamera.Start.class, this);
        evtManager.addListener(ShakeCamera.Stop.class, this);
    }

    @Override
    public void show() {
        this.addEventListeners();

        this.batch = new SpriteBatch();
        this.camera = createOrthoCam(500f);
        this.stage = new Stage();

        Label controls = new Label("", Assets.defaultSkin.get());
        controls.setText("Use the key [S] to activate your power.\n" +
                "[A] and [D] to move.\n" +
                "[SPACE] to jump.");
        controls.setPosition(10f, 410f);

        this.stage.addActor(controls);

        this.shapeDrawer = new ShapeDrawer(batch, new TextureRegion(Assets.shapeDrawer.get()));

        TiledMap map = new FixedTmxLoader().load("map2.tmx");
        this.mapRenderer = new OrthogonalTiledMapRenderer(map);

        this.manager = new GameManager(16);
        this.manager.createEntitiesFromMapObjects(map.getLayers().get("Objects").getObjects());

        this.cameraController = new FocusCameraController(
                camera,
                new EntityFocus(position -> manager.player.pos),
                new LockedToCameraConstraint(new Vector2(0.5f, .5f)),
                new SceneCameraConstraint(new Rectangle(0f, 0f, 33f * 16f, 27f * 16f))
        );
    }

    TaskCompletion completion = new TaskCompletion(
            Arrays.asList(
                    () -> {
                        return isKeyPressed(A);
                    },
                    () -> {
                        return isKeyPressed(V);
                    },
                    () -> {
                        return isKeyPressed(B);
                    }
            )
    );

    private void update(float dt) {
        this.manager.tick(dt);
        ParticleSystem.tick(dt);
        Astral.INST.tweenMgr.update(dt);
        this.cameraController.update(dt);
        completion.update();
        System.out.println(completion.isDone);
    }

    @Override
    public void render(float delta) {
        this.update(delta);
        clear();

        this.mapRenderer.setView(this.camera);
        this.batch.setColor(Color.PURPLE);
        this.mapRenderer.render();
        this.batch.setColor(Color.WHITE);

        this.batch.setProjectionMatrix(this.camera.combined);
        this.batch.begin();
        this.manager.render(this.batch, delta);
        ParticleSystem.render(this.batch, delta);
        if (isKeyPressed(L)) this.drawDebugLines();
        this.batch.end();

        this.stage.act(delta);
        this.stage.draw();
    }

    private void drawDebugLines() {
        this.shapeDrawer.setColor(Color.GREEN);
        this.manager.world.getRects().forEach(rect -> this.shapeDrawer.rectangle(rect.x, rect.y, rect.w, rect.h));
        if (!this.manager.player.canUsePower) {
            this.shapeDrawer.setColor(Color.PINK);
            this.shapeDrawer.rectangle(this.manager.player.effectRect());
        }
    }

    @Override
    public void resize(int width, int height) {
        updateCamera(this.camera, width, height, 500f);
        this.stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        this.batch.dispose();
        ParticleSystem.dispose();
        this.mapRenderer.dispose();
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof ShakeCamera.Start) {
            this.cameraController.addCameraConstraint(shakeConstraint);
        } else if (event instanceof ShakeCamera.Stop) {
            this.cameraController.removeCameraConstraint(shakeConstraint);
        }
    }
}
