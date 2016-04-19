package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.Gdx;

/**
 * Created by pythech on 02/04/16.
 */
public class ExampleLevel extends Level {
    public ExampleLevel() {
        super();

        int width = Gdx.graphics.getWidth() * 100;
        int height = Gdx.graphics.getHeight() * 100;

        this.playable = new Playable(0, 0, 112, 75, 250, 75, 100, 1, 100, world);
        this.map = new Map(width, height);

        addTriggers();
        addPlanets();
    }

    private void addPlanets() {
        solidObjects.add(new Planet(75, 75, 1e4f, 50, null, world));
        solidObjects.add(new Planet(400,400,1e4f,50,null,world));
    }


    protected void addTriggers() {
        super.addTriggers();
        triggers.add(new PositionTrigger(10, 10, 10, playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("just started the journey");
            }
        });
        triggers.add(new PositionTrigger(400,400,100,playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Final Destination reached");
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
