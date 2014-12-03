package com.fruit.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.maps.MapManager;
import com.fruit.visual.WorldRenderer;

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
        world = new World(new Vector2(0,0), true);
        world.setContactListener(new WorldContactListener(this));
        mapManager = new MapManager(this, true);
        objectManager = new ObjectManager(this);
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

}
