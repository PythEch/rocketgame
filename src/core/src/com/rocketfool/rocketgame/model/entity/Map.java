package com.rocketfool.rocketgame.model.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rocketfool.rocketgame.view.AssetManager;

/**
 * Created by pythech on 07/03/16.
 */
public class Map {
    //region Fields
    private int width;
    private int height;
    private Texture texture;
    //endregion

    //region Constructor
    public Map(int width, int height) {
        this.width = width;
        this.height = height;
    }
    //endregion

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
