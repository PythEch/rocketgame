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

    /** A base multiplier for thrust calculations, for convenience. */
    public static final float BASE = 5 * 1e3f / 60f;
    //endregion

    //region Fields
    /** Current thrust in Newtons in every "deltaTime"*/
    private float currentImpulse;
    /** Reaction wheel strength (assuming the craft rotates using electricity). */
    //TODO rename
    private float deltaAngularImpulse;
    /** Thrust change rate multiplier */
    private float deltaImpulse;
    /** Mass and fuel values are both in kg. */
    private float fuelLeft;
    private float width;
    private float height;
    /** Maximum thrust in Newtons in every "deltaTime" */
    private float maxImpulse;
    private boolean SASEnabled;
    private boolean maximizeImpulse;
    private boolean minimizeImpulse;
    private Vector2 bottomPosition;
    private Vector2 spawnPoint;
    //endregion

    //Notes
    // Similar to typical spacecraft, our typical crafts will have a dry mass of 100t and carry up to 400t of fuel.
    // Their max thrust will be in the 1-100 megaNewtons range, which is realistic.
    // Sources for comparison: (https://en.wikipedia.org/wiki/RD-180) (https://en.wikipedia.org/wiki/Saturn_V#US_Space_Shuttle)

    public Playable(float x, float y, float width, float height, float dryMass, float deltaAngularImpulse, float deltaImpulse, float maxImpulse, float fuel, World world) {
        this.currentImpulse = 0;
        this.width = width;
        this.height = height;
        this.deltaAngularImpulse = deltaAngularImpulse;
        this.deltaImpulse = deltaImpulse;
        this.fuelLeft = fuel;
        this.maxImpulse = maxImpulse;
        this.SASEnabled = false;
        this.maximizeImpulse = false;
        this.maximizeImpulse = false;

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
            float fuelSpent = currentImpulse / fuelSpecificImpulse;
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
            currentImpulse = 0;

        if (minimizeImpulse) {
            maximizeImpulse = false;
            minimizeThrust(deltaTime);
            if (currentImpulse == 0)
                minimizeImpulse = false;
        }

        if (maximizeImpulse) {
            minimizeImpulse = false;
            maximizeThrust(deltaTime);
            if (currentImpulse >= maxImpulse)
                maximizeImpulse = false;
        }

        // Push the spacecraft with the thrust we calculated
        Vector2 impulseVector = new Vector2(0, currentImpulse).rotateRad(body.getAngle());
        body.applyLinearImpulse(impulseVector.x, impulseVector.y, bottomPosition.x, bottomPosition.y, true);

    }
    //endregion

    //region Manually-controlled playable behaviour
    public void turnLeft(float deltaTime) {
        body.applyAngularImpulse(deltaAngularImpulse, true);
    }

    public void turnRight(float deltaTime) {
        body.applyAngularImpulse(-deltaAngularImpulse, true);
    }

    public void turnLeft(float deltaTime, float scale) {
        body.applyAngularImpulse(deltaAngularImpulse * scale, true);
    }

    public void turnRight(float deltaTime, float scale) {
        body.applyAngularImpulse(-deltaAngularImpulse * scale, true);
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
                if (spin > 0.0075f)
                    turnRight(deltaTime);
                else
                    this.body.setAngularVelocity(0);
            } else if (spin < 0f) {
                if (spin < -0.0075f)
                    turnLeft(deltaTime);
                else
                    this.body.setAngularVelocity(0);
            }
        }
    }

    public void increaseThrust(float deltaTime) {
        if (fuelLeft > 0) {
            currentImpulse = Math.min(currentImpulse + 2 * deltaImpulse, maxImpulse);
        }
    }

    public void decreaseThrust(float deltaTime) {
        currentImpulse = Math.max(0, currentImpulse - 2 * deltaImpulse);
    }

    public void toggleMaximizeThrust() {
        maximizeImpulse = !maximizeImpulse;
    }

    public void toggleMinimizeThrust() {
        minimizeImpulse = !minimizeImpulse;
    }

    public void maximizeThrust(float deltaTime) {
        if (fuelLeft > 0) {
            currentImpulse = Math.min(maxImpulse, currentImpulse + deltaImpulse * 10);
        }
    }

    public void minimizeThrust(float deltaTime) {
        currentImpulse = Math.max(0, currentImpulse - deltaImpulse * 10);
    }
    //endregion


    //region Getters & Setters
    public float getCurrentImpulse() {
        return currentImpulse;
    }

    public float getDeltaAngularImpulse() {
        return deltaAngularImpulse;
    }

    public float getDeltaImpulse() {
        return deltaImpulse;
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

    public float getMaxImpulse() {
        return maxImpulse;
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

    public void setCurrentImpulse(float currentImpulse) {
        this.currentImpulse = currentImpulse;
    }

    public void setFuelLeft(float fuel) {
        this.fuelLeft = fuel;
    }

    public void setSASEnabled(boolean set) {
        SASEnabled = set;
    }
    //endregion
}
