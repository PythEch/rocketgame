package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.rocketfool.rocketgame.util.Constants.toMeter;

/**
 * Instances of this class are player-controlled spacecraft, which may be ships, satellites, or others.
 * The constructor provides the properties of the crafts.
 */
public class Playable extends SolidObject {
    //region Fields
    private float currentImpulse;
    private float deltaAngularImpulse;
    private float deltaLinearImpulse;
    private float fuelLeft;
    private float width;
    private float height;
    private float maxImpulse;
    /** SAS is the system of a spacecraft that automatically stops its spinning. */
    private boolean SASenabled;
    private Vector2 bottomPosition;
    private Vector2 spawnPoint;
    //endregion


    public Playable(float x, float y, float width, float height, float mass, float rotateImpulse, float impulse, float maxImpulse, float fuel, World world) {
        this.currentImpulse = 0;
        this.deltaAngularImpulse = rotateImpulse;
        this.deltaLinearImpulse = impulse;
        this.fuelLeft = fuel;
        this.width = width;
        this.height = height;
        this.maxImpulse = maxImpulse;
        this.SASenabled = false;

        this.body = createBody(x, y, mass, world);
        this.spawnPoint = body.getPosition().cpy();
    }
	/** This creation is according to Box2D definitions.*/
    private Body createBody(float x, float y, float mass, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Gdx.graphics.getWidth() / 2f * toMeter, Gdx.graphics.getHeight() / 2f * toMeter);

        Body body = world.createBody(bodyDef);

        body.setTransform(x, y, -90 * MathUtils.degreesToRadians);

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
    /** Receives impulses and updates momenta of the body.*/
    @Override
    public void update(float deltaTime) {
        move(deltaTime);
        consumeFuelAndDecreaseMass(deltaTime);
    }

    private void consumeFuelAndDecreaseMass(float deltaTime) {
		//kilogram per liter is taken as 0.18
    	if (fuelLeft > 0) {
            float fuelSpent = currentImpulse * deltaTime / 100;
            fuelLeft -= fuelSpent;
            //decrease mass 0.18 for each fuel spent
            body.getMassData().mass -= fuelSpent * 0.18; //FIXME: make a constant for this
        }
    }
    //setter to use in level initializations
    public void setFuelLeft(float fuel){
        this.fuelLeft = fuel;
    }

    private void move(float deltaTime) {
        Vector2 bottomVector = new Vector2(0, -height / 2f * toMeter).rotateRad(body.getAngle());
        bottomPosition = bottomVector.add(body.getPosition());

        Vector2 impulseVector = new Vector2(0, deltaTime * currentImpulse).rotateRad(body.getAngle());

        body.applyLinearImpulse(impulseVector.x, impulseVector.y, bottomPosition.x, bottomPosition.y, false);

    }
    //endregion

    public void toggleSAS() {
        SASenabled = !SASenabled;
		if (SASenabled) {
            body.setAngularDamping( deltaAngularImpulse / 100 ); //TODO: Run SAS WHILE shift is pressed (toggle is too sensitive).
        }
        else {
            body.setAngularDamping(0);
        }
    }

    //region Getters & Setters
    public float getCurrentImpulse() {
        return currentImpulse;
    }

    public float getDeltaAngularImpulse() {
        return deltaAngularImpulse;
    }

    public float getDeltaLinearImpulse() {
        return deltaLinearImpulse;
    }

    public float getFuelLeft() {
        return fuelLeft;
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
        body.applyAngularImpulse(deltaAngularImpulse * deltaTime, true);
    }

    public void turnRight(float deltaTime) {
        body.applyAngularImpulse(-deltaAngularImpulse * deltaTime, true);
    }

    public void increaseThrust(float deltaTime) {
        // FIXME: Use Math.min with some max speed
        if (fuelLeft > 0) {
            currentImpulse = Math.max(0, currentImpulse + deltaTime * deltaLinearImpulse);
        }
    }

    public void decreaseThrust(float deltaTime) {
        currentImpulse = Math.max(0, currentImpulse - deltaTime * deltaLinearImpulse);
    }

    public Vector2 getSpawnPoint() {
        return spawnPoint;
    }
}
