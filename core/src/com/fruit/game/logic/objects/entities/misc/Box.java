package com.fruit.game.logic.objects.entities.misc;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.entities.*;
import com.fruit.game.logic.objects.items.ItemManager;
import com.fruit.game.visual.messages.TextMessage;
import com.fruit.game.visual.messages.TextRenderer;
import com.fruit.game.logic.objects.entities.Character;

public class Box extends Enemy {
    private World world;
    private ObjectManager objectManager;

    public Box(ObjectManager objectManager,float spawnX, float spawnY) {
        lastKnownX = spawnX;
        lastKnownY= spawnY;
        this.objectManager = objectManager;
        setEntityID(GameObject.BOX);
        setSaveInRooms(DO_SAVE);
        stats.setHealthPoints(3);
        stats.setBaseMaximumHealthPoints(3);
    }

    @Override
    public void update(float delta) {
        if(stats.getHealthPoints()<0){
            killYourself();
        }
    }

    @Override
    public void addToBox2dWorld(World world) {
        this.world = world;
        //setting width and height
        width = 64;
        height = 64;

        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(lastKnownX,lastKnownY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 1.0f;
        bodyDef.fixedRotation = true;

        //create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        //Shape definiton
        CircleShape shape = new CircleShape();
        shape.setRadius(Math.min(getWidth(),getHeight())/PIXELS_TO_UNITS);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 99999f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = TREASURE_BIT;
        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
        dropAllLoot(objectManager);
    }

    @Override
    public void onDamageTaken(Value value) {
        stats.changeHealthPoints(-1);
        Controller.addOnScreenMessage(new TextMessage("-1", getBody().getPosition().x * PIXELS_TO_UNITS,
                getBody().getPosition().y * PIXELS_TO_UNITS, 1.5f, TextRenderer.redFont, TextMessage.UP));
    }

    @Override
    public void onHealingTaken(Value amount) {

    }

    @Override
    public void onDirectContact(Character player) {

    }

    @Override
    public void onContactWithTerrain(int direction) {

    }
}
