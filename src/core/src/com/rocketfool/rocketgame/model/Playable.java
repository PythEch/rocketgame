package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.rocketfool.rocketgame.model.SolidObject;

import static com.rocketfool.rocketgame.util.Constants.toMeter;

/**
 * Created by pythech on 02/04/16.
 */
public class Playable extends SolidObject {
    //region Fields
    private float currentImpulse;
    private float rotateImpulse;
    private float impulse;
    private float fuel;
    private float width;
    private float height;
    private float maxImpulse;
    private boolean SASenabled;
    private Vector2 bottomPosition;
    //endregion


    public Playable(float x, float y, float width, float height, float mass, float rotateImpulse, float impulse, float maxImpulse, float fuel, World world) {
        this.currentImpulse = 0;
        this.rotateImpulse = rotateImpulse;
        this.impulse = impulse;
        this.fuel = fuel;
        this.width = width;
        this.height = height;
        this.maxImpulse = maxImpulse;
        this.SASenabled = false;

        this.body = createBody(x, y, mass, world);
    }

    private Body createBody(float x, float y, float mass, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Gdx.graphics.getWidth() / 2f * toMeter, Gdx.graphics.getHeight() / 2f * toMeter);

        Body body = world.createBody(bodyDef);

        body.setTransform(0, 0, -90 * MathUtils.degreesToRadians);

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(width / 2f * toMeter, height / 2f * toMeter);

        // Define properties of object here
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rectangle;
        fixtureDef.density = 1.0f; // TODO: Calculate a reasonable density
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;

        Fixture fixture = body.createFixture(fixtureDef);

        rectangle.dispose();

        return body;
    }
    //endregion


    //region Methods
    @Override
    public void update(float dt) {
        float angle = body.getAngle();

        Vector2 bottomVector = new Vector2(0, -height / 2f * toMeter).rotateRad(angle);
        bottomPosition = bottomVector.add(body.getPosition());

        Vector2 impulseVector = new Vector2(0, dt * currentImpulse).rotateRad(body.getAngle());

        body.applyLinearImpulse(impulseVector.x, impulseVector.y, bottomPosition.x, bottomPosition.y, false);

        if (SASenabled) {
            runSAS();
        }
    }

    private void runSAS() {
        //body.setAngularDamping(body.getAngularDamping() + 1);
    }

    private void consumeFuelAndDecreaseMass(float deltaTime) {

    }

    private void move(float deltaTime) {

    }
    //endregion

    public void toggleSAS() {
        SASenabled = !SASenabled;
        if (SASenabled) {
            body.setAngularDamping(rotateImpulse / 50);
        }
        else {
            body.setAngularDamping(0);
        }
    }

    //region Getters & Setters
    public float getCurrentImpulse() {
        return currentImpulse;
    }

    public float getRotateImpulse() {
        return rotateImpulse;
    }

    public float getImpulse() {
        return impulse;
    }

    public float getFuel() {
        return fuel;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Vector2 getBottomPosition(){
        return bottomPosition;
    }

    public void setCurrentImpulse(float currentImpulse) {
        this.currentImpulse = currentImpulse;
    }
    //endregion

    public void turnLeft(float deltaTime) {
        body.applyAngularImpulse(rotateImpulse * deltaTime, true);
    }

    public void turnRight(float deltaTime) {
        body.applyAngularImpulse(-rotateImpulse * deltaTime, true);
    }

    public void increaseThrust(float deltaTime) {
        // FIXME: Use Math.min with some max speed
        currentImpulse = Math.max(0, currentImpulse + deltaTime * impulse);
    }

    public void decreaseThrust(float deltaTime) {
        currentImpulse = Math.max(0, currentImpulse - deltaTime * impulse);
    }
}
