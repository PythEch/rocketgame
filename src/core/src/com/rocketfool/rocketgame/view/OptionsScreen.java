package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;
import com.rocketfool.rocketgame.external.RocketGame;
import com.rocketfool.rocketgame.util.Constants;
import com.sun.org.apache.bcel.internal.classfile.Constant;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.FileNotFoundException;

/**
 *
 */
public class OptionsScreen implements Screen {

    //Variables
    private Stage stage;
    private SpriteBatch batch;
    private BitmapFont font;
    private VideoPlayer videoPlayer;
    private String sfxStatus;
    private String fullscreenStatus;
    private RocketGame game;
    private MainMenuScreen mainMenuScreen;

    //Constructor
    public OptionsScreen(RocketGame game,SpriteBatch batch, BitmapFont font, MainMenuScreen mainMenuScreen) {
        this.game = game;
        this.batch = batch;
        this.font = font;
        this.mainMenuScreen = mainMenuScreen;
    }

    @Override
    public void show() {
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

        sfxStatus = "Off";
        fullscreenStatus = "Off";
        final TextButton sfx = new TextButton("Toggle Sfx:  " + sfxStatus, buttonStyle);
        final TextButton fullscreen = new TextButton("Fullscreen:  " + fullscreenStatus, buttonStyle);
        final TextButton back = new TextButton("Back", buttonStyle);

        //Align everything with regular size and spacing
        table.center();
        table.add(sfx).width(sfx.getPrefWidth() * 1.2f).height(sfx.getPrefHeight() * 1.2f).uniform();
        table.row().padTop(20);
        table.add(fullscreen).uniform().fill();
        table.row().padTop(20);
        table.add(back).uniform().fill();

        //Listeners
        //Sfx Listener
        sfx.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(sfxStatus.equals("Off"))
                {
                    sfxStatus = "On";
                }
                else
                {
                    sfxStatus = "Off";
                }
                sfx.setText("Toggle Sfx:  " + sfxStatus);
            }
        });

        //FullScreen Listener
        fullscreen.addListener(new ClickListener() {
            Boolean isFullScreen = false;

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!isFullScreen) {
                    Gdx.graphics.setDisplayMode(
                            Gdx.graphics.getDesktopDisplayMode().width,
                            Gdx.graphics.getDesktopDisplayMode().height,
                            true
                    );
                    isFullScreen = true;
                    fullscreenStatus = "On";
                    game.setFullScreen(true);
                }
                else
                {
                    Gdx.graphics.setDisplayMode(
                            Constants.GAME_WIDTH,
                            Constants.GAME_HEIGHT,
                            false
                    );
                    fullscreenStatus = "Off";
                    isFullScreen = false;
                    game.setFullScreen(false);
                }
                fullscreen.setText("Fullscreen:  " + fullscreenStatus);
            }
        });

        //Back Listener
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainMenuScreen.disposePlayer();
                game.setScreen(new MainMenuScreen(game,batch,font){});
            }
        });

        stage.addActor(table);

        //BQ video
        videoPlayer = VideoPlayerCreator.createVideoPlayer();
        videoPlayer.resize(1280, 720);
        try {
            videoPlayer.play(Gdx.files.internal("Backgrounds/optionScreen.webm"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

       if (!videoPlayer.render()) { // As soon as the video is finished, we start the file again using the same player.
            try {
                videoPlayer.play(Gdx.files.internal("Backgrounds/optionScreen.webm"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        videoPlayer.dispose();
    }
}
