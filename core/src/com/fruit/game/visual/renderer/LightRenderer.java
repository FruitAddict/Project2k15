package com.fruit.game.visual.renderer;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.fruit.game.Configuration;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;
import com.fruit.game.visual.PoolablePointLight;
import com.fruit.game.visual.tween.PoolablePointLightAccessor;
import com.fruit.game.visual.tween.TweenUtils;

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
        rayHandler.setAmbientLight(0.1f);
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

        //register tween accessor
        Tween.registerAccessor(PoolablePointLight.class,new PoolablePointLightAccessor());
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

    public void addFlickeringLight(Color color, float length, float flickerOffset, float posX, float posY){
        PoolablePointLight p = lightPool.obtain();
        p.setActive(true);
        p.setColor(color);
        p.setDistance(length);
        p.setPosition(posX, posY);
        p.setStaticLight(true);
        Filter filter = new Filter();
        filter.maskBits = ENEMY_BIT | CLUTTER_BIT |ITEM_BIT | TERRAIN_BIT | TREASURE_BIT | PORTAL_BIT;
        p.setContactFilter(filter);
        p.setXray(true);
        Timeline.createSequence()
                .push(Tween.set(p,PoolablePointLightAccessor.DISTANCE).target(length-flickerOffset))
                .push(Tween.to(p, PoolablePointLightAccessor.DISTANCE,0.75f).target(length))
                .repeatYoyo(Tween.INFINITY,0f)
                .start(TweenUtils.tweenManager);
        activeLights.add(p);
    }

    public void attachPointLightToBody(GameObject owner,Color color, float length){
        PoolablePointLight p = lightPool.obtain();
        p.setColor(color);
        p.setActive(true);
        p.setDistance(length);
        p.attachToBody(owner.getBody());
        p.setXray(true);
        p.setParentObject(owner);
        Filter filter = new Filter();
        filter.maskBits = ENEMY_BIT | CLUTTER_BIT |ITEM_BIT | TERRAIN_BIT | TREASURE_BIT | PORTAL_BIT;
        p.setContactFilter(filter);
        activeLights.add(p);
    }

    public void freeAttachedLight(GameObject owner){
        for(PoolablePointLight p : activeLights){
            if(p.getParentObject() == owner){
                lightPool.free(p);
                TweenUtils.tweenManager.killTarget(p);
                activeLights.removeValue(p, true);
            }
        }
    }

    public void setPlayerLight(Player player){
        playerLight.attachToBody(player.getBody());
        playerLight.setDistance(4.8f);
        playerLight.setColor(new Color(0.2f,0.2f,0.1f,0.8f));
        playerLight.setXray(true);
        Filter filter = new Filter();
        filter.maskBits = ENEMY_BIT | CLUTTER_BIT |ITEM_BIT | TERRAIN_BIT | TREASURE_BIT | PORTAL_BIT;
        playerLight.setContactFilter(filter);
    }

    public void updatePlayerLightLength(float length){
        playerLight.setDistance(length);
    }

    public RayHandler getRayHandler(){
        return rayHandler;
    }
}
