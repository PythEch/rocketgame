package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.Gdx;

/**
 * Created by pythech on 02/04/16.
 */
public class Level1 extends Level {
    public Level1() {
        super();

        int width = Gdx.graphics.getWidth() * 100;
        int height = Gdx.graphics.getHeight() * 100;

        this.playable = new Player(0, 0, world);
        this.map = new Map(width, height);

        addTriggers();
        addPlanets();
    }

    private void addPlanets() {
        solidObjects.add(new Planet(75, 75, 1e4f, 50, world));
    }

    @Override
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
