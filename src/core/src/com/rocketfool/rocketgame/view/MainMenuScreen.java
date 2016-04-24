package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;
import com.rocketfool.rocketgame.external.RocketGame;


import java.io.FileNotFoundException;

import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * A standard main menu screen for the game. It is a work in progress.
 */
public class MainMenuScreen implements Screen {
    private Stage stage;
    private RocketGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private VideoPlayer videoPlayer;

    public MainMenuScreen(RocketGame game, SpriteBatch batch, BitmapFont font) {
        this.game = game;
        this.batch = batch;
        this.font = font;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //Set the table as the main component of the menu screen.
        Table table = new Table();
        table.setFillParent(true);
        //table.setDebug(DEBUG);

        Skin skin = new Skin(Gdx.files.internal("Skin/uiskin.json"));
        /*TextureAtlas atlas = new TextureAtlas("Skin/uiskin.json");
        skin.addRegions(atlas);*/

        /*final CheckBoxStyle t = new CheckBoxStyle();
        t.font = mySkin.getFont("default");
        t.fontColor =new Color(0, 0, 0, 1f);
        t.disabledFontColor = new Color(0, 0, 0, 0.4f);
        t.checkboxOff = mySkin.getDrawable( "checkbox_off");
        t.checkboxOn = mySkin.getDrawable( "checkbox_on");
        mySkin.add("default", t);*/
/*
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = new BitmapFont(Gdx.files.internal("fonts/CartoonReliefWhite.fnt"));
        buttonStyle.fontColor = new Color(1, 1, 1, 1);
        buttonStyle.disabledFontColor = new Color(0, 0, 0, 0.4f);
        buttonStyle.up = skin.getDrawable("button_04");
        buttonStyle.down = skin.getDrawable("button_02");
        skin.add("default", buttonStyle);
*/
        Skin buttonStyle = skin;

        TextButton newGame = new TextButton("New Game", buttonStyle);
        TextButton options = new TextButton("Options", buttonStyle);
        TextButton exit = new TextButton("Exit", buttonStyle);

        //Align everything with regular size and spacing
        table.right().padRight(75);
        table.add(newGame).width(newGame.getPrefWidth() * 1.5f).height(newGame.getPrefHeight() * 1.5f).uniform();
        table.row().padTop(20);
        table.add(options).uniform().fill();
        table.row().padTop(20);
        table.add(exit).uniform().fill();

        newGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                videoPlayer.dispose();
                game.setScreen(new GameScreen(batch, font));
            }
        });

        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        });

        stage.addActor(table);

        videoPlayer = VideoPlayerCreator.createVideoPlayer();
        videoPlayer.resize(1280, 720);

        try {
            videoPlayer.play(Gdx.files.internal("Backgrounds/mainMenuScreen.webm"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (!videoPlayer.render()) { // As soon as the video is finished, we start the file again using the same player.
            try {
                videoPlayer.play(Gdx.files.internal("Backgrounds/mainMenuScreen.webm"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
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
}
