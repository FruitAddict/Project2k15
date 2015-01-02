package com.fruit.game;


import com.badlogic.gdx.Game;
import com.fruit.game.screens.SplashScreen;

/**
 * @author FruitAddict
 * The core class, is instantialized when the game is first run.
 */
public class MainGame extends Game {

	@Override
	public void create(){
		setScreen(new SplashScreen(this));
		Controller.registerMainGame(this);
	}

}
