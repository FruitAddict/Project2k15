package com.project2k15.logic.managers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.project2k15.rendering.Assets;

/**
 * Map manager. Holds all the references to rooms/maps and joins them together(TODO)
 */
public class MapManager {
    /**
     * currentMap holds reference to the currently loaded map.
     */
    TiledMap currentMap;
    Vector2 spawnPosition;

    public MapManager(){
        Assets.loadTestMap();
        currentMap = Assets.manager.get("64map.tmx");
        spawnPosition = new Vector2(300,300);
    }

    public MapLayer getCollisionLayer() {
        /**
         * Returns layer of rectangles that will be turned into PropertyRectangles and
         * inserted into a quadtree.
         */
        return currentMap.getLayers().get("collisionObjects");
    }

    public TiledMap getCurrentMap(){
        /**
         * Returns currently loaded map/room;
         */
        return currentMap;
    }

    public float getMapWidth() {
        return Float.parseFloat(currentMap.getProperties().get("width").toString()) * 64;
    }

    public float getMapHeight() {
        return Float.parseFloat(currentMap.getProperties().get("height").toString()) * 64;
    }

    public Vector2 getSpawnPosition() {
        return spawnPosition;
    }
}
