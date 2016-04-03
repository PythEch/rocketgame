package com.rocketfool.rocketgame.model;

/**
 * Created by pythech on 02/04/16.
 */
public abstract class FuelDepletionTrigger implements Trigger {
    private Playable playable;

    public FuelDepletionTrigger(Playable playable) {
        this.playable = playable;
    }

    @Override
    public final boolean isTriggered() {
        return playable.getFuel() <= 0;
    }
}
