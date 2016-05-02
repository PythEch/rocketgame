package com.rocketfool.rocketgame.external;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rocketfool.rocketgame.view.MainMenuScreen;
import com.rocketfool.rocketgame.view.SplashScreen;

/**
 * A Game class is used to switch between Screens and share data with them.
 *
 * @see com.badlogic.gdx.Screen
 */
public class RocketGame extends Game {
    //region Fields
    private SpriteBatch batch;
    private BitmapFont font;
    //endregion

    //region Methods
    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        this.setScreen(new SplashScreen(this, batch, font));
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
    //endregion
}
