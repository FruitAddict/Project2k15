package com.fruit.game.maps;


import com.fruit.game.Controller;
import com.fruit.game.logic.Constants;

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
        currentRoom.setPlayerVisitedThisRoom(true);
        if(currentRoom.getLinkedRoomEast() != null){
            currentRoom.getLinkedRoomEast().setPlayerVisitedThisRoom(true);
        }
        if(currentRoom.getLinkedRoomWest() != null) {
            currentRoom.getLinkedRoomWest().setPlayerVisitedThisRoom(true);
        }
        if(currentRoom.getLinkedRoomNorth() != null) {
            currentRoom.getLinkedRoomNorth().setPlayerVisitedThisRoom(true);
        }
        if(currentRoom.getLinkedRoomSouth() != null) {
            currentRoom.getLinkedRoomSouth().setPlayerVisitedThisRoom(true);
        }
    }

    public Room[][] getRoomMatrix(){
        return roomMatrix;
    }

    public Room getCurrentRoom(){
        return currentRoom;
    }

    public void revealMap(){
        for (int i = 0; i < roomMatrix.length; i++) {
            for (int j = 0; j < roomMatrix.length; j++) {
                if(roomMatrix[i][j] != null){
                    roomMatrix[i][j].setPlayerVisitedThisRoom(true);
                }
            }
        }
        Controller.getUserInterface().updateMinimap();
    }


}
