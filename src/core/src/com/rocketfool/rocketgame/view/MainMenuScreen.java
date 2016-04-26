package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;
import com.badlogic.gdx.video.VideoPlayerDesktop;
import com.rocketfool.rocketgame.external.RocketGame;
import com.rocketfool.rocketgame.util.Constants;


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
    private VideoPlayerDesktop videoPlayer;
    private OrthographicCamera camera;
    private Vector3 camTarget;
    private Vector3 rocketPos;
    private boolean focusRocket;
    private float elapsedTime;

    public MainMenuScreen(RocketGame game, SpriteBatch batch, BitmapFont font) {
        this.game = game;
        this.batch = batch;
        this.font = font;
    }

    @Override
    public void show() {
        focusRocket = false;
        elapsedTime = 0;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //Set the table as the main component of the menu screen.
        Table table = new Table();
        table.setFillParent(true);
        //table.setDebug(DEBUG);

        Skin skin = new Skin();
        TextureAtlas atlas = new TextureAtlas("Skin/ui-orange-pale.atlas");
        skin.addRegions(atlas);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = new BitmapFont(Gdx.files.internal("fonts/CartoonReliefWhite.fnt"));
        buttonStyle.fontColor = new Color(1, 1, 1, 1);
        buttonStyle.disabledFontColor = new Color(0, 0, 0, 0.4f);
        buttonStyle.up = skin.getDrawable("button_04");
        buttonStyle.down = skin.getDrawable("button_02");
        skin.add("default", buttonStyle);

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

        //Listeners
        //New game
        newGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                videoPlayer.dispose();
                game.setScreen(new GameScreen(batch, font));
            }
        });

        //Exit Game
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        });

        //Options
        options.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                focusRocket = true;
                camTarget = new Vector3(0, 0, 0);
                rocketPos = new Vector3(120 - 10f * (elapsedTime / 131f), -150 + 450f * (elapsedTime / 131f), 0);
                //120, -200
                //180, 180
            }
        });

        stage.addActor(table);

        //BQ video
        FitViewport viewport = new FitViewport((float)Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight());
        camera = (OrthographicCamera) viewport.getCamera();
        videoPlayer = new VideoPlayerDesktop(viewport);

        try {
            videoPlayer.play(Gdx.files.internal("Backgrounds/mainMenuScreen.webm"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        videoPlayer.resize(1280, 720);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render (float delta) {
        elapsedTime += delta;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (!videoPlayer.render()) { // As soon as the video is finished, we start the file again using the same player.
            try {
                videoPlayer.play(Gdx.files.internal("Backgrounds/mainMenuScreen.webm"));
                elapsedTime = 0;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        stage.act(delta);
        stage.draw();

        if (focusRocket) {
            camTarget = camTarget.add(rocketPos.x / 100f, rocketPos.y / 100f, 0);

            ((OrthographicCamera)stage.getCamera()).zoom -= 0.0070f;
            camera.zoom -= 0.0070f;
            System.out.println(camera.zoom);
            camera.position.set(camTarget);
            camera.update();
            stage.getCamera().update();

            if (camera.zoom <= 1 - 0.0070f * 100) {
                game.setScreen(new OptionsScreen(game,batch, font, this));
            }
        }

    }

    @Override
    public void dispose() {
        stage.dispose();
        videoPlayer.dispose();
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

    public void disposePlayer(){
        videoPlayer.dispose();
    }
}
