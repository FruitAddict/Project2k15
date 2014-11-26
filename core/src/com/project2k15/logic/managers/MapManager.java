package com.project2k15.logic.managers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.project2k15.logic.input.WorldInputProcessor;
import com.project2k15.logic.maps.Map;
import com.project2k15.logic.maps.RoomFiller;
import com.project2k15.rendering.Assets;

/**
 * Map manager. Holds all the references to rooms/maps and joins them together
 */
public class MapManager {
    /**
     * currentMap holds reference to the currently loaded map and
     * objectManager reference to recreate the quadtree each time the map is changed
     * inputProcessor reference to change the map size for bounds check ( so the camera
     * doesnt go over the edges)
     */
    private Vector2 spawnPosition;
    private Map currentMap;
    private Controller controller;

    public void setController(Controller controller){
        this.controller = controller;
    }

    public void createTestMap(){
        currentMap = new Map(this);
        currentMap.createTestMap();
        spawnPosition = currentMap.getCurrentRoom().getSpawnPositionCenter();
        RoomFiller.fillRoom(currentMap.getRoomArray().get(0),20,controller.getObjectManager(),controller);
        RoomFiller.fillRoom(currentMap.getRoomArray().get(1),20,controller.getObjectManager(),controller);
    }

    public Map getCurrentMap(){
        /**
         * Returns currently loaded map/room;
         */
        return currentMap;
    }

    public void changeMap(Map map){
        currentMap = map;
        controller.getWorldInputProcessor().setMapSize(getMapWidth(),getMapHeight());
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

