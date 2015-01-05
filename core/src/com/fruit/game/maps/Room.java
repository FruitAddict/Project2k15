package com.fruit.game.maps;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.utilities.MapObjectParser;

/**
 * Room class. Contains all the spawn& exit points and tiled map representation and items&entities stored.
 */
public class Room implements Constants {

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
    //properties and portal positions
    private Vector2 spawnPointCenter;
    private Vector2 spawnPointSouth, spawnPointNorth, spawnPointEast, spawnPointWest = null;
    private Vector2 portalPointSouth, portalPointNorth, portalPointEast, portalPointWest;
    //mob spawn points
    private Array<Vector2> mobSpawnPoints;
    //tile height&width
    private float tileWidth, tileHeight;
    //array to hold info about lights
    private Array<StaticLightContainer> staticLightPositions;
    //holds boolean indicating player presence in this room
    private boolean containsPlayer;
    //indicated whether this room is a boss room
    private boolean bossRoom;

    public Room(TiledMap tiledMap){
        this.tiledMap = tiledMap;
        gameObjectsStored = new Array<GameObject>();
        mobSpawnPoints = new Array<Vector2>();
        staticLightPositions = new Array<StaticLightContainer>();

        //get possible exit points in this room (so room transition can be blocked for the player easily)
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
        //parse and set spawn points and portals
        MapObjectParser.addInfoFromTiledMapToRoom(this);
    }

    public Array<GameObject> getGameObjectsStored(){
        return gameObjectsStored;
    }

    public void addGameObject(GameObject o){
        if(o.getSaveInRooms()){
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

    public Vector2 getSpawnPoint(int direction){
        switch(direction){
            case NORTH_DIR:{
                return spawnPointNorth!=null?spawnPointNorth:spawnPointCenter;
            }
            case SOUTH_DIR:{
                return spawnPointSouth!=null?spawnPointSouth:spawnPointCenter;
            }
            case EAST_DIR:{
                return spawnPointEast!=null?spawnPointEast:spawnPointCenter;
            }
            case WEST_DIR:{
                return spawnPointWest!=null?spawnPointWest:spawnPointCenter;
            }
            default: {
                return spawnPointCenter;
            }
        }
    }

    public Array<Vector2> getMobSpawnPoints(){
        return mobSpawnPoints;
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

    public float getTiledMapWidth(){
        return tiledMap.getProperties().get("width",Integer.class)*tileWidth;
    }

    public float getTiledMapHeight(){
        return tiledMap.getProperties().get("height",Integer.class)*tileHeight;
    }

    public float getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(float tileWidth) {
        this.tileWidth = tileWidth;
    }

    public float getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(float tileHeight) {
        this.tileHeight = tileHeight;
    }

    public Vector2 getPortalPointSouth() {
        return portalPointSouth;
    }

    public void setPortalPointSouth(Vector2 portalPointSouth) {
        this.portalPointSouth = portalPointSouth;
    }

    public Vector2 getPortalPointNorth() {
        return portalPointNorth;
    }

    public void setPortalPointNorth(Vector2 portalPointNorth) {
        this.portalPointNorth = portalPointNorth;
    }

    public Vector2 getPortalPointEast() {
        return portalPointEast;
    }

    public void setPortalPointEast(Vector2 portalPointEast) {
        this.portalPointEast = portalPointEast;
    }

    public Vector2 getPortalPointWest() {
        return portalPointWest;
    }

    public void setPortalPointWest(Vector2 portalPointWest) {
        this.portalPointWest = portalPointWest;
    }

    public void addMobSpawnPoint(Vector2 vector2){
        mobSpawnPoints.add(vector2);
    }

    public Array<StaticLightContainer> getStaticLightPositions(){
        return staticLightPositions;
    }

    public void addStaticLight(Vector2 position, Color color,float length){
        staticLightPositions.add(new StaticLightContainer(position,color,length));
    }

    public boolean isContainsPlayer() {
        return containsPlayer;
    }

    public void setContainsPlayer(boolean containsPlayer) {
        this.containsPlayer = containsPlayer;
    }

    public boolean isBossRoom() {
        return bossRoom;
    }

    public void setBossRoom(boolean bossRoom) {
        this.bossRoom = bossRoom;
    }

    public class StaticLightContainer{
        /**
         * This class contains x,y positions of a light and its color
         */
        public Vector2 position;
        public Color color;
        public float length;

        public StaticLightContainer(Vector2 position, Color color, float length){
            this.position = position;
            this.color = color;
            this.length = length;
        }
    }
}
