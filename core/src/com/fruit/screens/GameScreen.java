package com.fruit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fruit.Controller;
import com.fruit.MainGame;
import com.fruit.logic.WorldUpdater;
import com.fruit.logic.input.CustomInputMultiplexer;
import com.fruit.logic.input.WorldInputProcessor;
import com.fruit.logic.input.WorldInputProcessorTest;
import com.fruit.maps.MapManager;
import com.fruit.visual.GameCamera;
import com.fruit.visual.WorldRenderer;
import com.fruit.visual.ui.UserInterface;

/**
 * Class containing game loop and all important systems.
 * The game starts here after menu/splash screen.
 */
public class GameScreen implements Screen {

    private MainGame game;
    //camera with fixed virtual viewport
    private GameCamera camera;
    //map manager that holds abstracted map objects (they know what .tmx file represents each room. ,
    //what mobs to spawn in a room, what items are currently in rooms etc. WorldUpdater takes care
    //of actually placing stuff. This object is created as the first one
    private MapManager mapManager;
    //World updater handles all the game logic, it contains its own object manager that handles
    //adding/removing game objects from the world
    private WorldUpdater worldUpdater;
    //worldRenderer takes care of all the rendering ( lights, world etc. )
    //Contains its own object renderer that knows how to render game objects based on their type ID
    private WorldRenderer worldRenderer;
    //SpriteBatch that is passed to world renderer
    private SpriteBatch spriteBatch;
    //Input multiplexer to reroute user input between Stage (UI) and Game World.
    private CustomInputMultiplexer customInputMultiplexer;
    //User Interface in-game. Different than the main menu
    private UserInterface userInterface;

    public GameScreen(MainGame game){
        this.game = game;
        worldUpdater = new WorldUpdater();
        camera = new GameCamera();
        spriteBatch = new SpriteBatch();
        worldRenderer = new WorldRenderer(spriteBatch,camera, worldUpdater);
        userInterface = new UserInterface(camera,worldUpdater);

        //registering renderer and updater to controller utility class for communication between logic
        //and rendering
        Controller.registerWorldRenderer(worldRenderer);
        Controller.registerWorldUpdater(worldUpdater);

        //input stuff
        WorldInputProcessor worldInputProcessor = new WorldInputProcessor(worldUpdater.getObjectManager().getPlayer(), camera);
        customInputMultiplexer = new CustomInputMultiplexer(userInterface, worldInputProcessor);
        Gdx.input.setInputProcessor(customInputMultiplexer);

        //make camera follow the player
        camera.setObjectToFollow(worldUpdater.getObjectManager().getPlayer());
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
        camera.setToOrtho(false, 840, 480 * (Gdx.graphics.getWidth()/Gdx.graphics.getHeight()));
        userInterface.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        //setting up the camera after show is called (e.g. switching from app to app)
        camera.setToOrtho(false, 840, 480 * (Gdx.graphics.getWidth()/Gdx.graphics.getHeight()));
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
