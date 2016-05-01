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

    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 720;

    /**
     * Assume a fixed but similar frame rate to the one used by the monitor.
     * This has to be fixed because otherwise we cannot guarantee the ship will go to the exact same length in every frame.
     * TODO: Calculate this according to the system frame rate
     */
    public static final float FRAME_RATE = 1 / 60f;

    public static final String PREFERENCES = "rocketgame.prefs";

    // Box2D constants recommended by the manual
    public static final int VELOCITY_ITERATIONS = 8;
    public static final int POSITION_ITERATIONS = 3;
}
