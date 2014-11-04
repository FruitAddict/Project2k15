package com.project2k15.test;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.project2k15.assets.Assets;
import com.project2k15.inputprocessors.TestInputProcessor;

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


    public TestGameScreen(Game game){
        this.game = game;
        batch = new SpriteBatch();
        Assets.loadTestMap();
        cam = new OrthographicCamera(30, 30 * (Gdx.graphics.getWidth() / Gdx.graphics.getHeight()));
        cam.setToOrtho(false);
        map = Assets.manager.get("testmap2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map,batch);
        renderer.setView(cam);
        testProc = new TestInputProcessor(cam);
        Gdx.input.setInputProcessor(testProc);
    }
    @Override
    public void render(float delta) {
        testProc.translateCamera();
        cam.update();
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(cam);
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(0));
        renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(1));
        batch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            cam.translate(-1,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (cam.zoom > 0.2) {
                cam.zoom -= 0.03;
            }
            System.out.println(cam.zoom);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.zoom += 0.03;
            System.out.println(cam.zoom);
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
