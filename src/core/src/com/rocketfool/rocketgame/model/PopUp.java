package com.rocketfool.rocketgame.model;

/**
 * Created by Omer on 25/04/2016.
 */
public class PopUp {

    //properties
    private String text;
    private String lastText;
    private String title;
    private boolean propertyChanged;

    //constructors
    public PopUp(String title, String text) {
        this.text = text;
        this.title = title;
        this.lastText = text;
        propertyChanged = false;
    }

    public PopUp(String text) {
        this("", text);
    }

    public PopUp() {
        this("", "");
    }

    //basic getter setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text + "\n\n" + this.text;
        this.lastText = text;
        propertyChanged = true;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPropertyChanged() {
        return propertyChanged;
    }

    public void setPropertyChanged(boolean propertyChanged) {
        this.propertyChanged = propertyChanged;
    }

    public String getLastText() {
        return lastText;
    }
}