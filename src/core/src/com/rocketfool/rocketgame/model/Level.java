package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Class to create instances of all levels. Also, it performs most of the calculations.
 */
public class Level {
    protected static /*final*/ float G = 6.67408e-11f;

    protected World world;
    protected Playable playable;
    protected Map map;
    protected Array<Trigger> triggers;
    protected Array<Waypoint> waypoints;
    protected Array<SolidObject> solidObjects;
    protected Array<GameObject> gameObjects;
    protected float timePassed;
    protected int score;
    protected State state;
    boolean temp = true;

    public Level() {
        this.world = new World(new Vector2(0, 0), true);
        this.triggers = new Array<Trigger>();
        this.waypoints = new Array<Waypoint>();
        this.solidObjects = new Array<SolidObject>();
        this.gameObjects = new Array<GameObject>();
        this.timePassed = 0;

        this.score = 0;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void saveGame() {
    } //TODO: Implement

    public void update(float deltaTime) {
        //Calculate physical quantities and game components
        updateSolidObjects(deltaTime);
        updateGravity(deltaTime);
        updateTriggers(deltaTime);
        updateVisualObjects(deltaTime);
        updateWaypoints(deltaTime);

        timePassed += deltaTime;
        world.step(1 / 60f, 6, 2);
    }

    private void updateSolidObjects(float deltaTime) {
        playable.update(deltaTime);
    }

    private void updateTriggers(float deltaTime) {
        for (Trigger trigger : triggers) {
            if (trigger.isTriggered()) {
                trigger.triggerPerformed();
            }
        }
    }


    private void updateGravity(float deltaTime) {
        for (SolidObject solidObject : solidObjects) {
            if (!(solidObject instanceof Planet))
                continue;

            Planet planet = (Planet) solidObject;

            Body spaceship = playable.getBody();

            // Get the directionVector by substracting the middle points of two objects
            Vector2 directionVector = planet.getBody().getPosition().sub(spaceship.getPosition());

            // uses F = G * M * m / r^2
            // We also use directionVector as it's also capable of calculating the distance
            // between the two objects.
            // Another interesting thing is LibGDX also provieds len2() method which is
            // basically len^2 so we can get r^2 this way with less code
            // it's also faster because normally distance calculation involves an Math.sqrt()
            // while len2() doesn't have to do so, so we don't have two Math.pow(Math.sqrt(distance), 2)
            // which is unnecessary work.
            float forceScalar = G * spaceship.getMass() * planet.getMass() / directionVector.len2(); //**

            // So now we have the value of the force and the direction
            // We have to get a vector with given direction and value
            Vector2 forceVector = directionVector.setLength(forceScalar);

            // apply this force to spaceship
            spaceship.applyForceToCenter(forceVector, true);

            //**temp spot
            //if (temp) //( (System.currentTimeMillis() / 1000) % 7 == 0 )
            //    this.drawTrajectory( (CelestialObject) this.solidObjects.get(0) , this.playable );
            //temp = false;
        }
    }

    private void updateWaypoints(float deltaTime) {

        //remove waypoints form screen to meet the endgame condition
        for (int i =0; i<waypoints.size; i++) {

            if (playable.getBody().getPosition().dst(waypoints.get(i).getX(), waypoints.get(i).getY()) <= 10){
                waypoints.get(i).setOnScreen(false);
            }
        }
    }

    private void updateParticles(float deltaTime) {
    } //TODO: implement

    private void updateVisualObjects(float deltaTime) {
        for (GameObject go : gameObjects) {
            go.update(deltaTime);
        }
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

    public float getTimePassed() {
        return timePassed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * This method is needed for analysis and debugging, but we may remove it if necessary.
     */
    public Vector2 getPlanetLocation(int no) {
        if (com.rocketfool.rocketgame.util.Constants.DEBUG) {
            return this.solidObjects.get(no).getBody().getPosition();
        } else {
            return null;
        }
    }

    /**
     * Trajectory rawing method, very close to actually working...
     */
    public void drawTrajectory(CelestialObject body, Playable craft) { //should take the nearest body
        //http://physics.stackexchange.com/questions/99094/using-2d-position-velocity-and-mass-to-determine-the-parametric-position-equat
        double a, e,
                dtheta,
                r1, rpath, rper,
                v, vx, vy,
                vtan, vtheta,
                mu,
                x, y,
                root,
                part1, part2, part3;

        float[] pathx, pathy;

        pathx = new float[41];
        pathy = new float[41];

        v = craft.getBody().getLinearVelocity().len();
        vx = craft.getBody().getLinearVelocity().x;
        vy = craft.getBody().getLinearVelocity().y;

        x = craft.getBody().getPosition().x;
        y = craft.getBody().getPosition().y;

        double G2 = 6.67e-11;//** error cause does not seem to be G

        root = Math.sqrt(x * x + y * y);

        mu = body.getMass() * G;

        r1 = craft.getBody().getPosition().dst(body.getBody().getPosition());


        a = mu * r1 / (2 * mu - r1 * v * v);

        vtheta = (x * vy - y * vx) / root;

        e = Math.sqrt(1 + r1 * vtheta * vtheta / mu * (r1 * v * v / mu - 2));

        vtan = (x * vx + y * vy) / root;

        if (vtan * vtheta < 0)
            part1 = -1; //dtheta = -1 * Math.acos( (a * ( 1 - e * e )- root) / e / root ) - Math.atan2( y , x ); // arccos(-10) is NaN!
        else
            part1 = 1; //dtheta = Math.acos( (a * ( 1 - e * e )- root) / e / root ) - Math.atan2( y , x );

        part2 = Math.acos(((a * (1 - e * e) - root) / (e * root))); //** part2 is not =0, the sign is a -, not 1/... , not root^2
        part3 = Math.atan2(y, x);
        dtheta = part1 * part2 - part3;

        //rper = a * ( 1 - e );

        //dtheta = Math.acos( ( a * ( 1 - e * e ) / r1 / e ) -  ( 1 / e ) ); //can we rev-use the equation for this? NaN here too???!? what?

        int i = 0;
        for (double theta = 0f; theta < 2 * Math.PI; theta = (theta + Math.PI / 10)) {

            rpath = a * (1 - e * e) / (1 + e * Math.cos(theta));
            System.err.println(rpath); //needs to resemble altitude data, and it does!
            pathx[i] = (float) (rpath * Math.cos(theta + dtheta)); //** dtheta cannot be ignored!
            pathy[i] = (float) (rpath * Math.sin(theta + dtheta) * -1);
            System.err.println("(" + (int) pathx[i] + "," + (int) pathy[i] + ")");
            i++;
        }
        System.err.println("[" + x + "," + y + "]");
        //System.err.println( mu + " " + root + " " + vtan + " " + vtheta );
        System.err.println();
        //System.err.println( vtan + " " + v + " " + vx + " " + vy );
        System.err.println(part1 + " " + part2 + " " + part3 + " " + dtheta);
        System.err.println("problem, not /1/:" + ((a * (1 - e * e) - root) / (e * root))
                + "\n \t " + ((a * (1 - e * e) / r1 / e) - (1 / e)));
        //If I find an alternative way to receive dtheta, this is done! *** :D
    }

    public Array<Planet> getPlanets() {
        Array<Planet> planets = new Array<Planet>();
        for (SolidObject solidObject : solidObjects) {
            if (solidObject instanceof Planet) {
                planets.add((Planet) solidObject);
            }
        }
        return planets;
    }

    enum State {
        RUNNING, PAUSED, GAME_OVER
    }
}