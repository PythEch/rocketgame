package com.rocketfool.rocketgame.view;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by pythech on 28/04/16.
 */
public class PopupAcessor implements TweenAccessor<PopupView> {
    public static final int Y_COORD = 0;

    @Override
    public int getValues(PopupView target, int tweenType, float[] returnValues) {
        switch (tweenType)
        {
            case Y_COORD:
                returnValues[0] = target.getyCoord();
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(PopupView target, int tweenType, float[] newValues) {
        switch (tweenType)
        {
            case Y_COORD:
                target.setyCoord(newValues[0]);
                break;
            default:
                assert false;
        }

    }
}
