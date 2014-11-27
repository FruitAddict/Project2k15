package com.project2k15.rendering;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.project2k15.logic.managers.Controller;
import com.project2k15.logic.managers.MapManager;
import com.project2k15.logic.managers.ObjectManager;
import com.project2k15.rendering.objectrenderer.ObjectRenderer;
import com.project2k15.rendering.ui.GuiStage;

import java.util.PriorityQueue;

public class WorldUpdater {
    /**
     * WorldUpdater class. Contains references to mapManager, object manager, gui camera, objectrenderer and stores the tiled map renderer.
     * boolean debug enabled to use for quadtree drawing atm.
     */
    private MapManager mapManager;
    private TiledMap map;
    private ObjectManager objectManager;
    private GuiStage guiStage;
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
    private SpriteBatch batch;
    public static boolean debugEnabled = false;
    private Controller controller;
    private ObjectRenderer objectRenderer;
    //test TODO various msg rendering
    private PriorityQueue<String> messageQueue;

    public WorldUpdater(){
        messageQueue = new PriorityQueue<String>();
    }

    public void setController(Controller controller){
        this.controller = controller;
        this.mapManager = controller.getMapManager();
        this.objectManager= controller.getObjectManager();
        this.guiStage = controller.getGuiStage();
        this.camera= controller.getCam();
        this.batch = controller.getBatch();
        map = mapManager.getCurrentMap().getCurrentRoom().getTiledMap();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map, batch);
        objectRenderer = new ObjectRenderer();
    }

    public void onMapChanged(){
        map = mapManager.getCurrentMap().getCurrentRoom().getTiledMap();
        messageQueue.offer("Map changed...");
    }

    public void update(float delta){
        camera.update();
        tiledMapRenderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        tiledMapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(0));
        tiledMapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(1));
        tiledMapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(2));
        objectManager.update(delta);
        objectRenderer.render(delta, controller.getObjectManager().getObjectList(), batch);
        if(debugEnabled){
            objectManager.getTree().debugDraw(batch);
        }
        batch.end();
        guiStage.act(Math.min(delta,1/30f));
        guiStage.draw();
    }
}
