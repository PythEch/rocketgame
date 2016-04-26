package com.rocketfool.rocketgame.model;

/**
 * Triggered when fuel is depleted (not modeled yet).
 */
public abstract class FuelDepletionTrigger implements Trigger {
    //region Fields
    private Playable playable;
    //endregion

    //region Constructor
    public FuelDepletionTrigger(Playable playable) {
        this.playable = playable;
    }
    //endregion

    //region Methods
    @Override
    public final boolean isTriggered() {
        return playable.getFuelLeft() <= 0;
    }
    //endregion
}
