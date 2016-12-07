package com.mousetis.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author Matt Mousetis
 * 
 */

public class GamePreferences 
{
    public static final String TAG = GamePreferences.class.getName();
    public static final GamePreferences instance = new GamePreferences();

    public boolean sound;
    public boolean music;
    public float volSound;
    public float volMusic;
    public int charSkin;
    public boolean showFpsCounter;
    private Preferences prefs;
    public int Highscore1;
    public int Highscore2;
    public int Highscore3;

    // singleton: prevent instantiation from other classes
    private GamePreferences()
    {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    }
    
    /**
     * loads the sound music and what not for the menu
     * 
     */
    public void load() 
    {
        sound = prefs.getBoolean("sound", true);
        music = prefs.getBoolean("music", true);
        Highscore1 = prefs.getInteger("HighScore1");
        Highscore2 = prefs.getInteger("HighScore2");
        Highscore3 = prefs.getInteger("HighScore3");
        volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0.0f, 1.0f);
        volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f), 0.0f, 1.0f);
        charSkin = MathUtils.clamp(prefs.getInteger("charSkin", 0), 0, 2);
        showFpsCounter = prefs.getBoolean("showFpsCounter", false);
    }

    /**
     * saves the sound music ect.
     * 
     */
    public void save() 
    {
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.putFloat("volSound", volSound);
        prefs.putFloat("volMusic", volMusic);
        prefs.putInteger("charSkin", charSkin);
        prefs.putBoolean("showFpsCounter", showFpsCounter);
        prefs.putInteger("HighScore1", Highscore1);
        prefs.putInteger("HighScore2", Highscore2);
        prefs.putInteger("HighScore3", Highscore3);
        prefs.flush();
    }
    
}