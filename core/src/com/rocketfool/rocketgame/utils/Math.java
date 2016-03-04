package com.rocketfool.rocketgame.utils;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by pythech on 04/03/16.
 */
public final class Math {
    public static float offsetX(float angle, float amount) {
        return MathUtils.sin(angle) * amount;
    }

    public static float offsetY(float angle, float amount) {
        return MathUtils.cos(angle) * amount;
    }
}
