package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Causes the out-of-map endgame scenario when triggered.
 */
public abstract class OutOfMapTrigger implements Trigger {
    //region Fields
    private Map map;
    private SolidObject target;
    //endregion

    //region Constructor
    public OutOfMapTrigger(Map map, SolidObject target) {
        this.map = map;
        this.target = target;
    }
    //endregion

    //region Methods
    @Override
    public final boolean isTriggered() {
        Vector2 pos = target.getBody().getPosition();
        int width = map.getWidth();
        int height = map.getHeight();

        return pos.x < 0 || pos.y < 0 || pos.x > width || pos.y > height;
    }
    //endregion
}
