package com.project2k15.rendering.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.project2k15.logic.input.CustomInputMultiplexer;
import com.project2k15.logic.managers.MapManager;
import com.project2k15.logic.managers.ObjectManager;
import com.project2k15.logic.input.WorldInputProcessor;
import com.project2k15.logic.entities.Player;
import com.project2k15.rendering.WorldRenderer;
import com.project2k15.rendering.ui.GuiStage;

/**
 * Main game screen. Contains main gameloop.
 */
public class GameScreen implements Screen {
    /**
     * Game - instance received from the MainGame class, needed to change screens. (Eg. go back to main menu).
     * OrthographicCamera - camera to manage all the wizardy connecting game world with the view port on the screen
     * ObjectManager - Manager for updating all in-game objects. At the moment entities handle their own animations
     * MapManager - handles everything map releated
     * GuiStage - draws UI to the screen, reroutes input and generally handles UI.
     * SpriteBatch - main batch for drawing stuff
     * Player
     * InputMultiplexer - Used to reroute input between UI and game.
     */
    Game game;
    ObjectManager objectManager;
    OrthographicCamera gameCamera;
    MapManager mapManager;
    GuiStage guiStage;
    Player player;
    SpriteBatch batch;
    CustomInputMultiplexer inputMultiplexer;

    /**
     * WorldRenderer that hides ugly world rendering code from the game loop
     */
    WorldRenderer worldRenderer;

    public GameScreen(Game game){
        this.game = game;
    }

    @Override
    public void render(float delta) {
        /**
         * Game loop. Handles input, renders map, renders objects and finnaly renders UI.
         */
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        inputMultiplexer.updateInput(delta);
        worldRenderer.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        /**
         * Resizes stage and the camera viewport (again according to the viewport)
         */
        gameCamera.setToOrtho(false, 300, 300*(Gdx.graphics.getWidth()/Gdx.graphics.getHeight()));
        guiStage.getViewport().update(width, height, true);
        System.out.println(300*(width/height));
    }

    @Override
    public void show() {
        /**
         * Initializer
         */
        gameCamera = new OrthographicCamera();
        mapManager = new MapManager();
        batch = new SpriteBatch();
        objectManager = new ObjectManager(mapManager.getCollisionLayer(),(int)mapManager.getMapWidth(),(int)mapManager.getMapHeight());
        player = new Player(mapManager.getSpawnPosition().x, mapManager.getSpawnPosition().y,batch,objectManager);
        objectManager.addObject(player);
        guiStage = new GuiStage(objectManager,gameCamera,player,mapManager,batch);
        worldRenderer = new WorldRenderer(mapManager.getCurrentMap(),objectManager,guiStage,gameCamera,batch);

        /**
         * Setting up the camera
         * Sets the viewport to 300 pixels wide and 300 pixels * aspect ratio height
         * ( correct proportions, squares are squares)
         */
        gameCamera.setToOrtho(false, 300, 300*(Gdx.graphics.getWidth()/Gdx.graphics.getHeight()));
        /**
         * Setting the input processors
         * The input first goes to guiStage, then reroutes to game screen if not used.
         */
        WorldInputProcessor worldInputProcessor = new WorldInputProcessor(gameCamera,player,mapManager.getMapWidth(),mapManager.getMapHeight());
        inputMultiplexer = new CustomInputMultiplexer(guiStage, worldInputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);

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
        guiStage.dispose();
    }
}
