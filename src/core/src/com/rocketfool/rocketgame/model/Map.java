package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rocketfool.rocketgame.view.AssetManager;

/**
 * Outlines playable area.
 */
public class Map {
    //region Fields
    private int width;
    private int height;
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
