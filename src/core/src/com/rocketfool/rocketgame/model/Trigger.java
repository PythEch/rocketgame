package com.rocketfool.rocketgame.model;

/**
 * Triggers are like listeners; they check for certains events/changes and perform actions.
 */
public abstract class Trigger {
    protected boolean runOnce = false;
    protected boolean isTriggeredBefore = false;

    public boolean isTriggered() {
        if (runOnce && isTriggeredBefore) {
            return false;
        }

        boolean result = isTriggeredInternal();
        if (result) {
            isTriggeredBefore = true;
        }
        return result;
    }

    protected abstract boolean isTriggeredInternal();
    public abstract void triggerPerformed();

    public boolean isRunOnce() {
        return runOnce;
    }

    public boolean isTriggeredBefore() {
        return isTriggeredBefore;
    }
}
