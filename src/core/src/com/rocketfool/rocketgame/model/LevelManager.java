package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import static com.rocketfool.rocketgame.util.Constants.DEBUG;

/**
 * Created by OmerE. & HYamanS.  on 22/04/2016.
 */
public class LevelManager {
    public LevelManager() {

    }

    //level creation methods
    public static Level createLevel1() {
        Level level = new Level();
        // trying to implement the timer in order to create story dialogs during the gameplay
        Timer timer = new Timer();
        timer.start();

        //init of map
        level.map = new Map(Gdx.graphics.getWidth() * 100, Gdx.graphics.getHeight() * 100);


        //initialization of the rocket
        level.playable = new Playable(2000, 2000, 88, 108, 1e5f, 250, 200, 1000, 1e25f, level.world);
        level.playable.getBody().setLinearVelocity(0, 0);


        level.triggers.add(new PositionTrigger(1000, 1000, 200, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("You've reached the Earth");
            }
        });
        level.triggers.add(new PositionTrigger(5000, 3000, 350, level.playable) {
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

        //this object stands for the earth and its properties
        level.solidObjects.add(new Planet(1000, 1000, 6 * 1e24f, 100, null, level.world));
        //this object stands for the moon and its properties
        level.solidObjects.add(new Planet(5000, 3000, 2.7f * 1e25f, 250, null, level.world));
        //obstacle


        //bottom of the earth for landing and ending the level.
        level.waypoints.add(new Waypoint(1000, 1000, 300));
        level.waypoints.add(new Waypoint(5000, 3000, 400));

        addDefaultTriggers(level);

        return level;
    }

    public static Level createLevel2() {
        Level level = new Level();

        level.playable = new Playable(300, 300, 88, 108, 1e5f, 250, 220, 1000, 1e25f, level.world);
        level.playable.getBody().setLinearVelocity(0f, 0f);

        //edit the size of the map here
        level.map = new Map(Gdx.graphics.getWidth() * 90, Gdx.graphics.getHeight() * 90);

        //add triggers to level 2
        final PositionTrigger marsTrig = new PositionTrigger(5000, 2000, 400, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Go back to Earth, Moron!");
            }
        };
        level.triggers.add(marsTrig);

        final PositionTrigger earthTrig = new PositionTrigger(220, 300, 200, level.playable) {
            @Override
            public void triggerPerformed() {
                if(marsTrig.isTriggeredBefore()) {
                    System.out.println("Mission Completed");
                }
                else {
                    System.out.println("Go to Mars, Adventurer!");
                }
            }
        };
        level.triggers.add(earthTrig);

        if (DEBUG) {
            level.triggers.add(new PositionTrigger(300, 300, 10, level.playable) {
                @Override
                public void triggerPerformed() {
                    periodStopWatch.updatePeriod();
                }
            });
        }

        level.solidObjects.add(new Planet(220, 300, 6 * 1e24f, 100, null, level.world));
        //this object stands for the mars
        level.solidObjects.add(new Planet(5000, 2000, 2.7f * 1e25f, 250, null, level.world));
        //TODO:add the cargo object below to rotate around the mars

        //bottom of the planet
        //TODO:cycle through waypoints during the level, so add more
        level.waypoints.add(new Waypoint(200, 200, 100));
        level.waypoints.add(new Waypoint(4000,7000, 250));

        addDefaultTriggers(level);

        return level;
    }

    public static Level createLevel3() {
        Level level = new Level();

        level.playable = new Playable(200, 300, 88, 105, 1e5f, 250, 200, 1000, 1e25f, level.world);
        level.playable.getBody().setLinearVelocity(0f, 0f);

        //edit the size of the map here
        level.map = new Map(Gdx.graphics.getWidth() * 100, Gdx.graphics.getHeight() * 100);

        //add triggers for the level
        //TODO:increase numbers of triggers for the texts that should appear near the planets
        level.triggers.add(new PositionTrigger(100, 100, 10, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("You should start your journey now!");
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

        level.solidObjects.add(new Planet(200, 200, 6 * 1e24f, 100, null, level.world));
        //this object stands for the planet 1
        level.solidObjects.add(new Planet(1200, 3000, 2.7f * 1e21f, 250, null, level.world));
        //this object stands for planet 2
        level.solidObjects.add(new Planet(3200, 1220, 6 * 1e24f, 150, null, level.world));
        //this object stands for the planet 3
        level.solidObjects.add(new Planet(5300, 3500, 2.7f * 1e21f, 300, null, level.world));
        //this object stands for the planet 4
        level.solidObjects.add(new Planet(6000, 800, 4 * 1e24f, 80, null, level.world));


        //bottom of the planet
        level.waypoints.add(new Waypoint(200, 200, 300));
        level.waypoints.add(new Waypoint(1200, 3000, 750));
        level.waypoints.add(new Waypoint(3200, 1120, 450));
        level.waypoints.add(new Waypoint(5300, 3500, 900));
        level.waypoints.add(new Waypoint(6000, 800, 240));


        addDefaultTriggers(level);

        return level;
    }

    public static Level createLevel4() {
        Level level = new Level();

        //initialization of the rocket
        level.playable = new Playable(200, 300, 4 * 1e5f, level.world);
        level.playable.getBody().setLinearVelocity(0f, 0f);

        //init of map
        level.map = new Map(Gdx.graphics.getWidth() * 100, Gdx.graphics.getHeight() * 100);

        //triggers
        level.triggers.add(new PositionTrigger(200, 300, 200, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Oh it seems there will be lots of asteroids on the way back home, " +
                        "we should doge them and we might even try different paths on the way. ");
            }
        });
        level.triggers.add(new PositionTrigger(500, 500, 100, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("fasten your seat belts crew. This will be a bumpy ride.");
            }
        });
        level.triggers.add(new PositionTrigger(3000, 2000, 500, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Did you know that steroids are rich in precious" +
                        " metals and other metals, as well as water.");
            }
        });
        level.triggers.add(new PositionTrigger(2000, 1000, 500, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Some asteroids have moons of their own!");
            }
        });
        level.triggers.add(new PositionTrigger(4500, 2500, 350, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("What a strange trip it has been");
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

        //this object stands for the target planet and its properties
        level.solidObjects.add(new Planet(6000, 3000, 2.7f * 1e25f, 150, null, level.world));

        //TODO: Dispose method could be implemented for level class to remove the objects going out of the map and summoning new ones
        //loop for randomizing the movement directions, velocities, sizes and the shapes of the asteroids
        for (int i = 0; i < 50; i++) {
            Vector2 vector = new Vector2(((float) Math.random()) * (float) Math.pow(-1, i) * 10f, ((float) Math.random()) * (float) Math.random() * 10f);
            if (i % 2 == 0) {
                level.solidObjects.add(new RoundObstacle(((float) (Math.random()) * 8000) - 100, ((float) (Math.random()) * 2500) - 100 + 30 * i, 10, vector, level.world));
            } else {
                level.solidObjects.add(new RectangleObstacle(((float) (Math.random()) * 8000) - 100, ((float) (Math.random()) * 2500) - 200 + 30 * i, i * 2, i + 1, vector, level.world));
            }
        }

        //Target location and exit location are indicated by waypoints
        level.waypoints.add(new Waypoint(100, 100, 40));
        level.waypoints.add(new Waypoint(4500, 2500, 20));

        addDefaultTriggers(level);

        return level;
    }

    public static Level createLevel5() {
        //LEVEL 1 Design but low fuel
        Level level = new Level();

        //initialization of the rocket
        level.playable = new Playable(2000, 2000, 88, 108, 1e5f, 250, 200, 1000, 1e25f, level.world);
        level.playable.getBody().setLinearVelocity(15f, 15f);
        level.playable.setFuelLeft(10);

        //init of map
        level.map = new Map(Gdx.graphics.getWidth() * 25, Gdx.graphics.getHeight() * 25);


        level.triggers.add(new PositionTrigger(1000, 1000, 200, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("You've reached the Earth");
            }
        });
        level.triggers.add(new PositionTrigger(5000, 3000, 350, level.playable) {
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

        //this object stands for the earth and its properties
        level.solidObjects.add(new Planet(1000, 1000, 6 * 1e24f, 100, null, level.world));
        //this object stands for the moon and its properties
        level.solidObjects.add(new Planet(5000, 3000, 2.7f * 1e25f, 250, null, level.world));

        //bottom of the earth for landing and ending the level.
        level.waypoints.add(new Waypoint(1000, 1000, 300));
        level.waypoints.add(new Waypoint(5000, 3000, 400));

        addDefaultTriggers(level);

        return level;
    }

    //OUT OF COOL LEVEL IDEAS
    public static Level createLevel6() {
        return null;
    }

    //trigger methods
    private static void addDefaultTriggers(final Level level) {
        level.triggers.add(new OutOfMapTrigger(level.map, level.playable) {
            @Override
            public void triggerPerformed() {
                //TODO:Create pop-up options for the triggers
               /* Skin skin = new Skin(Gdx.files.internal("Skin/uiskin.json"));
                new Dialog("Some Dialog", skin, "dialog") {
                    protected void result (Object object) {
                        System.out.println("Chosen: " + object);
                    }
                }.text("RETRY?").button("Yes", true).button("No", false).key(Input.Keys.ENTER, true)
                        .key(Input.Keys.ESCAPE, false).show();
                */
                System.out.println("Out of map");
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
