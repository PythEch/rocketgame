package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import static com.rocketfool.rocketgame.model.Playable.BASE;
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

        //this object stands for the earth and its properties
        level.solidObjects.add(new Planet(1000, 1000, 6 * 1e24f, 100, null, level.world));
        //this object stands for the moon and its properties
        level.solidObjects.add(new Planet(5000, 3000, 2.7f * 1e25f, 250, null, level.world));

        //initialization of the rocket
        level.playable = new Playable(2000, 2000, 88, 108, 1e5f, 250 * BASE , 200 * BASE, 1000 * BASE, 5e5f, level.world);
        level.playable.getBody().setLinearVelocity(0, 0); //TODO We can edit fuel levels later.

        //endGame Triggers
        final PositionTrigger earthTrig = new PositionTrigger(1000, 1000, 200, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("How can you forget you wallet at mars?!?!?");
            }
        };
        level.triggers.add(earthTrig);
        final PositionTrigger moon = new PositionTrigger(5000, 300, 400, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("We can return home now!");
            }
        };
        level.triggers.add(moon);
        final PositionTrigger earthTrig2 = new PositionTrigger(1000, 1000, 200, level.playable) {
            @Override
            public void triggerPerformed() {
                if (moon.isTriggeredBefore() && earthTrig.isTriggeredBefore()) {
                    System.out.println("Mission Completed");
                } else {
                    System.out.println("Mission is not yet over!");
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
        addDefaultTriggers(level);

        //bottom of the earth for landing and ending the level.
        level.waypoints.add(new Waypoint(1000, 1000, 300));
        level.waypoints.add(new Waypoint(5000, 3000, 400));

        //timer and pop up conditions.
        //TODO:Implement text and time when pop up is ready.
        if (level.timePassed > 10)
            System.out.println("space is large");
        if(level.timePassed > 15)
            System.out.println("time flies so quickly");


        return level;
    }

    public static Level createLevel2() {
        Level level = new Level();

        level.playable = new Playable(300, 300, 88, 108, 1e5f, 250 * BASE, 220 * BASE, 1000 * BASE, 5e5f, level.world);
        level.playable.getBody().setLinearVelocity(0f, 0f);

        //edit the size of the map here
        level.map = new Map(Gdx.graphics.getWidth() * 90, Gdx.graphics.getHeight() * 90);

        //add planets (earth and mars)
        //TODO:add the cargo object below to rotate around the mars, this could also be a waypoint
        level.solidObjects.add(new Planet(220, 300, 6 * 1e24f, 100, null, level.world));
        level.solidObjects.add(new Planet(5000, 2000, 2.7f * 1e25f, 250, null, level.world));

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
                if (marsTrig.isTriggeredBefore()) {
                    System.out.println("Mission Completed");
                } else {
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
        addDefaultTriggers(level);

        //add waypoints
        level.waypoints.add(new Waypoint(200, 200, 100));
        level.waypoints.add(new Waypoint(4000, 7000, 250));

        //timer and pop up conditions.
        //TODO:Implement text and time when pop up is ready.
        if (level.timePassed > 10)
            System.out.println("space is large");
        if(level.timePassed > 15)
            System.out.println("time flies so quickly");

        return level;
    }

    public static Level createLevel3() {
        Level level = new Level();

        level.playable = new Playable(200, 300, 88, 105, 1e5f, 250 * BASE, 200 * BASE, 1000 * BASE, 5e5f, level.world);
        level.playable.getBody().setLinearVelocity(0f, 0f);

        //edit the size of the map here
        level.map = new Map(Gdx.graphics.getWidth() * 100, Gdx.graphics.getHeight() * 100);

        //Add planets here
        level.solidObjects.add(new Planet(200, 200, 6 * 1e24f, 100, null, level.world));
        //this object stands for the planet 1
        level.solidObjects.add(new Planet(1200, 3000, 2.7f * 1e21f, 250, null, level.world));
        //this object stands for planet 2
        level.solidObjects.add(new Planet(3200, 1220, 6 * 1e24f, 150, null, level.world));
        //this object stands for the planet 3
        level.solidObjects.add(new Planet(5300, 3500, 2.7f * 1e21f, 300, null, level.world));
        //this object stands for the planet 4
        level.solidObjects.add(new Planet(6000, 800, 4 * 1e24f, 80, null, level.world));

        //add unnecesary triggers for text and pop up
        //TODO:increase numbers of basic & nonessential triggers for the texts that should appear near the planets
        level.triggers.add(new PositionTrigger(100, 100, 10, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("You should start your journey now!");
            }
        });

        //add important endgame triggers here
        final PositionTrigger planet1 = new PositionTrigger(1200, 3000, 500, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("What a beautiful planet");
            }
        };
        level.triggers.add(planet1);
        final PositionTrigger planet2 = new PositionTrigger(3200, 1220, 300, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Lets check the others");
            }
        };
        level.triggers.add(planet2);
        final PositionTrigger planet3 = new PositionTrigger(6000, 800, 160, level.playable) {
            @Override
            public void triggerPerformed() {
                if (planet1.isTriggered() && planet2.isTriggeredBefore())
                    System.out.println("lets check the next one");
                else
                    System.out.println("We skipped one planet it might be there!");
            }
        };
        level.triggers.add(planet3);
        final PositionTrigger planet4 = new PositionTrigger(5300, 3500, 600, level.playable) {
            @Override
            public void triggerPerformed() {
                if (planet1.isTriggeredBefore() && planet2.isTriggeredBefore() && planet3.isTriggered())
                    System.out.println("OK its here we can go home");
                else
                    System.out.println("We should check the remaining ones");
            }
        };
        level.triggers.add(planet4);
        final PositionTrigger earthTrig = new PositionTrigger(200, 200, 200, level.playable) {
            @Override
            public void triggerPerformed() {
                if (planet1.isTriggeredBefore() && planet2.isTriggeredBefore() && planet3.isTriggeredBefore() && planet4.isTriggeredBefore()) {
                    System.out.println("Mission Completed");
                } else {
                    System.out.println("Some Planets still remain to be discovered");
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
        addDefaultTriggers(level);

        //Wapoints as bottom of the planets
        level.waypoints.add(new Waypoint(200, 200, 300));
        level.waypoints.add(new Waypoint(1200, 3000, 750));
        level.waypoints.add(new Waypoint(3200, 1120, 450));
        level.waypoints.add(new Waypoint(5300, 3500, 900));
        level.waypoints.add(new Waypoint(6000, 800, 240));

        //timer and pop up conditions.
        //TODO:Implement text and time when pop up is ready.
        if (level.timePassed > 10)
            System.out.println("space is large");
        if(level.timePassed > 15)
            System.out.println("time flies so quickly");

        return level;
    }

    public static Level createLevel4() {
        Level level = new Level();

        //initialization of the rocket
        level.playable = new Playable(200, 300, 88, 105, 1e5f, 500 * BASE, 100 * BASE, 1000 * BASE, 5 * 1e5f, level.world);
        level.playable.getBody().setLinearVelocity(0f, 0f);

        //init of map
        level.map = new Map(Gdx.graphics.getWidth() * 100, Gdx.graphics.getHeight() * 100);

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

        //triggers for pop ups
        level.triggers.add(new PositionTrigger(200, 300, 200, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Oh it seems there will be lots of asteroids on the way back home, " +
                        "we should dodge them and we might even try different paths on the way. ");
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

        //endgame triggers
        final PositionTrigger planet = new PositionTrigger(6000, 3000, 300, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("nice we can go back where we started");
            }
        };
        level.triggers.add(planet);
        final PositionTrigger endGame = new PositionTrigger(200, 200, 300, level.playable) {
            @Override
            public void triggerPerformed() {
                if (planet.isTriggeredBefore()) {
                    System.out.println("Mission Completed");
                } else {
                    System.out.println("We need to go to that planet");
                }
            }
        };
        level.triggers.add(endGame);

        if (DEBUG) {
            level.triggers.add(new PositionTrigger(300, 300, 10, level.playable) {
                @Override
                public void triggerPerformed() {
                    periodStopWatch.updatePeriod();
                }
            });
        }
        addDefaultTriggers(level);

        //Target location and exit location are indicated by waypoints
        level.waypoints.add(new Waypoint(200, 200, 300));
        level.waypoints.add(new Waypoint(6000, 3000, 20));

        //timer and pop up conditions.
        //TODO:Implement text and time when pop up is ready.
        if (level.timePassed > 10)
            System.out.println("space is large");
        if(level.timePassed > 15)
            System.out.println("time flies so quickly");

        return level;
    }

    public static Level createLevel5() {
        //LEVEL 1 Design but low fuel
        Level level = new Level();

        //initialization of the rocket
        level.playable = new Playable(2000, 2000, 88, 108, 1e5f, 250 * BASE, 200 * BASE, 1000 * BASE, 5e5f, level.world);
        level.playable.getBody().setLinearVelocity(15f, 15f);
        level.playable.setFuelLeft(10);

        //init of map
        level.map = new Map(Gdx.graphics.getWidth() * 25, Gdx.graphics.getHeight() * 25);

        //these objects stand for the earth and moon respectively
        level.solidObjects.add(new Planet(1000, 1000, 6 * 1e24f, 100, null, level.world));
        level.solidObjects.add(new Planet(5000, 3000, 2.7f * 1e25f, 250, null, level.world));

        //trigger based pop ups written here:

        //end game triggers
        final PositionTrigger moon = new PositionTrigger(5000, 3000, 500, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("We can return home now!");
            }
        };
        level.triggers.add(moon);
        final PositionTrigger earthTrig = new PositionTrigger(1000, 1000, 200, level.playable) {
            @Override
            public void triggerPerformed() {
                if (moon.isTriggeredBefore()) {
                    System.out.println("Mission Completed");
                } else {
                    System.out.println("Mission is not yet over!");
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
        addDefaultTriggers(level);

        //bottom of the earth for landing and ending the level.
        level.waypoints.add(new Waypoint(1000, 1000, 300));
        level.waypoints.add(new Waypoint(5000, 3000, 400));

        //timer and pop up conditions.
        //TODO:Implement text and time when pop up is ready.
        if (level.timePassed > 10)
            System.out.println("space is large");
        if(level.timePassed > 15)
            System.out.println("time flies so quickly");

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

}
