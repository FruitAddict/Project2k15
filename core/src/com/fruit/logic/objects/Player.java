package com.fruit.logic.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fruit.logic.Constants;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.abstracted.Character;

public class Player extends Character implements Constants {
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

    public Player(ObjectManager objectManager, float spawnCoordX, float spawnCoordY,float width, float height){
        this.width = width;
        this.height = height;
        this.spawnCoordY = spawnCoordY;
        this.spawnCoordX = spawnCoordX;
        this.objectManager = objectManager;
        setTypeID(PLAYER_TYPE);
        setMaxVelocity(3);
        setSpeed(0.25f);
        setGroupID(NO_GROUP);
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
        fixtureDef.density = 100f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = PLAYER_BIT;
        fixtureDef.filter.maskBits= PROJECTILE_BIT |CLUTTER_BIT | TERRAIN_BIT |ENEMY_BIT | PORTAL_BIT;
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
        updateFacing();
        stateTime+=delta;
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
