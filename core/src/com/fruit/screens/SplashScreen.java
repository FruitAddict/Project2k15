package com.fruit.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fruit.MainGame;
import com.fruit.managers.Assets;

/**
 * Splash screen, appears for 5 seconds when the program is started.
 */
public class SplashScreen implements Screen,TweenAccessor<SplashScreen> {

    private Game game;
    private Texture splashImage;
    private SpriteBatch batch;
    private float timePassed;
    private float disappearThreshold;
    private BitmapFont font;

    public SplashScreen(Game game){
        Assets.loadSplashScreen();
        Assets.loadTestMap();
        this.game = game;
        Tween.registerAccessor(SplashScreen.class, this);
        splashImage = (Texture)Assets.getAsset("splashtoday.jpg", Texture.class);
        batch = new SpriteBatch();
        timePassed = 0f;
        disappearThreshold = 0.5f;
        font = new BitmapFont();
        font.scale(2);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timePassed+=delta;
        if (Assets.manager.update() && timePassed > 5) {
            this.dispose();
            game.setScreen(new GameScreen(game));
        } else {
            batch.begin();
            batch.draw(splashImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            font.draw(batch, Float.toString(Assets.manager.getProgress()), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            batch.end();
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public int getValues(SplashScreen splashScreen, int i, float[] floats) {
        return 0;
    }

    @Override
    public void setValues(SplashScreen splashScreen, int i, float[] floats) {

    }
}
