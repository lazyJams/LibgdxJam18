package net.lazyio.astral.util;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import net.lazyio.astral.util.function.TFunc;
import net.lazyio.astral.util.function.VoidTFunc;

public class TweenUtils {

    public static <T> TweenAccessor<T> createAndRegAccessor(Class<T> toRegister, TFunc<T, Integer, float[], Integer> getFunc, VoidTFunc<T, Integer, float[]> setFunc) {
        TweenAccessor<T> tweenAccessor = new TweenAccessor<T>() {
            @Override
            public int getValues(T target, int tweenType, float[] returnValues) {
                return getFunc.apply(target, tweenType, returnValues);
            }

            @Override
            public void setValues(T target, int tweenType, float[] newValues) {
                setFunc.apply(target, tweenType, newValues);
            }
        };
        Tween.registerAccessor(toRegister, tweenAccessor);
        return tweenAccessor;
    }
}
