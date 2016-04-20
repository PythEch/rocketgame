package com.rocketfool.rocketgame.model;

/**
 * Triggered when fuel is depleted (not modeled yet).
 */
public abstract class FuelDepletionTrigger implements Trigger {
    private Playable playable;

    public FuelDepletionTrigger(Playable playable) {
        this.playable = playable;
    }

    @Override
    public final boolean isTriggered() {
        return playable.getFuelLeft() <= 0;
    }
}
