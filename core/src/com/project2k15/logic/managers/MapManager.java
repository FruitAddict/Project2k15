package com.project2k15.logic.managers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.project2k15.logic.input.WorldInputProcessor;
import com.project2k15.logic.maps.Map;
import com.project2k15.rendering.Assets;

/**
 * Map manager. Holds all the references to rooms/maps and joins them together(TODO)
 */
public class MapManager {
    /**
     * currentMap holds reference to the currently loaded map and
     * objectManager reference to recreate the quadtree each time the map is changed
     * inputProcessor reference to change the map size for bounds check ( so the camera
     * doesnt go over the edges)
     */
    private Vector2 spawnPosition;
    private ObjectManager objectManager;
    private WorldInputProcessor worldInputProcessor;
    private Map currentMap;

    public MapManager(){
        Assets.loadTestMap();
        currentMap = new Map(this);
        spawnPosition = currentMap.getCurrentRoom().getSpawnPosition();
    }

    /**
     * Test map
     * TODO map generator
     */


    public void setWorldInputProcessor(WorldInputProcessor processor){
        worldInputProcessor = processor;
    }

    public void setObjectManager(ObjectManager objectManager){
        this.objectManager = objectManager;
    }

    public Map getCurrentMap(){
        /**
         * Returns currently loaded map/room;
         */
        return currentMap;
    }

    public void changeMap(Map map){
        currentMap = map;
        objectManager.setMap(currentMap);
        worldInputProcessor.setMapSize(getMapWidth(),getMapHeight());
    }

    public float getMapWidth() {
        return currentMap.getCurrentRoom().getWidth();
    }

    public float getMapHeight() {
        return currentMap.getCurrentRoom().getHeight();
    }

    public Vector2 getSpawnPosition() {
        return spawnPosition;
    }
}

