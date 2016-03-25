package com.rocketfool.rocketgame.util;

public final class Constants {
    /**
     * Stands for Pixels Per Meter, used for Box2D as
     * Box2D uses Meters for its internal calculation instead of Pixels.
     */
    public static final float PPM = 16;

    //region PPM Aliases
    public static final float toPixel = PPM;
    public static final float toMeter = 1f / PPM;

    public static final float fromMeterToPixel = toPixel;
    public static final float fromPixelToMeter = toMeter;

    public static final float toLibGDX = toPixel;
    public static final float toBox2D = toMeter;

    public static final float fromBox2DtoLibGDX = toPixel;
    public static final float fromLibGDXtoBox2D = toMeter;
    //endregion

    /** Used to indicate whether DEBUG features should be enabled. */
    public static final boolean DEBUG = true;
}
