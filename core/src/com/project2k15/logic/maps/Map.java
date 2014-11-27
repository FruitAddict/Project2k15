package com.project2k15.logic.maps;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.managers.MapManager;
import com.project2k15.rendering.Assets;


public class Map {
    /**
     * Map class. Contains array of rooms and their linking points.
     * Rooms hold entity lists etc, dispose method ensure that those references are properly
     * handled by GC.
     */
    private Array<Room> roomArray;
    private Room currentRoom;
    private MapManager manager;

    public Map(MapManager mapManager){
        roomArray = new Array<Room>();
        this.manager = mapManager;
    }

    public void dispose(){
        roomArray.clear();
    }

    public Room getCurrentRoom(){
        return currentRoom;
    }

    public Array<PropertyRectangle> getTerrainRecs(){
        return currentRoom.getTerrainCollisionRectangles();
    }

    /**
     * TEST MAP
     */
    public void createTestMap(){
        Room firstRoom = new Room(this,new int[]{1,1,1,1}, (TiledMap)Assets.manager.get("64map.tmx"), new Vector2(200,100));
        Room secondRoom = new Room(this,new int[]{0,0,0,0},(TiledMap)Assets.manager.get("map.tmx"),new Vector2(300,300));
        roomArray.add(firstRoom);
        roomArray.add(secondRoom);
        currentRoom = roomArray.get(0);
    }

    public void changeRoom(int id){
        currentRoom = roomArray.get(id);
    }

    public Array<Room> getRoomArray(){
        return roomArray;
    }

}
