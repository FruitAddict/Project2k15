package com.fruit.tests;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.MovableGameObject;


public class Box extends MovableGameObject {
    private World world;
    private float spawnX;
    private float spawnY;
    private ObjectManager objectManager;

    public Box(ObjectManager objectManager,float spawnX, float spawnY) {
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.objectManager = objectManager;
    }

    @Override
    public void update(float delta) {
        //nothing
    }

    @Override
    public void addToWorld(World world) {
        this.world = world;
        //setting width and height
        width = 64;
        height = 64;

        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spawnX,spawnY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 1.0f;
        bodyDef.angularDamping =1.0f;

        //create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        //Shape definiton
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/PIXELS_TO_METERS/2,height/PIXELS_TO_METERS/2);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 50f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CLUTTER_BIT;
        body.createFixture(fixtureDef);

        //set other stuff
        setTypeID(CLUTTER_TYPE);
        setMaxVelocity(2);

        //dispose shape
        shape.dispose();
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
    }
}
