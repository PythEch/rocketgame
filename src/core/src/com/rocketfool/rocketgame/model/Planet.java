package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.physics.box2d.*;

/**
 * A planet or moon, with a fixed mass.
 */
public class Planet extends CelestialObject {
    //region Fields
    /**
     * An optional primary planet, if exists the planet will orbit around its primary planet
     */
    private Planet primary;
    /**
     * Used to determine which texture will be used in View
     */
    private int planetType;
    //endregion

    //region Constructor

    /**
     * If the Primary is provided, the planet will orbit around its primary planet
     *
     * @param x          X-coord
     * @param y          Y-coord
     * @param mass       Mass in Kgs
     * @param radius     Radius in Meters
     * @param primary    Optional primary planet to orbit around
     * @param world      The Box2D world that this planet will be created in
     * @param planetType Used to determine which texture will be used in View
     */
    public Planet(float x, float y, float mass, float radius, Planet primary, World world, int planetType) {
        super(mass, radius);
        this.primary = primary;
        this.planetType = planetType;
        this.body = createBody(x, y, mass, radius, world);
    }

    private Body createBody(float x, float y, float mass, float radius, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.restitution = 0.0f;
        fixtureDef.friction = 10000f;

        Fixture fixture = body.createFixture(fixtureDef);

        fixture.setUserData(Level.ObjectType.PLANET);

        circle.dispose();

        return body;
    }
    //endregion

    //region Methods
    @Override
    public void update(float dt) {
    }
    //endregion

    //region Getters & Setters
    public int getPlanetType() {
        return planetType;
    }

    public void setPlanetType(int planetType) {
        this.planetType = planetType;
    }

    public Planet getPrimary() {
        return primary;
    }

    public void setPrimary(Planet primary) {
        this.primary = primary;
    }
    //endregion
}
