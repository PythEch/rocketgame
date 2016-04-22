package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.Gdx;
import static com.rocketfool.rocketgame.util.Constants.*;

/**
 * Created by Omer on 22/04/2016.
 */
public class LevelManager {
    public LevelManager() {

    }

    //level creation methods
    public static Level createLevel1() {
        Level level = new Level();

        level.playable = new Playable(300, 300, 112, 75, 1e5f, 250, 200, 1000, 1e25f, level.world);
        level.playable.getBody().setLinearVelocity( 30f , -30f );

        level.map = new Map(Gdx.graphics.getWidth() * 10, Gdx.graphics.getHeight() * 10);

        level.triggers.add(new PositionTrigger(10, 10, 10, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("just started the journey");
            }
        });
        level.triggers.add(new PositionTrigger(400,400,100, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Final Destination reached");
            }
        });
        if (DEBUG){
            level.triggers.add(new PositionTrigger(300, 300, 10, level.playable) {
                @Override
                public void triggerPerformed() {periodStopWatch.updatePeriod(); }
            });
        }

        // G = 6.67408f*1e-11f*1e-12f;
        // Expected G, which doesn't work for some reason
        // Current scale 1/10^6 approx
        level.G = 4*1e-20f; //Plan B: G is whatever we like... idare eder...**

        level.solidObjects.add(new Planet( 200, 200,     6*1e24f,    100, null, level.world));
        //solidObjects.add(new Planet( 2500 , 3000,     2.7f*1e25f,     250, null, world));
        //solidObjects.add(new Planet( 1700 , 2400,     4.7f*1e24f,     200, null, world));

        level.waypoints.add( new Waypoint(300 , 300 , 20) );

        addDefaultTriggers(level);

        return level;
    }

    public static Level createLevel2(){
        return null;
    }

    public static Level createLevel3(){
        return null;
    }

    public static Level createLevel4(){
        return null;
    }

    public static Level createLevel5(){
        return null;
    }

    public static Level createLevel6(){
        return null;
    }

    //trigger methods
    private static void addDefaultTriggers(Level level) {
        level.triggers.add(new OutOfMapTrigger(level.map, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("OUT OF MAP!");
            }
        });

        level.triggers.add(new FuelDepletionTrigger(level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("NO FUEL!");
            }
        });
    }

    // Stopwatch methods
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
