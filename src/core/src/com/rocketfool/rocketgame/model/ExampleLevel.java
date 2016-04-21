package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.rocketfool.rocketgame.util.Constants;
import com.badlogic.gdx.utils.Array;

/**
 * Our map for testing the current physical behaviour. It is a work in progress right now.
 */
public class ExampleLevel extends Level {
    public ExampleLevel() {
        super();

        int width = Gdx.graphics.getWidth() * 10;
        int height = Gdx.graphics.getHeight() * 10;

        this.playable = new Playable(300, 300, 112, 75, 1e5f, 250, 200, 1000, 1e25f, world);
        this.playable.getBody().setLinearVelocity( 30f , -30f );

        this.map = new Map(width, height);

        addTriggers();
        addPlanets();
    }

    private void addPlanets() {

       // G = 6.67408f*1e-11f*1e-12f;
        // Expected G, which doesn't work for some reason
      // Current scale 1/10^6 approx
        G = 4*1e-20f; //Plan B: G is whatever we like... idare eder...**

        solidObjects.add(new Planet( 200, 200,     6*1e24f,    100, null, world));
        //solidObjects.add(new Planet( 2500 , 3000,     2.7f*1e25f,     250, null, world));
        //solidObjects.add(new Planet( 1700 , 2400,     4.7f*1e24f,     200, null, world));
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
        if (Constants.DEBUG){
            triggers.add(new PositionTrigger(300, 300, 10, playable) {
                @Override
                public void triggerPerformed() {ExampleLevel.periodStopWatch.updatePeriod(); }
            });
        }
    }

    private void addObstacles() {

    }

    private void addParticles() {

    }

    private void addWaypoints(){
            waypoints.add( new Waypoint(300 , 300 , 20) );
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    public static class periodStopWatch{
        private static long stopTime = System.currentTimeMillis();
        private static long startTime = stopTime;
        private static long period = -1;

        public static void updatePeriod() {
            stopTime = System.currentTimeMillis();
            if ( stopTime - startTime >= 2000 ) {
                period = ((stopTime - startTime) / 1000);
                startTime = stopTime;
                System.err.println("Rotation complete. Period updated");
            }
        }

        public static long getPeriod(){return period;}
    }
}
