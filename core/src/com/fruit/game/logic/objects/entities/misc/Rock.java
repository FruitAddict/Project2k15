package com.fruit.game.logic.objects.entities.misc;

import com.badlogic.gdx.physics.box2d.*;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.utilities.Utils;

/**
 * @Author FruitAddict
 */
public class Rock extends GameObject {

    public static final int ROCK_1 = 0;
    public static final int ROCK_2 = 1;

    private ObjectManager objectManager;
    private int rockType;

    public Rock(ObjectManager objectManager, float spawnX, float spawnY){
        this.objectManager = objectManager;
        lastKnownX = spawnX;
        lastKnownY = spawnY;
        setEntityID(GameObject.ROCK);
        setSaveInRooms(true);
        width = 64;
        height = 64;
        setRockType(Utils.randomGenerator.nextInt(2));
    }
    @Override
    public void update(float delta) {

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


        //Shape definiton
        CircleShape shape = new CircleShape();
        shape.setRadius(Math.min(getWidth(),getHeight())/2/PIXELS_TO_UNITS);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 100f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CLUTTER_BIT;
        body.createFixture(fixtureDef);

        //dispose of shape
        shape.dispose();
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
    }

    public int getRockType() {
        return rockType;
    }

    public void setRockType(int rockType) {
        this.rockType = rockType;
    }
}
