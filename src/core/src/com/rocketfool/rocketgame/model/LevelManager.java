package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.Gdx;

import static com.rocketfool.rocketgame.util.Constants.DEBUG;

/**
 * Created by Omer on 22/04/2016.
 */
public class LevelManager {
    public LevelManager() {

    }

    //level creation methods
    public static Level createLevel1() {
        Level level = new Level();

        level.playable = new Playable(800, 800, 112, 75, 1e5f, 250, 200, 1000, 1e25f, level.world);
        level.playable.getBody().setLinearVelocity(30f, -30f);

        level.map = new Map(Gdx.graphics.getWidth() * 30, Gdx.graphics.getHeight() * 30);

        //add trigers to level1
        level.triggers.add(new PositionTrigger(200 , 200, 200, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("You've reached the Earth");
            }
        });
        level.triggers.add(new PositionTrigger(400, 400, 100, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("You've reached the Moon");
            }
        });
        if (DEBUG) {
            level.triggers.add(new PositionTrigger(300, 300, 10, level.playable) {
                @Override
                public void triggerPerformed() {
                    periodStopWatch.updatePeriod();
                }
            });
        }

        // G = 6.67408f*1e-11f*1e-12f;
        // Expected G, which doesn't work for some reason
        // Current scale 1/10^6 approx
        level.G = 4 * 1e-20f; //Plan B: G is whatever we like... idare eder...**

        //this object stands for the earth and its properties
        level.solidObjects.add(new Planet(200, 200, 6 * 1e24f, 100, null, level.world));
        //this object stands for the moon and its properties
        level.solidObjects.add(new Planet(1000, 1000, 2.7f * 1e25f, 250, null, level.world));

        //bottom of the earth for landing and ending the level.
        level.waypoints.add(new Waypoint(200, 100, 100));

        addDefaultTriggers(level);

        return level;
    }

    public static Level createLevel2() {
        Level level = new Level();

        level.playable = new Playable(300, 300, 112, 75, 1e5f, 0, 0, 1000, 1e25f, level.world);
        //level.playable.getBody().setLinearVelocity( 30f , -30f );

        //edit the size of the map here
        level.map = new Map(Gdx.graphics.getWidth() * 30, Gdx.graphics.getHeight() * 30);

        //add trigers to level1
        level.triggers.add(new PositionTrigger(300, 300, 10, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("You should start your journey now!");
            }
        });
        level.triggers.add(new PositionTrigger(2500, 3000, 350, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("And this is Mars!");
            }
        });
        if (DEBUG) {
            level.triggers.add(new PositionTrigger(300, 300, 10, level.playable) {
                @Override
                public void triggerPerformed() {
                    periodStopWatch.updatePeriod();
                }
            });
        }

        // G = 6.67408f*1e-11f*1e-12f;
        // Expected G, which doesn't work for some reason
        // Current scale 1/10^6 approx
        level.G = 4 * 1e-20f; //Plan B: G is whatever we like... idare eder...**

        level.solidObjects.add(new Planet(200, 200, 6 * 1e24f, 100, null, level.world));
        //this object stands for the mars
        level.solidObjects.add(new Planet(2500, 3000, 2.7f * 1e25f, 250, null, level.world));
        //TODO:add the cargo object below to rotate around the mars

        //bottom of the planet
        level.waypoints.add(new Waypoint(200, 100, 100));

        addDefaultTriggers(level);

        return level;
    }

    public static Level createLevel3() {
        return null;
    }

    public static Level createLevel4() {
        return null;
    }

    public static Level createLevel5() {
        return null;
    }

    public static Level createLevel6() {
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
    public static class periodStopWatch {
        private static long stopTime = System.currentTimeMillis();
        private static long startTime = stopTime;
        private static long period = -1;

        public static void updatePeriod() {
            stopTime = System.currentTimeMillis();
            if (stopTime - startTime >= 2000) {
                period = ((stopTime - startTime) / 1000);
                startTime = stopTime;
                System.err.println("Rotation complete. Period updated");
            }
        }

        public static long getPeriod() {
            return period;
        }
    }
}
