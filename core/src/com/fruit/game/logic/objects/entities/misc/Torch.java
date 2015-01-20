package com.fruit.game.logic.objects.entities.misc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.*;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.visual.renderer.ParticleRenderer;

/**
 * @Author FruitAddict
 */
public class Torch extends GameObject {

    private ObjectManager objectManager;
    public float stateTime;

    public Torch(ObjectManager objectManager, float spawnPointX, float spawnPointY){
        this.objectManager = objectManager;
        this.lastKnownX = spawnPointX;
        this.lastKnownY = spawnPointY;
        width = 32;
        height = 53;
        setEntityID(GameObject.TORCH);
        setSaveInRooms(true);
    }

    @Override
    public void update(float delta) {
        stateTime+=delta;
    }

    @Override
    public void addToBox2dWorld(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(lastKnownX,lastKnownY);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        //create the body
        body = world.createBody(bodyDef);
        //add userdata
        body.setUserData(this);

        CircleShape shape = new CircleShape();
        shape.setRadius(Math.min(width, height) / 2 / PIXELS_TO_UNITS);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 100f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CLUTTER_BIT;
        body.createFixture(fixtureDef);

        //dispose of shape
        shape.dispose();
        Controller.getWorldRenderer().getParticleRenderer().addParticleEffect(this, ParticleRenderer.TORCH);
        Controller.getWorldRenderer().getLightRenderer()
                .addFlickeringLight(new Color(255/255f, 147/255f, 41/255f,0.9f),1.5f,0.25f,getBodyPositionX(),getBodyPositionY());
        //Controller.getWorldRenderer().getLightRenderer().addPointLight(new Color(255/255f, 147/255f, 41/255f,0.9f),1.5f,getBodyPositionX(),getBodyPositionY(),true);
    }

    @Override
    public void killYourself(){
        Controller.getWorldRenderer().getLightRenderer().freeAttachedLight(this);
        objectManager.removeObject(this);
    }
}
