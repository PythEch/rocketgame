package com.rocketfool.rocketgame.external;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rocketfool.rocketgame.view.GameScreen;
import com.rocketfool.rocketgame.view.MainMenuScreen;
import com.rocketfool.rocketgame.view.Splash.SplashScreen;
import static com.rocketfool.rocketgame.util.Constants.QUICK_LOAD;

/**
 * Prepares the game environment. It is run once.
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

        if ( !QUICK_LOAD )
            this.setScreen(new SplashScreen(this, batch, font));
        else
            this.setScreen(new GameScreen(batch, font));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
    //endregion
}
