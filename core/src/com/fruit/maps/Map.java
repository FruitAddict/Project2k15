package com.fruit.maps;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.fruit.logic.Constants;
import com.fruit.utilities.MapObjectParser;
import com.fruit.visual.Assets;

public class Map implements Constants {
    //map must contain reference to the map manager
    private MapManager mapManager;
    //list of all rooms in this map
    private Array<Room> roomArray; //not using it yet TODO
    //current room (the one player is in)
    private Room currentRoom;

    //constructor takes MapManager reference and number of the circle to be generated. Right now there is
    //no save-game capability.
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
                System.out.println("Couldn't generate requested map.");
                System.exit(1);
            }
        }
    }

    public void addRoom(Room room){
        //Adds room to this map,
        if(roomArray!=null){
            roomArray.add(room);
        } else {
            roomArray = new Array<>();
            roomArray.add(room);
        }
    }

    public void changeRoom(Room room){
        //changes the room to the requested one if it exists
        //within current map. Map changes require greater effort
        if(roomArray.contains(room,true)){
            currentRoom=room;
        }
    }

    public void createFirstCircleLevel(){
        Room firstRoom = new Room((TiledMap)Assets.getAsset("64map.tmx",TiledMap.class));
        Room secondRoom = new Room((TiledMap)Assets.getAsset("64map2.tmx",TiledMap.class));
        addRoom(firstRoom);
        addRoom(secondRoom);
        MapObjectParser.addSpawnAndPortalPointsToRoom(firstRoom);
        MapObjectParser.addSpawnAndPortalPointsToRoom(secondRoom);

        firstRoom.setLinkedRoomSouth(secondRoom);
        firstRoom.setLinkedRoomEast(secondRoom);
        firstRoom.setLinkedRoomNorth(secondRoom);
        firstRoom.setLinkedRoomWest(secondRoom);
        secondRoom.setLinkedRoomWest(firstRoom);
        secondRoom.setLinkedRoomEast(firstRoom);
        secondRoom.setLinkedRoomNorth(firstRoom);
        secondRoom.setLinkedRoomSouth(firstRoom);

        currentRoom = firstRoom;
        //initially add all the game objects manually as no references to managers exist yet.
        MapObjectParser.addMapObjectsToWorld(mapManager.getWorldUpdater(), firstRoom);
    }


    public Room getCurrentRoom(){
        return currentRoom;
    }


}
