package com.rocketfool.rocketgame.external;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rocketfool.rocketgame.view.GameScreen;
import com.rocketfool.rocketgame.view.MainMenuScreen;
import com.rocketfool.rocketgame.view.Splash.SplashScreen;

import static com.rocketfool.rocketgame.util.Constants.QUICK_LOAD;

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
    //endregion

    //region Methods
    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        if (!QUICK_LOAD)
            this.setScreen(new MainMenuScreen(this, batch, font));
        else
            this.setScreen(new GameScreen(batch, font));
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
    //endregion
}
