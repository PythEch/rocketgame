package com.rocketfool.rocketgame.model.level.trigger;

import com.badlogic.gdx.math.Vector2;
import com.rocketfool.rocketgame.model.entity.SolidObject;
import com.rocketfool.rocketgame.model.entity.Map;

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
