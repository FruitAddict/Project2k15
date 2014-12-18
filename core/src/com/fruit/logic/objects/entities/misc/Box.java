package com.fruit.logic.objects.entities.misc;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.Controller;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.entities.Enemy;
import com.fruit.logic.objects.entities.GameObject;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.logic.objects.items.ItemManager;
import com.fruit.visual.messages.TextMessage;
import com.fruit.visual.messages.TextRenderer;

public class Box extends Enemy{
    private World world;
    private ObjectManager objectManager;

    public Box(ObjectManager objectManager,float spawnX, float spawnY) {
        lastKnownX = spawnX;
        lastKnownY= spawnY;
        this.objectManager = objectManager;
        setEntityID(GameObject.BOX);
        setSaveInRooms(DO_SAVE);
        stats.setHealthPoints(5);
        stats.setBaseMaximumHealthPoints(5);
        System.out.println(stats.getHealthPoints());
    }

    @Override
    public void update(float delta) {
        //TODO MAKE SOME SORT OF DROP RNG
        if(stats.getHealthPoints()<0){
            killYourself();
            ItemManager.addRandomItem(objectManager,getBody().getPosition().x,getBody().getPosition().y,1);
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
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/PIXELS_TO_METERS/2,height/PIXELS_TO_METERS/2);

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
    }

    @Override
    public void onDamageTaken(Value value) {
        stats.changeHealthPoints(-1);
        Controller.addOnScreenMessage(new TextMessage("Booo!", getBody().getPosition().x * PIXELS_TO_METERS,
                getBody().getPosition().y * PIXELS_TO_METERS, 1.5f, TextRenderer.redFont,TextMessage.UP));
        System.out.println(stats.getHealthPoints());
    }

    @Override
    public void onHealingTaken(Value amount) {

    }

    @Override
    public void onDirectContact(Player player) {

    }
}
