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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.video.VideoPlayerDesktop;
import com.rocketfool.rocketgame.external.RocketGame;
import com.rocketfool.rocketgame.model.LevelManager;

import java.io.FileNotFoundException;

/**
 * TODO: implement
 */
public class LevelSelectionScreen implements Screen {

    private Stage stage;
    private RocketGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private TextButton back;

    public LevelSelectionScreen(RocketGame game, SpriteBatch batch, BitmapFont font) {
        this.game = game;
        this.batch = batch;
        this.font = font;
    }

    @Override
    public void show() {

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

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


        TextButton level1 = new TextButton("Level 1", buttonStyle);
        TextButton level2 = new TextButton("Level 2", buttonStyle);
        TextButton level3 = new TextButton("Level 3", buttonStyle);
        TextButton level4 = new TextButton("Level 4", buttonStyle);
        TextButton level5 = new TextButton("Level 5", buttonStyle);
        back = new TextButton("Back", buttonStyle);


        level1.setWidth(level1.getPrefWidth() * 2f);
        level1.setHeight(level1.getPrefHeight() * 2.1f);
        level1.setPosition(460, 440);
        stage.addActor(level1);
        level2.setWidth(level1.getPrefWidth() * 2f);
        level2.setHeight(level1.getPrefHeight() * 2.1f);
        level2.setPosition(760, 440);
        stage.addActor(level2);
        level3.setWidth(level1.getPrefWidth() * 2f);
        level3.setHeight(level1.getPrefHeight() * 2.1f);
        level3.setPosition(1065, 440);
        stage.addActor(level3);
        level5.setWidth(level1.getPrefWidth() * 2f);
        level5.setHeight(level1.getPrefHeight() * 2.1f);
        level5.setPosition(918, 198);
        stage.addActor(level5);
        level4.setWidth(level1.getPrefWidth() * 2f);
        level4.setHeight(level1.getPrefHeight() * 2.1f);
        level4.setPosition(600, 198);
        stage.addActor(level4);

        back.setWidth(level1.getPrefWidth() * 2f);
        back.setHeight(level1.getPrefHeight() * 1.5f);
        back.setPosition(50, 50);

        //Listeners
        //Level1
        level1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(LevelManager.createLevel1(), game, batch, font));
            }
        });

        //Level2
        level2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //game.setScreen(new MoonCrashScreen(game, batch, font));
                // TODO: revert this shit
                game.setScreen(new GameScreen(LevelManager.createLevel2(), game, batch, font));
            }
        });

        //Level3
        level3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(LevelManager.createLevel3(), game, batch, font));
            }
        });

        //Level4
        level4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(LevelManager.createLevel4(), game, batch, font));
            }
        });

        //Level5
        level5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(LevelManager.createLevel5(), game, batch, font));
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game, batch, font));
            }
        });

        stage.addActor(back);

        stage.addAction(Actions.alpha(0f));//0 = transperent

        stage.setViewport(new FitViewport(1280, 720));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        //BQ picture
        Texture background = new Texture(Gdx.files.internal("Backgrounds/levelSelectionScreen.png"));

        batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
        batch.begin();
        batch.draw(background, 0, 0);
        back.draw(batch, 1);
        batch.end();

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

    }
}
