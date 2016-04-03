package com.rocketfool.rocketgame.model.level.trigger;

import com.rocketfool.rocketgame.model.entity.Player;

/**
 * Created by pythech on 02/04/16.
 */
public abstract class FuelDepletionTrigger implements Trigger {
    private Player player;

    public FuelDepletionTrigger(Player player) {
        this.player = player;
    }

    @Override
    public final boolean isTriggered() {
        return player.getFuel() <= 0;
    }
}
