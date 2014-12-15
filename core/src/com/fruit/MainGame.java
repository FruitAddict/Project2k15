package com.fruit;


import com.badlogic.gdx.Game;
import com.fruit.screens.SplashScreen;

/**
 * @author FruitAddict
 * The core class, is instantialized when the game is first run.
 */
public class MainGame extends Game {

	@Override
	public void create(){
		setScreen(new SplashScreen(this));
	}

}
