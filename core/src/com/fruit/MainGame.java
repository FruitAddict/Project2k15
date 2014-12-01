package com.fruit;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.fruit.screens.SplashScreen;

public class MainGame extends Game {
	//tween manager to be used through the game
	public static TweenManager tweenManager;
	@Override
	public void create(){
		setScreen(new SplashScreen(this));

		//init the tween manager
		tweenManager = new TweenManager();
		//set max attributes to 4 for rgba colors
		Tween.setCombinedAttributesLimit(4);
	}
}
