package com.fruit.logic.objects.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fruit.Controller;
import com.fruit.logic.Constants;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.entities.*;
import com.fruit.visual.Assets;
import com.fruit.visual.messages.TextMessage;

public class Player extends com.fruit.logic.objects.entities.Character implements Constants {
    //coordinates at which the player is first spawned.
    private float spawnCoordX;
    private float spawnCoordY;
    //object manager reference. Every object must have one as
    private ObjectManager objectManager;
    //objects life time is stored here
    private float stateTime;
    //variables to help with attacking delay.
    private float lastAttack;
    private float timeBetweenAttacks=0.25f;
    //types

    public Player(ObjectManager objectManager, float spawnCoordX, float spawnCoordY,float width, float height){
        //constructor of every entity should take object manager, initial spawn coords and width and height of this entity
        this.width = width;
        this.height = height;
        this.spawnCoordY = spawnCoordY;
        this.spawnCoordX = spawnCoordX;
        this.objectManager = objectManager;
        setEntityID(GameObject.PLAYER);
        setMaxVelocity(3);
        setSpeed(0.25f);
        setSaveInRooms(DONT_SAVE);
        setHealthPoints(5);
        setBaseMaximumHealthPoints(8);
    }

    @Override
    public void addToWorld(World world){
        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spawnCoordX,spawnCoordY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = 3.0f;
        bodyDef.allowSleep = false;

        //create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        //Shape definiton
        PolygonShape shape = new PolygonShape();
        //setAsBox take's half of width and height
        shape.setAsBox(width/PIXELS_TO_METERS/2,height/PIXELS_TO_METERS/2);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1000f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = PLAYER_BIT;
        fixtureDef.filter.maskBits= PROJECTILE_BIT |CLUTTER_BIT | TERRAIN_BIT |ENEMY_BIT | PORTAL_BIT | ITEM_BIT;
        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
    }

    @Override
    public void update(float delta) {
        updateEffects(delta);
        updateFacing();
        stateTime+=delta;

    }

    @Override
    public void changeHealthPoints(float amount){
        super.changeHealthPoints(amount);
        if(amount<0) {
            Controller.addOnScreenMessage(Float.toString(amount), getBody().getPosition().x * PIXELS_TO_METERS,
                    getBody().getPosition().y * PIXELS_TO_METERS, 1.5f);
        }else {
            Controller.addOnScreenMessage(new TextMessage(Float.toString(amount), getBody().getPosition().x * PIXELS_TO_METERS,
                    getBody().getPosition().y * PIXELS_TO_METERS, 3f, Assets.greenFont));
        }
    }

    public void attack(Vector2 direction){
        if(stateTime - lastAttack > timeBetweenAttacks) {
            objectManager.addObject(new Projectile(objectManager, getBody().getPosition().x, getBody().getPosition().y, direction));
            lastAttack = stateTime;
        }
    }

    public void addLinearVelocity(float velX,float velY){
        body.setLinearVelocity(body.getLinearVelocity().x + velX, body.getLinearVelocity().y);
        body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y+velY);
    }

    public float getTimeBetweenAttacks() {
        return timeBetweenAttacks;
    }

    public void setTimeBetweenAttacks(float timeBetweenAttacks) {
        this.timeBetweenAttacks = timeBetweenAttacks;
    }

    public ObjectManager getObjectManager(){
        return objectManager;
    }



}
