package net.lazyio.astral.particle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import net.lazyio.astral.Assets;
import net.lazyio.astral.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpiralParticle implements IParticle {

    private int c = 4;
    private int n = 0;

    private final Vector2 pos;
    private final Player player;
    private List<Vector2> tmp = new ArrayList<>();

    public SpiralParticle(Vector2 pos, Player player) {
        this.pos = pos;
        this.player = player;
    }

    @Override
    public boolean isDead() {
        return player.canUsePower;
    }

    @Override
    public void tick(float delta) {
        if(this.isDead()){
            n = 0;
            this.tmp.clear();
        } else {
            n++;
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        float a = n * 137.5f;
        double r = c * Math.sqrt(n);

        double x = r * Math.cos(a) + this.pos.x;
        double y = r * Math.sin(a) + this.pos.y;

        tmp.add(new Vector2((float) x, (float)y));

        for (Vector2 vector2 : tmp) {
            batch.setColor(Color.PURPLE.r, Color.PURPLE.g, Color.PURPLE.b, .5f);
            batch.draw(Assets.spiralParticle.get(), vector2.x, vector2.y);
        }
    }

    @Override
    public void dispose() {

    }
}
