package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.Gdx;

/**
 * Our map for testing the current physical behaviour. It is a work in progress right now.
 */
public class ExampleLevel extends Level {
    public ExampleLevel() {
        super();

        int width = Gdx.graphics.getWidth() * 100;
        int height = Gdx.graphics.getHeight() * 100;

        this.playable = new Playable(0, 0, 112, 75, 10, 150, 100, 500, 1, world);
        //float x, float y, float w, float h, float m, float rotateImpulse, float impulse, float maxImpulse, float fuel, World world

        this.map = new Map(width, height);//**?

        addTriggers();
        addPlanets();
    }

    private void addPlanets() {

        solidObjects.add(new Planet(75, 75, 3*1e3f, 50, null, world));
        //solidObjects.add(new Planet(400, 400, 1e4f, 100, null, world));
        solidObjects.add(new Planet(2000, 2000, 1e7f, 500, null, world));
    }

    protected void addTriggers() {
        super.addTriggers();
        triggers.add(new PositionTrigger(10, 10, 10, playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("omg");
            }
        });
    }

    private void addObstacles() {

    }

    private void addParticles() {

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
}
