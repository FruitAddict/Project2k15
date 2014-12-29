package com.fruit.maps;

import com.badlogic.gdx.math.Vector2;
import com.fruit.Controller;
import com.fruit.logic.Constants;
import com.fruit.logic.WorldUpdater;
import com.fruit.utilities.Utils;
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
    //seed for random generator
    private long seed;

    public MapManager(WorldUpdater updater, long seed){
        this.worldUpdater = updater;
        //seed the random
        Utils.initializeMapRandomGen(seed);
        Assets.disposeAll();
        //create the first circle map.
        //currentMap = MapGenerator.generateMap(1);
        currentMap = MapGenerator.generateMap(this,1);

    }

    public Map getCurrentMap(){
        return currentMap;
    }

    public WorldUpdater getWorldUpdater(){
        return worldUpdater;
    }

    public void requestChange(int fromDirection) {
        //general method to change maps/rooms. Will handle anything
        //called from world contact listener
        switch(fromDirection){
            case NORTH_DIR:{
                //first we check if the linked room at the specified direction is not null
                if(currentMap.getCurrentRoom().getLinkedRoomNorth()!=null) {
                    //temporairly store the reference to the north portal center vector
                    Vector2 tempPortalVector = currentMap.getCurrentRoom().getPortalPointNorth();
                    //then we change the room to the requested room using the room object itself
                    currentMap.setCurrentRoom(currentMap.getCurrentRoom().getLinkedRoomNorth());
                    //finally we schedule a change in player's position after this world step using opposite spawn points
                    //from the linked room
                    worldUpdater.getObjectManager().requestPositionChange(worldUpdater.getObjectManager().getPlayer()
                    ,currentMap.getCurrentRoom().getSpawnPoint(SOUTH_DIR));
                    //invoke onMapChange method (see below), pass it the new spawn point, direction the player will
                    // come out from and boolean indicating that the renderer should do a smooth transition between maps
                    onMapChange(tempPortalVector, currentMap.getCurrentRoom().getPortalPointSouth(), SOUTH_DIR, true);
                }
                break;
            }
            case SOUTH_DIR:{
                if(currentMap.getCurrentRoom().getLinkedRoomSouth()!=null) {
                    Vector2 tempPortalVector = currentMap.getCurrentRoom().getPortalPointSouth();
                    currentMap.setCurrentRoom(currentMap.getCurrentRoom().getLinkedRoomSouth());
                    worldUpdater.getObjectManager().requestPositionChange(worldUpdater.getObjectManager().getPlayer()
                            , currentMap.getCurrentRoom().getSpawnPoint(NORTH_DIR));
                    onMapChange(tempPortalVector, currentMap.getCurrentRoom().getPortalPointNorth(), NORTH_DIR, true);
                }
                break;
            }
            case EAST_DIR:{
                if(currentMap.getCurrentRoom().getLinkedRoomEast()!=null) {
                    Vector2 tempPortalVector = currentMap.getCurrentRoom().getPortalPointEast();
                    currentMap.setCurrentRoom(currentMap.getCurrentRoom().getLinkedRoomEast());
                    worldUpdater.getObjectManager().requestPositionChange(worldUpdater.getObjectManager().getPlayer()
                            , currentMap.getCurrentRoom().getSpawnPoint(WEST_DIR));
                    onMapChange(tempPortalVector, currentMap.getCurrentRoom().getPortalPointWest(), WEST_DIR, true);
                }
                break;
            }
            case WEST_DIR:{
                if(currentMap.getCurrentRoom().getLinkedRoomWest()!=null) {
                    Vector2 tempPortalVector = currentMap.getCurrentRoom().getPortalPointWest();
                    currentMap.setCurrentRoom(currentMap.getCurrentRoom().getLinkedRoomWest());
                    worldUpdater.getObjectManager().requestPositionChange(worldUpdater.getObjectManager().getPlayer()
                            , currentMap.getCurrentRoom().getSpawnPoint(EAST_DIR));
                    onMapChange(tempPortalVector, currentMap.getCurrentRoom().getPortalPointEast(), EAST_DIR, true);
                }
                break;
            }
        }
    }

    public void onMapChange(Vector2 portalPosition, Vector2 spawnPos,int direction, boolean doSmoothTransition){
        //on map change, tell the object manager to remove everything and re-add it based on the new map
        worldUpdater.getObjectManager().onMapChange();
        //tell the world renderer to change map that is rendered to the new one and pass it the new spawn position
        //and transition boolean (see changeRenderedMap method)
        Controller.getWorldRenderer().changeRenderedMap(new Vector2(portalPosition.x* PIXELS_TO_UNITS,portalPosition.y* PIXELS_TO_UNITS),
                new Vector2(spawnPos.x* PIXELS_TO_UNITS, spawnPos.y* PIXELS_TO_UNITS), direction, doSmoothTransition);
    }

    public float getCurrentMapWidth(){
        if(currentMap.getCurrentRoom()!=null){
            return currentMap.getCurrentRoom().getTiledMapWidth();
        } else {
            return 0;
        }
    }

    public float getCurrentMapHeight(){
        if(currentMap.getCurrentRoom()!=null){
            return currentMap.getCurrentRoom().getTiledMapHeight();
        }else {
            return 0;
        }
    }
}
