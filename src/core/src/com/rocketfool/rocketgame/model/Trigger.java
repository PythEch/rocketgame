package com.rocketfool.rocketgame.model;

/**
 * Triggers are like listeners; they check for certains events/changes and perform actions.
 */
public abstract class Trigger {
    protected boolean runOnce;
    protected boolean isTriggeredBefore;

    public Trigger(boolean runOnce) {
        this.runOnce = runOnce;
        this.isTriggeredBefore = false;
    }

    public Trigger() {
        this.runOnce = true;
        this.isTriggeredBefore = false;
    }

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
