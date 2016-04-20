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
        this.playable = new Playable(300, 300,     112, 75,   1e5f, 250, 200, 1000, 1, world);
                                    //x, y,         w, h,      m,  rttImp, imp, maxImp, fuel, world
        this.map = new Map(width, height);

        addTriggers();
        addPlanets();

        this.playable.getBody().setLinearVelocity( 27f , -27f );
    }

    private void addPlanets() {

       // G = 6.67408f*1e-11f*1e-12f; // Expected G, which doesn't work for some reason
      //Current scale 1/10^6 approx
        G = 4*1e-20f; //Plan B: G is whatever we like... idare eder...**

        //                              x,y         mass        radius
        solidObjects.add(new Planet(200, 150,     6*1e24f,    100, null, world));
        solidObjects.add(new Planet( 1600 , 1600,     2.7f*1e25f,     250, null, world));
        solidObjects.add(new Planet( 500 , 1200,     4.7f*1e24f,     200, null, world));
    }

    protected void addTriggers() {
        super.addTriggers();
        triggers.add(new PositionTrigger(10, 10, 10, playable) {
            @Override
            public void triggerPerformed() {
                //System.out.println("omg");
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
