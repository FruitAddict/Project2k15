package com.fruit.visual.renderer;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.fruit.logic.Constants;

import java.util.logging.Filter;

public class LightRenderer implements Constants {
    private WorldRenderer worldRenderer;
    private RayHandler rayHandler;
    private Array<PointLight> pointLights;

    public LightRenderer(WorldRenderer worldRenderer, World world){
        this.worldRenderer = worldRenderer;
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.1f);
        pointLights = new Array<PointLight>();
        addPointLight(32, new Color(0.6f,0.6f,0.6f,0.7f), 4, worldRenderer.getWorldUpdater().getPlayer().getBody());
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
        PointLight pointLight = new PointLight(rayHandler,rays,color,distance,0,0);
        com.badlogic.gdx.physics.box2d.Filter filter = new com.badlogic.gdx.physics.box2d.Filter();
        filter.maskBits = ENEMY_BIT | CLUTTER_BIT |ITEM_BIT | TERRAIN_BIT;
        pointLight.setContactFilter(filter);
        pointLight.setSoft(true);
        pointLight.attachToBody(body);
    }
}
