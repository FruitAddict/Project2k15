package com.fruit.logic;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.managers.Assets;
import com.fruit.utilities.MapObjectParser;

public class WorldUpdater {
    //Box2D world
    private World world;
    //Object manager
    private ObjectManager objectManager;
    //current tiled map TODO map manager again
    private TiledMap currentTiledMap;


    public WorldUpdater(){
        //world init. with no gravity (0,0) and sleeping enabled
        world = new World(new Vector2(0,0), true);
        world.setContactListener(new WorldContactListener());
        setCurrentTiledMap((TiledMap) Assets.getAsset("64map.tmx", TiledMap.class));
        MapObjectParser.addMapObjects(world,currentTiledMap);
        objectManager = new ObjectManager(this);
    }

    public void update(float delta){
        //update object manager by delta
        objectManager.update(delta);
        //update world by delta TODO fixed timestep with interpolation
        world.step(delta, 6, 2);
    }

    public World getWorld(){
        return world;
    }


    public TiledMap getCurrentTiledMap() {
        return currentTiledMap;
    }

    public void setCurrentTiledMap(TiledMap currentTiledMap) {
        this.currentTiledMap = currentTiledMap;
    }

    public ObjectManager getObjectManager(){
        return objectManager;
    }
}
