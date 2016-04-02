package com.rocketfool.rocketgame.level.trigger;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rocketfool.rocketgame.entity.GameObject;
import com.rocketfool.rocketgame.entity.SolidObject;
import com.rocketfool.rocketgame.screen.GameScreen;

import javax.swing.*;
import java.awt.event.MouseListener;

/**
 * Created by pythech on 02/04/16.
 */
public abstract class PositionTrigger extends Trigger {
    private float x;
    private float y;
    private float radius;
    private SolidObject target;

    public PositionTrigger(float x, float y, float radius, SolidObject target) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.target = target;
    }

    public final boolean isTriggered() {
        Vector2 pos = target.getBody().getPosition();

        return pos.dst(x, y) <= radius;
    }
}
