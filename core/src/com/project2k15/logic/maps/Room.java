package com.project2k15.logic.maps;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.collision.RectangleTypes;
import com.project2k15.logic.entities.abstracted.MovableObject;

public class Room implements RectangleTypes {
    /**
     * Room class. Stores reference to it's tiled map representation.
     * TiledMap - stores reference to the tiledmap (obtained from the mapmanager)
     * exit rectangles - passed to the object manager (when player enters them it changes the room)
     * exitPoints - explained in constructor
     *
     * spawnPosition vectors - center must always be created, others are created only when the map can be accessed from this side
     * (an exit point exists and has a valid link id ).
     *
     * entityList - full list of gameobjects
     *
     * mapObjects - terrain collision objects of this room
     */
    private TiledMap tiledMap;
    private PropertyRectangle exitNorth;
    private PropertyRectangle exitSouth;
    private PropertyRectangle exitWest;
    private PropertyRectangle exitEast;

    private float width;
    private float height;
    private int[] exitPoints;

    private Vector2 spawnPositionCenter;
    private Vector2 spawnPositionNorth;
    private Vector2 spawnPositionEast;
    private Vector2 spawnPositionSouth;
    private Vector2 spawnPositionWest;
    private Map parentMap;

    private Array<MovableObject> gameObjectList;
    private Array<PropertyRectangle> terrainCollisionRecs;


    public Room(Map map, int[] exitPoints, TiledMap tiledMap, Vector2 spawnPos){
        /**
         * ExitPoints format: [idNorth, idEast, idSouth, idWest]
         * if id equals -1, it means that there are no links on this side.
         * If it is not -1, the id number equals to index of the room in the roomArray of this
         * map.
         */
        this.tiledMap = tiledMap;
        parentMap = map;
        this.exitPoints = exitPoints;

        //Sets the width and height of the current room (TiledMap).

        width = getTiledMap().getProperties().get("width", Integer.class)
                * getTiledMap().getProperties().get("tilewidth", Integer.class);

        height = getTiledMap().getProperties().get("height",Integer.class)
                * getTiledMap().getProperties().get("tileheight", Integer.class);

        //Creating portal rectangles, first checks whether those linked rooms exist at all in the parent map. then checks
        //if those ids arent null references and finnaly creates those rectangles.

        try {
            if (exitPoints[0] != -1 ) {
                exitNorth = new PropertyRectangle(getWidth() / 2 - 64, getHeight() - 64, 128, 64, PORTAL_NORTH, exitPoints[0]);
                spawnPositionNorth = new Vector2(getWidth()/2-64,getHeight()-128);
            }
        } catch (IndexOutOfBoundsException ex){
            System.out.println("Id of the linked room at north was out of bounds.");
        }
        try {
            if (exitPoints[1] != -1) {
                exitEast = new PropertyRectangle(getWidth()-64, getHeight() / 2 - 64, 64, 128, PORTAL_EAST, exitPoints[1]);
                spawnPositionEast = new Vector2(getWidth()-128,getHeight()/2-64);
            }
        }catch ( IndexOutOfBoundsException ex){
            System.out.println("Id of the linked room at east was out of bounds.");
        }
        try {
            if (exitPoints[2] != -1) {
                exitSouth = new PropertyRectangle(getWidth() / 2 - 64, 0, 128, 64, PORTAL_SOUTH, exitPoints[2]);
                spawnPositionSouth = new Vector2(getWidth()/2-64,65);
            }
        }catch(IndexOutOfBoundsException ex){
            System.out.println("Id of the linked room at south was out of bounds.");
        }
        try {
            if (exitPoints[3] != -1) {
                exitWest = new PropertyRectangle(0, getHeight() / 2 - 64, 64, 128, PORTAL_WEST, exitPoints[3]);
                spawnPositionWest = new Vector2(65,getHeight()/2-64);
            }
        }catch(IndexOutOfBoundsException ex){
            System.out.println("Id of the linked room at west was out of bounds.");
        }
        spawnPositionCenter = spawnPos;

        //Initalizies array holding the game objects in this room (items, mobs etc)
        gameObjectList = new Array<MovableObject>();

        //gets the collision objects
        MapObjects collisionObjects = tiledMap.getLayers().get("collisionObjects").getObjects();
        terrainCollisionRecs = new Array<PropertyRectangle>();
        for (int i = 0; i < collisionObjects.getCount(); i++) {
            PropertyRectangle obj = new PropertyRectangle(((RectangleMapObject) collisionObjects.get(i)).getRectangle(), TERRAIN);
            terrainCollisionRecs.add(obj);
        }


    }

    public Array<PropertyRectangle> getPortalRecs(){
        /**
         * Returns portal rectangles created in the constructor.
         */
        Array<PropertyRectangle> returnArray = new Array<PropertyRectangle>();
        if(exitNorth!=null){
            returnArray.add(exitNorth);
        }
        if(exitEast!=null){
            returnArray.add(exitEast);
        }
        if(exitWest!=null){
            returnArray.add(exitWest);
        }
        if(exitSouth!=null){
            returnArray.add(exitSouth);
        }
        return returnArray;
    }


    public TiledMap getTiledMap(){
        return tiledMap;
    }

    public Array<MovableObject> getGameObjectList(){
        return gameObjectList;
    }

    public void addGameObject(MovableObject o){
        if(!gameObjectList.contains(o,true))
        gameObjectList.add(o);
    }

    public float getWidth(){
        return width;
    }
    public float getHeight(){
        return height;
    }

    public Vector2 getSpawnPositionCenter(){
        return spawnPositionCenter;
    }

    public Vector2 getSpawnPositionNorth(){
        return spawnPositionNorth!=null ? spawnPositionNorth : spawnPositionCenter;
    }
    public Vector2 getSpawnPositionEast(){
        return spawnPositionEast!=null? spawnPositionEast : spawnPositionCenter;
    }

    public Vector2 getSpawnPositionSouth(){
        return spawnPositionSouth!=null?spawnPositionSouth: spawnPositionCenter;
    }

    public Vector2 getSpawnPositionWest(){
        return spawnPositionWest!=null?spawnPositionWest: spawnPositionCenter;
    }

    public Vector2 getSpawnPositionByType(int type){
        /**
         * Returns spawn position by the given type.
         * Eg. player entered north portal of the room, so he receives
         * south spawn position of the new room etc.
         */
        if(type == PORTAL_NORTH){
            return getSpawnPositionSouth();
        }
        else if(type == PORTAL_EAST){
            return getSpawnPositionWest();
        }
        else if(type == PORTAL_SOUTH){
            return getSpawnPositionNorth();
        }
        else if(type == PORTAL_WEST){
            return getSpawnPositionEast();
        } else {
            return getSpawnPositionCenter();
        }
    }

    public Array<PropertyRectangle> getTerrainCollisionRectangles(){
        return terrainCollisionRecs;
    }

}