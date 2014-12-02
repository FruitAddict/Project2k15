package com.fruit.maps;


import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fruit.logic.objects.GameObject;

/**
 * Room class. Contains all the spawn& exit points and tiled map representation and items&entities stored.
 */
public class Room {
    private TiledMap tiledMap;
    private Array<GameObject> gameObjectsStored;

    public final int ID;

    private Room linkNorth, linkSouth, linkEast, linkWest = null;

    private boolean exitPointN, exitPointE, exitPointW, exitPointS = false;

    private Vector2 spawnPointCenter;
    private Vector2 spawnPointSouth, spawnPointNorth, spawnPointEast, spawnPointWest = null;

    public Room(TiledMap tiledMap, int id){
        ID = id;
        this.tiledMap = tiledMap;
        gameObjectsStored = new Array<>();

        String exitPoints = tiledMap.getProperties().get("ExitPoints", String.class);
        if(exitPoints.contains("N")){
            exitPointN= true;
        }
        if(exitPoints.contains("S")){
            exitPointS = true;
        }
        if(exitPoints.contains("W")){
            exitPointW = true;
        }
        if(exitPoints.contains("E")){
            exitPointE = true;
        }
    }

    public TiledMap getTiledMap(){
        return tiledMap;
    }


    public Room getLinkedRoomNorth() {
        return linkNorth;
    }

    public Room getLinkedRoomSouth() {
        return linkSouth;
    }

    public Room getLinkedRoomEast() {
        return linkEast;
    }

    public Room getLinkedRoomWest() {
        return linkWest;
    }

    public void setLinkedRoomNorth(Room linkNorth) {
        if(exitPointN) {
            this.linkNorth = linkNorth;
        }
    }

    public void setLinkedRoomSouth(Room linkSouth) {
        if(exitPointS) {
            this.linkSouth = linkSouth;
        }
    }

    public void setLinkedRoomEast(Room linkEast) {
        if(exitPointE) {
            this.linkEast = linkEast;
        }
    }

    public void setLinkedRoomWest(Room linkWest) {
        if(exitPointW) {
            this.linkWest = linkWest;
        }
    }

    public Vector2 getSpawnPointCenter() {
        return spawnPointCenter;
    }

    public Vector2 getSpawnPointSouth() {
        return spawnPointSouth;
    }

    public Vector2 getSpawnPointNorth() {
        return spawnPointNorth;
    }

    public Vector2 getSpawnPointEast() {
        return spawnPointEast;
    }

    public Vector2 getSpawnPointWest() {
        return spawnPointWest;
    }

    public void setSpawnPointCenter(Vector2 spawnPointCenter) {
        this.spawnPointCenter = spawnPointCenter;
    }

    public void setSpawnPointSouth(Vector2 spawnPointSouth) {
        this.spawnPointSouth = spawnPointSouth;
    }

    public void setSpawnPointNorth(Vector2 spawnPointNorth) {
        this.spawnPointNorth = spawnPointNorth;
    }

    public void setSpawnPointEast(Vector2 spawnPointEast) {
        this.spawnPointEast = spawnPointEast;
    }

    public void setSpawnPointWest(Vector2 spawnPointWest) {
        this.spawnPointWest = spawnPointWest;
    }
}
