package com.fruit.visual.renderer;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.fruit.Configuration;
import com.fruit.logic.Constants;
import com.fruit.visual.PoolablePointLight;

/**
 * @author FruitAddict
 * Light renderer class, works together with world renderer. Can be used to add lights to the world.
 */
public class LightRenderer implements Constants {
    private WorldRenderer worldRenderer;
    private RayHandler rayHandler;
    private PointLight playerLight;
    private Pool<PoolablePointLight> lightPool;
    private Array<PoolablePointLight> activeLights;

    public LightRenderer(WorldRenderer worldRenderer, World world){
        this.worldRenderer = worldRenderer;
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.05f);
        rayHandler.setCulling(true);

        if(!Configuration.shadowsEnabled){
            rayHandler.setShadows(false);
        }

        //create point light pool with the max size of 10
        lightPool = new Pool<PoolablePointLight>(1,10) {
            @Override
            protected PoolablePointLight newObject() {
                return new PoolablePointLight(rayHandler,12);
            }
        };

        //create array of active lights.
        activeLights = new Array<PoolablePointLight>();

        //point light reserved for player.
        playerLight = new PointLight(rayHandler,12);
    }

    public void render(){
        rayHandler.setCombinedMatrix(worldRenderer.getCamera().combined.scl(PIXELS_TO_UNITS));
        rayHandler.updateAndRender();
    }

    public void freeAllLights(){
        lightPool.freeAll(activeLights);
        activeLights.clear();
    }

    public void addPointLight(Color color, float length, float posX, float posY, boolean isStatic){
        PoolablePointLight p = lightPool.obtain();
        p.setActive(true);
        p.setColor(color);
        p.setDistance(length);
        p.setPosition(posX, posY);
        p.setStaticLight(isStatic);
        Filter filter = new Filter();
        filter.maskBits = ENEMY_BIT | CLUTTER_BIT |ITEM_BIT | TERRAIN_BIT | TREASURE_BIT | PORTAL_BIT;
        p.setContactFilter(filter);
        p.setXray(true);
        activeLights.add(p);
    }

    public void attachPointLightToBody(Body body,Color color, float length){
        PoolablePointLight p = lightPool.obtain();
        p.setColor(color);
        p.setActive(true);
        p.setDistance(length);
        p.attachToBody(body);
        p.setXray(true);
        Filter filter = new Filter();
        filter.maskBits = ENEMY_BIT | CLUTTER_BIT |ITEM_BIT | TERRAIN_BIT | TREASURE_BIT | PORTAL_BIT;
        p.setContactFilter(filter);
        activeLights.add(p);
    }

    public void setPlayerLight(Body body){
        playerLight.attachToBody(body);
        playerLight.setDistance(4.5f);
        playerLight.setColor(new Color(0f,0f,0f,1f));
        playerLight.setXray(true);
        Filter filter = new Filter();
        filter.maskBits = ENEMY_BIT | CLUTTER_BIT |ITEM_BIT | TERRAIN_BIT | TREASURE_BIT | PORTAL_BIT;
        playerLight.setContactFilter(filter);
    }

    public RayHandler getRayHandler(){
        return rayHandler;
    }
}
