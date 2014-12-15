package com.fruit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * @author FruitAddict
 * Sound Manager class stub. Should contain static methods/fields to play sounds
 * and/or loop/play music in the background.
 */
public class SoundManager {
    public static Sound sound = Gdx.audio.newSound(Gdx.files.internal("splash.wav"));
}
