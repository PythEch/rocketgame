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

        //distance is in m , mass is in kg
        this.playable = new Playable(200, 200, 112, 75, 1e5f, 150, 100, 500, 1, world);
        //x, y,         w, h,      m,  rttImp, imp, maxImp, fuel, world

        this.map = new Map(width, height);

        addTriggers();
        addPlanets();
    }

    private void addPlanets() {
        G = 6.67408f*1e-11f*1e-12f; //** distances scaled 1:1e6f , so G is decreased by the same amount to not affect energy relations
      //Currently everything is pretty much to scale and the distances are terribly large despite the 1/1000 000 scale
      //TODO: ERROR: Galiba scale-down etmekte bir mantık hatası var çünkü şu haliyle force r değişince çok çok hızlı değişiyor

        solidObjects.add(new Planet(150, 150,     6*1e24f,    127/2, null, world));
        //                                     mass        rad
        //solidObjects.add(new Planet( 15000 , 1,     2*1e30f,     1390/2, null, world));
        //                           10^5             mass        rad
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
