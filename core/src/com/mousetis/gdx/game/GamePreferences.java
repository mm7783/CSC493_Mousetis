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
        prefs.flush();
    }
    
}