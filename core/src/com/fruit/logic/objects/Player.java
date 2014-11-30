package com.fruit.logic.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fruit.logic.Constants;
import com.fruit.logic.ObjectManager;

public class Player extends Character implements Constants {
    private World world;
    private float spawnCoordX;
    private float spawnCoordY;
    private ObjectManager objectManager;
    //attacking times
    private float lastAttack;
    private float stateTime;

    public Player(ObjectManager objectManager, float spawnCoordX, float spawnCoordY,float width, float height){
        this.width = width;
        this.height = height;
        this.spawnCoordY = spawnCoordY;
        this.spawnCoordX = spawnCoordX;
        this.objectManager = objectManager;
    }

    @Override
    public void addToWorld(World world){
        this.world = world;

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
        body.createFixture(fixtureDef);

        //set other stuff
        setTypeID(PLAYER_TYPE);
        setMaxVelocity(3);
        setSpeed(0.25f);

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
        if(stateTime - lastAttack > 0.25f) {
            objectManager.addObject(new Projectile(objectManager, getBody().getPosition().x, getBody().getPosition().y, direction));
            lastAttack = stateTime;
        }
    }

}
