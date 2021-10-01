package net.lazyio.astral.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.lib.camera2d.constraint.CameraConstraint;

public class CameraShakeConstraint implements CameraConstraint {

    public float maxDistortion = 1f;
    public float duration = 0.3f;
    public float currentDuration = 0f;

    @Override
    public void applyConstraint(Camera camera, Vector2 focus, float delta) {
        if (currentDuration < duration) {
            float currentPower = maxDistortion * ((duration - currentDuration) / duration);
            camera.position.x = camera.position.x + MathUtils.random(-1f, 1f) * currentPower;
            camera.position.y = camera.position.y + MathUtils.random(-1f, 1f) * currentPower;
            camera.update();
            currentDuration = +delta;
        }
    }
}