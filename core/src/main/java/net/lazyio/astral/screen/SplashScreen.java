package net.lazyio.astral.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import net.lazyio.astral.Assets;
import net.lazyio.astral.Astral;

import static net.lazyio.astral.util.gdx.*;

public class SplashScreen extends ScreenAdapter {

    private Stage stage;

    @Override
    public void show() {
        this.stage = new Stage();

        Image logo = new Image(Assets.logo.get());
        logo.setBounds(screenWidth() / 2f - (32f * 8f) / 2f, screenHeight() / 2f - (32f * 8f) / 2f, 32f * 8f, 32f * 8f);

        this.stage.addActor(logo);
        this.stage.addAction(Actions.sequence(Actions.fadeIn(1f), Actions.fadeOut(1f)));

        doAfterTime(2.5f, () -> Astral.INST.setScreen(new MenuScreen()));
        setInput(this.stage);
    }

    @Override
    public void render(float delta) {
        clear();

        this.stage.act(delta);
        this.stage.draw();
    }

    @Override
    public void hide() {
        clearInput();
    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }
}