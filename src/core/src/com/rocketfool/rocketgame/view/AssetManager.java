package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by pythech on 25/03/16.
 */
public class AssetManager {

    public static Texture PLAYER_TEXTURE = new Texture(Gdx.files.internal("PNG/playerShip2_orange.png"));
    public static Texture MAP_TEXTURE = new Texture(Gdx.files.internal("Backgrounds/darkPurple.png"));
    //public static Texture MAP_VIDEO = new Texture(Gdx.files.internal("blablabla"));
    public static Texture SPLASH = new Texture(Gdx.files.internal("PNG/RocketFoolsSplash.png"));

}
