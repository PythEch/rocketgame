package com.rocketfool.rocketgame.view;

import aurelienribon.tweenengine.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rocketfool.rocketgame.model.PopUp;

/**
 * Created by pythech on 28/04/16.
 *
 * TODO: maybe pop this up when user clicks the head?
 */
public class PopupView {
    private PopUp popup;
    private OrthographicCamera camera;
    private BitmapFont font;
    private TweenManager tweenManager;
    private float yCoord;
    private float startingYCoord;
    private float elapsedTime;
    private float customDelay;
    private Tween tween;

    public PopupView(PopUp popup, OrthographicCamera camera) {
        this.popup = popup;
        this.camera = camera;
        this.font = new BitmapFont(); // TODO: select a font
        this.startingYCoord = -AssetManager.POPUP_BODY.getHeight();
        this.yCoord = startingYCoord;
        this.elapsedTime = 0;
        this.customDelay = -1;

        tweenManager = new TweenManager();
        Tween.registerAccessor(PopupView.class, new PopupAcessor());
    }

    public PopupView(PopUp popup, OrthographicCamera camera, float delay) {
        this(popup, camera);
        this.customDelay = delay;
    }

    public void update(float deltaTime) {
        elapsedTime += deltaTime;
        tweenManager.update(deltaTime);

        float origY = yCoord;

        if (popup.isPropertyChanged()) {
            popup.setPropertyChanged(false);
            elapsedTime = 0;

            if (tween != null && !tween.isFinished()) {
                tweenManager.killAll();
            }
            Tween.set(this, PopupAcessor.Y_COORD).target(origY).start(tweenManager);
            tween = Tween.to(this, PopupAcessor.Y_COORD, 2).target(0).start(tweenManager);
        }

        float delay;
        if (customDelay == -1) {
            delay = 2 + popup.getLastText().length() * 0.1f;
        }
        else {
            delay = customDelay;
        }
        if (tween != null && yCoord == 0 && elapsedTime >= delay) {
            Tween.set(this, PopupAcessor.Y_COORD).target(0).start(tweenManager);
            tween = Tween.to(this, PopupAcessor.Y_COORD, 2).target(startingYCoord).start(tweenManager);
        }
    }

    public void draw(SpriteBatch batch) {
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
        drawFont(batch);
    }

    private void drawFont(SpriteBatch batch) {
        String[] splitted = splitText(popup.getText(), 235 * camera.zoom, 140 * camera.zoom);
        String topText = splitted[0];
        String bottomText = splitted[1];

        font.drawWrapped(
                batch,
                topText,
                camera.position.x - (camera.viewportWidth / 2f - 160) * camera.zoom,
                camera.position.y - (camera.viewportHeight / 2f - 280 - yCoord) * camera.zoom,
                235 * camera.zoom
        );

        font.drawWrapped(
                batch,
                bottomText,
                camera.position.x - (camera.viewportWidth / 2f - 20) * camera.zoom,
                camera.position.y - (camera.viewportHeight / 2f - 280 - yCoord + 140) * camera.zoom,
                355 * camera.zoom
        );
    }

    private String[] splitText(String text, float width, float height) {
        String topText = text;

        BitmapFont.TextBounds bounds = font.getWrappedBounds(text, width);

        while (bounds.height > height) {
            topText = topText.substring(0, topText.lastIndexOf(' '));
            bounds = font.getWrappedBounds(topText, width);
        }

        String bottomText = text.substring(topText.length());

        return new String[]{topText, bottomText};
    }

    public float getyCoord() {
        return yCoord;
    }

    public void setyCoord(float yCoord) {
        this.yCoord = yCoord;
    }
}
