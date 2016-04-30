package com.rocketfool.rocketgame.view;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerDesktop;
import com.rocketfool.rocketgame.external.RocketGame;
import com.rocketfool.rocketgame.model.LevelManager;
import com.rocketfool.rocketgame.model.PopUp;

import java.io.FileNotFoundException;

/**
 * Created by pythech on 29/04/16.
 */
public class TakeoffScreen implements Screen {
    private VideoPlayer videoPlayer;
    private RocketGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private PopUp popup;
    private PopupView popupView;

    public TakeoffScreen(RocketGame game, SpriteBatch batch, BitmapFont font) {
        this.game = game;
        this.batch = batch;
        this.font = font;
    }

    @Override
    public void show() {

        FitViewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        videoPlayer = new VideoPlayerDesktop(viewport);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        popup = new PopUp();
        popupView = new PopupView(popup, camera);

        try {
            videoPlayer.play(Gdx.files.internal("Backgrounds/level2/earthTakeOff.webm"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                popup.setText("You starting a new journey. Be careful out there! ");
            }
        }, 0.4f);

        videoPlayer.resize(1280, 720);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (!videoPlayer.render()) { // As soon as the video is finished, we start level2.
            game.setScreen(new GameScreen(LevelManager.createLevel2(), game, batch, font));
        }

        popupView.update(v);

        batch.begin();
        popupView.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int i, int i1) {

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

    }
}
