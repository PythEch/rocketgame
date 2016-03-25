package com.rocketfool.rocketgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.rocketfool.rocketgame.screen.GameScreen;
import com.rocketfool.rocketgame.screen.MainMenuScreen;

/**
 * Created by pythech on 25/03/16.
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

        GameScreen screen = GameScreen.getInstance();
        screen.init(batch, font);
        this.setScreen(screen);
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
