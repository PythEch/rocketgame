package com.rocketfool.rocketgame.view;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerDesktop;
import com.rocketfool.rocketgame.external.RocketGame;
import com.rocketfool.rocketgame.model.LevelManager;

import java.io.FileNotFoundException;

/**
 * Created by pythech on 29/04/16.
 */
public class TakeoffScreen implements Screen {
    private VideoPlayer videoPlayer;
    private RocketGame game;
    private SpriteBatch batch;
    private BitmapFont font;

    public TakeoffScreen(RocketGame game, SpriteBatch batch, BitmapFont font) {
        this.game = game;
        this.batch = batch;
        this.font = font;
    }

    @Override
    public void show() {

        FitViewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        videoPlayer = new VideoPlayerDesktop(viewport);

        try {
            videoPlayer.play(Gdx.files.internal("Backgrounds/mainMenuScreen.webm"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        videoPlayer.resize(1280, 720);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (!videoPlayer.render()) { // As soon as the video is finished, we start the file again using the same player.
            game.setScreen(new GameScreen(LevelManager.createLevel2(), game, batch, font));
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
        videoPlayer.dispose();
    }
}
