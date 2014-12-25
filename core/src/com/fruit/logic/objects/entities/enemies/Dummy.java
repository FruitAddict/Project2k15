package com.fruit.logic.objects.entities.enemies;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.Controller;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.entities.Enemy;
import com.fruit.logic.objects.entities.GameObject;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.visual.messages.TextMessage;
import com.fruit.visual.messages.TextRenderer;

public class Dummy extends Enemy{

    private ObjectManager objectManager;

    public Dummy(ObjectManager objectManager,float spawnX, float spawnY) {
        lastKnownX = spawnX;
        lastKnownY= spawnY;
        this.objectManager = objectManager;
        setEntityID(GameObject.DUMMY);
        stats.setMaxVelocity(0);
        setSaveInRooms(DO_SAVE);
        stats.setHealthPoints(99999);
        stats.setBaseMaximumHealthPoints(1000000);
    }

    @Override
    public void update(float delta) {
        updatePassiveEffects(delta);
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
        CircleShape shape = new CircleShape();
        shape.setRadius(Math.min(width, height) / 2 / PIXELS_TO_METERS);

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
        stats.changeHealthPoints(-value.getValue()*stats.getDamageResistanceModifier());
        super.onDamageTaken(value);
    }

    @Override
    public void onHealingTaken(Value amount) {
        stats.changeHealthPoints(amount.getValue());
        super.onHealingTaken(amount);
    }
}
