package net.lazyio.astral.particle;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class TextParticle implements IParticle {

    private final BitmapFont font = new BitmapFont();

    private final Vector2 pos;
    private final String text;
    private float alpha = 1f;

    public TextParticle(Vector2 pos, String text) {
        this.pos = pos;
        this.text = text;
        this.font.getData().scale(-.3f);
    }

    @Override
    public boolean isDead() {
        return this.alpha < 0f;
    }

    @Override
    public void tick(float delta) {
        if (!this.isDead()) {
            this.alpha -= .01f;
            this.pos.x += MathUtils.random(-3f, 3f);
            this.pos.y += 2.5f;
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        if (!this.isDead()) {
            this.font.setColor(0f, 0f, 0f, this.alpha);
            this.font.draw(batch, this.text, this.pos.x, this.pos.y);
            this.font.setColor(1f, 1f, 1f, this.alpha);
            this.font.draw(batch, this.text, this.pos.x + .5f, this.pos.y + .5f);
        }
    }

    @Override
    public void dispose() {
        this.font.dispose();
    }
}
