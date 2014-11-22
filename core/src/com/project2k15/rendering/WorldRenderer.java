package com.project2k15.rendering;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.project2k15.logic.managers.ObjectManager;
import com.project2k15.rendering.ui.GuiStage;

public class WorldRenderer {
    /**
     * WorldRenderer class. Contaisn references to map, object manager, gui camera and stores the tiled map renderer.
     * boolean debug enabled to use for quadtree drawing atm.
     */
    private TiledMap map;
    private ObjectManager objectManager;
    private GuiStage guiStage;
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
    private SpriteBatch batch;
    public static boolean debugEnabled = false;

    public WorldRenderer(TiledMap map, ObjectManager manager, GuiStage guiStage, OrthographicCamera camera, SpriteBatch batch){
        this.map = map;
        this.objectManager=manager;
        this.guiStage = guiStage;
        this.camera= camera;
        this.batch = batch;
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map, batch);
    }

    public void render(float delta){
        camera.update();
        tiledMapRenderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        tiledMapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(0));
        tiledMapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(1));
        tiledMapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(2));
        objectManager.update(delta);
        if(debugEnabled){
            objectManager.getTree().debugDraw(batch);
        }
        batch.end();
        guiStage.act(Math.min(delta,1/30f));
        guiStage.draw();
    }
}
