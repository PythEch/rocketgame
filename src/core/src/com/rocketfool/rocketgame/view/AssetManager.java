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
    public static final Texture PLANET1 = new Texture(Gdx.files.internal("PNG/Planets/1.png"), true);
    public static final Texture PLANET2 = new Texture(Gdx.files.internal("PNG/Planets/2.png"), true);
    public static final Texture PLANET3 = new Texture(Gdx.files.internal("PNG/Planets/3.png"), true);
    public static final Texture PLANET4 = new Texture(Gdx.files.internal("PNG/Planets/4.png"), true);
    public static final Texture PLANET5 = new Texture(Gdx.files.internal("PNG/Planets/5.png"), true);
    public static final Texture PLANET6 = new Texture(Gdx.files.internal("PNG/Planets/6.png"), true);
    public static final Texture PLANET7 = new Texture(Gdx.files.internal("PNG/Planets/7.png"), true);
    //public static final Texture PLANET8 = new Texture(Gdx.files.internal("PNG/Planets/8.png"), true);

}
