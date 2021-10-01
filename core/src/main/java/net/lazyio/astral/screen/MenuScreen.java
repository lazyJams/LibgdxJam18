package net.lazyio.astral.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import net.lazyio.astral.Assets;
import net.lazyio.astral.Astral;

import static net.lazyio.astral.util.gdx.*;

public class MenuScreen extends ScreenAdapter {

    private Stage stage;

    @Override
    public void show() {
        this.stage = new Stage();

        Table table = new Table(Assets.defaultSkin.get());
        table.setPosition(screenWidth() / 2f, screenHeight() / 2f);

        TextButton textButton = new TextButton("Play", Assets.defaultSkin.get());
        textButton.setSize(100f, 50f);
        textButton.addListener(onClick((e, x, y) -> Astral.INST.setScreen(new StoryScreen())));
        table.add(textButton).size(200f, 75f);

        Label artist = new Label("Art by: am-projects", Assets.defaultSkin.get());
        artist.setPosition(10f, 30f);

        Label artLink = new Label("[CLICK ME] https://am-projects.itch.io/1-bit-16x16-2d-platformer-starter-tileset", Assets.defaultSkin.get());
        artLink.setPosition(10f, 5f);
        artLink.addListener(onClick((e, x, y) -> openURI("https://am-projects.itch.io/1-bit-16x16-2d-platformer-starter-tileset")));

        this.stage.addActor(table);
        this.stage.addActor(artist);
        this.stage.addActor(artLink);

        setInput(stage);
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
