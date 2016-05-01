package com.rocketfool.rocketgame.external;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rocketfool.rocketgame.view.MainMenuScreen;

/**
 * A Game class is used to switch between Screens and share data with them.
 *
 * @see com.badlogic.gdx.Screen
 */
public class RocketGame extends Game {
    //region Fields
    private SpriteBatch batch;
    private BitmapFont font;
    private boolean isFullScreen;
    private boolean isSfx;
    //endregion

    //region Methods
    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        setSfx( true);

        this.setScreen(new MainMenuScreen(this, batch, font));

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
    //endregion

    //region Getters & Setters
    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    public boolean isSfx()
    {
        return isSfx;
    }

    public void setSfx(boolean sfx){
        isSfx = sfx;
    }
    //endregion
}
