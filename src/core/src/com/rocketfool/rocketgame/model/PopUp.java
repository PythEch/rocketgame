package com.rocketfool.rocketgame.model;

/**
 * The popup that pops up at the bottom left of the screen (kind of like a notification screen)
 * so that the user will recieve commands and read the story here
 */
public class Popup {
    //region Fields
    /**
     * The text that is displayed on the screen, can be multiline
     */
    private String text;
    /**
     * This is stored so the {@link com.rocketfool.rocketgame.view.PopupView} will delay its close animation.
     */
    private String lastText;
    /**
     * Title of the popup
     * TODO: implement
     */
    private String title;
    /**
     * This is used to indicate when to pop to the view
     */
    private boolean propertyChanged;
    //endregion

    //region Constructors
    public Popup(String title, String text) {
        this.text = text;
        this.title = title;
        this.lastText = text;
        propertyChanged = false;
    }

    public Popup(String text) {
        this("", text);
    }

    public Popup() {
        this("", "");
    }
    //endregion

    //region Getters & Setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text + "\n\n" + this.text;
        this.lastText = text;
        // Make the popup view show itself
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
    //endregion
}