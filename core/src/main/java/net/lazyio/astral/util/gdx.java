package net.lazyio.astral.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import net.lazyio.astral.Assets;
import net.lazyio.astral.util.function.VoidFunc;
import net.lazyio.astral.util.function.VoidTFunc;

public class gdx {

    public static void setInput(InputProcessor processor) {
        Gdx.input.setInputProcessor(processor);
    }

    public static void clearInput() {
        Gdx.input.setInputProcessor(null);
    }

    public static int screenWidth() {
        return Gdx.graphics.getWidth();
    }

    public static int screenHeight() {
        return Gdx.graphics.getHeight();
    }

    public static void clear() {
        ScreenUtils.clear(Color.BLACK);
    }

    public static void openURI(String uri) {
        Gdx.net.openURI(uri);
    }

    public static void doAfterTime(float seconds, VoidFunc runCallback) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                runCallback.apply();
            }
        }, seconds);
    }

    public static ClickListener onClick(VoidTFunc<InputEvent, Float, Float> clickCallback) {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickCallback.apply(event, x, y);
            }
        };
    }

    public static OrthographicCamera createOrthoCam(float xUnits) {
        OrthographicCamera camera = new OrthographicCamera(xUnits, xUnits * screenHeight() / screenWidth());
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
        return camera;
    }

    public static void updateCamera(OrthographicCamera camera, int w, int h, float xUnits) {
        camera.viewportWidth = xUnits;
        camera.viewportHeight = xUnits * h / w;
        camera.update();
    }

    public static boolean isKeyPressed(int key) {
        return Gdx.input.isKeyPressed(key);
    }

    public static boolean keyJustPressed(int key) {
        return Gdx.input.isKeyJustPressed(key);
    }

    public static void biKeyPressedOr(int keyA, VoidFunc keyAFunc, int keyB, VoidFunc keyBFunc, VoidFunc orFunc) {
        if (Gdx.input.isKeyPressed(keyA)) keyAFunc.apply();
        else if (Gdx.input.isKeyPressed(keyB)) keyBFunc.apply();
        else orFunc.apply();
    }

    public static Animation<TextureRegion> createAnimation(Texture texture, int numCols, int numRows, float frameDur, Animation.PlayMode playMode) {
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / numCols, texture.getHeight() / numRows);
        Array<TextureRegion> keys = new Array<>(numCols * numRows);

        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                keys.add(tmp[j][i]);
            }
        }

        return new Animation<>(frameDur, keys, playMode);
    }


    public static int fps(){
        return Gdx.graphics.getFramesPerSecond();
    }

    public static float dt(){
        return Gdx.graphics.getDeltaTime();
    }
}
