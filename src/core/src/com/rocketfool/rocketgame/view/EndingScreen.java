package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerDesktop;
import com.rocketfool.rocketgame.external.RocketGame;
import com.rocketfool.rocketgame.model.Popup;
import com.rocketfool.rocketgame.util.GamePreferences;
import com.sun.org.apache.bcel.internal.generic.POP;

import java.io.FileNotFoundException;

public class EndingScreen implements Screen {
    private VideoPlayerDesktop videoPlayer;
    private RocketGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private FileHandle video;
    private Popup popup;
    private PopupView popupView;


    public EndingScreen(RocketGame game, SpriteBatch batch, BitmapFont font) {
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


        video = AssetManager.ENDING;

        try {
            videoPlayer.play(video);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        videoPlayer.resize(1280, 720);
        videoPlayer.setVolume(GamePreferences.getInstance().getMasterVolume());

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (!videoPlayer.render()) { // As soon as the video is we play next one or we go back to main menu
               game.setScreen(new MainMenuScreen(game, batch, font));
        }
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