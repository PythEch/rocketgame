package com.rocketfool.rocketgame.util;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;

/**
 * Some utilities for the game
 */
public class GameUtils {
    /**
     * Changes the mass of a given Body object, using a workaround
     * since Box2D it seems wasn't designed for objects with variable masses
     * @param body Box2D body
     * @param newMass The new mass to override
     */
    public static void changeMass(Body body, float newMass) {
        MassData tempMD = new MassData();
        tempMD.mass = newMass;
        tempMD.I = body.getMassData().I / body.getMass() * tempMD.mass;
        body.setMassData(tempMD);
    }
}
