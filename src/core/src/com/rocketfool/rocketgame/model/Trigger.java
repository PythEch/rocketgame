package com.rocketfool.rocketgame.model;

/**
 * Triggers are like listeners; they check for certains events/changes and perform actions.
 */
public interface Trigger {
    public boolean isTriggered();
    public void triggerPerformed();
}
