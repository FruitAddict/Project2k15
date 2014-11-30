package com.fruit.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fruit.logic.WorldUpdater;
import com.fruit.logic.input.CustomInputMultiplexer;
import com.fruit.logic.input.WorldInputProcessor;
import com.fruit.logic.input.WorldInputProcessorTest;
import com.fruit.visual.WorldRenderer;
import com.fruit.visual.ui.UserInterface;

public class GameScreen implements Screen {

    private Game game;
    private OrthographicCamera camera;
    private WorldUpdater worldUpdater;
    private WorldRenderer worldRenderer;
    private SpriteBatch spriteBatch;
    private CustomInputMultiplexer customInputMultiplexer;
    private UserInterface userInterface;

    public GameScreen(Game game){
        this.game = game;
        worldUpdater = new WorldUpdater();
        camera = new OrthographicCamera();
        spriteBatch = new SpriteBatch();
        worldRenderer = new WorldRenderer(spriteBatch,camera, worldUpdater);
        userInterface = new UserInterface(camera,worldUpdater);
        WorldInputProcessor worldInputProcessor = new WorldInputProcessor(worldUpdater.getObjectManager().getPlayer(), camera);
        customInputMultiplexer = new CustomInputMultiplexer(userInterface, worldInputProcessor);
        Gdx.input.setInputProcessor(customInputMultiplexer);
    }
    @Override
    public void render(float delta) {
        customInputMultiplexer.updateInput();
        worldUpdater.update(delta);
        worldRenderer.render(delta);
        userInterface.act(Math.min(1/30f,delta));
        userInterface.draw();
    }

    @Override
    public void resize(int width, int height) {
        //Resize camera viewport
        camera.setToOrtho(false, 840, 480 / (Gdx.graphics.getWidth() / Gdx.graphics.getHeight()));
        userInterface.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        //setting up the camera after show is called (e.g. switching from app to app)
        camera.setToOrtho(false, 840, 480/(Gdx.graphics.getWidth()/Gdx.graphics.getHeight()));
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
        spriteBatch.dispose();
    }
}
