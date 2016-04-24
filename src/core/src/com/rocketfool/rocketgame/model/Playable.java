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
    private final float fuelSpecificImpulse = 4500;    // impulse in N generated per kg of fuel //TODO: improve?
                                                       // http://www.esa.int/Education/Solid_and_liquid_fuel_rockets4/(print)
    private final float thrustMultiplier = 4 * 1e3f;   // A base multiplier for thrust management.

    private float currentImpulse;
    private float deltaAngularImpulse;
    private float deltaLinearImpulse;
    private float fuelLeft;
    private float width;
    private float height;
    private float maxDeltaLinearImpulse;
    private boolean SASEnabled;
    private boolean maximizeThrust;
    private boolean minimizeThrust;
    private Vector2 bottomPosition;
    private Vector2 spawnPoint;
    //endregion

    //region Constructors

    /** Constructor for a player-controlled space craft. Mass and fuel values are in kg.*/
    public Playable(float x, float y, float width, float height, float dryMass, float deltaAngularImpulse, float deltaLinearImpulse, float maxDeltaLinearImpulse, float fuel, World world) {
        this.currentImpulse = 0;
        this.width = width;
        this.height = height;
        this.deltaAngularImpulse = deltaAngularImpulse;
        this.deltaLinearImpulse = deltaLinearImpulse;
        this.fuelLeft = fuel;
        this.maxDeltaLinearImpulse = maxDeltaLinearImpulse;
        this.SASEnabled = false;
        this.maximizeThrust = false;
        this.maximizeThrust = false;

        this.body = createBody(x, y, (dryMass + fuel) , world);
        this.spawnPoint = body.getPosition().cpy();
    }

    /** Constructor for a player-controlled space craft with default values. Mass and fuel values are in kg.*/
    public Playable(float x, float y, float fuel, World world) {
        // Similar to typical spacecraft, our typical crafts will have a dry mass of 100t and carry up to 400t of fuel.
        this.width = 88;
        this.height = 105;
        float dryMass = 1*1e5f;
        this.currentImpulse = 0;                               //Current thrust in Newtons per "delta t"
        this.deltaAngularImpulse = 800 * thrustMultiplier;     //Reaction wheel strength (assuming the craft rotates using electricity.
        this.deltaLinearImpulse = 100 * thrustMultiplier;      //Thrust change rate multiplier
        this.maxDeltaLinearImpulse = 1000 * thrustMultiplier;  //Total max thrust therefore is 4000 kN, which is realistic,
        this.fuelLeft = fuel;                                  //https://en.wikipedia.org/wiki/RD-180

        this.SASEnabled = false;
        this.maximizeThrust = false;
        this.maximizeThrust = false;

        this.body = createBody(x, y, ( dryMass + fuel) , world);
        this.spawnPoint = body.getPosition().cpy();
    }
    //endregion

	/** This object's creation is according to Box2D definitions.*/
    private Body createBody(float x, float y, float mass, World world) {
        // In Box2D, every body has a location and some fixtures (in this case one).
        // The location and orientation of the object is determined by the body while properties like
        // its mass and friction depend on its fixtures.

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
        fixtureDef.density = mass / ( width * toMeter * height  * toMeter ); //**
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;

        Fixture fixture = body.createFixture(fixtureDef);

        fixture.setUserData(Level.ObjectType.PLAYABLE);

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
        float newMass;
        MassData tempMD;
        if (fuelLeft > 0) {
            float fuelSpent = currentImpulse * deltaTime / fuelSpecificImpulse;
            fuelLeft -= fuelSpent;

            //The mass information of the body changes only when MassData is updated
            //because of the nature of the Box2D engine.
            newMass =  body.getMass() - fuelSpent;
            tempMD = new MassData();
            tempMD.mass = newMass;
            tempMD.I = body.getMassData().I / body.getMass() * tempMD.mass;
            body.setMassData( tempMD );
        }
    }

    private void move(float deltaTime) {
        Vector2 bottomVector;
        Vector2 impulseVector;

        bottomVector = new Vector2(0, -height / 2f * toMeter).rotateRad(body.getAngle());
        bottomPosition = bottomVector.add(body.getPosition());

        if (fuelLeft <= 0)
            currentImpulse = 0;
        if (minimizeThrust) {
            maximizeThrust = false;
            minimizeThrust(deltaTime);
            if ( currentImpulse == 0 )
                minimizeThrust = false;
        }
        if (maximizeThrust) {
            minimizeThrust = false;
            maximizeThrust(deltaTime);
            if ( currentImpulse >= maxDeltaLinearImpulse )
                maximizeThrust = false;
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

    public float getMaxDeltaLinearImpulse(){ return maxDeltaLinearImpulse; }

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

    //setter to use in level initializations
    public void setFuelLeft(float fuel){
        this.fuelLeft = fuel;
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
            currentImpulse = Math.min(currentImpulse + deltaTime * 2 * deltaLinearImpulse , maxDeltaLinearImpulse);
        }
    }

    public void decreaseThrust(float deltaTime) {
        currentImpulse = Math.max(0, currentImpulse - deltaTime * 2 * deltaLinearImpulse);
    }

    public void toggleMaximizeThrust(){
        maximizeThrust = !maximizeThrust;
    }

    public void toggleMinimizeThrust(){
        minimizeThrust = !minimizeThrust;
    }

    public void maximizeThrust( float deltaTime ){
        if (fuelLeft > 0 ) {
            currentImpulse = Math.min(maxDeltaLinearImpulse , currentImpulse + deltaTime * deltaLinearImpulse * 10);
        }
    }

    public void minimizeThrust( float deltaTime ){
        currentImpulse = Math.max(0, currentImpulse - deltaTime * deltaLinearImpulse * 10);
    }
    //endregion




}
