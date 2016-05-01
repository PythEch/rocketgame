package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.rocketfool.rocketgame.controller.WorldController;
import com.rocketfool.rocketgame.view.GameScreen;
import com.badlogic.gdx.utils.Timer;

import static com.rocketfool.rocketgame.util.Constants.DEBUG;
import static com.rocketfool.rocketgame.util.Constants.FRAME_RATE;

/**
 * Class to create instances of all levels. Also, it performs most of the calculations.
 */
public class Level {
    //region Constants
    public static final float G = 6.67408f * 1e-20f;
    //endregion

    //region Fields
    protected static byte levelNo;
    protected World world;
    protected Playable playable;
    protected GameScreen screen;
    protected Map map;
    protected Array<Trigger> triggers;
    protected Waypoint waypoint;
    protected Array<Planet> planets;
    protected Array<SolidObject> solidObjects;
    protected float timePassedReal;
    protected float timePassedFixed;
    protected float currentGravForce;
    protected int score;
    protected State state;
    protected int health = 3;
    protected PopUp popUp;
    protected Timer timer;
    protected ObjectiveWindow objectiveWindow;
    protected long timerDelay;
    //endregion

    //region Nested Types
    public enum ObjectType {
        PLANET, OBSTACLE, PLAYABLE
    }

    public enum State {
        RUNNING, PAUSED, GAME_OVER, HEALTH_OVER, LEVEL_FINISHED
    }
    //endregion

    //region Constructor
    public Level() {
        this.state = State.PAUSED;

        // Create a Box2D world with no gravity
        this.world = new World(new Vector2(0, 0), true);

        this.triggers = new Array<Trigger>();
        this.planets = new Array<Planet>();
        this.solidObjects = new Array<SolidObject>();
        this.timePassedReal = 0;
        this.timePassedFixed = 0;
        this.score = 0;
        this.popUp = new PopUp();
        this.objectiveWindow = new ObjectiveWindow();

        // Register collisions
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                // User data stores the type of the object (for now)
                ObjectType typeA = (ObjectType) contact.getFixtureA().getUserData();
                ObjectType typeB = (ObjectType) contact.getFixtureB().getUserData();

                // Check if playable crashes with a planet or an obstacle
                if ((typeA == ObjectType.PLANET && typeB == ObjectType.PLAYABLE) ||
                        (typeB == ObjectType.PLANET && typeA == ObjectType.PLAYABLE)) {
                    planetCollision(contact);
                } else if ((typeA == ObjectType.OBSTACLE && typeB == ObjectType.PLAYABLE) ||
                        (typeB == ObjectType.OBSTACLE && typeA == ObjectType.PLAYABLE)) {
                    obstacleCollision(contact);
                }
            }

            //region Methods just to override
            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
            //endregion
        });
    }
    //endregion

    //region Methods

    public void resetLevel() {
        Level newWorld = null;

        switch (levelNo) {
            case 0:
                newWorld = LevelManager.createLevel0();
                break;
            case 1:
                newWorld = LevelManager.createLevel1();
                break;
            case 2:
                newWorld = LevelManager.createLevel2();
                break;
            case 3:
                newWorld = LevelManager.createLevel3();
                break;
            case 4:
                newWorld = LevelManager.createLevel4();
                break;
            case 5:
                newWorld = LevelManager.createLevel5();
                break;
        }

        newWorld.setScreenReference(screen);

        this.world = newWorld.world;
        this.playable = newWorld.playable;
        //his.screen = newWorld.screen;
        this.map = newWorld.map;
        this.triggers = newWorld.triggers;
        this.waypoint = newWorld.waypoint;
        this.planets = newWorld.planets;
        this.timePassedReal = newWorld.timePassedReal;
        this.timePassedFixed = newWorld.timePassedFixed;
        this.currentGravForce = newWorld.currentGravForce;
        this.solidObjects = newWorld.solidObjects;
        this.score = newWorld.score;
        this.state = newWorld.state;
        //this.health -= 1;
        this.popUp = newWorld.popUp;

        this.timer = newWorld.timer;


        screen.lookAt(playable);

        playable.update(0);
    }

    /**
     * Update models per frame
     */
    public void update(float deltaTime) {
        if (state == State.RUNNING) {
            timePassedReal += deltaTime;
            // Hack to make physics engine stable
            deltaTime = FRAME_RATE;
            timePassedFixed += deltaTime;

            playable.update(deltaTime);
            updateGravity(deltaTime);
            updateTriggers(deltaTime);
            updateVisualObjects(deltaTime);
            if (waypoint != null)
                waypoint.update(deltaTime);
            updatePresetOrbits();

            // A world step simulates the Box2D world
            world.step(deltaTime, 8, 3);
        }
    }

    /**
     * Fires triggers that have their conditions fulfilled
     */
    private void updateTriggers(float deltaTime) {
        for (Trigger trigger : triggers) {
            if (trigger.isTriggered()) {
                trigger.triggerPerformed();
            }
        }
    }

    /**
     * Calculates the gravity forces and applies them to the spacecraft
     */
    private void updateGravity(float deltaTime) {
        for (Planet planet : planets) {
            Body spaceship = playable.getBody();

            // Get the directionVector by subtracting the middle points of two objects
            Vector2 directionVector = planet.getBody().getPosition().sub(spaceship.getPosition());

            // uses F = G * M * m / r^2
            // We also use directionVector as it's also capable of calculating the distance
            // between the two objects.
            // Another interesting thing is LibGDX also provieds len2() method which is
            // basically len^2 so we can get r^2 this way with less code
            // it's also faster because normally distance calculation involves an Math.sqrt()
            // while len2() doesn't have to do so, so we don't have two Math.pow(Math.sqrt(distance), 2)
            // which is unnecessary work.

            float forceScalar = G * spaceship.getMass() * planet.getMass() / directionVector.len2();
            currentGravForce = forceScalar;

            // So now we have the value of the force and the direction
            // We have to get a vector with given direction and value
            Vector2 forceVector = directionVector.setLength(forceScalar);

            // apply this force to spaceship
            spaceship.applyForceToCenter(forceVector, true);
        }
    }

    /**
     * This is used to update objects like background visuals
     */
    private void updateVisualObjects(float deltaTime) {
        for (GameObject go : solidObjects) {
            go.update(deltaTime);
        }
    }

    /**
     * Updates any preset orbits of game objects.
     * This is all still a little hardcoded, but that is fine for the current scope of the game.
     */
    public void updatePresetOrbits() {
        for (GameObject obj: solidObjects){
            if ((obj instanceof  SolidObject) && (((SolidObject) obj).isOrbitPreset()))
                quickPresetOrbits((SolidObject) obj, levelNo, timePassedFixed);
        }
        for (GameObject obj: planets){
            if ((obj instanceof  SolidObject) && (((SolidObject) obj).isOrbitPreset()))
                quickPresetOrbits((SolidObject) obj, levelNo, timePassedFixed);
        }
    }

    private void planetCollision(Contact contact) {
        System.out.println("planet collision");
        if (!DEBUG)
            setState(State.HEALTH_OVER);
    }

    private void obstacleCollision(Contact contact) {
        System.out.println("obstacle collision");
        if (!DEBUG)
            setState(State.HEALTH_OVER);
    }

    /**
     * This method is used to make a a body circle around another (to simulate a circular orbit).
     */
    public static void presetOrbit(SolidObject orbiter, SolidObject focus, float orbitRadius, float period,
                                   float timePassed, float phase){
        float x;
        float y;
        float fx = focus.getBody().getPosition().x;
        float fy = focus.getBody().getPosition().y;
        double w = 2 * Math.PI / period;
        double t = (double) (timePassed);
        x =  fx + (float) (orbitRadius * Math.cos(w*t + phase));
        y =  fy + (float) (orbitRadius * Math.sin(w*t + phase));
        orbiter.getBody().setTransform(x,y,0f);
    }

    /** Orbits of certain game objects that were created in advance.
     */
    public static void quickPresetOrbits(SolidObject orbiter, int selection , float timePassed){
        if (selection == 1) { //Moon around Earth quick preset for Level 1
            float x = ((Planet) orbiter).getPrimary().getBody().getPosition().x;
            float y = ((Planet) orbiter).getPrimary().getBody().getPosition().y;
            float w = (float) (2 * Math.PI / 3000);
            float phase = (float) (Math.PI * 2f / 3f);
            orbiter.getBody().setTransform(
                    x + (float) (7615 * Math.cos(w * timePassed + phase)),
                    y + (float) (7615 * Math.sin(w * timePassed + phase)),
                    0f);
        }
        else if (selection == 2){ //Moon around Earth quick preset for Level 2
            float x = ((Planet) orbiter).getPrimary().getBody().getPosition().x;
            float y = ((Planet) orbiter).getPrimary().getBody().getPosition().y;
            float w = (float) (2 * Math.PI / 3000);
            float phase = 0f;
            orbiter.getBody().setTransform(
                    x + (float) (7615 * Math.cos(w * timePassed + phase)),
                    y + (float) (7615 * Math.sin(w * timePassed + phase)),
                    0f);
        }
        else if (selection == 4){ //Spacecraft in Level 4
            float x = 11000;//Mars location
            float y = 7000; //Mars location
            float w = (float) (2 * Math.PI / 240);
            float phase = 4f;
            orbiter.getBody().setTransform(
                    x + (float) (1500 * Math.cos(w * timePassed + phase)),
                    y + (float) (1500 * Math.sin(w * timePassed + phase)),
                    0f);
        }
        else if (selection == 5) { //Moon around Earth quick preset for Level 5
            float x = ((Planet) orbiter).getPrimary().getBody().getPosition().x;
            float y = ((Planet) orbiter).getPrimary().getBody().getPosition().y;
            float w = (float) (2 * Math.PI / 3000);
            float phase = -1f;
            orbiter.getBody().setTransform(
                    x + (float) (7615 * Math.cos(w * timePassed + phase)),
                    y + (float) (7615 * Math.sin(w * timePassed + phase)),
                    0f);
        }
    }

    private void updateParticles(float deltaTime) {
        //TODO: implement
    }
    //endregion

    //region Getters & Setters
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        if (state == State.HEALTH_OVER) {
            healthOver();
        }
        else if (state == State.GAME_OVER) {
            Timer.instance().stop();
        }
        else if (state == State.RUNNING) {
            Timer.instance().start();
        }
        else if (state == State.PAUSED) {
            Timer.instance().stop();
        }
    }

    public void healthOver() {
        health -= 1;
        if (health == 0) {
            setState(State.GAME_OVER);
        }
        else {
            resetLevel();
            setState(State.RUNNING);
        }
    }

    public Array<Planet> getPlanets() {
        return planets;
    }

    public float getCurrentGravForce() {
        return currentGravForce;
    }

    public World getWorld() {
        return world;
    }

    public Playable getPlayable() {
        return playable;
    }

    public Map getMap() {
        return map;
    }

    public float getTimePassedReal() {
        return timePassedReal;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setScreenReference(GameScreen screen){ this.screen = screen; }

    public PopUp getPopUp() {
        return popUp;
    }

    public Timer getTimer(){return timer;}

    public Waypoint getWaypoint() {
        return waypoint;
    }

    public void setWaypoint(Waypoint waypoint) {
        this.waypoint = waypoint;
    }

    public Array<Trigger> getTriggers() {
        return triggers;
    }

    public Array<SolidObject> getSolidObjects() {
        return solidObjects;
    }

    public static byte getLevelNo() {
        return levelNo;
    }

    public ObjectiveWindow getObjectiveWindow() {
        return objectiveWindow;
    }

    //endregion
}