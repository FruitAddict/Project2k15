package com.fruit.visual;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.fruit.logic.Constants;
import com.fruit.logic.WorldUpdater;

public class WorldRenderer implements Constants {
    //TiledMap renderer.
    private TiledMapRenderer tiledMapRenderer;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    //world updater reference to fetch list of objects to pass to Object Renderer.
    private WorldUpdater worldUpdater;
    //Object renderer that handles rendering game objects (e.g. player, projectiles, collectibles etc).
    private ObjectRenderer objectRenderer;


    public WorldRenderer(SpriteBatch batch, OrthographicCamera camera, WorldUpdater worldUpdater){
        tiledMapRenderer = new OrthogonalTiledMapRenderer(worldUpdater.getCurrentTiledMap(), batch);
        objectRenderer = new ObjectRenderer();
        this.batch = batch;
        this.camera = camera;
        this.worldUpdater = worldUpdater;
        
    }
    public void render(float delta){
        camera.update();
        tiledMapRenderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.render();
        batch.begin();
        objectRenderer.render(delta, worldUpdater.getObjectManager().getGameObjects(), batch);
        batch.end();

    }
}
