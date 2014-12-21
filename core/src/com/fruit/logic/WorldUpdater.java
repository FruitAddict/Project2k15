package com.fruit.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.Controller;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.maps.MapManager;
import com.fruit.utilities.MapObjectParser;

public class WorldUpdater {
    //Box2D world
    private World world;
    //Object manager
    private ObjectManager objectManager;
    //stores game time - for how long this particular session was running for
    private float stateTime;
    //Map manager
    private MapManager mapManager;

    public WorldUpdater(){
        //world init. with no gravity (0,0) and sleeping enabled
        Controller.registerWorldUpdater(this);
        world = new World(new Vector2(0,0), true);
        world.setContactListener(new WorldContactListener(this));
        objectManager = new ObjectManager(this);
        mapManager = new MapManager(this, 1337); //seeds TODO seeds should be added in menu
        //add any objects that are in the current room to the game world.
        //MapObjectParser.addMapObjectsToWorld(this, mapManager.getCurrentMap().getCurrentRoom());
    }

    public void update(float delta){
        //increase statetime
        stateTime += delta;
        //update object manager by delta
        objectManager.update(delta);
        //update world by delta TODO fixed timestep with interpolation
        world.step(delta, 6, 2);
    }

    public World getWorld(){
        return world;
    }

    public ObjectManager getObjectManager(){
        return objectManager;
    }

    public MapManager getMapManager(){
        return mapManager;
    }

    public Player getPlayer(){
        System.out.println("called");
        if(objectManager!=null){
            System.out.println("returning player");
            return objectManager.getPlayer();
        } else {
            System.out.println("not returning player(object manager is null)");
            return null;
        }
    }

}
