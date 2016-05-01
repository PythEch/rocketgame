package com.rocketfool.rocketgame.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by alpino-64 on 28.04.2016.
 */
public class GamePreferences {
    private float masterVolume;
    private boolean fullscreen;
    private int[] scores;
    private boolean[] levelsCleared;

    private static GamePreferences instance = new GamePreferences();
    private Preferences prefs;

    // singleton: prevent instantiation from other classes
    private GamePreferences() {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    }

    public static GamePreferences getInstance() { return instance; }

    public void load() {
        masterVolume = prefs.getFloat("masterVolume", 1);
        System.out.println(masterVolume);
        fullscreen = prefs.getBoolean("fullscreen", false);

        scores = new int[5];
        scores[0] = prefs.getInteger("scores_0", 0);
        scores[1] = prefs.getInteger("scores_1", 0);
        scores[2] = prefs.getInteger("scores_2", 0);
        scores[3] = prefs.getInteger("scores_3", 0);
        scores[4] = prefs.getInteger("scores_4", 0);

        levelsCleared = new boolean[5];
        for (int i = 0; i < levelsCleared.length; i++) {
            levelsCleared[i] = scores[i] > 0;
        }
    }

    public void save() {
        prefs.putFloat("masterVolume", masterVolume);
        prefs.putBoolean("fullscreen", fullscreen);

        prefs.putInteger("scores_0", scores[0]);
        prefs.putInteger("scores_1", scores[1]);
        prefs.putInteger("scores_2", scores[2]);
        prefs.putInteger("scores_3", scores[3]);
        prefs.putInteger("scores_4", scores[4]);
        prefs.flush();
    }

    public float getMasterVolume(){
        return masterVolume;
    }

    public void setMasterVolume(float i){
        masterVolume = i;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public int[] getScores() {
        return scores;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }

    public void setScore(int index, int score) {
        scores[index] = score;
    }
}
