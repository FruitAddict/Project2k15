package com.project2k15.logic.maps;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.collision.RectangleTypes;
import com.project2k15.logic.managers.MapManager;
import com.project2k15.rendering.Assets;


public class Map {
    /**
     * Map class. Contains rooms and their linking points
     */
    private Array<Room> roomArray;
    private Room currentRoom;
    private MapManager manager;

    public Map(MapManager mapManager){
        roomArray = new Array<Room>();
        this.manager = mapManager;
        craeteTestMap();
    }

    public void dispose(){
        roomArray.clear();
    }

    public Room getCurrentRoom(){
        return currentRoom;
    }

    /**
     * TEST MAP
     */
    private void craeteTestMap(){
        Room firstRoom = new Room(new int[]{1,-1,-1,-1}, (TiledMap)Assets.manager.get("64map.tmx"), new Vector2(300,300));
        Room secondRoom = new Room(new int[]{-1,0,-1,-1},(TiledMap)Assets.manager.get("map.tmx"),new Vector2(300,300));
        roomArray.add(firstRoom);
        roomArray.add(secondRoom);
        currentRoom = roomArray.get(0);
    }

    public void changeRoom(int id){
        currentRoom = roomArray.get(id);
    }

    public class Room implements RectangleTypes{
        int type;
        int id;
        int[] exitPoints;
        TiledMap map;
        PropertyRectangle exitNorth;
        PropertyRectangle exitSouth;
        PropertyRectangle exitWest;
        PropertyRectangle exitEast;
        float width;
        float height;
        Vector2 spawnPosition;
        /**
         * Class representing a single room. Contains possible exit points ( Clockwise order
         * NESW ), each linking to specifid ID ( in the map room list )
         */
        public Room(int[] exitPoints, TiledMap map, Vector2 spawnPos){
            this.exitPoints = exitPoints;
            this.map = map;
            width = getMap().getProperties().get("width", Integer.class)
                    * getMap().getProperties().get("tilewidth", Integer.class);

            height = getMap().getProperties().get("height",Integer.class)
                    * getMap().getProperties().get("tileheight", Integer.class);
            if(exitPoints[0]!=-1){
                exitNorth = new PropertyRectangle(getWidth()/2-64,getHeight()-64,128,64,PORTAL,exitPoints[0]);
            }
            if(exitPoints[1]!=-1){
                exitEast = new PropertyRectangle(getWidth()-64,getHeight()/2-64,64,128,PORTAL,exitPoints[1]);
            }
            if(exitPoints[2]!=-1){
                exitSouth = new PropertyRectangle(getWidth()/2-64,0,128,64,PORTAL,exitPoints[2]);
            }
            if(exitPoints[3]!=-1){
                exitWest = new PropertyRectangle(0,getHeight()/2-64,64,128,PORTAL,exitPoints[3]);
            }
            spawnPosition = spawnPos;

          }

        public Array<PropertyRectangle> getPortalRecs(){
            Array<PropertyRectangle> returnArray = new Array<PropertyRectangle>();
            if(exitNorth!=null){
                returnArray.add(exitNorth);
            }
            if(exitEast!=null){
                returnArray.add(exitNorth);
            }
            if(exitWest!=null){
                returnArray.add(exitWest);
            }
            if(exitSouth!=null){
                returnArray.add(exitSouth);
            }
            return returnArray;
        }


        public TiledMap getMap(){
            return map;
        }

        public float getWidth(){
            return width;
        }
        public float getHeight(){
            return height;
        }

        public Vector2 getSpawnPosition(){
            return spawnPosition;
        }

    }
}
