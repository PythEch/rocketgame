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
    private final float fuelSpecificImpulse = 1; //impulse supplied per kg of fuel consumed***

    private float currentImpulse;
    private float deltaAngularImpulse;
    private float deltaLinearImpulse;
    private float fuelLeft;
    private float width;
    private float height;
    private float maxImpulse;
    private boolean SASEnabled;
    private boolean maximizeThrust;
    private boolean minimizeThrust;
    private Vector2 bottomPosition;
    private Vector2 spawnPoint;
    //endregion

    /** Contructor for a player-controlled space craft. Mass and fuel values are in kg.*/
    public Playable(float x, float y, float width, float height, float dryMass, float rotateImpulse, float impulse, float maxImpulse, float fuel, World world) {
        // Similar to typical spacecraft, our typical crafts will have a dry mass of 100t and carry up to 400t of fuel
        this.currentImpulse = 0;
        this.deltaAngularImpulse = rotateImpulse;
        this.deltaLinearImpulse = impulse;
        this.fuelLeft = fuel;
        this.width = width;
        this.height = height;
        this.maxImpulse = maxImpulse;
        this.SASEnabled = false;
        this.maximizeThrust = false;
        this.maximizeThrust = false;

        this.body = createBody(x, y, (dryMass + fuel) , world);
        this.spawnPoint = body.getPosition().cpy();
    }

	/** This object's creation is according to Box2D definitions.*/
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
        fixtureDef.density = 1.0f;
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
            float fuelSpent = currentImpulse * deltaTime / 100; //K1
            fuelLeft -= fuelSpent;
            //decrease mass 0.18 for each fuel spent
            body.getMassData().mass -= fuelSpent * 0.18; //FIXME: Work in progress by Levent
        }
    }

    //setter to use in level initializations
    public void setFuelLeft(float fuel){
        this.fuelLeft = fuel;
    }

    private void move(float deltaTime) {
        Vector2 bottomVector;
        Vector2 impulseVector;

        bottomVector = new Vector2(0, -height / 2f * toMeter).rotateRad(body.getAngle());
        bottomPosition = bottomVector.add(body.getPosition());

        if (fuelLeft <= 0)
            currentImpulse = 0;
        else if (maximizeThrust) {
            maximizeThrust(deltaTime);
            if ( currentImpulse >= maxImpulse )
                maximizeThrust = false;
        }
        else if (minimizeThrust) {
            minimizeThrust(deltaTime);
            if ( currentImpulse == 0 )
                minimizeThrust = false;
        }

        impulseVector = new Vector2(0, deltaTime * currentImpulse).rotateRad(body.getAngle());

        body.applyLinearImpulse(impulseVector.x, impulseVector.y, bottomPosition.x, bottomPosition.y, false);

    }
    //endregion

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

    public boolean getSASEnabled() { return SASEnabled;}

    public Vector2 getSpawnPoint() {
        return spawnPoint;
    }

    public void setCurrentImpulse(float currentImpulse) {
        this.currentImpulse = currentImpulse;
    }
    //endregion


    //region Manually-controlled playable behaviour

    public void turnLeft(float deltaTime) {
        body.applyAngularImpulse(deltaAngularImpulse * deltaTime, true);
    }

    public void turnRight(float deltaTime) {
        body.applyAngularImpulse(-deltaAngularImpulse * deltaTime, true);
    }

    public void toggleSAS() {
        SASEnabled = !SASEnabled;
    }

    /** The Stability Assist System (SAS) of a spacecraft automatically prevents unwanted
        spinning to make controlling the craft much easier.*/
    public void runSAS( float deltaTime ){
        if (SASEnabled){
            if ( this.getBody().getAngularVelocity() < 0 )
                turnLeft( deltaTime );
            else if ( this.getBody().getAngularVelocity() > 0 )
                turnRight( deltaTime );
        }
    }

    public void increaseThrust(float deltaTime) {
        if (fuelLeft > 0 ) {
            currentImpulse = Math.min(currentImpulse + deltaTime * deltaLinearImpulse , maxImpulse);
        }
    }

    public void decreaseThrust(float deltaTime) {
        currentImpulse = Math.max(0, currentImpulse - deltaTime * deltaLinearImpulse);
    }

    public void toggleMaximizeThrust(){
        maximizeThrust = !maximizeThrust;
    }

    public void toggleMinimizeThrust(){
        minimizeThrust = !minimizeThrust;
    }

    public void maximizeThrust( float deltaTime ){
        if (fuelLeft > 0 ) {
            currentImpulse = Math.min(maxImpulse , currentImpulse + deltaTime * deltaLinearImpulse * 10);
        }
    }

    public void minimizeThrust( float deltaTime ){
        currentImpulse = Math.max(0, currentImpulse - deltaTime * deltaLinearImpulse * 10);
    }
    //endregion




}
