package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Non-physical bodies as visual indicators for game objectives.
 */
public class Waypoint extends GameObject {
    private boolean onScreen;
    private PositionTrigger positionTrigger;
    private Level level;

    //constructors

    public Waypoint(Level level, PositionTrigger positionTrigger) {
        this.positionTrigger = positionTrigger;
        this.onScreen = false;
        this.level = level;
    }

    public Waypoint(Level level, float x, float y, float radius) {
        this(level, new PositionTrigger(x, y, radius, level.getPlayable()));
        level.triggers.add(positionTrigger);
    }

    //methods
    public Vector2 getPosition() {
        float angle = getAngle();

        return level.getPlayable().getBody().getPosition().add(new Vector2(5, 0).rotate(angle));
    }

    public float getAngle() {
        Vector2 directionVector = positionTrigger.getPosition().sub(level.getPlayable().getBody().getPosition());
        return directionVector.angle();
    }

    public boolean isOnScreen() {
        return onScreen;
    }

    public void setOnScreen(boolean onScreen) {
        this.onScreen = onScreen;
    }

    public void setPositionTrigger(PositionTrigger positionTrigger) {
        this.positionTrigger = positionTrigger;
    }

    @Override
    public void update(float deltaTime) {
        if (positionTrigger.isTriggeredBefore()) {
            setOnScreen(false);
        }
    }
}
