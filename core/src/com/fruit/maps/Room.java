package com.fruit.maps;


import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fruit.logic.objects.*;

/**
 * Room class. Contains all the spawn& exit points and tiled map representation and items&entities stored.
 */
public class Room {
    //Tiled map representation of this room
    private TiledMap tiledMap;
    //Game objects this room contains (eg. clutter like rocks, mobs and items).
    private Array<GameObject> gameObjectsStored;
    //rooms linked in each direction, starts with null. Can be set to anything
    private Room linkNorth, linkSouth, linkEast, linkWest = null;

    //booleans to check whether specific exit points are possible in this room, so when
    //other rooms try to link to this room from unavailable direction no link is made.
    private boolean exitPointN, exitPointE, exitPointW, exitPointS = false;

    //spawn positions (center must always be present). Auto generated based on the .tmx map
    //properties
    private Vector2 spawnPointCenter;
    private Vector2 spawnPointSouth, spawnPointNorth, spawnPointEast, spawnPointWest = null;
    //mob spawn points
    private Array<Vector2> mobSpawnPoints;

    public Room(TiledMap tiledMap){
        this.tiledMap = tiledMap;
        gameObjectsStored = new Array<GameObject>();
        mobSpawnPoints = new Array<Vector2>();

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

    public Array<GameObject> getGameObjectsStored(){
        return gameObjectsStored;
    }

    public void addGameObject(GameObject o){
        //we only store characters right now (e.g. mobs)
        //todo items
        if(o instanceof com.fruit.logic.objects.Character){
            gameObjectsStored.add(o);
        }
    }

    public void removeGameObject(GameObject o){
        if(gameObjectsStored.contains(o,true)){
            gameObjectsStored.removeValue(o,true);
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
        //if it is possible to link something on the X side, create the link
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
