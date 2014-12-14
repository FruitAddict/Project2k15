package com.fruit.logic.objects.entities.enemies;

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
import com.fruit.visual.Assets;
import com.fruit.visual.messages.TextMessage;

public class Dummy extends Enemy{

    private ObjectManager objectManager;

    public Dummy(ObjectManager objectManager,float spawnX, float spawnY) {
        lastKnownX = spawnX;
        lastKnownY= spawnY;
        this.objectManager = objectManager;
        setEntityID(GameObject.DUMMY);
        setMaxVelocity(0);
        setSaveInRooms(DO_SAVE);
        healthPoints = 99999;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void addToBox2dWorld(World world) {
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
    public void onDirectContact(Player player) {
    }

    @Override
    public void onDamageTaken(Value value) {
        changeHealthPoints(-value.getValue()*damageResistanceModifier);
        Controller.addOnScreenMessage(Float.toString(value.getValue()), getBody().getPosition().x * PIXELS_TO_METERS,
                getBody().getPosition().y * PIXELS_TO_METERS, 1.5f);
    }

    @Override
    public void onHealingTaken(Value amount) {
        changeHealthPoints(amount.getValue());
        Controller.addOnScreenMessage(new TextMessage(Float.toString(amount.getValue()), getBody().getPosition().x * PIXELS_TO_METERS,
                getBody().getPosition().y * PIXELS_TO_METERS, 3f, Assets.greenFont));
    }
}
