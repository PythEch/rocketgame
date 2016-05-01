package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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

    //region Getters & Setters
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getRadius() {
        return (float)Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2)) / 2f;
    }

    public Vector2 getCenter() {
        return new Vector2(width / 2f, height / 2f);
    }
    //endregion
}
