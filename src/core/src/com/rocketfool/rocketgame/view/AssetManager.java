package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    public static final Texture MINIMAP_PLANET = new Texture(Gdx.files.internal("PNG/red_dot.png"));
    public static final Texture MINIMAP_PLAYER = new Texture(Gdx.files.internal("PNG/green_dot.png"));
    public static final Texture PROGFILLER = new Texture(Gdx.files.internal("PNG/progFiller.png"), true);
    public static final Texture PLANET1 = new Texture(Gdx.files.internal("PNG/Planets/1.png"), true);
    public static final Texture PLANET2 = new Texture(Gdx.files.internal("PNG/Planets/2.png"), true);
    public static final Texture PLANET3 = new Texture(Gdx.files.internal("PNG/Planets/3.png"), true);
    public static final Texture PLANET4 = new Texture(Gdx.files.internal("PNG/Planets/4.png"), true);
    public static final Texture PLANET5 = new Texture(Gdx.files.internal("PNG/Planets/5.png"), true);
    public static final Texture PLANET6 = new Texture(Gdx.files.internal("PNG/Planets/6.png"), true);
    public static final Texture PLANET7 = new Texture(Gdx.files.internal("PNG/Planets/7.png"), true);
    public static final Texture POPUP_HEAD = new Texture(Gdx.files.internal("PNG/title.png"));
    public static final Texture POPUP_BODY = new Texture(Gdx.files.internal("PNG/popup.png"));
    public static final Sound THRUSTER_STARTER = Gdx.audio.newSound(Gdx.files.internal("SFX/ThrusterStarter.wav"));
    public static final Sound THRUSTER_GOINGER = Gdx.audio.newSound(Gdx.files.internal("SFX/ThrusterGoinger.wav"));
    public static final Sound THRUSTER_ENDER = Gdx.audio.newSound(Gdx.files.internal("SFX/ThrusterEnder.wav"));
    public static final Music BQ_MUSIC = Gdx.audio.newMusic(Gdx.files.internal("SFX/Space_Atmospheric_01.wav"));

}
