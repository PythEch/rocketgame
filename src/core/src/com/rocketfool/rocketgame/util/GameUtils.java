package com.rocketfool.rocketgame.util;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;

/**
 * Created by pythech on 25/04/16.
 */
public class GameUtils {

    public static int levelsCleared = 0;

    public static void saveGame(){
        //FIXME: Is there anything else to save? :D
        PrintWriter writer;
        try
        {
            writer = new PrintWriter("RocketGameSave.txt", "UTF-8");
            writer.println( levelsCleared );
            writer.close();
        }
        catch ( java.io.FileNotFoundException ex )
        {
            System.err.println( " SAVING ERROR! File not found! " ); //TODO Pop-up instead of out print
        }
        catch ( java.io.UnsupportedEncodingException exc )
        {
            System.err.println( " SAVING ERROR! Encoding failed! " ); //TODO Pop-up instead of out print
        }
    }

    public static void loadGame(){
        Scanner scan;
        File savedGame;
        try
        {
            savedGame = new File("RocketGameSave.txt");
            scan = new Scanner(savedGame);
            levelsCleared = Integer.parseInt(scan.next());

        }
        catch ( java.io.FileNotFoundException ex )
        {
            System.err.println(" LOADING ERROR! File not found! "); //TODO Pop-up instead of out print
        }
    }


    public static void changeMass(Body body, float newMass) {
        MassData tempMD = new MassData();
        tempMD.mass = newMass;
        tempMD.I = body.getMassData().I / body.getMass() * tempMD.mass;
        body.setMassData(tempMD);
    }
}
