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
    public static final Texture LEVEL4PLAYER2 = new Texture(Gdx.files.internal("PNG/player2.png"));
    public static final Texture MAP_TEXTURE = new Texture(Gdx.files.internal("Backgrounds/bq.png"), true);
    public static final Texture SPLASH = new Texture(Gdx.files.internal("PNG/RocketFoolsSplash.png"));
    public static final Texture GHOST = new Texture(Gdx.files.internal("PNG/Yellow_icon.png"), true);
    public static final Texture WARNING = new Texture(Gdx.files.internal("PNG/redWarning.png"), true);
    public static final Texture OVERLAY = new Texture(Gdx.files.internal("PNG/overlay.png"), true);
    public static final Texture SAS_ON = new Texture(Gdx.files.internal("PNG/SASgreen.png"), true);
    public static final Texture SAS_OFF = new Texture(Gdx.files.internal("PNG/SASred.png"), true);
    public static final Texture MINIMAP_PLANET = new Texture(Gdx.files.internal("PNG/red_dot.png")); //ToDo: increase quality
    public static final Texture MAPBORDER_DOT = new Texture(Gdx.files.internal("PNG/mapBorderDot.png")); //ToDo: increase quality
    public static final Texture MINIMAP_PLAYER = new Texture(Gdx.files.internal("PNG/green_dot.png"));
    public static final Texture PROGFILLER = new Texture(Gdx.files.internal("PNG/progFiller.png"), true);
    public static final Texture PLANET1 = new Texture(Gdx.files.internal("PNG/Planets/1.png"), true);
    public static final Texture PLANET2 = new Texture(Gdx.files.internal("PNG/Planets/2.png"), true);
    public static final Texture PLANET3 = new Texture(Gdx.files.internal("PNG/Planets/3.png"), true);
    public static final Texture PLANET4 = new Texture(Gdx.files.internal("PNG/Planets/4.png"), true);
    public static final Texture PLANET5 = new Texture(Gdx.files.internal("PNG/Planets/5.png"), true);
    public static final Texture PLANET6 = new Texture(Gdx.files.internal("PNG/Planets/6.png"), true);
    public static final Texture PLANET7 = new Texture(Gdx.files.internal("PNG/Planets/7.png"), true);
    public static final Texture MARS = new Texture(Gdx.files.internal("PNG/Planets/mars.png"), true); // CASE 10
    public static final Texture EARTH = new Texture(Gdx.files.internal("PNG/Planets/Earth.png"), true); //CASE 8
    public static final Texture MOON = new Texture(Gdx.files.internal("PNG/Planets/Moon.png"), true); // CASE 9
    public static final Texture POPUP_HEAD = new Texture(Gdx.files.internal("PNG/title.png"));
    public static final Texture POPUP_BODY = new Texture(Gdx.files.internal("PNG/popup.png"));
    public static final Texture TOXIC_METEOR = new Texture(Gdx.files.internal("PNG/toxicMeteor.png"));
    public static final Texture METEOR_NORMAL = new Texture(Gdx.files.internal("PNG/meteorNormal.png"));
    public static final Texture METEOR_NORMAL2 = new Texture(Gdx.files.internal("PNG/meteorNormal2.png"));
    public static final Texture ALIEN_ROCK1 = new Texture(Gdx.files.internal("PNG/allienrock1.png"));
    public static final Texture ALIEN_ROCK2 = new Texture(Gdx.files.internal("PNG/allienrock2.png"));
    public static final Texture MAP_BORDER = new Texture(Gdx.files.internal("PNG/mapBorder.png"));
    public static final Texture LEVEL_FINISHED = new Texture(Gdx.files.internal("Backgrounds/levelFinish.png"));
    public static final Texture CROSS_HERE = new Texture(Gdx.files.internal("PNG/crossHere.png"));
    public static final Sound THRUSTER_STARTER = Gdx.audio.newSound(Gdx.files.internal("SFX/ThrusterStarter.wav"));
    public static final Sound THRUSTER_GOINGER = Gdx.audio.newSound(Gdx.files.internal("SFX/ThrusterGoinger.wav"));
    public static final Sound THRUSTER_ENDER = Gdx.audio.newSound(Gdx.files.internal("SFX/ThrusterEnder.wav"));
    public static final Sound POPUP_OPENER = Gdx.audio.newSound(Gdx.files.internal("SFX/popupOpening.wav"));
    public static final Music POPUP_SHUTTER_1 = Gdx.audio.newMusic(Gdx.files.internal("SFX/Transmission_06.wav"));
    public static final Music BQ_MUSIC = Gdx.audio.newMusic(Gdx.files.internal("SFX/Space_Atmospheric_01.wav"));
    public static final Music WARNING_SOUND= Gdx.audio.newMusic(Gdx.files.internal("SFX/Emergency_Warning_05.wav"));
    public static final Sound EXPLOSION = Gdx.audio.newSound(Gdx.files.internal("SFX/Explosion_01.wav"));
    public static final Sound DEATH_SIGN = Gdx.audio.newSound(Gdx.files.internal("SFX/Medical_EKG_Flat_Line_01.wav"));
    public static final FileHandle TAKEOFF_VIDEO = Gdx.files.internal("Backgrounds/level2/earthTakeOff.webm");
    public static final FileHandle LEVEL1START = Gdx.files.internal("Backgrounds/cutScreens/level1Start.webm");
    public static final FileHandle LEVEL1END = Gdx.files.internal("Backgrounds/cutScreens/level1End.webm");
    public static final FileHandle LEVEL2START = Gdx.files.internal("Backgrounds/cutScreens/level2Start.webm");
    public static final FileHandle LEVEL2END = Gdx.files.internal("Backgrounds/cutScreens/level2End.webm");
    public static final FileHandle LEVEL3START = Gdx.files.internal("Backgrounds/cutScreens/level3Start.webm");
    public static final FileHandle LEVEL3END = Gdx.files.internal("Backgrounds/cutScreens/level3End.webm");
    public static final FileHandle LEVEL4START = Gdx.files.internal("Backgrounds/cutScreens/level4Start.webm");
    public static final FileHandle LEVEL4END = Gdx.files.internal("Backgrounds/cutScreens/level4end.webm");
    public static final FileHandle LEVEL5START = Gdx.files.internal("Backgrounds/cutScreens/level5Start.webm");
    public static final FileHandle LEVEL5END = Gdx.files.internal("Backgrounds/cutScreens/level5End.webm");
    public static final FileHandle ENDING = Gdx.files.internal("Backgrounds/ending.webm");

}
