package com.fruit.maps;

import com.fruit.logic.Constants;
import com.fruit.logic.WorldUpdater;
import com.fruit.visual.Assets;

public class MapManager implements Constants {
    private WorldUpdater worldUpdater;
    private Map currentMap;

    public MapManager(WorldUpdater updater, boolean newGame){
        this.worldUpdater = updater;
        if(newGame){
            Assets.disposeAll();
            Assets.loadIntroLevel();
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
                    //we notify the world renderer that the room has changed and
                    //tiled map renderer should change maps
                    worldUpdater.getWorldRenderer().changeRenderedMap();
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
                    worldUpdater.getWorldRenderer().changeRenderedMap();
                    worldUpdater.getObjectManager().requestPositionChange(worldUpdater.getObjectManager().getPlayer()
                            ,currentMap.getCurrentRoom().getLinkedRoomSouth().getSpawnPointNorth());
                    onMapChange();
                }
                break;
            }
            case EAST_DIR:{
                if(currentMap.getCurrentRoom().getLinkedRoomEast()!=null) {
                    currentMap.changeRoom(currentMap.getCurrentRoom().getLinkedRoomEast());
                    worldUpdater.getWorldRenderer().changeRenderedMap();
                    worldUpdater.getObjectManager().requestPositionChange(worldUpdater.getObjectManager().getPlayer()
                            ,currentMap.getCurrentRoom().getLinkedRoomEast().getSpawnPointWest());
                    onMapChange();
                }
                break;
            }
            case WEST_DIR:{
                if(currentMap.getCurrentRoom().getLinkedRoomWest()!=null) {
                    currentMap.changeRoom(currentMap.getCurrentRoom().getLinkedRoomWest());
                    worldUpdater.getWorldRenderer().changeRenderedMap();
                    worldUpdater.getObjectManager().requestPositionChange(worldUpdater.getObjectManager().getPlayer()
                            ,currentMap.getCurrentRoom().getLinkedRoomWest().getSpawnPointEast());
                    onMapChange();
                }
                break;
            }
        }
    }

    public void onMapChange(){
        worldUpdater.getObjectManager().onMapChange();
    }
}
