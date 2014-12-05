package com.fruit.tests;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.Controller;
import com.fruit.logic.ObjectManager;

public class Dummy extends com.fruit.logic.objects.abstracted.Character {

    private ObjectManager objectManager;

    public Dummy(ObjectManager objectManager,float spawnX, float spawnY) {
        lastKnownX = spawnX;
        lastKnownY= spawnY;
        this.objectManager = objectManager;
        setTypeID(DUMMY_TYPE);
        setMaxVelocity(0);
        setGroupID(ENEMIES_GROUP);
        healthPoints = 99999;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void addToWorld(World world) {
        //setting width and height
        width = 64;
        height = 92;

        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(lastKnownX,lastKnownY);
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
        fixtureDef.density = 100000f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ENEMY_BIT;
        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
    }

    @Override
    public void changeHealthPoints(int amount){
        super.changeHealthPoints(amount);
        Controller.addOnScreenMessage(Integer.toString(amount), getBody().getPosition().x * PIXELS_TO_METERS,
                getBody().getPosition().y * PIXELS_TO_METERS, 1.5f);
    }
}
