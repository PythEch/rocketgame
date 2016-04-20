package com.rocketfool.rocketgame.util;
/**
 * List of global constants for the game.
 */
public final class Constants {
    /**
     * Stands for Pixels Per Meter, used for Box2D as
     * Box2D uses Meters for its internal calculation instead of Pixels.
     */
    public static final float PPM = 16;

    //region PPM Aliases
    public static final float toPixel = PPM;
    public static final float toMeter = 1f / PPM;
    //endregion

    /** Used to indicate whether DEBUG features should be enabled. */
    public static final boolean DEBUG = true;
}
