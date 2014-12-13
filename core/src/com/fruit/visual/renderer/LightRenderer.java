package com.fruit.visual.renderer;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.fruit.logic.Constants;


public class LightRenderer implements Constants {
    private WorldRenderer worldRenderer;
    private RayHandler rayHandler;
    private Array<PointLight> pointLights;
    private PointLight pointLight;

    public LightRenderer(WorldRenderer worldRenderer, World world){
        this.worldRenderer = worldRenderer;
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.30f);
        pointLights = new Array<PointLight>();
        addPointLight(32, new Color(0f,0f,0.3f,0.7f), 4, worldRenderer.getWorldUpdater().getPlayer().getBody());
    }

    public void render(){
        rayHandler.setCombinedMatrix(worldRenderer.getCamera().combined.scl(PIXELS_TO_METERS));
        rayHandler.updateAndRender();
    }

    public void addPointLight(PointLight p){
        pointLights.add(p);
    }

    public void addPointLight(int rays, Color color,float distance, float x, float y){
        PointLight pointLight = new PointLight(rayHandler,rays,color,distance,x,y);
    }

    public void addPointLight(int rays, Color color, float distance, Body body){
        pointLight = new PointLight(rayHandler,rays,color,distance,0,0);
        Filter filter = new Filter();
        filter.maskBits = ENEMY_BIT | CLUTTER_BIT |ITEM_BIT | TERRAIN_BIT;
        pointLight.setContactFilter(filter);
        pointLight.setSoft(false);
        pointLight.attachToBody(body);
    }

    //TODO REMOVE
    public void changePlayerLightColor(Color c){
        pointLight.setColor(c);
    }

    public RayHandler getRayHandler(){
        return rayHandler;
    }
}
