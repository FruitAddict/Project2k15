package com.project2k15.rendering.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.project2k15.rendering.Assets;
import com.project2k15.test.testmobs.TestGameScreen;

/**
 * Splash screen, appears for 5 seconds when the program is started.
 */
public class SplashScreen implements Screen {

    private Game game;
    private Texture splashImage;
    private SpriteBatch batch;
    private float timePassed;
    private float disappearThreshold;

    public SplashScreen(Game game){
        Assets.loadSplashScreen();
        this.game = game;
        splashImage = Assets.manager.get("splashtoday.jpg");
        batch = new SpriteBatch();
        timePassed = 0f;
        disappearThreshold = 0.5f;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(splashImage,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.end();

        timePassed+=delta;

        if(timePassed > disappearThreshold) {
            this.dispose();
            Assets.disposeAll();
            /**
             * REDIRECT TO TEST GAME SCREEN.
             */
            game.setScreen(new GameScreen(game));
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
}
