package com.fruit.maps;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.fruit.logic.Constants;
import com.fruit.utilities.MapObjectParser;
import com.fruit.visual.Assets;

public class Map implements Constants {
    private MapManager mapManager;
    private Array<Room> roomArray; //not using it yet TODO
    private Room currentRoom;
    private int lastID;

    public Map(MapManager mapManager, int circleNumber){ //circle number is level to be generated
        this.mapManager = mapManager;
        roomArray = new Array<>();
        switch(circleNumber) {
            case 1 : {
                createFirstCircleLevel();
                break;
            }
            default:{
                assert false : "Error";
            }
        }
    }

    public void addRoom(Room room){
        if(roomArray!=null){
            roomArray.add(room);
        } else {
            roomArray = new Array<>();
            roomArray.add(room);
        }
    }

    public void changeRoom(Room room){
        if(roomArray.contains(room,true)){
            currentRoom=room;
        }
    }

    public void changeRoom(int idNumber){
        for(Room r : roomArray){
            if(r.ID == idNumber){
                currentRoom= r;
            }
        }
    }

    public void createFirstCircleLevel(){
        Room firstRoom = new Room((TiledMap)Assets.getAsset("64map.tmx",TiledMap.class),lastID++);
        Room secondRoom = new Room((TiledMap)Assets.getAsset("64map2.tmx",TiledMap.class),lastID++);
        roomArray.add(firstRoom);
        roomArray.add(secondRoom);
        MapObjectParser.addSpawnPointsToRoom(firstRoom);
        MapObjectParser.addSpawnPointsToRoom(secondRoom);

        firstRoom.setLinkedRoomSouth(secondRoom);
        firstRoom.setLinkedRoomEast(secondRoom);
        firstRoom.setLinkedRoomNorth(secondRoom);
        firstRoom.setLinkedRoomWest(secondRoom);
        secondRoom.setLinkedRoomWest(firstRoom);
        secondRoom.setLinkedRoomEast(firstRoom);
        secondRoom.setLinkedRoomNorth(firstRoom);
        secondRoom.setLinkedRoomSouth(firstRoom);

        currentRoom = firstRoom;
        MapObjectParser.addMapObjectsToWorld(mapManager.getWorldUpdater().getWorld(), firstRoom);
    }


    public Room getCurrentRoom(){
        return currentRoom;
    }


}
