package com.fruit;

import com.badlogic.gdx.Game;
import com.fruit.screens.SplashScreen;

public class MainGame extends Game {
	@Override
	public void create(){
		setScreen(new SplashScreen(this));
	}
}
