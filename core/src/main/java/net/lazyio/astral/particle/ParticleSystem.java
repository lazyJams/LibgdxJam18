package net.lazyio.astral.particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import net.lazyio.astral.Astral;
import net.lazyio.astral.entity.Entity;
import net.lazyio.astral.entity.EntityTags;
import net.lazyio.astral.event.IEventListener;
import net.lazyio.astral.event.events.CollisionEvent;
import net.lazyio.astral.event.events.PowerUsage;

import java.util.ArrayList;
import java.util.List;

public class ParticleSystem {

    private static final int MAX_PARTICLES = 250;
    private static int currentCount;
    private static final List<IParticle> PARTICLES = new ArrayList<>();

    static {
        IEventListener listener = (event) -> {
            if (event instanceof CollisionEvent) {
                CollisionEvent collisionEvent = (CollisionEvent) event;
                if(collisionEvent.other.userData.tag.equals(EntityTags.GOAL)){
                    Entity other = collisionEvent.other.userData;
                    ParticleSystem.addParticle(new TextParticle(new Vector2(other.pos.x, other.pos.y), "Win"));
                }
            } else if(event instanceof PowerUsage.Start) {
                PowerUsage.Start start = (PowerUsage.Start) event;
                ParticleSystem.addParticle(new SpiralParticle(start.player.pos, start.player));
            }
        };

        Astral.INST.evtManager.addListener(CollisionEvent.class, listener);
        Astral.INST.evtManager.addListener(PowerUsage.Start.class, listener);
    }

    public static void addParticle(IParticle particle) {
        if (currentCount < MAX_PARTICLES) {
            PARTICLES.add(particle);
            currentCount++;
        }
    }

    public static void render(SpriteBatch batch, float delta) {
        PARTICLES.forEach(p -> p.render(batch, delta));
    }

    public static void tick(float delta) {
        PARTICLES.forEach(p -> p.tick(delta));

        for (int i = PARTICLES.size() - 1; i > 0; i--) {
            IParticle particle = PARTICLES.get(i);
            if (particle.isDead()) {
                PARTICLES.remove(particle);
                currentCount--;
            }
        }
    }

    public static void dispose() {
        PARTICLES.forEach(IParticle::dispose);
    }
}
