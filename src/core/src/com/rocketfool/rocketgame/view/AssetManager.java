package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

/**
 * List of assets as global constants.
 */
public class AssetManager {

    public static final Texture PLAYER_TEXTURE = new Texture(Gdx.files.internal("PNG/rocket.png"));
    public static Texture MAP_TEXTURE = new Texture(Gdx.files.internal("Backgrounds/bq.png"), true);
    public static final Texture SPLASH = new Texture(Gdx.files.internal("PNG/RocketFoolsSplash.png"));
    public static final Texture GHOST = new Texture(Gdx.files.internal("PNG/Yellow_icon.png"), true);
    public static final Texture WARNING = new Texture(Gdx.files.internal("PNG/redWarning.png"), true);
    public static final Texture OVERLAY = new Texture(Gdx.files.internal("PNG/overlay.png"), true);
    public static final Texture PROGFILLER = new Texture(Gdx.files.internal("PNG/progFiller.png"), true);

}
