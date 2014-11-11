package com.project2k15.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.project2k15.entities.Player;
import com.project2k15.utilities.Assets;
import com.project2k15.utilities.ObjectManager;
import com.project2k15.utilities.TestInputProcessor;

/**
 * Various tests are performed here
 */
public class TestGameScreen implements Screen {
    Game game;
    SpriteBatch batch;
    OrthographicCamera cam;
    TestInputProcessor testProc;
    TiledMap map;
    TiledMapRenderer renderer;
    Player player;
    ObjectManager manager;


    public TestGameScreen(Game game){
        this.game = game;
        batch = new SpriteBatch();
        Assets.loadTestMap();
        cam = new OrthographicCamera(30, 30 * (Gdx.graphics.getWidth() / Gdx.graphics.getHeight()));
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = Assets.manager.get("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, batch);
        renderer.setView(cam);
        player = new Player(124, 250);
        testProc = new TestInputProcessor(cam, player);
        cam.zoom = 0.5f;
        Gdx.input.setInputProcessor(testProc);
        manager = new ObjectManager(map.getLayers().get("collisionObjects"));
        manager.objectList.add(player);
    }
    @Override
    public void render(float delta) {
        testProc.translateCamera();
        cam.update();
        manager.update(delta);
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(cam);
        batch.setProjectionMatrix(cam.combined);
        renderer.render();
        batch.begin();
        batch.draw((Texture) Assets.manager.get("playersheet.png"), player.getPosition().x, player.getPosition().y, player.getWidth(), player.getHeight());
        batch.end();

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
