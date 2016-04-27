package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.rocketfool.rocketgame.model.PopUp;

/**
 * Created by pythech on 28/04/16.
 */
public class PopupView {
    private PopUp popup;
    private OrthographicCamera camera;

    public PopupView(PopUp popup, OrthographicCamera camera) {
        this.popup = popup;
        this.camera = camera;
    }

    public void draw(SpriteBatch batch) {
        Texture texture = AssetManager.POPUP_BODY;
        batch.draw(
                texture,
                camera.position.x - (camera.viewportWidth / 2f) * camera.zoom, //462 Fuel bar's starting pointX
                camera.position.y - (camera.viewportHeight / 2f) * camera.zoom, //635.9f Fuel bar's starting pointY
                0,
                0,
                texture.getWidth(),
                texture.getHeight(), //When bar is max
                camera.zoom,
                camera.zoom,
                0,
                0,
                0,
                texture.getWidth() ,
                texture.getHeight() ,
                false,
                false
        );
    }
}
