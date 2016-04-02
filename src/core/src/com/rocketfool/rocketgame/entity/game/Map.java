package com.rocketfool.rocketgame.entity.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.rocketfool.rocketgame.entity.VisualObject;
import com.rocketfool.rocketgame.util.TextureManager;

/**
 * Created by pythech on 07/03/16.
 */
public class Map extends VisualObject {
    //region Fields
    private int width;
    private int height;
    //endregion

    //region Constructor
    public Map(int width, int height) {
        this.width = width;
        this.height = height;

        texture = TextureManager.MAP_TEXTURE;
        // this makes background repeatable
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }
    //endregion

    //region Methods
    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(
                texture,
                0,
                0,
                0,
                0,
                width,
                height
        );
    }
    //endregion
}
