package net.lazyio.astral.entity;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Response;
import net.lazyio.astral.Assets;
import net.lazyio.astral.Astral;
import net.lazyio.astral.entity.behaviors.IAffectedByPower;
import net.lazyio.astral.entity.behaviors.IRender;
import net.lazyio.astral.entity.behaviors.ITick;
import net.lazyio.astral.event.events.CollisionEvent;
import net.lazyio.astral.event.events.PowerUsage;
import net.lazyio.astral.util.GameCollisions;
import net.lazyio.astral.util.TweenUtils;

import static com.badlogic.gdx.Input.Keys.*;
import static net.lazyio.astral.util.gdx.*;

public class Player extends Entity implements IRender, ITick {

    private static final int VEL_X = 0;
    private static final int VEL_Y = 1;

    public final float gravity = 200f;
    public float tickGravity = gravity;
    public boolean isJumping = false;
    public final float jumpTimeMax = .25f;
    public float jumpTime = 0f;
    public float jumpSpeed = 3f;
    public float maxSpeed = 5f;
    public float acc = 2f;
    public float deceleration = 2f;
    public boolean canUsePower = true;
    public float powerTimer = -1f;
    public final float powerTime = 300f;

    public final Vector2 respawnPoint = new Vector2();

    private Collisions tmpCol = new Collisions();

    private Animation<TextureRegion> animation;
    private float stateTime = 0f;

    public Player(Vector2 pos, Vector2 size) {
        super(pos, size, EntityTags.PLAYER);
        bbOff = new Vector2(4f, 0f);

        this.animation = createAnimation(Assets.player.get(), 1, 4, .25f, Animation.PlayMode.LOOP);

        TweenUtils.createAndRegAccessor(
                Player.class,
                (player, tweenType, returnValues) -> {
                    if (tweenType == VEL_X) {
                        returnValues[0] = player.vel.x;
                        return 1;
                    }
                    return 0;
                },
                (player, tweenType, newValues) -> {
                    if (tweenType == VEL_X) {
                        player.vel.x = newValues[0];
                    }
                }
        );
    }

    @Override
    public void tick(float delta) {

        boolean jumpPressed = isKeyPressed(SPACE);
        boolean jumpJustPressed = keyJustPressed(SPACE);

        this.powerTick();

        this.vel.set(this.vel.x, this.tickGravity * delta);

        if (!jumpPressed) this.isJumping = false;
        if (jumpJustPressed) {
            this.world.project(
                    this.item,
                    this.itemPos.x + this.bbOff.x,
                    this.itemPos.y + this.bbOff.y - 1f,
                    this.size.x,
                    this.size.y,
                    this.itemPos.x + this.bbOff.x + this.size.x,
                    this.itemPos.y + this.bbOff.y + this.size.y,
                    CollisionFilter.defaultFilter, this.tmpCol
            );
            if (this.tmpCol.size() > 0) {
                this.isJumping = true;
                Assets.jumpSound.get().play();
            }
        }

        if (jumpPressed && this.isJumping && this.jumpTime < this.jumpTimeMax) {
            this.vel.y = this.jumpSpeed;
            this.jumpTime += delta;
        }

        biKeyPressedOr(
                D, () -> {
                    float per = (this.maxSpeed - this.vel.x) / this.maxSpeed;
                    Tween.to(this, VEL_X, this.acc * per)
                            .target(this.maxSpeed)
                            .ease(TweenEquations.easeOutCirc)
                            .start(Astral.INST.tweenMgr);
                },
                A, () -> {
                    float per = Math.abs((-this.maxSpeed - this.vel.x) / this.maxSpeed);
                    Tween.to(this, VEL_X, this.acc * per)
                            .target(-this.maxSpeed)
                            .ease(TweenEquations.easeOutCirc)
                            .start(Astral.INST.tweenMgr);
                },
                () -> {
                    float per = Math.abs(this.vel.x / this.maxSpeed);
                    Tween.to(this, VEL_X, this.deceleration * per)
                            .target(0f)
                            .ease(TweenEquations.easeOutCirc)
                            .start(Astral.INST.tweenMgr);
                }
        );

        Response.Result moveResult = this.world.move(this.item, this.itemPos.x + this.vel.x, this.itemPos.y + this.vel.y, GameCollisions.playerFilter);

        if (!moveResult.projectedCollisions.isEmpty()) {
            for (int i = 0; i < moveResult.projectedCollisions.size(); i++) {

                Collision collision = moveResult.projectedCollisions.get(i);
                String otherTag = ((Entity) collision.other.userData).tag;

                if (collision.normal.y != 0) {
                    this.vel.y = 0f;
                    this.jumpTime = this.jumpTimeMax;

                    if (collision.normal.y == 1) {
                        this.jumpTime = 0;
                        this.isJumping = false;
                    }
                }

                switch (otherTag) {
                    case EntityTags.SPIKES:
                    case EntityTags.ENEMY:
                        Assets.hurtSound.get().play();
                        this.respawn();
                        return;
                    case EntityTags.GOAL:
                        Astral.INST.evtManager.sendEvent(new CollisionEvent(collision.item, collision.other));
                        break;
                }
            }
        }

        if (moveResult.goalX < 0f) {
            moveResult.goalX = 0f;
        }

        // FIXME: 01/10/2021
        this.itemPos.set(moveResult.goalX, moveResult.goalY);
        this.pos.set(moveResult.goalX - bbOff.x, moveResult.goalY - bbOff.y);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        this.stateTime += delta;
        batch.draw(this.animation.getKeyFrame(this.stateTime), this.pos.x, this.pos.y);
    }

    private void powerTick() {
        if (keyJustPressed(S)) {
            if (this.canUsePower) {
                this.powerTimer = this.powerTime;
                this.canUsePower = false;
                this.tickGravity = this.gravity;
                Astral.INST.evtManager.sendEvent(new PowerUsage.Start(this));
            } else {
                this.powerTimer = -1f;
                this.canUsePower = true;
                this.tickGravity = -this.gravity;
                Astral.INST.evtManager.sendEvent(new PowerUsage.Stop(this));
            }
        }

        if (this.powerTimer > 0) {
            this.powerTimer--;
        } else {
            this.canUsePower = true;
            this.tickGravity = -this.gravity;
        }

        if (!this.canUsePower) {
            this.world.getItems().stream()
                    .filter(item -> item.userData instanceof IAffectedByPower)
                    .forEach(item -> {
                                Entity entity = ((Entity) item.userData);
                                Rectangle itemRect = new Rectangle(entity.pos.x, entity.pos.y, entity.size.x, entity.size.y);
                                if (itemRect.overlaps(this.effectRect())) {
                                    ((IAffectedByPower) item.userData).onEffect();
                                }
                            }
                    );
        }
    }

    // FIXME: 01/10/2021

    public void respawn() {
        this.vel.setZero();
        this.pos.set(this.respawnPoint.x - bbOff.x, this.respawnPoint.y - bbOff.y);
        this.itemPos.set(this.respawnPoint);
        this.world.update(this.item, this.respawnPoint.x, this.respawnPoint.y);
        this.canUsePower = true;
        this.powerTimer = -1;
        System.out.println("called");
    }

    public void setRespawnPoint(float x, float y) {
        this.respawnPoint.set(x, y);
    }

    public Rectangle effectRect() {
        return new Rectangle(
                this.pos.x + (this.size.x / 2) - 50f,
                this.pos.y + (this.size.y / 2) - 50f,
                100f,
                100f
        );
    }
}
