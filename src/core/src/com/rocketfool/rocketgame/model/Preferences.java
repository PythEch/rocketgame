package com.rocketfool.rocketgame.model;

/**
 * Created by alpino-64 on 28.04.2016.
 */
public class Preferences {
    private float masterVolume = 1;
    private static Preferences instance = new Preferences();


    private Preferences (){}

    public static Preferences getInstance() { return instance; }

    public float getMasterVolume(){
        return masterVolume;
    }

    public void setMasterVolume(float i){
        masterVolume = i;
    }


}
