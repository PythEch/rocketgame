package com.rocketfool.rocketgame.view;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerDesktop;
import com.rocketfool.rocketgame.external.RocketGame;
import com.rocketfool.rocketgame.model.Level;
import com.rocketfool.rocketgame.model.Popup;
import com.rocketfool.rocketgame.util.GamePreferences;

import java.io.FileNotFoundException;

/**
 * Created by pythech on 29/04/16.
 */
public class CutsceneScreen implements Screen {
    private VideoPlayerDesktop videoPlayer;
    private RocketGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private Popup popup;
    private PopupView popupView;
    private FileHandle videoHandle;
    private Level level;
    private String welcomeText;
    private Screen cutscene;
    private OrthographicCamera camera;

    public CutsceneScreen(RocketGame game, SpriteBatch batch, BitmapFont font, FileHandle fileHandle, Level level, String welcomeText) {
        this.game = game;
        this.batch = batch;
        this.font = font;
        this.videoHandle = fileHandle;
        this.level = level;
        this.welcomeText = welcomeText;
    }

    public CutsceneScreen(RocketGame game, SpriteBatch batch, BitmapFont font, FileHandle fileHandle, String welcomeText, Screen cutscene) {
        this.game = game;
        this.batch = batch;
        this.font = font;
        this.videoHandle = fileHandle;
        this.cutscene = cutscene;
        this.welcomeText = welcomeText;
    }

    @Override
    public void show() {
        FitViewport viewport = new FitViewport(1280, 720);
        videoPlayer = new VideoPlayerDesktop(viewport);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        popup = new Popup();
        popupView = new PopupView(popup, camera);

        try {
            videoPlayer.play(videoHandle);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Timer timer = new Timer();

        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                popup.setText(welcomeText);
            }
        }, 0.4f);

        timer.start();

        if(GamePreferences.getInstance().isFullscreen())
        {
            videoPlayer.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        else {
            videoPlayer.resize(1280, 720);
        }
        videoPlayer.setVolume(GamePreferences.getInstance().getMasterVolume());
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (!videoPlayer.render()) { // As soon as the video is finished, we start level2.
            if (cutscene == null && level != null) {
                game.setScreen(new GameScreen(level, game, batch, font));
            } else if (cutscene != null && level == null) {
                game.setScreen(cutscene);
            }
        }

        popupView.update(v);

        batch.setProjectionMatrix(camera.combined);
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
