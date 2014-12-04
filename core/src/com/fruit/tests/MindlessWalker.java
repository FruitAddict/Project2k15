package com.fruit.tests;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.logic.Constants;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.Character;

import java.util.Random;

public class MindlessWalker extends Character implements Constants{
    private World world;
    private ObjectManager objectManager;
    private float timeSpentDoingShit = 0;
    private Random rng = new Random();
    private float stateTime;
    private int random;
    private float spawnX;
    private float spawnY;
    private float healthPoints=5;

    public MindlessWalker(ObjectManager objectManager, float spawnX, float spawnY){
        this.objectManager = objectManager;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    @Override
    public void update(float delta) {
        if(healthPoints <= 0){
            killYourself();
        }
        updateFacing();
        if (timeSpentDoingShit == 0) {
            random = rng.nextInt(4);
            stateTime += delta;
            switch (random) {
                case 0: {
                    moveSouth();
                    break;
                }
                case 1: {
                    moveEast();
                    break;
                }
                case 2: {
                    moveNorth();
                    break;
                }
                case 3: {
                    moveWest();
                    break;
                }
            }
            timeSpentDoingShit += delta;
        } else if (timeSpentDoingShit > 0 && timeSpentDoingShit < 1) {
            stateTime += delta;
            switch (random) {
                case 0: {
                    if(body.getPosition().y >0)
                    moveSouth();
                    break;
                }
                case 1: {
                    if(body.getPosition().x < 2048/PIXELS_TO_METERS)
                    moveEast();
                    break;
                }
                case 2: {
                    if(body.getPosition().y <2048/PIXELS_TO_METERS)
                    moveNorth();
                    break;
                }
                case 3: {
                    if(body.getPosition().x > 0)
                    moveWest();
                    break;
                }

            }
            timeSpentDoingShit += delta;
        } else {
            timeSpentDoingShit = 0;
        }
    }

    @Override
    public void addToWorld(World world) {
        this.world = world;
        //setting width and height
        width = 32;
        height = 48;

        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spawnX,spawnY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = 2.0f;
        bodyDef.allowSleep = false;

        //create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        //Shape definiton
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/PIXELS_TO_METERS/2,height/PIXELS_TO_METERS/2);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 100f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ENEMY_BIT;
        body.createFixture(fixtureDef);

        //set other stuff
        setTypeID(WALKER_TYPE);
        setMaxVelocity(3);
        setSpeed(0.25f);

        //dispose shape
        shape.dispose();
    }
    @Override
    public void killYourself(){
        objectManager.removeObject(this);
    }

    public void damage(float damage){
        healthPoints+=damage;
    }
}