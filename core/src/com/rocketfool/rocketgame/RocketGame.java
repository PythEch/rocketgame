package com.rocketfool.rocketgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.*;

public class RocketGame extends ApplicationAdapter {
    public static final String VERSION = "0.1";
    public static final String TITLE = "Rocket Game";

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Texture backgroundImage;

    private Player player;

    @Override
    public void create() {
        batch = new SpriteBatch();

        backgroundImage = new Texture("Backgrounds/darkPurple.png");
        // makes background repeatable
        backgroundImage.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);

        player = new Player(new Texture("PNG/playerShip2_orange.png"));
    }

    @Override
    public void render() {
        draw();

        player.move();
    }

    private void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen.
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Drawing goes here!

        batch.draw(backgroundImage, 0, 0, 0, 0, WIDTH, HEIGHT);
        player.draw(batch);

        batch.end();
    }
}
