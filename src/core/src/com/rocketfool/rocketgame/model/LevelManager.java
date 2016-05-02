package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.rocketfool.rocketgame.controller.WorldController;
import com.rocketfool.rocketgame.util.Constants;

import static com.rocketfool.rocketgame.model.Playable.BASE;

/**
 * Designs and initiates levels and manages them.
 */
public class LevelManager {
    /**
     * message display time
     */
    static float time;

    //region Methods

    /**
     * Test Level
     */
    public static Level createLevel0() {
        final Level level = new Level();
        level.levelNo = 0;

        level.map = new Map(Gdx.graphics.getWidth() * 500, Gdx.graphics.getHeight() * 500);
        level.playable = new Playable(1000, 1000, 88, 108, 1e5f, 750 * BASE, 200 * BASE, 1000 * BASE, 1.0e5f, level.world);
        return level;
    }

    public static Level createLevel1() {
        final Level level = new Level();
        level.setState(Level.State.PAUSED);
        level.levelNo = 1;
        final Popup popup = level.popup;
        final ObjectiveWindow objectiveWindow = level.objectiveWindow;
        popup.setTitle("HQ");

        //levelMap
        level.map = new Map(Gdx.graphics.getWidth() * 600, Gdx.graphics.getHeight() * 700);

        //Earth
        level.planets.add(new Planet(14000, 9000, 6.0f * 1e25f, 650, null, level.world, 1));
        //Moon
        level.planets.add(new Planet(7000, 12000, 1.0f * 1.0e25f, 170, level.planets.get(0), level.world, 3));
        level.planets.get(1).setOrbitPreset(true);
        //initialization of the rocket
        level.playable = new Playable(20000, 18000, 88, 108, 1e5f, 750 * BASE, 200 * BASE, 1000 * BASE, 0.75e5f, level.world);
        level.playable.getBody().setLinearVelocity(5f, 2f);
        level.playable.getBody().setAngularVelocity(5f);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger earthTrig = new PositionTrigger(14000, 9000, 1000, level.playable) {
            @Override
            public void triggerAction() {
                if (level.playable.getBody().getLinearVelocity().len() < 80) {
                    WorldController.controlState = 7;
                    level.setState(Level.State.LEVEL_FINISHED);
                }
            }
        };
        level.triggers.add(earthTrig);

        //level starts here
        time = 6f;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               level.screen.getMinimap().setEnabled(false);
                               TrajectorySimulator.enabled = false;
                               WorldController.controlState = 1;
                           }
                       },
                time);
        time += 2; //<-- this float shows the duration to display the message above
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("What was THAT? A strange meteor whizzing by left you spinning chaotically in space!" +
                                       "You must regain control! But don't worry, as you guide I, Professor Tekman will help you!");
                               objectiveWindow.setText("Regain control of the ship.");
                           }
                       },
                time);
        time += 16;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Use RIGHT & LEFT arrow keys to control angular movement.");
                               popup.setText("Now stop that spinning.");
                               WorldController.controlState = 2;
                           }
                       },
                time);

        level.triggers.add(new Trigger() {
            @Override
            public boolean isTriggeredInternal() {
                return WorldController.controlState == 2 && level.playable.getBody().getAngularVelocity() < 0.25;
            }

            @Override
            public void triggerAction() {
                popup.setText("SAS restored! \n The RIGHT SHIFT key toggles the SAS, which automatically reduces spinning to 0.");
                WorldController.controlState = 3;
            }
        });

        level.triggers.add(new Trigger() {
            @Override
            public boolean isTriggeredInternal() {
                return WorldController.controlState == 3 && level.playable.getBody().getAngularVelocity() == 0 && level.playable.getSASEnabled();
            }

            @Override
            public void triggerAction() {

                popup.setText("That's better! Now use the UP & DOWN arrow keys to increase/ decrease" +
                        "your thrust. \n Try to reduce your velocity to zero " +
                        "(The velocity display is at the top).");
                WorldController.controlState = 4;
            }
        });
        level.triggers.add(new Trigger() {
            @Override
            public boolean isTriggeredInternal() {
                return WorldController.controlState == 4 && (level.playable.getBody().getLinearVelocity().len() < 1.5);
            }

            @Override
            public void triggerAction() {
                popup.setText("OK! Now let's get our bearings! \n Use A & S keys to zoom out or zoom in, and press ESC for the Pause Menu." +
                        " Then let's turn up those engines!");
                level.playable.setCurrentThrust(0);
                WorldController.controlState = 5;
            }
        });

        level.triggers.add(new Trigger() {
            @Override
            public boolean isTriggeredInternal() {
                return WorldController.controlState == 5 && level.playable.getCurrentThrust() > 100;
            }

            @Override
            public void triggerAction() {
                TrajectorySimulator.enabled = true;
                popup.setText("Supercomputer connections restored! Trajectory calculation features online." +
                        " (Press T to activate it. It doesn't work unless you're moving.)");
                WorldController.controlState = 6;

                time = 5f;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       level.screen.getMinimap().setEnabled(true);
                                       popup.setText("Navigation system restored! Minimap online Look's like Earth's not too far away.");
                                   }
                               },
                        time);
                time += 8f;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       popup.setText("All systems restored! Now it's time to find your way back home! " +
                                               "This blue waypoint will guide you to your objective!");
                                       level.waypoint = new Waypoint(level, 14000, 9000, 1000);
                                       objectiveWindow.setText("Find your way back to Earth");
                                   }
                               },
                        time);
                time += 15;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       popup.setText("As said by Newton's first law of motion, unless a force (like thrust) " +
                                               "acts on you, you would drift at constant speed for ever! So better not run out of fuel in space!");
                                   }
                               },
                        time);
                time += 23;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       popup.setText("As Newton's second law of motion says, force is the rate of change of a body's" +
                                               "momentum, which its likelihood of preserving its speed. \n" +
                                               "The rocket is pretty heavy, so accelerating takes time. Be careful, because "
                                               + "that means stopping takes time too!");
                                   }
                               },
                        time);
                time += 28;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       popup.setText("See that trail behind the rocket? As Newton's third law of motion says, " +
                                               "forces exist in opposite-directioned pairs. \n" +
                                               "That's why you need to blast away tons of hot plasma to accelerate!");
                                   }
                               },
                        time);
                time += 23;
                Timer.schedule(new Timer.Task() {
                                   @Override
                                   public void run() {
                                       popup.setText("Remember to slow down near Earth! Coming in too fast will make you crash!");
                                   }
                               },
                        time);

            }
        });

        return level;
    }

    public static Level createLevel2() {
        Timer.instance().stop();
        final Level level = new Level();
        level.levelNo = 2;
        level.timer = new Timer();
        final Popup popup = level.popup;
        final ObjectiveWindow objectiveWindow = level.objectiveWindow;
        popup.setTitle("HQ");

        //init of map
        level.map = new Map(1000 * 600, 720 * 600);

        //Earth
        Planet earth = new Planet(14000, 11000, 6.0f * 1.0e25f, 650, null, level.world, 8);
        level.planets.add(earth);
        //Moon
        Planet moon = new Planet(21000, 14000, 1.0f * 1.0e25f, 170, earth, level.world, 9);
        level.planets.add(moon);
        moon.setOrbitPreset(true);
        level.solidObjects.add(new MoonAsteroid(moon, 3.25e2f, 40, level.world));
        //initialization of the rocket
        level.playable = new Playable(14550, 11550, 88, 108, 1e5f, 750 * BASE, 200 * BASE, 1000 * BASE, 1.0e5f, level.world);
        level.playable.getBody().setLinearVelocity(50f, -50f);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers & waypoints

        final PositionTrigger outOfEarthTrig = new PositionTrigger(14000, 11000, 800, level.playable, true) {
            @Override
            public void triggerAction() {
                if (time % 100 < 90) {
                    popup.setText("I'm impressed that you are out of orbit already. But don't get too exited. Get close to the Moon.");
                    System.out.println(time % 100);
                } else {
                    popup.setText("Great, now that you're free from Earth's orbit");
                    System.out.println(time % 100);
                }
                objectiveWindow.setText("Examine the object on the Moon's orbit");
            }
        };
        level.triggers.add(outOfEarthTrig);

        final PositionTrigger moonTrig = new PositionTrigger(level.planets.get(1), 0, 0, 450, level.playable) {
            @Override
            public void triggerAction() {
                //(Half way through mission)
                if (level.playable.getBody().getLinearVelocity().len() < 70) {
                    popup.setText("You've reached the Moon. And what strange things have we found here? Better take it back to Earth!");
                    objectiveWindow.setText("Return to Earth");
                    // FIXME: waypointleri tekrar düşün rip
                    //level.waypoints.removeIndex(0);
                    //level.waypoints.add(new Waypoint(14000, 11000, 800));
                }
            }
        };
        level.triggers.add(moonTrig);
        level.waypoint = new Waypoint(level, moonTrig);

        final PositionTrigger earthTrig = new PositionTrigger(14000, 11000, 750, level.playable) {
            @Override
            public void triggerAction() {
                if (moonTrig.isTriggeredBefore()) {
                    if (level.playable.getBody().getLinearVelocity().len() < 70) {
                        level.setState(Level.State.LEVEL_FINISHED);
                        //TODO end of level screen
                        //Title: "Mission Accomplished!"
                        //Text: "Congratulations! Our researchers will examine this craft! It looks like we've finally been visited by aliens!");
                    }

                }
            }
        };

        level.waypoint = new Waypoint(level, moonTrig);

        //level starts here
        time = 5f;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("You are on the Earth's orbit right now."
                                       + " Camera controls restored.");
                               objectiveWindow.setText("Examine the object on the Moon's orbit");
                               WorldController.controlState = -1;
                               if (Constants.DEBUG)
                                   WorldController.controlState = 7;
                           }
                       },
                time);
        time += 12;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               if (!outOfEarthTrig.isTriggeredInternal()) {
                                   popup.setText("The Moon might be too far to see, but your minimap can help you find it.");
                               }
                           }
                       },
                time);
        time += 5;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               if (!outOfEarthTrig.isTriggeredInternal()) {
                                   popup.setText("Thrusting straight towards the Moon would be inefficient and difficult " +
                                           "because you work directly against Earth's gravity. \n"
                                           + "Orbits are weird like that...");
                               }
                           }
                       },
                time);
        time += 10;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               if (!outOfEarthTrig.isTriggeredInternal()) {
                                   popup.setText("First let's quickly recall Newton's Universal Law of Gravitation: \n" +
                                           "F = G * M * m / r^2 \n " +
                                           "You probably remember this equation, but don't forget that r is " +
                                           "squared, so gravity weakens quickly as you get farther away from a planet.");
                               }
                           }
                       },
                time);
        time += 12;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               if (!outOfEarthTrig.isTriggeredInternal()) {
                                   popup.setText("Anyway, orbits can be regarded as continuous falling due to gravity, " +
                                           "but actually going too fast to hit the ground, like in Newton's famous " +
                                           "cannonball model, which... ");
                               }
                           }
                       },
                time);
        time += 10;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               if (!outOfEarthTrig.isTriggeredInternal()) {
                                   popup.setText("Sorry, I'm getting carried away. Returning to the situation at hand... ");
                               }
                           }
                       },
                time);
        time += 6;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               if (!outOfEarthTrig.isTriggeredInternal()) {
                                   popup.setText("Perhaps you can do a Hohmann transfer to work with the gravity " +
                                           "instead of against it? ");
                               }
                           }
                       },
                time);
        time += 7;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               if (!outOfEarthTrig.isTriggeredInternal()) {
                                   popup.setText(
                                           "Control systems are online." +
                                                   "The Hohmann Transfer: \n" +
                                                   "1. Fire your engines in the way you are flying in your orbit ('prograde'). \n" +
                                                   "2. This will raise the highest point of your orbit (the apoapsis) on the " +
                                                   "other side of the planet. Wait until you get there. \n" +
                                                   "3. Then burn prograde again and there! You've efficiently raised your orbit"
                                                   + "altitude! Now give it a try!");
                               }
                               WorldController.controlState = 6;
                           }
                       },
                time);
        time += 33;
        /// Z-X for only DeBug, players should not able to use these??
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               if (!outOfEarthTrig.isTriggeredInternal()) {
                                   popup.setText(
                                           "While you are approaching the moon \n" +
                                                   "Your speed should be less than half."
                                   );
                                   WorldController.controlState = 7;
                               }
                           }
                       },
                time);
        time += 7;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Finally, I should remind you to watch your fuel levels! Good luck!");
                           }
                       },
                time);
        time += 7;
        return level;
    }

    public static Level createLevel3() {
        Timer.instance().stop();
        final Level level = new Level();
        level.levelNo = 3;
        level.timer = new Timer();
        final Popup popup = level.popup;
        final ObjectiveWindow objectiveWindow = level.objectiveWindow;
        popup.setTitle("HQ");
        WorldController.controlState = 7;

        //init of map
        int width = Gdx.graphics.getWidth() * 250;
        int height = Gdx.graphics.getHeight() * 250;
        level.map = new Map(width, height);

        //obstacles
        //Loop for randomizing the movement directions, velocities, sizes and the shapes of the asteroids
        for (int i = 0; i < 150; i++) {
            Vector2 speed = new Vector2(((float) Math.random()) * (float) Math.pow(-1, i) * 20f, ((float) Math.random()) * (float) Math.random() * 20f);
            //Vector2
            if (i % 2 == 0) {
                level.solidObjects.add(new RoundObstacle(((float) (Math.random()) * 17500) + 500f, ((float) (Math.random()) * 10000) + 500f + 30 * i / 2, 10 + i / 2, speed, level.world));
            } else {
                level.solidObjects.add(new RectangleObstacle(((float) (Math.random()) * 17500) + 500f, ((float) (Math.random()) * 10000) + 500f + 30 * i / 2, i / 2, i / 3, speed, level.world));
            }
        }
        //TODO: Dispose method could maybe be implemented for level class to remove the objects going out of the map and summoning new ones

        //initialization of the rocket
        level.playable = new Playable(700, 700, 88, 108, 1e5f, 950 * BASE, 200 * BASE, 1000 * BASE, 1.5e5f, level.world);
        level.playable.getBody().setLinearVelocity(2, 3);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger asteroidsPassed = new PositionTrigger(17000, 10500, 750, level.playable) {
            @Override
            public void triggerAction() {
                level.setState(Level.State.LEVEL_FINISHED);
                //TODO End of level popup here
                //Text: "Congratulations! You exhibited some nice piloting! We are right on the track to reach Mars. Great Job Martian!"
            }
        };
        level.triggers.add(asteroidsPassed);
        level.waypoint = new Waypoint(level, asteroidsPassed);
        //level starts here
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               level.screen.getMinimap().setEnabled(false);
                           }
                       },
                0.5f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("The reason we are not getting enough signal should be these meteors." +
                                       "But we need to pass them first. But \n" +
                                       "Your scanners don't seem to pick them up!");
                               objectiveWindow.setText("Reach Mars");
                           }
                       },
                18.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Space might not be so empty after all. Even the smallest rock or space junk piece can cause serious damage to the craft," +
                                       " especially at higher speeds, so it would be best to avoid even touching anything!");
                           }
                       },
                30.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Be extra careful because some of them are moving too!");
                           }
                       },
                35.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Did you know? \n The distance between Earth and Mars is 225 million kilometers.");
                           }
                       },
                45.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Some of the rocks looks weird here! It seems matter in these rocks are not in the periodic table." + "\n"
                               + "It should be one which hit us!");
                           }
                       },
                75.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("It is better to stay away from them now, our friend needs our help.");
                           }
                       },
                105.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Did you know that if two pieces of the same type of metal touch in space, " +
                                       "they will bond and be permanently stuck together.");
                           }
                       },
                135.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Did you know that on Venus a day is longer than a year.");
                           }
                       },
                165.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Did you know that the Moon is very slowly drifting away from Earth?");
                           }
                       },
                195.0f);

        return level;
    }

    public static Level createLevel4() {
        Timer.instance().stop();
        final Level level = new Level();
        level.levelNo = 4;
        level.timer = new Timer();
        final Popup popup = level.popup;
        final ObjectiveWindow objectiveWindow = level.objectiveWindow;
        popup.setTitle("HQ");
        WorldController.controlState = 7;

        //init of map
        level.map = new Map(Gdx.graphics.getWidth() * 300, Gdx.graphics.getHeight() * 300);

        //Mars
        level.planets.add(new Planet(11000, 7000, 8e25f, 700, null, level.world, 10));

        //initialization of the rocket
        level.playable = new Playable(7500, 9000, 88, 108, 1e5f, 750 * BASE, 200 * BASE, 1000 * BASE, 1.5e5f, level.world);
        level.playable.getBody().setLinearVelocity(0, 0);

        //Stranded spacecraft
        level.solidObjects.add(new Playable(4000, 4000, 750, 750, 1e5f, 0, 0, 0, 0, level.world));
        ((SolidObject) level.solidObjects.get(0)).setOrbitPreset(true);//TODO set width,height to 1,1 to avoid collisions (for now needed for visibility)
        //Collisions are almost impossible with 1,1 because when <750px this despawns anyway
        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger endTrig = new PositionTrigger(5000, 10000, 1000f, level.playable) {
            @Override
            public void triggerAction() {
                //Popup Text:("Congratulations! You saved your friend! Looks like he is interesting information about the aliens too!!");}
            }
        };

        final PositionTrigger craftTrig = new PositionTrigger(((SolidObject) level.solidObjects.get(0)), 0, 0, 150f, level.playable) {
            @Override
            public void triggerAction() {
                //(Halfway point)
                popup.setText("Great work! Now let's head back!");
                objectiveWindow.setText("Head home, towards Earth");
                level.waypoint = new Waypoint(level, 5000, 10000, 1000f);
                level.triggers.add(endTrig);
            }
        };
        level.triggers.add(craftTrig);
        level.waypoint = new Waypoint(level, craftTrig);

        //level starts here
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               objectiveWindow.setText("Save your friend!");
                               popup.setText("Your friend's in sight in low orbit! But be careful with the strong " +
                                       "gravity! All that fuel and life support you're carrying is making you heavy!");
                           }
                       },
                3.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("You have to pass over your friend's ship in order to save them from the orbit.");
                           }
                       },
                18.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("You'll need to orbit Mars carefully to catch up with them! Because remember," +
                                       "although the total energy of a body in orbit is conserved, it continuously changes form " +
                                       "between gravitational potential energy (high up) kinetic energy (near the surface). ");
                           }
                       },
                25.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Therefore, expect to orbit faster at lower altitudes to chase them or take a high" +
                                       "orbit to wait for them to come near you.");
                           }
                       },
                35.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Did you know that all of space is completely silent? You hear your engine because there" +
                                       " is air around you, but nothing beyond that!");
                           }
                       },
                65.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Did you know that if you put Saturn in water it would float?");
                           }
                       },
                95.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Did you know that the hottest planet is not the closest planet to the Sun?");
                           }
                       },
                125.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Did you know that he full cost of a spacesuit like the one" +
                                       " that you are wearing is about $11 million although 70% of this is for " +
                                       "the backpack and the control module?");
                           }
                       },
                155.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Did you know that neutron stars can spin at a rate of 600 rotations per second?");
                           }
                       },
                185.0f);
        return level;
    }

    /**
     * This level was cut for being too difficult to balance and too difficult to play.
     */
    public static Level createLevelX() {
        final Level level = new Level();
        level.levelNo = 5;
        level.timer = new Timer();
        final Popup popup = level.popup;
        final ObjectiveWindow objectiveWindow = level.objectiveWindow;
        popup.setTitle("HQ");

        //map
        level.map = new Map(Gdx.graphics.getWidth() * 500, Gdx.graphics.getHeight() * 500);

        //earth
        level.planets.add(new Planet(5000, 5000, 6 * 1e24f, 800, null, level.world, 2));
        //moon
        level.planets.add(new Planet(12500, 13000, 6 * 1e24f, 100, null, level.world, 3));

        //rocket
        level.playable = new Playable(13000, 13000, 88, 108, 1e5f, 250 * BASE, 200 * BASE, 1000 * BASE, 5e5f, level.world);
        level.playable.getBody().setLinearVelocity(45, -5);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger earthTrig = new PositionTrigger(5000, 5000, 850, level.playable) {
            @Override
            public void triggerAction() {
                //System.out.println("Congratulations! You've just landed Earth. Great Job! Level 5 is completed.");
                popup.setText("Congratulations! You've just landed Earth. Great Job! Level 5 is completed.");
                // end of level
            }
        };
        level.triggers.add(earthTrig);

        //level starts here
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("In order to check out the news. Land on the Earth.");
                               level.waypoint = new Waypoint(level, 5000, 5805, 10);
                               objectiveWindow.setText("Land on the Earth.");
                               popup.setText("In order to check out the news. Land on the Earth.");
                           }
                       },
                5.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("You have to make a gravity-assisted landing.");
                               popup.setText("You have to make a gravity-assisted landing.");
                           }
                       },
                20.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("Spaceships usually use this method to land on the Earth.");
                               popup.setText("Spaceships usually use this method to land on the Earth.");
                           }
                       },
                40.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("Moon is an important object during the process of gravity-assisted landing.");
                               popup.setText("Moon is an important object during the process of gravity-assisted landing.");
                           }
                       },
                60.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("Are you excited for the news about the aliens?");
                               popup.setText("Are you excited for the news about the aliens?");
                           }
                       },
                80.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("Time for some alien investigation!");
                               popup.setText("Time for some alien investigation!");
                           }
                       },
                100.0f);

        return level;
    }

    public static Level createLevel5() {
        Timer.instance().stop();
        //Previously Level 6. Don't delete this one. :)
        /* For this level
         * 
         * TODO: are trigger and waypoint places correct?
         * TODO: simple crosshairs sprite for all waypoints in this level
         *
         * Others
         * TODO update GameUtils.levelsCleared (all levels)
         * TODO check save/load function.
         * TODO restore max zoom to 200-400 instead of 550
         * TODO endgame popups/screens for out-of-map, no fuel, etc.
         * TODO play-testing all levels
         * TODO spelling checks
         * TODO code clean-up
         * TODO everything else that I'm forgetting
         */

        final Level level = new Level();
        level.levelNo = 5;
        level.timer = new Timer();
        final Popup popup = level.popup;
        final ObjectiveWindow objectiveWindow = level.objectiveWindow;
        popup.setTitle("HQ");
        WorldController.controlState = 7;

        //map
        level.map = new Map(Gdx.graphics.getWidth() * 600, Gdx.graphics.getHeight() * 600);

        //secretPlanet
        level.planets.add(new Planet(16000, 15000, 1.0e25f, 125, null, level.world, 7));
        //planet0
        level.planets.add(new Planet(11000, 13000, 4.0e25f, 800, null, level.world, 6));
        //planet1
        level.planets.add(new Planet(5500, 5500, 3.25e25f, 650, null, level.world, 1));
        //planet2
        level.planets.add(new Planet(14000, 3700, 2.5e25f, 500, null, level.world, 4));
        //planet3
        level.planets.add(new Planet(8200, 7200, 1.75e25f, 350, null, level.world, 3));

        //rocket
        level.playable = new Playable(4000, 4000, 88, 108, 1e5f, 750 * BASE, 200 * BASE, 1000 * BASE, 1.5e5f, level.world);

        //default Triggers
        addDefaultTriggers(level);

        //waypoints
        /*
        Waypoint zerothPlanet = new Waypoint(11000, 11500, 5);
        level.waypoints.add(zerothPlanet);
        Waypoint firstPlanet = new Waypoint(5500, 4600, 5);
        level.waypoints.add(firstPlanet);
        Waypoint secondWaypoint = new Waypoint(1400, 3700, 5);
        level.waypoints.add(secondWaypoint);
        Waypoint thirdPlanet = new Waypoint(8650, 7200, 5);
        level.waypoints.add(thirdPlanet);*/
        // u wot m8

        //FIXME: fix these waypoints, make them in order

        //endGame Triggers
        final PositionTrigger planet0 = new PositionTrigger(11000, 13000, 1500, level.playable) {
            @Override
            public void triggerAction() {
                popup.setText("This is not the planet you are looking for. Move along!");
                //level.waypoints.removeIndex(0);
            }
        };
        level.triggers.add(planet0);

        final PositionTrigger planet1 = new PositionTrigger(5500, 5500, 1000, level.playable) {
            @Override
            public void triggerAction() {
                popup.setText("What a beautiful planet this is! However, there is no sign of life here.");
                objectiveWindow.setText("Continue investigating planets");
                //level.waypoints.removeIndex(1);

            }
        };
        level.triggers.add(planet1);

        final PositionTrigger planet2 = new PositionTrigger(14000, 3700, 850, level.playable) {
            @Override
            public void triggerAction() {
                popup.setText("Nothing to do here! I don't want to be on this planet anymore.");
                //level.waypoints.removeIndex(2);

            }
        };
        level.triggers.add(planet2);

        final PositionTrigger planet3 = new PositionTrigger(8200, 7200, 700, level.playable) {
            @Override
            public void triggerAction() {
                //if (planet1.isTriggered() && planet2.isTriggeredBefore()) {
                popup.setText("I have a bad feeling about this planet. Let's move on!");
                //level.waypoints.removeIndex(3);
                //}
                //else {
                //    popup.setText("It seems we skipped one planet on which aliens might be living!");
                //}

            }
        };
        level.triggers.add(planet3);

        //add final waypoint for the secret planet
        if (planet0.isTriggeredBefore() && planet1.isTriggeredBefore() && planet2.isTriggeredBefore() && planet3.isTriggered()) {
            //Waypoint secretOne = new Waypoint(16000, 14750, 5);
            //level.waypoints.add(secretOne);
        }
        final PositionTrigger secret = new PositionTrigger(16000, 15000, 300, level.playable) {
            @Override
            public void triggerAction() {

                if (planet0.isTriggeredBefore() && planet1.isTriggeredBefore() && planet2.isTriggeredBefore() && planet3.isTriggeredBefore()) {
                    level.setState(Level.State.GAME_OVER);

                } else {
                    popup.setText("We better check all the other planets before looking here.");
                }

            }
        };
        level.triggers.add(secret);

        //level starts here
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("Is this really happening? As there now is evidence of existence of aliens, " +
                                       "your mission is to find their home planet in the space. There are several planets here," +
                                       "but our research suggests they're one one of these. Start searching!");
                               objectiveWindow.setText("Find the mysterious planet of the aliens");
                           }
                       },
                5.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popup.setText("...and there seems to be no sign of intelligent life anywhere...");
                           }
                       },
                120.0f);

        return level;
    }

    //trigger methods
    private static void addDefaultTriggers(final Level level) {
        // out of map trigger
        level.triggers.add(new Trigger() {
            @Override
            protected boolean isTriggeredInternal() {
                Vector2 pos = level.playable.getBody().getPosition();
                int width = level.map.getWidth();
                int height = level.map.getHeight();

                return pos.x < 0 || pos.y < 0 || pos.x > width || pos.y > height;
            }

            @Override
            public void triggerAction() {
                if (!Constants.DEBUG) {
                    level.healthLost();
                }
            }
        });

        // fuel depletion trigger
        level.triggers.add(new Trigger() {
            @Override
            protected boolean isTriggeredInternal() {
                return level.playable.getFuelLeft() <= 0;
            }

            @Override
            public void triggerAction() {
                if (!Constants.DEBUG) {
                    level.healthLost();
                }
            }
        });
    }
    //endregion
}
