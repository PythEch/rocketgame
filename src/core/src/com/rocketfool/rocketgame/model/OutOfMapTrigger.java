package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by pythech on 02/04/16.
 */
public abstract class OutOfMapTrigger implements Trigger {
    private Map map;
    private SolidObject target;

    public OutOfMapTrigger(Map map, SolidObject target) {
        this.map = map;
        this.target = target;
    }

    @Override
    public final boolean isTriggered() {
        Vector2 pos = target.getBody().getPosition();
        int width = map.getWidth();
        int height = map.getHeight();

        return pos.x < 0 || pos.y < 0 || pos.x > width || pos.y > height;
    }
}
