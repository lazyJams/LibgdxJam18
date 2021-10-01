package net.lazyio.astral;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import net.lazyio.astral.event.EventManager;
import net.lazyio.astral.screen.GameScreen;

import static net.lazyio.astral.util.gdx.dt;
import static net.lazyio.astral.util.gdx.fps;

public class Astral extends Game {

    public static Astral INST;

    public TweenManager tweenMgr;
    public EventManager evtManager;

    public boolean debug;

    public Astral(boolean debug) {
        this.debug = debug;
    }

    @Override
    public void create() {
        INST = this;

        this.tweenMgr = new TweenManager();
        this.evtManager = new EventManager();

        Assets.init();

        this.setScreen(new GameScreen());
    }

    @Override
    public void render() {
        super.render();
        Gdx.graphics.setTitle("FPS: " + fps() + " | DT: " + dt());
    }

    @Override
    public void dispose() {
        super.dispose();
        Assets.dispose();
    }
}