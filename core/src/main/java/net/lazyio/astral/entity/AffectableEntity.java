package net.lazyio.astral.entity;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import net.lazyio.astral.Assets;
import net.lazyio.astral.Astral;
import net.lazyio.astral.entity.behaviors.IAffectedByPower;
import net.lazyio.astral.entity.behaviors.IRender;
import net.lazyio.astral.event.events.ShakeCamera;
import net.lazyio.astral.util.TweenUtils;

public class AffectableEntity extends Entity implements IRender, IAffectedByPower {

    private static final int POS_Y = 0;

    private boolean done = false;
    private final float targetPos;

    public AffectableEntity(Vector2 pos, Vector2 size) {
        super(pos, size, EntityTags.AFFECT);

        this.targetPos = this.pos.y + this.size.y;

        TweenUtils.createAndRegAccessor(
                AffectableEntity.class,
                (affectableEntity, tweenType, returnValues) -> {
                    if (tweenType == POS_Y) {
                        returnValues[0] = affectableEntity.pos.y;
                        return 1;
                    }
                    return 0;
                },
                (affectableEntity, tweenType, newValues) -> {
                    if (tweenType == POS_Y) affectableEntity.pos.y = newValues[0];
                }
        );
    }

    @Override
    public void onEffect() {
        if (!this.done) {
            Tween.to(this, POS_Y, 2.3f)
                    .target(this.targetPos)
                    .ease(TweenEquations.easeOutSine)
                    .setCallbackTriggers(TweenCallback.COMPLETE)
                    .setCallback((type, source) -> {
                        done = true;
                        Astral.INST.evtManager.sendEvent(new ShakeCamera.Stop());
                    })
                    .start(Astral.INST.tweenMgr);
            Astral.INST.evtManager.sendEvent(new ShakeCamera.Start());
            this.world.update(this.item, this.pos.x, this.targetPos);
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        batch.draw(Assets.tutWall.get(), this.pos.x, this.pos.y);
    }
}
