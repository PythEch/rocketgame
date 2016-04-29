package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.rocketfool.rocketgame.util.Constants;

import static com.rocketfool.rocketgame.model.Playable.BASE;

/**
 * Designs and initiates levels and manages them.
 */
public class LevelManager {
    static float time; //message display time
    //region Methods

    /** Test Level */
    public static Level createLevel0(){
        final Level level = new Level();
        Level.levelNo = 0;

        level.map = new Map(Gdx.graphics.getWidth() * 500, Gdx.graphics.getHeight() * 500);
        level.playable = new Playable(1000, 1000, 88, 108, 1e5f, 750 * BASE, 200 * BASE, 1000 * BASE, 1.0e5f, level.world);
        return level;
    }

    public static Level createLevel1() {

        final Level level = new Level();
        Level.levelNo = 1;
        level.timer = new Timer();
        final PopUp popUp = level.popUp;
        final ObjectiveWindow objectiveWindow = new ObjectiveWindow();
        popUp.setTitle("HQ");

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
        final PositionTrigger earthTrig = new PositionTrigger(14000, 9000, 1250, level.playable) {
            @Override
            public void triggerPerformed() {
                //TODO: Stop level
                //TODO: End of level popup to move to menu or next level or restart
                //Title: ("Mission Accomplished!")
                //Text: ("Congratulations! You made it back safely! Hopefully this experience will help in the future!");
            }
        };
        level.triggers.add(earthTrig);

        //level starts here
        level.timer.start();
        time = 0.01f;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               level.screen.getMinimap().setEnabled(false);
                               TrajectorySimulator.enabled = false;
                           }
                       },
                time);
        time += 2; //<-- this float shows the duration to display the message above
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("What was THAT? A strange object whizzing by left you spinning chaotically in space!" +
                                       "You must regain control!");
                               objectiveWindow.setText("Regain control of the ship.");
                           }
                       },
                time);
        time += 7;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("Use RIGHT & LEFT arrow keys to control angular movement.");

                           }
                       },
                time);
        time += 6;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("SAS restored! \n The RIGHT SHIFT key toggles the SAS, which automatically reduces spinning.");
                           }
                       },
                time);
        time += 7;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("That's better! Now use the UP & DOWN arrow keys to increase/ decrease" +
                                       "your thrust. \n Try to reduce your velocity to zero " +
                                       "(The velocity display is at the top).");
                           }
                       },
                time);
        time += 10;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("As said by Newton's first law of motion, unless a force (like thrust) " +
                                       "acts on you, you would drift at constant speed for ever!");
                           }
                       },
                time);
        time += 10;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("OK! Now let's get our bearings! \n \n Use A & S keys to zoom out or zoom in, and press ESC for the Pause Menu.");
                           }
                       },
                time);
        time += 7;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               TrajectorySimulator.enabled = true;
                               popUp.setText("Supercomputer connections restored! Trajectory calculation features online." +
                                       " (Press T to activate it. It doesn't work unless you're moving.)");
                           }
                       },
                time);
        time += 4;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("The yellow dots in front of you simulates your future motion.");
                           }
                       },
                time);
        time += 7;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               level.screen.getMinimap().setEnabled(true);
                               popUp.setText("Navigation system restored! Minimap online!.");
                           }
                       },
                time);
        time += 4;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("All systems restored! Now it's time to find your way back home!");
                               Waypoint earth = new Waypoint(14000, 9000, 1250);
                               level.waypoints.add(earth);
                               objectiveWindow.setText("Find your way back to Earth");
                           }
                       },
                time);
        time += 6;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("As Newton's second law of motion says, force is the rate of change of " +
                                       "momentum, which is a body's likelihood of preserving its speed. \n" +
                                       "The rocket is pretty heavy, so accelerating takes a while. Be careful, because "
                                       + "that means stopping takes time too!");
                           }
                       },
                time);
        time += 20;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("See that trail behind the rocket? As Newton's third law of motion says," +
                                       "forces exist in opposite-directioned pairs. \n" +
                                       "That's why you need to blast away tons of hot plasma to accelerate!");
                           }
                       },
                time);
        return level;
    }

    public static Level createLevel2() {

        final Level level = new Level();
        Level.levelNo = 2;
        level.timer = new Timer();
        final PopUp popUp = level.popUp;
        final ObjectiveWindow objectiveWindow = new ObjectiveWindow();
        popUp.setTitle("HQ");

        //init of map
        level.map = new Map(Gdx.graphics.getWidth() * 300, Gdx.graphics.getHeight() * 300);

        //Earth
        level.planets.add(new Planet(9000, 7000, 6.0f * 1.0e25f, 650, null, level.world, 2));
        //Moon
        level.planets.add(new Planet(16000, 10000, 1.0f * 1.0e25f, 170, level.planets.get(0), level.world, 3));
        level.planets.get(1).setOrbitPreset(true);
        //initialization of the rocket
        level.playable = new Playable(9550, 7550, 88, 108, 1e5f, 750 * BASE, 200 * BASE, 1000 * BASE, 1.0e5f, level.world);
        level.playable.getBody().setLinearVelocity(50f, -50f);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers & waypoints
        final PositionTrigger moonTrig = new PositionTrigger(level.planets.get(1), 170, 0, 150, level.playable) {
            @Override
            public void triggerPerformed() {
                //(Half way through mission)
                popUp.setText("You've reached the Moon. And what strange things have we found here? Better take it back to Earth!");
                objectiveWindow.setText("Return to Earth");
                level.waypoints.removeIndex(0);
                level.waypoints.add(new Waypoint(9000, 7000, 800));
            }
        };
        level.triggers.add(moonTrig);
        level.waypoints.add(new Waypoint(moonTrig));//<== TODO: Simple Waypoint image: crashed UFO on the Moon.
        final PositionTrigger earthTrig = new PositionTrigger(9000, 7000, 800, level.playable) {
            @Override
            public void triggerPerformed() {
                if (moonTrig.isTriggeredBefore()) {
                    //TODO stop game here
                    //TODO end of level screen
                    //Title: "Mission Accomplished!"
                    //Text: "Congratulations! Our researchers will examine this craft! It looks like we've finally been visited by aliens!");
                }
            }
        };
        level.triggers.add(earthTrig);

        //level starts here
        level.timer.start();
        time = 0.01f;

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               level.screen.setZoom(7f);
                           }
                       },
                time);
        time += 4;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("That strange object from earlier seems to have crashed on the Moon! " +
                                       "Your task is to investigate it and recover what you find!");
                               objectiveWindow.setText("Examine the object on the Moon");
                           }
                       },
                time);
        time += 7;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("The Moon might be too far to see, but your minimap can help you find it.");
                           }
                       },
                time);
        time += 5;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("Thrusting straight towards the Moon would be inefficient and difficult " +
                                       "because you work directly against Earth's gravity. \n"
                                     + "Orbits are weird like that...");
                           }
                       },
                time);
        time += 10;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("First let's quickly recall Newton's Universal Law of Gravitation: \n" +
                                       "F = G * M * m / r^2 \n " +
                                       "You probably remember what the letters stand for but don't forget that r is " +
                                       "squared, so gravity weakens quickly as you get farther away from a planet.");
                           }
                       },
                time);
        time += 12;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("Anyway, orbits can be regarded as continuous falling due to gravity, " +
                                       "but actually going too fast to hit the ground, like in Newton's famous " +
                                       "cannonball model, which... ");
                           }
                       },
                time);
        time += 10;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("Sorry, I'm getting carried away. Returning to the situation at hand... " );
                           }
                       },
                time);
        time += 5;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("Perhaps you can do a Hohmann transfer to work with the gravity " +
                                       "instead of against it? ");
                           }
                       },
                time);
        time += 5;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText(
                                       "The Hohmann Transfer: \n" +
                                       "1. Fire your engines in the way you are flying in your orbit ('prograde'). \n" +
                                       "2. This will raise the highest point of your orbit (the apoapsis) on the " +
                                          "other side of the planet. Wait until you get there. \n" +
                                       "3. Then burn prograde again and there! You've efficiently raised your orbit"
                                       +  "altitude! Now give it a try!");
                           }
                       },
                time);
        time += 30;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText(
                                       "New controls available: \n" +
                                               "Press Z to rapidly maximize your thrust. \n" +
                                               "Press X to rapidly cut down your thrust. \n" +
                                               "Press R to show the forces currently acting on your craft.");
                           }
                       },
                time);
        time += 7;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               popUp.setText("Finally, I should remind you to watch your fuel levels! Good luck!");
                           }
                       },
                time);
        time += 7;
        return level;
    }

    public static Level createLevel3() {
        //Level Outline II, D part

        final Level level = new Level();
        Level.levelNo = 3;
        level.timer = new Timer();
        final PopUp popUp = level.popUp;
        final ObjectiveWindow objectiveWindow = new ObjectiveWindow();
        popUp.setTitle("HQ");

        //init of map
        level.map = new Map(Gdx.graphics.getWidth() * 300, Gdx.graphics.getHeight() * 300);

        //earth
        level.planets.add(new Planet(1000, 1000, 6 * 1e24f, 800, null, level.world,4));

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
        level.playable = new Playable(2000, 2000, 88, 108, 1e5f, 750 * BASE, 200 * BASE, 1000 * BASE, 2e5f, level.world);
        level.playable.getBody().setLinearVelocity(3, 5);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger asteroidsPassed = new PositionTrigger(9000, 8000, 100, level.playable) {
            @Override
            public void triggerPerformed() {
                //System.out.println("Congratulations! You've managed to pass all of the obstacles . We are right on the track to reach Mars. Great Job Martian! Level 3 is completed.");
                popUp.setText("Congratulations! You've managed to pass all of the obstacles . We are right on the track to reach Mars. Great Job Martian! Level 3 is completed.");
                //TODO: Next level should be given here. However, the method createLevel4() fails here.
            }
        };
        level.triggers.add(asteroidsPassed);

        //level starts here
        level.timer.start();

        //TODO: Take off animation is needed (it is not essential)

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("You took a SOS code from a who is at Mars right now. Your friend's ship stuck " +
                                       //"at the orbit of the Mars. To reach Mars, you have to travel far away. Begin!");
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
                               //System.out.println("The distance between Earth and Mars is 225 million kilometers.");
                               popUp.setText("The distance between Earth and Mars is 225 million kilometers.");
                           }
                       },
                20.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("Looks like there are lots of obstacles on your way. Try to pass through them.");
                               popUp.setText("Looks like there are lots of obstacles on your way. Try to pass through them.");
                           }
                       },
                40.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("Space might not be so empty after all. Even the smallest rock or space junk piece can cause serious damage to the craft, " +
                                       //"especially at higher speeds, so it would be best to avoid even touching anything!");
                               popUp.setText("Space might not be so empty after all. Even the smallest rock or space junk piece can cause serious damage to the craft," +
                                       " especially at higher speeds, so it would be best to avoid even touching anything!");
                           }
                       },
                45.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("Be careful!");
                               popUp.setText("Be careful!");
                           }
                       },
                60.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("Piloting is hard, isn't it?");
                               popUp.setText("Piloting is hard, isn't it?");
                           }
                       },
                80.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("Avoid collisions with obstacles!");
                               popUp.setText("Avoid collisions with obstacles!");
                           }
                       },
                100.0f);

        return level;
    }

    public static Level createLevel4() {
        //Level Outline II, C part

        final Level level = new Level();
        Level.levelNo = 4;
        level.timer = new Timer();
        final PopUp popUp = level.popUp;
        final ObjectiveWindow objectiveWindow = new ObjectiveWindow();
        popUp.setTitle("HQ");

        //init of map
        level.map = new Map(Gdx.graphics.getWidth() * 200, Gdx.graphics.getHeight() * 200);

        //Mars
        level.planets.add(new Planet(6500, 5000, 6 * 1e24f, 900, null, level.world,5));

        //initialization of the rocket
        level.playable = new Playable(1000, 1000, 88, 108, 1e5f, 750 * BASE, 200 * BASE, 1000 * BASE, 2e5f, level.world);
        level.playable.getBody().setLinearVelocity(0, 0);

        //Stranded spacecraft
        level.gameObjects.add(new Playable(0, 0, 88, 108, 1e5f, 0, 0, 0, 0, level.world));
        ((SolidObject) level.gameObjects.get(0)).setOrbitPreset(true);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger marsTrig = new PositionTrigger(6500, 5000, 1000, level.playable) {
            @Override
            public void triggerPerformed() {
                //System.out.println("Help your friend!");
                popUp.setText("Help your friend!");
                objectiveWindow.setText("Help your friend!");
            }
        };
        level.triggers.add(marsTrig);

        final PositionTrigger friend = new PositionTrigger(6500, 5925, 25, level.playable) {
            @Override
            public void triggerPerformed() {
                //System.out.println("Congratulations! You saved your friend!");
                //System.out.println("News: Scientists've discovered some clues about the aliens and their location from the ship that you had brought. Go back to Earth for the news.");
                popUp.setText("Congratulations! You saved your friend! There are good news awaiting you at your home!");
                //TODO: Next level should be given here. However, the method createLevel5() fails here.
            }
        };
        level.triggers.add(friend);

        //level starts here
        level.timer.start();

        //TODO: Take off animation is needed (it is not essential)

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("Mars is close. Your friend needs help! GO!");
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
                               //System.out.println("You have to pass through your friend's ship in order to save it from the orbit.");
                               popUp.setText("You have to pass through your friend's ship in order to save it from the orbit.");
                           }
                       },
                20.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("After saving the ship, you can go back to Earth.");
                               popUp.setText("After saving the ship, you can go back to Earth.");
                           }
                       },
                40.0f);


        return level;
    }

    public static Level createLevel5() {
        //Level Outline II, E part

        final Level level = new Level();
        Level.levelNo = 5;
        level.timer = new Timer();
        final PopUp popUp = level.popUp;
        final ObjectiveWindow objectiveWindow = new ObjectiveWindow();
        popUp.setTitle("HQ");

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
            public void triggerPerformed() {
                //System.out.println("Congratulations! You've just landed Earth. Great Job! Level 5 is completed.");
                popUp.setText("Congratulations! You've just landed Earth. Great Job! Level 5 is completed.");
                //TODO: Landing animation is required here.
                //TODO: Next level should be given here. However, the method createLevel6() fails here.
            }
        };
        level.triggers.add(earthTrig);

        //level starts here
        level.timer.start();

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("In order to check out the news. Land on the Earth.");
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
                               //System.out.println("You have to make a gravity-assisted landing.");
                               popUp.setText("You have to make a gravity-assisted landing.");
                           }
                       },
                20.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("Spaceships usually use this method to land on the Earth.");
                               popUp.setText("Spaceships usually use this method to land on the Earth.");
                           }
                       },
                40.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("Moon is an important object during the process of gravity-assisted landing.");
                               popUp.setText("Moon is an important object during the process of gravity-assisted landing.");
                           }
                       },
                60.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("Are you excited for the news about the aliens?");
                               popUp.setText("Are you excited for the news about the aliens?");
                           }
                       },
                80.0f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("Time for some alien investigation!");
                               popUp.setText("Time for some alien investigation!");
                           }
                       },
                100.0f);

        return level;
    }

    public static Level createLevel6() {
        //Level Outline II, F part

        final Level level = new Level();
        Level.levelNo = 6;
        level.timer = new Timer();
        final PopUp popUp = level.popUp;
        final ObjectiveWindow objectiveWindow = new ObjectiveWindow();
        popUp.setTitle("HQ");

        //map
        level.map = new Map(Gdx.graphics.getWidth() * 600, Gdx.graphics.getHeight() * 600);

        //earth
        level.planets.add(new Planet(11000, 13000, 6 * 1e24f, 800, null, level.world, 6));
        //secretPlanet
        level.planets.add(new Planet(16000, 15000, 6 * 1e24f, 100, null, level.world, 7));

        //planet1
        level.planets.add(new Planet(5500, 5500, 6 * 1e24f, 650, null, level.world, 1));
        //planet2
        level.planets.add(new Planet(14000, 3000, 2.7f * 1e21f, 500, null, level.world, 2));
        //planet3
        level.planets.add(new Planet(8200, 7200, 6 * 1e24f, 350, null, level.world, 3));

        //rocket
        level.playable = new Playable(4000, 4850, 88, 108, 1e5f, 250 * BASE, 200 * BASE, 1000 * BASE, 5e5f, level.world);
        level.playable.getBody().setLinearVelocity(0, 0);

        //default Triggers
        addDefaultTriggers(level);

        //endGame Triggers
        final PositionTrigger earthTrig = new PositionTrigger(11000, 13000, 400, level.playable) {
            @Override
            public void triggerPerformed() {
                //System.out.println("You can't return to Earth yet");
                popUp.setText("You can't return to Earth yet, go exploring");
            }
        };
        level.triggers.add(earthTrig);
        final PositionTrigger planet1 = new PositionTrigger(5500, 5500, 1000, level.playable) {
            @Override
            public void triggerPerformed() {
                //System.out.println("What a beautiful planet it is. However, there is no sign of life here.");
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
                //System.out.println("Nothing over here! Lets check the other planets.");
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
                    //System.out.println("I have a bad feeling about this planet. Let's move on!");
                    popUp.setText("I have a bad feeling about this planet. Let's move on!");
                    Waypoint secretOne = new Waypoint(9500, 10105, 5);
                    level.waypoints.add(secretOne);
                } else {
                    //System.out.println("It seems we skipped one planet which aliens might be living!");
                    popUp.setText("It seems we skipped one planet which aliens might be living!");
                }
            }
        };
        level.triggers.add(planet3);

        final PositionTrigger secret = new PositionTrigger(16000, 15000, 300, level.playable) {
            @Override
            public void triggerPerformed() {
                if (planet1.isTriggeredBefore() && planet2.isTriggeredBefore() && planet3.isTriggered()) {
                    //System.out.println("Oh My God! ALIENS! They are here, they have a planet called Oz-Turca and they call themselves Oz-Jans.");
                    popUp.setText("Oh My God! ALIENS! They are here, they have a planet called Oz-Turca and they call themselves Oz-Jans.");
                }
                //TODO: Alien & Human Gardasligi animasyonu. GAME OVER HERE
                else {
                    //System.out.println("We better check all the other planets before looking into this.");
                    popUp.setText("We better check all the other planets before looking into this.");
                }
            }
        };
        level.triggers.add(secret);


        //level starts here
        level.timer.start();

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               //System.out.println("Is this real? There are evidence of existence of aliens. Your mission is to find their planet in the space. There are several planets. Start searching!");
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
                               //System.out.println("Space Never Gets Boring!!! It's Always FUN!!!");
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
                if (!Constants.DEBUG) {
                    //System.out.println("Out of map");
                    level.healthOver();
                }
            }
        });

        level.triggers.add(new FuelDepletionTrigger(level.playable) {
            @Override
            public void triggerPerformed() {
                //System.out.println("NO FUEL!");
                level.healthOver();
            }
        });
    }
    //endregion
}
