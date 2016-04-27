package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import static com.rocketfool.rocketgame.model.Playable.BASE;
import static com.rocketfool.rocketgame.util.Constants.DEBUG;

/**
 * Designs and initiates levels and manages them.
 */
public class LevelManager {
    //region Methods
    public static Level createLevel1() {
        //Level Outline II, A part

        final Level level = new Level();
        Timer timer = new Timer();

        //levelMap
        level.map = new Map(Gdx.graphics.getWidth() * 400, Gdx.graphics.getHeight() * 400);

        //Earth
        level.planets.add(new Planet(11000, 6000, 6 * 1e24f, 800, null, level.world,1));

        //initialization of the rocket
        level.playable = new Playable(16500, 12000, 88, 108, 1e5f, 400 * BASE, 200 * BASE, 1000 * BASE, 5e5f, level.world);
        level.playable.getBody().setLinearVelocity(5f, 2f);
        level.playable.getBody().setAngularVelocity(5f);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger earthTrig = new PositionTrigger(11000, 6000, 1500, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Congratulations! You regained the control of the ship and reached the Earth. Level 1 is completed.");
                //TODO: Next level should be given here. However, the method createLevel2() fails here.
            }
        };
        level.triggers.add(earthTrig);

        //level starts here
        timer.start();

        //TODO: Conflict animation goes here (intro cutscene).

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Your bad luck during the pursuit of the aliens left you spinning out of control in space! " +
                                       "The ship is damaged.! You must regain control!");
                               Waypoint earth = new Waypoint(1000, 1855, 5);
                               level.waypoints.add(earth);
                           }
                       },
                5.0f);
        //TODO: Update objective: "Regain control of the ship."
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Use RIGHT & LEFT arrow keys to regain control of your angular movement.");
                           }
                       },
                15.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("The Stability Assist System restored!" +
                                       "The RIGHT SHIFT key toggles the SAS, which automatically reduces spinning.");
                           }
                       },
                25.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("The RIGHT SHIFT key toggles the SAS, which automatically reduces spinning.");
                           }
                       },
                30.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Use UP & DOWN arrow keys to increase and decrease the thrust.");
                           }
                       },
                35.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Use A & S keys to zoom out or zoom in, press ESC for the Pause Menu.");
                           }
                       },
                40.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Supercomputer connections restored! Trajectory calculation features online." +
                                       " (Press T to activate it)");
                           }
                       },
                45.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("The yellow dots in front of you simulates your future motion.");
                           }
                       },
                50.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Navigation system restored! Minimap online!.");
                           }
                       },
                60.0f);
        //TODO enable minimap here
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("All systems restored. Additional information is along the top of the screen.");
                           }
                       },
                65.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Now it's time to find your way back!");
                           }
                       },
                70.0f);
        //TODO: Update objective: Return to Earth
        return level;
    }

    public static Level createLevel2() {
        //Level Outline II, B part

        final Level level = new Level();
        Timer timer = new Timer();

        //init of map
        level.map = new Map(Gdx.graphics.getWidth() * 100, Gdx.graphics.getHeight() * 100);

        //earth
        level.planets.add(new Planet(1000, 1000, 6 * 1e24f, 800, null, level.world,2));
        //moon
        level.planets.add(new Planet(6500, 7000, 6 * 1e24f, 100, null, level.world,3));

        //initialization of the rocket
        level.playable = new Playable(1000, 1500, 88, 108, 1e5f, 250 * BASE, 200 * BASE, 1000 * BASE, 5e5f, level.world);
        level.playable.getBody().setLinearVelocity(0, 30);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger moonTrig = new PositionTrigger(6500, 7000, 150, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("You've reached the Moon. Find the jurisdiction now!");
            }
        };
        level.triggers.add(moonTrig);

        final PositionTrigger earthTrig = new PositionTrigger(1000, 1000, 850, level.playable) {
            @Override
            public void triggerPerformed() {
                if (moonTrig.isTriggeredBefore()) {
                    System.out.println("Congratulations! You found a crashed spaceship at Moon. Our scientists are going to analyze it. Great Job! Level 2 is completed.");
                    //TODO: Next level should be given here. However, the method createLevel3() fails here.
                }
            }
        };
        level.triggers.add(earthTrig);

        //level starts here
        timer.start();

        //TODO: Some explosion at the Moon is needed.

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("You took off from the Earth because it looks like there is a crash at the Moon. Could they be ALIENS? Go check it out.");
                               Waypoint alienShip = new Waypoint(6500, 7105, 5);
                               level.waypoints.add(alienShip);
                           }
                       },
                5.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("You are heading towards to the natural satellite of the Earth: Moon.");
                           }
                       },
                20.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Moon is the closest neighbour of our planet in the space.");
                           }
                       },
                40.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("When you get close to the Moon, you have to be careful. You are very fast and you should make a smooth curve.");
                           }
                       },
                60.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("You have to bring anything you found in the accident area back to the Earth.");
                           }
                       },
                80.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Brace yourself for any possible alien attack or crash because of your bad piloting.");
                           }
                       },
                100.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("After your investigation, go back to Earth!");
                           }
                       },
                175.0f);

        return level;
    }

    public static Level createLevel3() {
        //Level Outline II, D part

        final Level level = new Level();
        Timer timer = new Timer();

        //init of map
        level.map = new Map(Gdx.graphics.getWidth() * 100, Gdx.graphics.getHeight() * 100);

        //earth
        level.planets.add(new Planet(1000, 1000, 6 * 1e24f, 800, null, level.world,4));

        //obstacles
        //TODO: Dispose method could be implemented for level class to remove the objects going out of the map and summoning new ones
        //loop for randomizing the movement directions, velocities, sizes and the shapes of the asteroids
        for (int i = 0; i < 50; i++) {
            Vector2 vector = new Vector2(((float) Math.random()) * (float) Math.pow(-1, i) * 20f, ((float) Math.random()) * (float) Math.random() * 20f);
            if (i % 2 == 0) {
                level.gameObjects.add(new RoundObstacle(((float) (Math.random()) * 8020), ((float) (Math.random()) * 3000) + 30 * i, 10, vector, level.world));
            } else {
                level.gameObjects.add(new RectangleObstacle(((float) (Math.random()) * 8020), ((float) (Math.random()) * 3000) + 30 * i, i * 2, i + 0.5f, vector, level.world));
            }
        }

        //initialization of the rocket
        level.playable = new Playable(1000, 1500, 88, 108, 1e5f, 250 * BASE, 200 * BASE, 1000 * BASE, 5e5f, level.world);
        level.playable.getBody().setLinearVelocity(0, 30);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger asteroidsPassed = new PositionTrigger(6500, 5000, 25, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Congratulations! You've managed to pass all of the obstacles without any collisions. We are right on track to reach Mars. Great Job Martian! Level 3 is completed.");
                //TODO: Next level should be given here. However, the method createLevel4() fails here.
            }
        };
        level.triggers.add(asteroidsPassed);

        //level starts here
        timer.start();

        //TODO: Take off animation is needed (it is not essential)

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("You took a SOS code from a who is at Mars right now. Your friend's ship stuck at the orbit of the Mars. To reach Mars, you have to travel far away. Begin!");
                               Waypoint endGame = new Waypoint(6500, 5000, 25);
                               level.waypoints.add(endGame);
                           }
                       },
                5.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("The distance between Earth and Mars is 225 million kilometers.");
                           }
                       },
                20.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Looks like there are lots of obstacles on your way. Try to pass through them.");
                           }
                       },
                40.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Be careful!");
                           }
                       },
                60.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Piloting is hard, isn't it?");
                           }
                       },
                80.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Avoid collisions with obstacles!");
                           }
                       },
                100.0f);

        return level;
    }

    public static Level createLevel4() {
        //Level Outline II, C part

        final Level level = new Level();
        Timer timer = new Timer();

        //init of map
        level.map = new Map(Gdx.graphics.getWidth() * 100, Gdx.graphics.getHeight() * 100);

        //earth
        level.planets.add(new Planet(6500, 5000, 6 * 1e24f, 900, null, level.world,5));

        //initialization of the rocket
        level.playable = new Playable(1000, 1000, 88, 108, 1e5f, 250 * BASE, 200 * BASE, 1000 * BASE, 5e5f, level.world);
        level.playable.getBody().setLinearVelocity(0, 0);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger marsTrig = new PositionTrigger(6500, 5000, 1000, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Help your friend!");
            }
        };
        level.triggers.add(marsTrig);

        final PositionTrigger friend = new PositionTrigger(6500, 5925, 25, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Congratulations! You saved your friend!");
                System.out.println("News: Scientists've discovered some clues about the aliens and their location from the ship that you had brought. Go back to Earth for the news.");
                //TODO: Next level should be given here. However, the method createLevel5() fails here.
            }
        };
        level.triggers.add(friend);

        //level starts here
        timer.start();

        //TODO: Take off animation is needed (it is not essential)

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Mars is close. Your friend needs help! GO!");
                               Waypoint friendShip = new Waypoint(6500, 5925, 25);
                               level.waypoints.add(friendShip);
                           }
                       },
                5.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("You have to pass through your friend's ship in order to save it from the orbit.");
                           }
                       },
                20.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("After saving the ship, go back to Earth.");
                           }
                       },
                40.0f);

        return level;
    }

    public static Level createLevel5() {
        //Level Outline II, E part

        final Level level = new Level();
        Timer timer = new Timer();

        //map
        level.map = new Map(Gdx.graphics.getWidth() * 100, Gdx.graphics.getHeight() * 100);

        //earth
        level.planets.add(new Planet(1000, 1000, 6 * 1e24f, 800, null, level.world, 2));
        //moon
        level.planets.add(new Planet(6500, 7000, 6 * 1e24f, 100, null, level.world, 3));

        //rocket
        level.playable = new Playable(7000, 7000, 88, 108, 1e5f, 250 * BASE, 200 * BASE, 1000 * BASE, 5e5f, level.world);
        level.playable.getBody().setLinearVelocity(45, 0);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger earthTrig = new PositionTrigger(1000, 1000, 850, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Congratulations! You've just landed Earth. Great Job! Level 5 is completed.");
                //TODO: Landing animation is required here.
                //TODO: Next level should be given here. However, the method createLevel6() fails here.
            }
        };
        level.triggers.add(earthTrig);

        //level starts here
        timer.start();

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("In order to check out the news. Land on the Earth.");
                               Waypoint earth = new Waypoint(1000, 1805, 5);
                               level.waypoints.add(earth);
                           }
                       },
                5.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("You have to make a gravity-assisted landing.");
                           }
                       },
                20.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Spaceships usually use this method to land on the Earth.");
                           }
                       },
                40.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Moon is an important object during the process of gravity-assisted landing.");
                           }
                       },
                60.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Are you excited for the news about the aliens?");
                           }
                       },
                80.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Time for some alien investigation!");
                           }
                       },
                100.0f);

        return level;
    }

    public static Level createLevel6() {
        //Level Outline II, F part

        final Level level = new Level();
        Timer timer = new Timer();

        //map
        level.map = new Map(Gdx.graphics.getWidth() * 100, Gdx.graphics.getHeight() * 100);

        //earth
        level.planets.add(new Planet(1000, 1000, 6 * 1e24f, 800, null, level.world,6));
        //secretPlanet
        level.planets.add(new Planet(6500, 7000, 6 * 1e24f, 100, null, level.world,7));

        //planet1
        level.planets.add(new Planet(2500, 2000, 6 * 1e24f, 650, null, level.world,1));
        //planet2
        level.planets.add(new Planet(3750, 4200, 2.7f * 1e21f, 500, null, level.world,2));
        //planet3
        level.planets.add(new Planet(3200, 1220, 6 * 1e24f, 350, null, level.world,3));

        //rocket
        level.playable = new Playable(1000, 1850, 88, 108, 1e5f, 250 * BASE, 200 * BASE, 1000 * BASE, 5e5f, level.world);
        level.playable.getBody().setLinearVelocity(0, 0);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger earthTrig = new PositionTrigger(1000, 1000, 850, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Congratulations! You've just landed Earth. Great Job! Level 5 is completed.");
                //TODO: Landing animation is required here.
                //TODO: Next level should be given here. However, the method createLevel6() fails here.
            }
        };
        level.triggers.add(earthTrig);

        //add important endgame triggers here
        final PositionTrigger planet1 = new PositionTrigger(2500, 2000, 700, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("What a beautiful planet it is. However, there is no sign of life here.");
                Waypoint secondPlanet = new Waypoint(3750, 4705, 5);
                level.waypoints.add(secondPlanet);
            }
        };
        level.triggers.add(planet1);

        final PositionTrigger planet2 = new PositionTrigger(3750, 4200, 600, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Nothing over here! Lets check the others.");
                Waypoint thirdPlanet = new Waypoint(3200, 1375, 5);
                level.waypoints.add(thirdPlanet);
            }
        };
        level.triggers.add(planet2);

        final PositionTrigger planet3 = new PositionTrigger(3200, 1220, 500, level.playable) {
            @Override
            public void triggerPerformed() {
                if (planet1.isTriggered() && planet2.isTriggeredBefore()) {
                    System.out.println("I have a bad feeling about this planet. Move on!");
                    Waypoint secretOne = new Waypoint(6500, 7105, 5);
                    level.waypoints.add(secretOne);
                } else {
                    System.out.println("It seems we skipped one planet which aliens might be there!");
                }
            }
        };
        level.triggers.add(planet3);

        final PositionTrigger secret = new PositionTrigger(6500, 7000, 100, level.playable) {
            @Override
            public void triggerPerformed() {
                if (planet1.isTriggeredBefore() && planet2.isTriggeredBefore() && planet3.isTriggered())
                    System.out.println("Oh My God! ALIENS! They are here, they have a planet called Oz-Turca and they call themselves Oz-Jans.");
                    //TODO: Alien & Human Gardasligi animasyonu. GAME OVER HERE
                else
                    System.out.println("I think we missed some planets. Before looking into this.");
            }
        };
        level.triggers.add(secret);

        //level starts here
        timer.start();

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Is this real? There are evidence of existence of aliens. Your mission is to find their planet in the space. There are several planets. Start searching!");
                               Waypoint firstPlanet = new Waypoint(3750, 4805, 5);
                               level.waypoints.add(firstPlanet);
                           }
                       },
                5.0f);

        return level;
    }

    //trigger methods
    private static void addDefaultTriggers(final Level level) {
        level.triggers.add(new OutOfMapTrigger(level.map, level.playable) {
            @Override
            public void triggerPerformed() {
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
    //endregion
}
