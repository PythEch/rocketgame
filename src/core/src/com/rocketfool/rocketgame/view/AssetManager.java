package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

/**
 * List of assets as global constants.
 */
public class AssetManager {

    public static final Texture PLAYER_TEXTURE = new Texture(Gdx.files.internal("PNG/playerShip2_orange.png"));
    public static final Texture MAP_TEXTURE = new Texture(Gdx.files.internal("Backgrounds/darkPurple.png"));
    //public static final Texture MAP_VIDEO = new Texture(Gdx.files.internal("blablabla"));
    public static final Texture SPLASH = new Texture(Gdx.files.internal("PNG/RocketFoolsSplash.png"));

}
