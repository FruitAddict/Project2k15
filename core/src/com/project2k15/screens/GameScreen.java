package com.project2k15.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.project2k15.utilities.ObjectManager;

/**
 * Main game screen. Contains main gameloop.
 */
public class GameScreen implements Screen {
    /**
     * Game - instance received from the MainGame class, needed to change screens. (Eg. go back to main menu).
     * OrthographicCamera - camera to manage all the wizardy connecting game world with the view port on the screen
     * ObjectManager - Manager for updating all in-game objects. At the moment entities handle their own animations
     */
    Game game;
    ObjectManager objectManager;
    OrthographicCamera mainCamera;

    public GameScreen(Game game){
        this.game = game;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        /**
         * Initializer
         */
        mainCamera = new OrthographicCamera();
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

    }
}
