package com.fruit.logic;

import com.badlogic.gdx.utils.Array;
import com.fruit.logic.objects.GameObject;
import com.fruit.logic.objects.Player;

public class ObjectManager {
    //array to store game objects
    private Array<GameObject> gameObjects;
    //array to store objects scheduled to be added to the world
    private Array<GameObject> scheduledToAdd;
    //array to store objects scheduled to be removed from the world
    private Array<GameObject> scheduledToRemove;

    //reference to the player
    private Player player;

    //box2d worldupdater reference
    private WorldUpdater worldUpdater;

    public ObjectManager(WorldUpdater updater){
        gameObjects = new Array<GameObject>();
        scheduledToAdd = new Array<GameObject>();
        scheduledToRemove = new Array<GameObject>();
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
        if(gameObjects.size>0){
            //finnaly we update all objects using their update methods
            //that can contain code for AI etc.
            for(GameObject o: gameObjects){
                o.update(delta);
            }
        }
    }

    public void addObject(GameObject o){
        scheduledToAdd.add(o);
    }

    public void removeObject(GameObject o){
        scheduledToRemove.add(o);
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

}
