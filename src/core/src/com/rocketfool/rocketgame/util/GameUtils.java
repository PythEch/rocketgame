package com.rocketfool.rocketgame.util;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;

/**
 * Created by pythech on 25/04/16.
 */
public class GameUtils {
    public static void changeMass(Body body, float newMass) {
        MassData tempMD = new MassData();
        tempMD.mass = newMass;
        tempMD.I = body.getMassData().I / body.getMass() * tempMD.mass;
        body.setMassData(tempMD);
    }
}
