package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import static com.rocketfool.rocketgame.model.Playable.BASE;

/**
 * Designs and initiates levels and manages them.
 */
public class LevelManager {
    //region Methods
    public static Level createLevel1() {
        //Level Outline II, A part

        final Level level = new Level();
        Timer timer = new Timer();
        final PopUp popUp = new PopUp();
        final ObjectiveWindow objectiveWindow = new ObjectiveWindow();
        popUp.setTitle("HQ");

        //levelMap
        level.map = new Map(Gdx.graphics.getWidth() * 400, Gdx.graphics.getHeight() * 400);

        //Earth
        level.planets.add(new Planet(11000, 6000, 6 * 1e24f, 800, null, level.world));
        //TODO add the Moon (because it should exist)
        //initialization of the rocket
        level.playable = new Playable(16500, 12000, 88, 108, 1e5f, 400 * BASE, 200 * BASE, 1000 * BASE, 2e5f, level.world);
        level.playable.getBody().setLinearVelocity(5f, 2f);
        level.playable.getBody().setAngularVelocity(5f);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger earthTrig = new PositionTrigger(11000, 6000, 1500, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Congratulations! You regained the control of the ship and reached the Earth. Level 1 is completed.");
                popUp.setText("Mission Accomplished!!!");
                //TODO: End of level popup to move to menu or next level or restart
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
                               popUp.setText("Your bad luck during the pursuit of the aliens left you spinning out of control in space!" +
                                       "The ship is damaged.! You must regain control!");
                           }
                       },
                5.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Use RIGHT & LEFT arrow keys to regain control of your angular movement.");
                               popUp.setText("Use RIGHT & LEFT arrow keys to regain control of your angular movement.");
                               objectiveWindow.setText("Regain control of the ship");
                           }
                       },
                15.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("The Stability Assist System restored!" +
                                       "The RIGHT SHIFT key toggles the SAS, which automatically reduces spinning.");
                               popUp.setText("The RIGHT SHIFT key toggles the SAS, which automatically reduces spinning.");
                           }
                       },
                25.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("The RIGHT SHIFT key toggles the SAS, which automatically reduces spinning.");
                               popUp.setText("The RIGHT SHIFT key toggles the SAS, which automatically reduces spinning.");
                           }
                       },
                30.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Use UP & DOWN arrow keys to increase and decrease the thrust.");
                               popUp.setText("Use UP & DOWN arrow keys to increase and decrease the thrust.");
                           }
                       },
                35.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Newton states that a body will continue to move at constant speed or continue to not move at all, " +
                                       "unless a force is being applied to it. In outer space, that could mean moving forever…");
                               popUp.setText("Newton states that a body will continue to move at constant speed or continue to not move at all, " +
                                       "unless a force is being applied to it. In outer space, that could mean moving forever…");
                           }
                       },
                80.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Use A & S keys to zoom out or zoom in, press ESC for the Pause Menu.");
                               popUp.setText("Use A & S keys to zoom out or zoom in, press ESC for the Pause Menu.");
                           }
                       },
                40.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Supercomputer connections restored! Trajectory calculation features online." +
                                       " (Press T to activate it)");
                               popUp.setText("Supercomputer connections restored! Trajectory calculation features online." +
                                       " (Press T to activate it)");
                           }
                       },
                45.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("The yellow dots in front of you simulates your future motion.");
                               popUp.setText("The yellow dots in front of you simulates your future motion.");
                           }
                       },
                50.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Navigation system restored! Minimap online!.");
                               popUp.setText("Navigation system restored! Minimap online!.");
                           }
                       },
                60.0f);
        //TODO enable minimap here
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("All systems restored. Additional information is along the top of the screen.");
                               popUp.setText("All systems restored. Additional information is along the top of the screen.");
                           }
                       },
                65.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Now it's time to find your way back!");
                               popUp.setText("Now it's time to find your way back!");
                               objectiveWindow.setText("Find your way back home!");
                           }
                       },
                70.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("An orbit around a planet depends on 3 things: the masses of the orbiter and the planet," +
                                       " the distance between them, and the velocity of the orbiter");
                               popUp.setText("An orbit around a planet depends on 3 things: the masses of the orbiter and the planet," +
                                       " the distance between them, and the velocity of the orbiter");
                           }
                       },
                80.0f);


        return level;
    }

    public static Level createLevel2() {
        //Level Outline II, B part

        final Level level = new Level();
        Timer timer = new Timer();
        final PopUp popUp = new PopUp();
        final ObjectiveWindow objectiveWindow = new ObjectiveWindow();
        popUp.setTitle("HQ");

        //init of map
        level.map = new Map(Gdx.graphics.getWidth() * 300, Gdx.graphics.getHeight() * 300);

        //earth
        level.planets.add(new Planet(9000, 7000, 8.9f * 1e24f, 750, null, level.world));
        //moon
        level.planets.add(new Planet(14000, 8000, 2.3f * 1e24f, 200, null, level.world));

        //initialization of the rocket
        level.playable = new Playable(9650, 6350, 88, 108, 2e5f, 250 * BASE, 200 * BASE, 1000 * BASE, 1.5e5f, level.world);
        level.playable.getBody().setLinearVelocity(-18f, -18f);                                       //**TODO

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger moonTrig = new PositionTrigger(14200, 8000, 100, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("You've reached the Moon. Do what you can do and come back home!");
                popUp.setText("You've reached the Moon. Do what you can do and come back home!");
                objectiveWindow.setText("Return home again");
            }
        };
        level.triggers.add(moonTrig);

        final PositionTrigger earthTrig = new PositionTrigger(1000, 1000, 850, level.playable) {
            @Override
            public void triggerPerformed() {
                if (moonTrig.isTriggeredBefore()) {
                    System.out.println("Congratulations! Our researchers will be able to find our instructor by examining this craft!!!");
                    popUp.setText("Congratulations! Our researchers will be able to find our instructor by examining this craft!!!");
                    //TODO: Next level should be given here. However, the method createLevel3() fails here.
                }
            }
        };
        level.triggers.add(earthTrig);

        //level starts here
        timer.start();

        //TODO: Crashed UFO at the Moon is needed (simple waypoint image)

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               level.screen.setZoom(6f); //FIXME
                           }
                       },
                0.001f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("You took off from the Earth because it looks like there is a crash at the Moon. Could they be ALIENS? Go check it out.");
                               Waypoint alienShip = new Waypoint(6500, 7105, 5);
                               level.waypoints.add(alienShip);
                               popUp.setText("You took off from the Earth because it looks like there is a crash at the Moon. Could they be ALIENS? Go check it out.");
                               objectiveWindow.setText("Examine the Moon");
                           }
                       },
                5.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("You are heading towards to the natural satellite of the Earth: Moon.");
                               popUp.setText("You are heading towards to the natural satellite of the Earth: Moon.");
                           }
                       },
                20.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Moon is the closest neighbour of our planet in the space.");
                               popUp.setText("Moon is the closest neighbour of our planet in the space.");
                           }
                       },
                40.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("When you get close to the Moon, you have to be careful. You are very fast and you should make a smooth curve.");
                               popUp.setText("When you get close to the Moon, you have to be careful. You are very fast and you should make a smooth curve.");
                           }
                       },
                60.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("You have to bring anything you found in the accident area back to the Earth.");
                               popUp.setText("You have to bring anything you found in the accident area back to the Earth.");
                           }
                       },
                80.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Brace yourself for any possible alien attack or crash because of your bad piloting.");
                               popUp.setText("Brace yourself for any possible alien attack or crash because of your bad piloting.");
                           }
                       },
                100.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("After your investigation, go back to Earth!");
                               popUp.setText("After your investigation, go back to Earth!");
                           }
                       },
                150.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("The parachutes and the Autopilot of the spacecraft (if they exist) can handle the take-offs and landings for you. " +
                                       "All you need to do is get close to the location you need to land. " +
                                       "However, bear in mind that trying to land at high speeds might cause the spacecraft to crash.");
                               popUp.setText("The parachutes and the Autopilot of the spacecraft (if they exist) can handle the take-offs and landings for you." +
                                       " All you need to do is get close to the location you need to land. " +
                                       " However, bear in mind that trying to land at high speeds might cause the spacecraft to crash.");
                           }
                       },
                200.0f);


        return level;
    }

    public static Level createLevel3() {
        //Level Outline II, D part

        final Level level = new Level();
        Timer timer = new Timer();
        final PopUp popUp = new PopUp();
        final ObjectiveWindow objectiveWindow = new ObjectiveWindow();
        popUp.setTitle("HQ");

        //init of map
        level.map = new Map(Gdx.graphics.getWidth() * 300, Gdx.graphics.getHeight() * 300);

        //earth
        level.planets.add(new Planet(1000, 1000, 6 * 1e24f, 800, null, level.world));

        //obstacles
        //TODO: Dispose method could be implemented for level class to remove the objects going out of the map and summoning new ones
        //loop for randomizing the movement directions, velocities, sizes and the shapes of the asteroids
        for (int i = 0; i < 100; i++) {
            Vector2 vector = new Vector2(((float) Math.random()) * (float) Math.pow(-1, i) * 20f, ((float) Math.random()) * (float) Math.random() * 20f);
            if (i % 2 == 0) {
                level.gameObjects.add(new RoundObstacle(((float) (Math.random()) * 10000), ((float) (Math.random()) * 7000) + 30 * i, 10, vector, level.world));
            } else {
                level.gameObjects.add(new RectangleObstacle(((float) (Math.random()) * 10000), ((float) (Math.random()) * 7000) + 30 * i, i * 2, i + 0.5f, vector, level.world));
            }
        }

        //initialization of the rocket
        level.playable = new Playable(2000, 2000, 88, 108, 1e5f, 250 * BASE, 200 * BASE, 1000 * BASE, 5e5f, level.world);
        level.playable.getBody().setLinearVelocity(3, 5);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger asteroidsPassed = new PositionTrigger(9000, 8000, 100, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Congratulations! You've managed to pass all of the obstacles . We are right on the track to reach Mars. Great Job Martian! Level 3 is completed.");
                popUp.setText("Congratulations! You've managed to pass all of the obstacles . We are right on the track to reach Mars. Great Job Martian! Level 3 is completed.");
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
                               System.out.println("You took a SOS code from a who is at Mars right now. Your friend's ship stuck " +
                                       "at the orbit of the Mars. To reach Mars, you have to travel far away. Begin!");
                               popUp.setText("You took a SOS code from a who is at Mars right now. Your friend's ship stuck " +
                                       "at the orbit of the Mars. To reach Mars, you have to travel far away. Begin!");
                               Waypoint endGame = new Waypoint(9000, 8000, 50);
                               level.waypoints.add(endGame);
                               objectiveWindow.setText("Reach Mars");
                           }
                       },
                5.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("The distance between Earth and Mars is 225 million kilometers.");
                               popUp.setText("The distance between Earth and Mars is 225 million kilometers.");
                           }
                       },
                20.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Looks like there are lots of obstacles on your way. Try to pass through them.");
                               popUp.setText("Looks like there are lots of obstacles on your way. Try to pass through them.");
                           }
                       },
                40.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Space might not be so empty after all. Even the smallest rock or space junk piece can cause serious damage to the craft, " +
                                       "especially at higher speeds, so it would be best to avoid even touching anything!");
                               popUp.setText("Space might not be so empty after all. Even the smallest rock or space junk piece can cause serious damage to the craft," +
                                       " especially at higher speeds, so it would be best to avoid even touching anything!");
                           }
                       },
                45.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Be careful!");
                               popUp.setText("Be careful!");
                           }
                       },
                60.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Piloting is hard, isn't it?");
                               popUp.setText("Piloting is hard, isn't it?");
                           }
                       },
                80.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Avoid collisions with obstacles!");
                               popUp.setText("Avoid collisions with obstacles!");
                           }
                       },
                100.0f);

        return level;
    }

    public static Level createLevel4() {
        //Level Outline II, C part

        final Level level = new Level();
        Timer timer = new Timer();
        final PopUp popUp = new PopUp();
        final ObjectiveWindow objectiveWindow = new ObjectiveWindow();
        popUp.setTitle("HQ");

        //init of map
        level.map = new Map(Gdx.graphics.getWidth() * 200, Gdx.graphics.getHeight() * 200);

        //earth
        level.planets.add(new Planet(6500, 5000, 6 * 1e24f, 900, null, level.world));

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
                popUp.setText("Help your friend!");
                objectiveWindow.setText("Help your friend!");
            }
        };
        level.triggers.add(marsTrig);

        final PositionTrigger friend = new PositionTrigger(6500, 5925, 25, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Congratulations! You saved your friend!");
                System.out.println("News: Scientists've discovered some clues about the aliens and their location from the ship that you had brought. Go back to Earth for the news.");
                popUp.setText("Congratulations! You saved your friend! There are good news awaiting you at your home!");
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
                               Waypoint friendShip = new Waypoint(6500, 5925, 10);
                               level.waypoints.add(friendShip);
                               objectiveWindow.setText("Save your friend!");
                               popUp.setText("Mars is close. Your friend needs help! GO");
                           }
                       },
                5.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("You have to pass through your friend's ship in order to save it from the orbit.");
                               popUp.setText("You have to pass through your friend's ship in order to save it from the orbit.");
                           }
                       },
                20.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("After saving the ship, you can go back to Earth.");
                               popUp.setText("After saving the ship, you can go back to Earth.");
                           }
                       },
                40.0f);


        return level;
    }

    public static Level createLevel5() {
        //Level Outline II, E part

        final Level level = new Level();
        Timer timer = new Timer();
        final PopUp popUp = new PopUp();
        final ObjectiveWindow objectiveWindow = new ObjectiveWindow();
        popUp.setTitle("HQ");

        //map
        level.map = new Map(Gdx.graphics.getWidth() * 500, Gdx.graphics.getHeight() * 500);

        //earth
        level.planets.add(new Planet(5000, 5000, 6 * 1e24f, 800, null, level.world));
        //moon
        level.planets.add(new Planet(12500, 13000, 6 * 1e24f, 100, null, level.world));

        //rocket
        level.playable = new Playable(13000, 13000, 88, 108, 1e5f, 250 * BASE, 200 * BASE, 1000 * BASE, 5e5f, level.world);
        level.playable.getBody().setLinearVelocity(45, -5);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger earthTrig = new PositionTrigger(5000, 5000, 850, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Congratulations! You've just landed Earth. Great Job! Level 5 is completed.");
                popUp.setText("Congratulations! You've just landed Earth. Great Job! Level 5 is completed.");
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
                               Waypoint earth = new Waypoint(5000, 5805, 10);
                               level.waypoints.add(earth);
                               objectiveWindow.setText("Land on the Earth.");
                               popUp.setText("In order to check out the news. Land on the Earth.");
                           }
                       },
                5.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("You have to make a gravity-assisted landing.");
                               popUp.setText("You have to make a gravity-assisted landing.");
                           }
                       },
                20.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Spaceships usually use this method to land on the Earth.");
                               popUp.setText("Spaceships usually use this method to land on the Earth.");
                           }
                       },
                40.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Moon is an important object during the process of gravity-assisted landing.");
                               popUp.setText("Moon is an important object during the process of gravity-assisted landing.");
                           }
                       },
                60.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Are you excited for the news about the aliens?");
                               popUp.setText("Are you excited for the news about the aliens?");
                           }
                       },
                80.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Time for some alien investigation!");
                               popUp.setText("Time for some alien investigation!");
                           }
                       },
                100.0f);

        return level;
    }

    public static Level createLevel6() {
        //Level Outline II, F part

        final Level level = new Level();
        Timer timer = new Timer();
        final PopUp popUp = new PopUp();
        final ObjectiveWindow objectiveWindow = new ObjectiveWindow();
        popUp.setTitle("HQ");

        //map
        level.map = new Map(Gdx.graphics.getWidth() * 600, Gdx.graphics.getHeight() * 600);

        //earth
        level.planets.add(new Planet(11000, 13000, 6 * 1e24f, 800, null, level.world));
        //secretPlanet
        level.planets.add(new Planet(16000, 15000, 6 * 1e24f, 100, null, level.world));

        //planet1
        level.planets.add(new Planet(5500, 5500, 6 * 1e24f, 650, null, level.world));
        //planet2
        level.planets.add(new Planet(14000, 3000, 2.7f * 1e21f, 500, null, level.world));
        //planet3
        level.planets.add(new Planet(8200, 7200, 6 * 1e24f, 350, null, level.world));

        //rocket
        level.playable = new Playable(4000, 4850, 88, 108, 1e5f, 250 * BASE, 200 * BASE, 1000 * BASE, 5e5f, level.world);
        level.playable.getBody().setLinearVelocity(0, 0);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger earthTrig = new PositionTrigger(11000, 13000, 400, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("You can't return to Earth yet");
                popUp.setText("You can't return to Earth yet, go exploring");

            }
        };
        level.triggers.add(earthTrig);
        final PositionTrigger planet1 = new PositionTrigger(5500, 5500, 1000, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("What a beautiful planet it is. However, there is no sign of life here.");
                popUp.setText("What a beautiful planet it is. However, there is no sign of life here.");
                Waypoint secondPlanet = new Waypoint(14000, 3600, 5);
                level.waypoints.add(secondPlanet);
                objectiveWindow.setText("Continue investigating planets");
            }
        };
        level.triggers.add(planet1);

        final PositionTrigger planet2 = new PositionTrigger(14000, 3000, 850, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Nothing over here! Lets check the other planets.");
                popUp.setText("Nothing over here! Lets check the others planets.");
                Waypoint thirdPlanet = new Waypoint(8200, 7000, 5);
                level.waypoints.add(thirdPlanet);
            }
        };
        level.triggers.add(planet2);

        final PositionTrigger planet3 = new PositionTrigger(8200, 7200, 700, level.playable) {
            @Override
            public void triggerPerformed() {
                if (planet1.isTriggered() && planet2.isTriggeredBefore()) {
                    System.out.println("I have a bad feeling about this planet. Let's move on!");
                    popUp.setText("I have a bad feeling about this planet. Let's move on!");
                    Waypoint secretOne = new Waypoint(9500, 10105, 5);
                    level.waypoints.add(secretOne);
                } else {
                    System.out.println("It seems we skipped one planet which aliens might be living!");
                    popUp.setText("It seems we skipped one planet which aliens might be living!");
                }
            }
        };
        level.triggers.add(planet3);

        final PositionTrigger secret = new PositionTrigger(16000, 15000, 300, level.playable) {
            @Override
            public void triggerPerformed() {
                if (planet1.isTriggeredBefore() && planet2.isTriggeredBefore() && planet3.isTriggered()) {
                    System.out.println("Oh My God! ALIENS! They are here, they have a planet called Oz-Turca and they call themselves Oz-Jans.");
                    popUp.setText("Oh My God! ALIENS! They are here, they have a planet called Oz-Turca and they call themselves Oz-Jans.");
                }
                //TODO: Alien & Human Gardasligi animasyonu. GAME OVER HERE
                else {
                    System.out.println("We better check all the other planets before looking into this.");
                    popUp.setText("We better check all the other planets before looking into this.");
                }
            }
        };
        level.triggers.add(secret);


        //level starts here
        timer.start();

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Is this real? There are evidence of existence of aliens. Your mission is to find their planet in the space. There are several planets. Start searching!");
                               Waypoint firstPlanet = new Waypoint(5500, 6200, 5);
                               level.waypoints.add(firstPlanet);
                               popUp.setText("Is this real? There are evidence of existence of aliens. Your mission is to find their planet " +
                                       "in the space. There are several planets. Start searching!");
                               objectiveWindow.setText("Find the mysterious planet of the aliens");
                           }
                       },
                5.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               System.out.println("Space Never Gets Boring!!! It's Always FUN!!!");
                               Waypoint firstPlanet = new Waypoint(5500, 6200, 5);
                               level.waypoints.add(firstPlanet);
                               popUp.setText("Space Never Gets Boring!!! It's Always FUN!!!");
                           }
                       },
                100.0f);

        return level;
    }

    //trigger methods
    private static void addDefaultTriggers(final Level level) {
        level.triggers.add(new OutOfMapTrigger(level.map, level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("Out of map");
                level.healthOver();
            }
        });

        level.triggers.add(new FuelDepletionTrigger(level.playable) {
            @Override
            public void triggerPerformed() {
                System.out.println("NO FUEL!");
                level.healthOver();
            }
        });
    }
    //endregion
}
