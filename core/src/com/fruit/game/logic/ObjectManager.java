package com.fruit.game.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;
import com.fruit.game.utilities.MapObjectParser;

public class ObjectManager {

    //array to store game objects
    private Array<GameObject> gameObjects;
    //array to store objects scheduled to be added to the world
    private Array<GameObject> scheduledToAdd;
    //array to store objects scheduled to be removed from the world
    private Array<GameObject> scheduledToRemove;
    //array to store objects that are scheduled to get their position changed
    //e.g. player during changing rooms.
    private Array<PositionUpdateRequest> scheduledToUpdatePos;
    //reference to the player
    private Player player;
    //box2d worldupdater reference
    private WorldUpdater worldUpdater;
    //when removeFlag is true, the update method will remove everything from both the object array
    //and the game world except for player and then turn this flag to false.
    private boolean removeFlag = false;

    public ObjectManager(WorldUpdater updater){
        gameObjects = new Array<>();
        scheduledToAdd = new Array<>();
        scheduledToRemove = new Array<>();
        scheduledToUpdatePos = new Array<>();
        this.worldUpdater = updater;
        player = new Player(this,3,3);
        gameObjects.add(player);
        player.addToBox2dWorld(worldUpdater.getWorld());
    }

    public void update(float delta){
        if(scheduledToAdd.size > 0){
            //first we check if any objects need to be added to the gameworld
            for(GameObject o : scheduledToAdd){
                o.addToBox2dWorld(worldUpdater.getWorld());
                gameObjects.add(o);
            }
        }
        scheduledToAdd.clear();
        if(scheduledToRemove.size>0){
            //then we do the same for objects scheduled for removal
            for(GameObject o: scheduledToRemove){
                if(gameObjects.contains(o,true)) {
                    //we check whether this object exists in the gameObject list
                    worldUpdater.getWorld().destroyBody(o.getBody());
                    gameObjects.removeValue(o, true);
                }
            }
        }
        scheduledToRemove.clear();
        if(scheduledToUpdatePos.size>0){
            //same thing for updating position rapidly
            for(PositionUpdateRequest ur : scheduledToUpdatePos){
                if(gameObjects.contains(ur.requestedObject,true)){
                    ur.requestedObject.getBody().setTransform(ur.requestedPosition,ur.requestedObject.getBody().getAngle());
                }
            }
        }
        scheduledToUpdatePos.clear();

        if(gameObjects.size>0){
            //finnaly we update all objects using their update methods
            //that can contain code for AI etc.
            for(GameObject o: gameObjects){
                o.update(delta);
            }
        }
        //if the world was flagged to be cleared(between room/map changes)
        if(removeFlag){
           onRoomChangeRequest();
        }
    }

    public void onRoomChangeRequest(){
        //update all the last know positions just in case
        for(GameObject o: gameObjects){
            o.setLastKnownX(o.getBody().getPosition().x);
            o.setLastKnownY(o.getBody().getPosition().y);
        }
        Array<Body> bodies = new Array<>();
        worldUpdater.getWorld().getBodies(bodies);
        //we update all the objects lastKnownPositions to their current position if they are to be readded later
        //we get all the bodies from the world
        for(Body body : bodies){
            if(!(body.getUserData() instanceof  Player)) {
                //and destroy them
                worldUpdater.getWorld().destroyBody(body);
            }
        }
        //we clear the gameobject array (other lists are already clear
        //as this is the last step of this loop)
        gameObjects.clear();
        //we readd the player into it
        gameObjects.add(player);
        removeFlag=false;
        //we add all the objects from the new room/map
        MapObjectParser.addMapObjectsToWorld(worldUpdater, worldUpdater.getMapManager().getCurrentMap().getCurrentRoom());
    }

    public void addObject(GameObject o){
        scheduledToAdd.add(o);
        //try to add it to the room too
        worldUpdater.getMapManager().getCurrentMap().getCurrentRoom().addGameObject(o);
    }

    public void addObjects(Array<GameObject> o){
        scheduledToAdd.addAll(o);
    }

    public void removeObject(GameObject o){
        scheduledToRemove.add(o);
        //remove it from the room if it exists in this list
        worldUpdater.getMapManager().getCurrentMap().getCurrentRoom().removeGameObject(o);
    }

    public void removeAllGameObjects(boolean withPlayer){
        scheduledToRemove.addAll(gameObjects);
        if(!withPlayer) {
            scheduledToRemove.removeValue(player, true);
        }
    }

    public void onMapChange(){
        //when mapmanager calls this function, the flag to remove everything
        //is true and on the next object update everything will be removed
        //(except for player)
        removeFlag = true;
    }

    public void requestPositionChange(GameObject o, Vector2 position){
        scheduledToUpdatePos.add(new PositionUpdateRequest(o,position));
    }

    public Player getPlayer(){
        return player;
    }

    public Array<GameObject> getGameObjects(){
        return gameObjects;
    }

    public int getNumberOfObjects(){
        return gameObjects.size;
    }

    public WorldUpdater getWorldUpdater(){
        return worldUpdater;
    }

    private class PositionUpdateRequest {
        /**
         * Inner class to queue position update requests up easily.
         * Contains reference to the object (subject to pos. request) and vector2
         * with requested position.
         */
        private Vector2 requestedPosition;
        private GameObject requestedObject;

        public PositionUpdateRequest(GameObject requestedObject, Vector2 requestedPosition){
            this.requestedObject = requestedObject;
            this.requestedPosition = requestedPosition;
        }
    }
}
