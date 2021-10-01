package net.lazyio.astral.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import net.lazyio.astral.Assets;
import net.lazyio.astral.Astral;

import static com.badlogic.gdx.Input.Keys.SPACE;
import static net.lazyio.astral.util.gdx.*;

public class StoryScreen extends ScreenAdapter {

    private Stage stage;

    private TypingLabel storyActor;

    private boolean storyEnded = false;

    @Override
    public void show() {
        this.stage = new Stage();

        Table table = new Table(Assets.defaultSkin.get());

        this.storyActor = new TypingLabel("", Assets.defaultSkin.get());
        this.storyActor.setText(
                "A man that born with a singular power.\n " +
                        "Gravity is zero when he is around.\n " +
                        "After some time he learned how to control it.\n " +
                        "Now he can use for a little time."
        );

        table.add(this.storyActor);
        table.pack();

        table.setBounds(screenWidth() / 2f - 100f, screenHeight() / 2f - 100f, 200f, 200f);

        Label skipLabel = new Label("[SPACE] Skip", Assets.defaultSkin.get());
        skipLabel.setPosition(10f, 10f);

        this.stage.addActor(skipLabel);

        this.stage.addActor(table);

        setInput(this.stage);
    }

    @Override
    public void render(float delta) {
        clear();

        if (isKeyPressed(SPACE)) this.storyActor.skipToTheEnd();

        if (this.storyActor.hasEnded() && !this.storyEnded) {
            this.stage.addAction(Actions.fadeOut(1.8f));
            doAfterTime(2f, () -> Astral.INST.setScreen(new GameScreen()));
            this.storyEnded = true;
        }

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
