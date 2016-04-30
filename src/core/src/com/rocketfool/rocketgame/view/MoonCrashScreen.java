package com.rocketfool.rocketgame.view;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rocketfool.rocketgame.external.RocketGame;
import com.rocketfool.rocketgame.model.PopUp;
import com.rocketfool.rocketgame.view.AssetManager;
import com.rocketfool.rocketgame.view.MainMenuScreen;
import com.rocketfool.rocketgame.view.PopupView;
import com.rocketfool.rocketgame.view.SpriteAccessor;

public class MoonCrashScreen implements Screen {

    //Variables
    private RocketGame game;
    private Sprite splash;
    private SpriteBatch batch;
    private BitmapFont font;
    private PopUp popup;
    private PopupView popupView;
    private OrthographicCamera camera;
    private Viewport viewport;

    private float meteorAngle;
    private float meteorDistance;
    private Vector2 meteorCenter;
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

        this.popup = new PopUp();
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
                System.out.println("test");
                game.setScreen(new TakeoffScreen(game,batch,font));
            }
        },13f);

        meteorAngle = 250;
        meteorCenter = new Vector2(500, 600);
        meteorDistance = 30;
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
        batch.draw(texture,0,0);
        popupView.draw(batch);
        drawMeteor(batch);
        batch.end();
    }

    private void updateMeteor(float deltaTime) {
        meteorAngle += 0.1f;
        meteorPosition = meteorCenter.add(new Vector2(0, meteorDistance).rotateRad(meteorAngle));
    }

    private void drawMeteor(SpriteBatch batch) {
        Texture texture = AssetManager.TOXIC_METEOR;
        batch.draw(
                texture,
                meteorPosition.x,
                meteorPosition.y
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