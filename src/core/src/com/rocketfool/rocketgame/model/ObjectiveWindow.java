package com.rocketfool.rocketgame.model;

/**
 * Created by Omer on 26/04/2016.
 */
public class ObjectiveWindow {
    //region Fields
    private String text = "";
    private String title = "";
    //endregion

    //region Constructors
    public ObjectiveWindow(String title, String text) {
        this.text = text;
        this.title = title;
    }

    public ObjectiveWindow(String text) {
        this.text = text;
    }

    public ObjectiveWindow() {
    }
    //endregion

    //region Getters & Setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    //endregion
}
