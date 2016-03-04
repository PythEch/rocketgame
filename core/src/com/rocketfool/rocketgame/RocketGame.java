package com.rocketfool.rocketgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.*;

public class RocketGame extends ApplicationAdapter {


    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Texture backgroundImage;

    private Player player;

    @Override
    public void create() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        batch = new SpriteBatch();

        backgroundImage = new Texture("Backgrounds/darkPurple.png");
        // makes background repeatable
        backgroundImage.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);

        FileHandle fh = new FileHandle("PNG/playerShip2_orange.png");
        player = new Player(new Texture(fh, true));
    }

    @Override
    public void render() {
        draw();

        player.move();

        // Make the camera focus on the player
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
    }

    private void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen.
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Drawing goes here!

        // FIXME: This looks buggy, watch out
        // Draw the background according to the camera position
        batch.draw(
                backgroundImage,
                camera.position.x - WIDTH / 2,
                camera.position.y - HEIGHT / 2,
                (int)camera.position.x,
                -(int)camera.position.y,
                WIDTH,
                HEIGHT
        );
        player.draw(batch);

        batch.end();
    }
}
