package com.project2k15.core;

import com.badlogic.gdx.Game;
import com.project2k15.logic.ScreenStorage;

/**
 * Main class of the program. Extends Game to allow easy screen management and switching.
 */

public class MainGame extends Game {

	@Override
	public void create() {
		setScreen(ScreenStorage.getSplashScreen(this));
	}

}
