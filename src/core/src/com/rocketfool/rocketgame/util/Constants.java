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
    public static boolean DEBUG = true;
    /** Used to disable some features to reduce load time for debugging. */
    public static final boolean QUICK_LOAD = false;
    //TODO: Where can we use this other than RocketGame.java?

    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 720;
}
