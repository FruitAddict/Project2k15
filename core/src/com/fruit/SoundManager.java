package com.fruit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * @author FruitAddict
 */
public class SoundManager {
    public static Sound sound = Gdx.audio.newSound(Gdx.files.internal("splash.wav"));
}
