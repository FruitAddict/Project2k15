package com.project2k15.utilities;

import com.badlogic.gdx.Game;
import com.project2k15.screens.GameScreen;
import com.project2k15.screens.SplashScreen;

/**
 * Singleton class for storing screens nad other things that can be reused.
 * Created for better memory management and to ensure that no screen will be created twice by accident.
 */
public class ScreenStorage {
    private static volatile GameScreen gameScreen;
    private static volatile SplashScreen splashScreen;

    public static synchronized GameScreen getGameScreen(Game game){
        return (gameScreen != null) ? gameScreen : (gameScreen = new GameScreen(game));
    }

    public static synchronized SplashScreen getSplashScreen(Game game){
        return (splashScreen != null) ? splashScreen : (splashScreen = new SplashScreen(game));
    }
}
