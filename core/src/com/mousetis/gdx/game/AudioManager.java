package com.mousetis.gdx.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;


/**
 * 
 * @author mmous
 *
 */

public class AudioManager 
{
    public static final AudioManager instance = new AudioManager();
    private Music playingMusic;

    // singleton: prevent instantiation from other classes
    private AudioManager() 
    {
    }

    /**
     * plays the sound given to the method
     * @param sound
     */
    public void play(Sound sound) 
    {
        play(sound, 1);
    }

    /**
     * plays the sound at a given volume
     * @param sound
     * @param volume
     */
    public void play(Sound sound, float volume) 
    {
        play(sound, volume, 1);
    }

    /**
     * plays sound at given volume and pitch
     * @param sound
     * @param volume
     * @param pitch
     */
    public void play(Sound sound, float volume, float pitch) 
    {
        play(sound, volume, pitch, 0);
    }

    /**
     * you get the idea from the previous methods
     * @param sound
     * @param volume
     * @param pitch
     * @param pan
     */
    public void play(Sound sound, float volume, float pitch,
                     float pan) {
        if (!GamePreferences.instance.sound) return;
        sound.play(GamePreferences.instance.volSound * volume, pitch, pan);
    }

    /**
     * plays the music
     * @param music
     */
    public void play(Music music) 
    {
        stopMusic();
        playingMusic = music;
        if (GamePreferences.instance.music) {
            music.setLooping(true);
            music.setVolume(GamePreferences.instance.volMusic);
            music.play();
        }
    }

    /**
     * stops the music
     */
    public void stopMusic() 
    {
        if (playingMusic != null) playingMusic.stop();
    }

    /**
     * makes changes when the settings are updated
     */
    public void onSettingsUpdated() 
    {
        if (playingMusic == null) return;
        playingMusic.setVolume(GamePreferences.instance.volMusic);
        if (GamePreferences.instance.music) {
            if (!playingMusic.isPlaying()) playingMusic.play();
        } else {
            playingMusic.pause();
        }
    }
}