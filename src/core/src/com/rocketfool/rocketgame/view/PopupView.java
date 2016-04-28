package com.rocketfool.rocketgame.view;

import aurelienribon.tweenengine.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.rocketfool.rocketgame.model.PopUp;
import com.rocketfool.rocketgame.view.Splash.SpriteAccessor;

/**
 * Created by pythech on 28/04/16.
 */
public class PopupView {
    private PopUp popup;
    private OrthographicCamera camera;
    private BitmapFont font;
    private TweenManager tweenManager;
    private float yCoord;

    public PopupView(PopUp popup, OrthographicCamera camera) {
        this.popup = popup;
        this.camera = camera;
        this.font = new BitmapFont(); // TODO: select a font
        this.yCoord = -AssetManager.POPUP_BODY.getHeight();

        tweenManager = new TweenManager();
        Tween.registerAccessor(PopupView.class, new PopupAcessor());
    }

    public void draw(SpriteBatch batch) {
        if (popup.isPropertyChanged()) {
            Tween.set(this, PopupAcessor.Y_COORD).target(-AssetManager.POPUP_BODY.getHeight()).start(tweenManager);
            Tween.to(this, PopupAcessor.Y_COORD, 2).target(0).repeatYoyo(1, 4).start(tweenManager);
            popup.setPropertyChanged(false);
        }

        tweenManager.update(Gdx.graphics.getDeltaTime());

        Texture bodyTexture = AssetManager.POPUP_BODY;
        batch.draw(
                bodyTexture,
                camera.position.x - (camera.viewportWidth / 2f) * camera.zoom,
                camera.position.y - (camera.viewportHeight / 2f - yCoord) * camera.zoom,
                0,
                0,
                bodyTexture.getWidth(),
                bodyTexture.getHeight(),
                camera.zoom,
                camera.zoom,
                0,
                0,
                0,
                bodyTexture.getWidth(),
                bodyTexture.getHeight(),
                false,
                false
        );

        Texture headTexture = AssetManager.POPUP_HEAD;
        batch.draw(
                headTexture,
                camera.position.x - (camera.viewportWidth / 2f) * camera.zoom,
                camera.position.y - (camera.viewportHeight / 2f - bodyTexture.getHeight() - yCoord) * camera.zoom,
                0,
                0,
                headTexture.getWidth(),
                headTexture.getHeight(),
                camera.zoom,
                camera.zoom,
                0,
                0,
                0,
                headTexture.getWidth(),
                headTexture.getHeight(),
                false,
                false
        );

        font.setScale(camera.zoom);
        font.draw(
                batch,
                popup.getText(),
                camera.position.x - (camera.viewportWidth / 2f - 150) * camera.zoom,
                camera.position.y - (camera.viewportHeight / 2f - 280 -yCoord) * camera.zoom
        );
    }

    public float getyCoord() {
        return yCoord;
    }

    public void setyCoord(float yCoord) {
        this.yCoord = yCoord;
    }
}
