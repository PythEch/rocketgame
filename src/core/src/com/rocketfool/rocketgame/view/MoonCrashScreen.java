package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rocketfool.rocketgame.external.RocketGame;
import com.rocketfool.rocketgame.model.LevelManager;
import com.rocketfool.rocketgame.model.Popup;

public class MoonCrashScreen implements Screen {
    private static final float METEOR_DISTANCE = 450;
    private static final Vector2 METEOR_CENTER = new Vector2(970, 400);

    //Variables
    private RocketGame game;
    private Sprite splash;
    private SpriteBatch batch;
    private BitmapFont font;
    private Popup popup;
    private PopupView popupView;
    private OrthographicCamera camera;
    private Viewport viewport;

    private float meteorAngle;
    private Vector2 meteorPosition;

    public MoonCrashScreen(RocketGame game, SpriteBatch batch, BitmapFont font) {
        this.game = game;
        this.batch = batch;
        this.font = font;
    }

    @Override
    public void show() {
        splash = new Sprite(AssetManager.SPLASH);
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.popup = new Popup();
        this.popupView = new PopupView(popup, camera);


        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                popup.setText("It is a great idea to check it out.");
                popup.setText("It looks like there is an unidentified meteor on Moon's orbit.");
            }
        }, 1f);


        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new CutsceneScreen(game, batch, font, AssetManager.LEVEL2START, LevelManager.createLevel2(), "You starting a new journey. Be careful out there!"));
            }
        }, 13f);

        meteorAngle = 150;
        meteorPosition = new Vector2(0, 0);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        popupView.update(dt);
        Texture texture = new Texture(Gdx.files.internal("Backgrounds/level2/MoonCrashBQ.png"));
        updateMeteor(dt);

        batch.begin();
        splash.draw(batch);
        batch.draw(texture, 0, 0);
        popupView.draw(batch);
        drawMeteor(batch);
        batch.end();
    }

    private void updateMeteor(float deltaTime) {
        meteorAngle -= 0.2f;
        meteorPosition = METEOR_CENTER.cpy().add(new Vector2(0, METEOR_DISTANCE).rotate(meteorAngle));
    }

    private void drawMeteor(SpriteBatch batch) {
        Texture texture = AssetManager.TOXIC_METEOR;
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        batch.draw(
                texture,
                meteorPosition.x,
                meteorPosition.y,
                texture.getWidth() / 2f,
                texture.getHeight() / 2f,
                texture.getWidth(),
                texture.getHeight(),
                0.5f,
                0.5f,
                meteorAngle * 4,
                0,
                0,
                texture.getWidth(),
                texture.getHeight(),
                false,
                false
        );
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        splash.getTexture().dispose();
    }
}