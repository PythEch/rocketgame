package com.rocketfool.rocketgame.model;

/**
 * Triggers are like listeners; they check for certains events/changes and perform actions.
 */
public abstract class Trigger {
    //region Fields
    /**
     * If runOnce is true, the trigger will be obselete after the first time the trigger is fired
     */
    protected boolean runOnce;
    /**
     * Used to indicate whether trigger was triggered successfuly
     */
    protected boolean isTriggeredBefore;
    //endregion

    //region Constructors
    public Trigger(boolean runOnce) {
        this.runOnce = runOnce;
        this.isTriggeredBefore = false;
    }

    public Trigger() {
        this.runOnce = true;
        this.isTriggeredBefore = false;
    }
    //endregion

    //region Methods
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

    public boolean isRunOnce() {
        return runOnce;
    }

    public boolean isTriggeredBefore() {
        return isTriggeredBefore;
    }
    //endregion

    //region Methods that will be implemented by the child class
    protected abstract boolean isTriggeredInternal();

    public abstract void triggerAction();
    //endregion
}
