package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.rocketfool.rocketgame.util.GameUtils;

import static com.rocketfool.rocketgame.util.Constants.toMeter;

/**
 * Instances of this class are player-controlled spacecraft, which may be ships, satellites, or others.
 * The constructor provides the properties of the crafts.
 */
public class Playable extends SolidObject {
    //region Constants
    /**
    * Impulse in Ns generated per kg of fuel, in every deltaT (which is about 1/60 s).
    * Source for comparison: ( http://www.esa.int/Education/Solid_and_liquid_fuel_rockets4/(print) )
    */
    private final float fuelSpecificImpulse = 4500;
    private final float maxVelocity = 1200;

    /** A base multiplier for thrust calculations, for convenience. */
    public static final float BASE = 5 * 1e3f;
    //endregion

    //region Fields
    /** Current thrust in Newtons in every "deltaTime"*/
    private float currentThrust;
    /** Reaction wheel strength (assuming the craft rotates using electricity). */
    //TODO rename
    private float deltaAngularImpulse;
    /** Thrust change rate multiplier */
    private float deltaThrust;
    /** Mass and fuel values are both in kg. */
    private float fuelLeft;
    private float startingFuel;
    private float width;
    private float height;
    /** Maximum thrust in Newtons in every "deltaTime" */
    private float maxThrust;
    private boolean SASEnabled;
    private boolean maximizeThrust;
    private boolean minimizeThrust;
    private Vector2 bottomPosition;
    private Vector2 spawnPoint;
    //endregion

    //Notes
    // Similar to typical spacecraft, our typical crafts will have a dry mass of 100t and carry up to 400t of fuel.
    // Their max thrust will be in the 1-100 megaNewtons range, which is realistic.
    // Sources for comparison: (https://en.wikipedia.org/wiki/RD-180) (https://en.wikipedia.org/wiki/Saturn_V#US_Space_Shuttle)

    public Playable(float x, float y, float width, float height, float dryMass, float deltaAngularImpulse, float deltaThrust, float maxThrust, float fuel, World world) {
        this.currentThrust = 0;
        this.width = width;
        this.height = height;
        this.deltaAngularImpulse = deltaAngularImpulse;
        this.deltaThrust = deltaThrust;
        this.fuelLeft = fuel;
        this.startingFuel = fuel;
        this.maxThrust = maxThrust;
        this.SASEnabled = false;
        this.maximizeThrust = false;
        this.maximizeThrust = false;

        this.body = createBody(x, y, (dryMass + fuel), world);
        this.spawnPoint = body.getPosition().cpy();
    }

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
        fixtureDef.density = mass / (width * toMeter * height * toMeter);
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;

        Fixture fixture = body.createFixture(fixtureDef);

        fixture.setUserData(Level.ObjectType.PLAYABLE);

        rectangle.dispose();

        return body;
    }
    //endregion


    //region Methods
    /**
     * Receives impulses and updates momenta of the body.
     */
    @Override
    public void update(float deltaTime) {
        move(deltaTime);
        consumeFuelAndDecreaseMass(deltaTime);
    }

    /** Reduce mass of the spacecraft by burning its fuel */
    private void consumeFuelAndDecreaseMass(float deltaTime) {
        if (fuelLeft > 0) {
            float fuelSpent = currentThrust * deltaTime / fuelSpecificImpulse;
            fuelLeft -= fuelSpent;

            //The mass information of the body changes only when MassData is updated
            //because of the nature of the Box2D engine.
            GameUtils.changeMass(body, body.getMass() - fuelSpent);
        }
    }

    /** Uses the momenta of the burned fuel to move the spacecraft in space */
    private void move(float deltaTime) {
        // Calculate the bottom position of the spacecraft
        Vector2 bottomVector = new Vector2(0, -height / 2f * toMeter).rotateRad(body.getAngle());
        bottomPosition = bottomVector.add(body.getPosition());

        // Manipulate thrust according to these preconditions
        if (fuelLeft <= 0)
            currentThrust = 0;

        if (minimizeThrust) {
            maximizeThrust = false;
            minimizeThrust(deltaTime);
            if (currentThrust == 0)
                minimizeThrust = false;
        }

        if (maximizeThrust) {
            minimizeThrust = false;
            maximizeThrust(deltaTime);
            if (currentThrust >= maxThrust)
                maximizeThrust = false;
        }

        // Push the spacecraft with the thrust we calculated
        Vector2 impulseVector = new Vector2(0, currentThrust * deltaTime).rotateRad(body.getAngle());
        body.applyLinearImpulse(impulseVector.x, impulseVector.y, bottomPosition.x, bottomPosition.y, true);

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

    /**
     * The Stability Assist System (SAS) of a spacecraft automatically prevents unwanted
     * spinning to make controlling the craft much easier.
     */
    public void runSAS(float deltaTime) {
        float spin = this.getBody().getAngularVelocity(); //NOTE: This 1/100 the value on the Debug Screen...
        if (SASEnabled) {
            if (spin > 0f) {
                if (spin > 0.001f)
                    turnRight(deltaTime);
                else
                    turnRight(deltaTime / 1000f);
            } else if (spin < 0f) {
                if (spin < -0.001f)
                    turnLeft(deltaTime);
                else
                    turnLeft(deltaTime / 1000f);
            }
            System.err.println(spin);
        }
    }

    public void increaseThrust(float deltaTime) {
        if (fuelLeft > 0) {
            currentThrust = Math.min(currentThrust + deltaTime * 2 * deltaThrust, maxThrust);
        }
    }

    public void decreaseThrust(float deltaTime) {
        currentThrust = Math.max(0, currentThrust - deltaTime * 2 * deltaThrust);
    }

    public void toggleMaximizeThrust() {
        maximizeThrust = !maximizeThrust;
    }

    public void toggleMinimizeThrust() {
        minimizeThrust = !minimizeThrust;
    }

    public void maximizeThrust(float deltaTime) {
        if (fuelLeft > 0) {
            currentThrust = Math.min(maxThrust, currentThrust + deltaTime * deltaThrust * 10);
        }
    }

    public void minimizeThrust(float deltaTime) {
        currentThrust = Math.max(0, currentThrust - deltaTime * deltaThrust * 10);
    }
    //endregion


    //region Getters & Setters
    public float getCurrentThrust() {
        return currentThrust;
    }

    public float getDeltaAngularImpulse() {
        return deltaAngularImpulse;
    }

    public float getDeltaThrust() {
        return deltaThrust;
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

    public float getMaxThrust() {
        return maxThrust;
    }

    public Vector2 getBottomPosition() {
        return bottomPosition;
    }

    public boolean getSASEnabled() {
        return SASEnabled;
    }

    public Vector2 getSpawnPoint() {
        return spawnPoint;
    }

    public void setCurrentThrust(float currentThrust) {
        this.currentThrust = currentThrust;
    }

    public void setFuelLeft(float fuel) {
        this.fuelLeft = fuel;
    }

    public void setSASEnabled(boolean set) {
        SASEnabled = set;
    }

    public float getStartingFuel() {
        return startingFuel;
    }

    public float getMaxVelocity() {
        return maxVelocity;
    }

    //endregion
}
