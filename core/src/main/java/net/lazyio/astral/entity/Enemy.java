package net.lazyio.astral.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.Response;
import net.lazyio.astral.Assets;
import net.lazyio.astral.ai.Brain;
import net.lazyio.astral.ai.tasks.IdleTask;
import net.lazyio.astral.ai.tasks.MoveRandomlyTask;
import net.lazyio.astral.entity.behaviors.IRender;
import net.lazyio.astral.entity.behaviors.ITick;
import net.lazyio.astral.util.GameCollisions;

import static net.lazyio.astral.util.gdx.createAnimation;

public class Enemy extends Entity implements ITick, IRender {

    public final Animation<TextureRegion> animation;
    public float stateTime;

    public Vector2 spawnPoint = new Vector2();

    public Brain brain;

    public Enemy(Vector2 pos) {
        super(pos, new Vector2(8f, 8f), EntityTags.ENEMY);
        bbOff.set(4f, 0f);

        this.brain = new Brain();
        this.brain.addTask(new IdleTask(this, MathUtils.random(2f)));
        this.brain.addTask(new MoveRandomlyTask(this, MathUtils.random(1f, 3f)));

        this.animation = createAnimation(Assets.enemy.get(), 1, 3, .25f, Animation.PlayMode.LOOP);
    }

    @Override
    public void tick(float delta) {

        this.brain.update(delta, false);

        this.vel.set(this.vel.x * 10f * delta, -200f * delta);

        Response.Result move = this.world.move(this.item, this.itemPos.x + this.vel.x, this.itemPos.y + this.vel.y, GameCollisions.enemyFilter);

        if (!move.projectedCollisions.isEmpty()) {
            for (int i = 0; i < move.projectedCollisions.size(); i++) {
                Collision collision = move.projectedCollisions.get(i);
                Entity entity = (Entity) collision.other.userData;

                if (entity.tag.equals(EntityTags.SPIKES)) {
                    this.respawn();
                    return;
                }
            }
        }

        if (move.goalX < 0)
            move.goalX = 0f;

        // FIXME: 01/10/2021
        
        this.itemPos.set(move.goalX, move.goalY);
        this.pos.set(move.goalX - bbOff.x, move.goalY - bbOff.y);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        this.stateTime += delta;
        batch.draw(this.animation.getKeyFrame(stateTime), this.pos.x, this.pos.y);
    }

    public void setSpawnPoint(float x, float y) {
        this.spawnPoint.set(x, y);
    }

    private void respawn() {
        this.pos.set(this.spawnPoint);
    }
}
