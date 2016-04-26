package com.rocketfool.rocketgame.model;

/**
 * Created by Omer on 25/04/2016.
 */
public class PopUp {

    private String text;
    private String title;

    //constructors

    public PopUp(String title, String text) {
        this.text = text;
        this.title = title;
    }

    public PopUp(String text) {
        this.text = text;
    }

    public PopUp() {

    }

    //basic getter setters
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

}