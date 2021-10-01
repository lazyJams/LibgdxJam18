package net.lazyio.astral.particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IParticle {

    boolean isDead();

    void tick(float delta);

    void render(SpriteBatch batch, float delta);

    void dispose();
}
