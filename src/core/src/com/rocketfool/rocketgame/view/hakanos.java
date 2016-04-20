package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;

/**
 * Created by alpino-64 on 20.04.2016.
 */
public class hakanos extends ApplicationAdapter{

    SpriteBatch batch;
    TextureAtlas textureAtlas;
    Animation animation;
    float elapsedTime = 0f;

    public hakanos() {
        super();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        textureAtlas = new TextureAtlas(Gdx.files.internal("Backgrounds/meteorSheets/meteors.atlas"));
        animation = new Animation(1f/80f, textureAtlas.getRegions() );
     }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void render() {
        elapsedTime = elapsedTime + Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(animation.getKeyFrame(elapsedTime,true),0,0);
        batch.end();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

