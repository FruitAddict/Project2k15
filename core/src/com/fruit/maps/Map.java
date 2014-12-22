package com.fruit.maps;

import com.fruit.logic.Constants;

public class Map implements Constants {
    //map must contain reference to the map manager
    private MapManager mapManager;
    //Matrix of all rooms on this map
    private Room[][] roomMatrix;
    //current room (the one player is in)
    private Room currentRoom;

    //constructor takes MapManager reference and number of the circle to be generated. Right now there is
    //no save-game capability.
    public Map(MapManager mapManager, int mapDimensions){
        this.mapManager = mapManager;
        roomMatrix = new Room[mapDimensions][mapDimensions];
    }

    public void setCurrentRoom(Room room){
        if(currentRoom!=null){
            currentRoom.setContainsPlayer(false);
        }
        currentRoom = room;
        currentRoom.setContainsPlayer(true);
    }

    public Room[][] getRoomMatrix(){
        return roomMatrix;
    }

    public Room getCurrentRoom(){
        return currentRoom;
    }


}
