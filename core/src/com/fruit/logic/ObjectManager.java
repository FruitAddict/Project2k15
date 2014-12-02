package com.fruit.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.fruit.logic.objects.GameObject;
import com.fruit.logic.objects.Player;
import com.fruit.utilities.MapObjectParser;

public class ObjectManager {
    //array to store game objects
    private Array<GameObject> gameObjects;
    //array to store objects scheduled to be added to the world
    private Array<GameObject> scheduledToAdd;
    //array to store objects scheduled to be removed from the world
    private Array<GameObject> scheduledToRemove;
    //array to store objects that are scheduled to get their position changed
    //e.g. player during changing rooms.
    private Array<UpdateRequest> scheduledToUpdatePos;

    //reference to the player
    private Player player;

    //box2d worldupdater reference
    private WorldUpdater worldUpdater;

    //flag TODO make it neater
    private boolean removeFlag = false;

    public ObjectManager(WorldUpdater updater){
        gameObjects = new Array<>();
        scheduledToAdd = new Array<>();
        scheduledToRemove = new Array<>();
        scheduledToUpdatePos = new Array<>();
        this.worldUpdater = updater;
        player = new Player(this,3,3,32,48);
        gameObjects.add(player);
        player.addToWorld(worldUpdater.getWorld());
    }

    public void update(float delta){
        if(scheduledToAdd.size > 0){
            //first we check if any objects need to be added to the gameworld
            for(GameObject o : scheduledToAdd){
                o.addToWorld(worldUpdater.getWorld());
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
            for(UpdateRequest ur : scheduledToUpdatePos){
                if(gameObjects.contains(ur.requestedObject,true)){
                    System.out.println(ur.requestedPosition);
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

        if(removeFlag){
            Array<Body> bodies = new Array<>();
            worldUpdater.getWorld().getBodies(bodies);
            for(Body body : bodies){
                if(!(body.getUserData() instanceof  Player)) {
                    worldUpdater.getWorld().destroyBody(body);
                }
            }
            gameObjects.clear();
            gameObjects.add(player);
            removeFlag=false;
            MapObjectParser.addMapObjectsToWorld(worldUpdater.getWorld(),worldUpdater.getMapManager().getCurrentMap().getCurrentRoom());
        }
    }

    public void addObject(GameObject o){
        scheduledToAdd.add(o);
    }

    public void removeObject(GameObject o){
        scheduledToRemove.add(o);
    }

    public void removeAllGameObjects(boolean withPlayer){
        scheduledToRemove.addAll(gameObjects);
        if(!withPlayer) {
            scheduledToRemove.removeValue(player, true);
        }
    }

    public void onMapChange(){
        removeFlag = true;
    }

    public void requestPositionChange(GameObject o, Vector2 position){
        scheduledToUpdatePos.add(new UpdateRequest(o,position));
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

    private class UpdateRequest{
        private Vector2 requestedPosition;
        private GameObject requestedObject;

        public UpdateRequest(GameObject requestedObject , Vector2 requestedPosition){
            this.requestedObject = requestedObject;
            this.requestedPosition = requestedPosition;
        }
    }
}
