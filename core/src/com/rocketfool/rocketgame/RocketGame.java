package com.rocketfool.rocketgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RocketGame extends ApplicationAdapter {
    public static final String VERSION = "0.1";
    public static final String TITLE = "Rocket Game";

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Texture playerImage;
    private Texture backgroundImage;

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerImage = new Texture("PNG/playerShip2_orange.png");
        backgroundImage = new Texture("Backgrounds/darkPurple.png");
        // makes background repeatable
        backgroundImage.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen.
        batch.begin();
        // Drawing goes here!

        // repeat background
        batch.draw(backgroundImage, 0, 0, 0, 0, WIDTH, HEIGHT);
        // scale spaceship by 0.5
        batch.draw(playerImage, WIDTH / 2, HEIGHT / 2, playerImage.getWidth() / 2, playerImage.getHeight() / 2);

        batch.end();
    }
}
