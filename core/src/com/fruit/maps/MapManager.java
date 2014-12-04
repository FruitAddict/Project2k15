package com.fruit.maps;

import com.fruit.Controller;
import com.fruit.logic.Constants;
import com.fruit.logic.WorldUpdater;
import com.fruit.visual.Assets;

public class MapManager implements Constants {
    /**
     * Map manager that is storing current map and provides methods to
     * request room/map changing based on an event from the Contact Listener.
     */

    //reference to the worldupdater
    private WorldUpdater worldUpdater;
    //reference to the current map ( the one player is in)
    private Map currentMap;

    public MapManager(WorldUpdater updater, boolean newGame){
        this.worldUpdater = updater;
        if(newGame){
            Assets.disposeAll();
            //load intro .tmx files
            Assets.loadIntroLevel();
            //create the first circle map.
            currentMap = new Map(this, 1);
        }
    }

    public Map getCurrentMap(){
        return currentMap;
    }

    public WorldUpdater getWorldUpdater(){
        return worldUpdater;
    }

    public void requestChange(int fromDirection) {
        //generall method to change maps/rooms. Will handle anything
        switch(fromDirection){
            case NORTH_DIR:{
                //first we check if the linked room at the specified direction is not null
                if(currentMap.getCurrentRoom().getLinkedRoomNorth()!=null) {
                    //then we change the room to the requested room using the room object itself
                    currentMap.changeRoom(currentMap.getCurrentRoom().getLinkedRoomNorth());
                    //finally we schedule a change in player's position after this world step using opposite spawn points
                    //from the linekd room
                    worldUpdater.getObjectManager().requestPositionChange(worldUpdater.getObjectManager().getPlayer()
                    ,currentMap.getCurrentRoom().getLinkedRoomNorth().getSpawnPointSouth());
                    onMapChange();
                }
                break;
            }
            case SOUTH_DIR:{
                if(currentMap.getCurrentRoom().getLinkedRoomSouth()!=null) {
                    currentMap.changeRoom(currentMap.getCurrentRoom().getLinkedRoomSouth());
                    worldUpdater.getObjectManager().requestPositionChange(worldUpdater.getObjectManager().getPlayer()
                            ,currentMap.getCurrentRoom().getLinkedRoomSouth().getSpawnPointNorth());
                    onMapChange();
                }
                break;
            }
            case EAST_DIR:{
                if(currentMap.getCurrentRoom().getLinkedRoomEast()!=null) {
                    currentMap.changeRoom(currentMap.getCurrentRoom().getLinkedRoomEast());
                    worldUpdater.getObjectManager().requestPositionChange(worldUpdater.getObjectManager().getPlayer()
                            ,currentMap.getCurrentRoom().getLinkedRoomEast().getSpawnPointWest());
                    onMapChange();
                }
                break;
            }
            case WEST_DIR:{
                if(currentMap.getCurrentRoom().getLinkedRoomWest()!=null) {
                    currentMap.changeRoom(currentMap.getCurrentRoom().getLinkedRoomWest());
                    worldUpdater.getObjectManager().requestPositionChange(worldUpdater.getObjectManager().getPlayer()
                            ,currentMap.getCurrentRoom().getLinkedRoomWest().getSpawnPointEast());
                    onMapChange();
                }
                break;
            }
        }
    }

    public void onMapChange(){
        //on map change, tell the object manager to remove everything and re-add it based on the new map
        worldUpdater.getObjectManager().onMapChange();
        //tell the world renderer to change map that is rendered to the new one
        Controller.getWorldRenderer().changeRenderedMap();
    }
}
