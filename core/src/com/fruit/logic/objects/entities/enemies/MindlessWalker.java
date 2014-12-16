package com.fruit.logic.objects.entities.enemies;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.Controller;
import com.fruit.logic.Constants;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.entities.Enemy;
import com.fruit.logic.objects.entities.GameObject;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.logic.objects.items.HealthPotion;
import com.fruit.logic.objects.items.SphereOfProtection;
import com.fruit.utilities.Utils;
import com.fruit.visual.Assets;
import com.fruit.visual.messages.TextMessage;

import java.util.Random;

public class MindlessWalker extends Enemy implements Constants{
    private World world;
    private ObjectManager objectManager;
    private float timeSpentDoingShit = 0;
    private Random rng = new Random();
    private float stateTime;
    private int random;

    public MindlessWalker(ObjectManager objectManager, float spawnX, float spawnY){
        this.objectManager = objectManager;
        lastKnownX = spawnX;
        lastKnownY = spawnY;
        setEntityID(GameObject.MINDLESS_WALKER);
        stats.setMaxVelocity(1.5f);
        stats.setSpeed(0.2f);
        setSaveInRooms(DO_SAVE);
        stats.setHealthPoints(5);
        stats.setBaseMaximumHealthPoints(10);

        stats.setBaseDamage(1.5f);
        stats.setBaseDamageModifier(1f);
    }

    @Override
    public void update(float delta) {
        if(stats.getHealthPoints() <= 0){
            killYourself();
        }else {
            updateFacing();
            updatePassiveEffects(delta);
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
            } else {
                timeSpentDoingShit = 0;
            }
        }
    }

    @Override
    public void addToBox2dWorld(World world) {
        this.world = world;
        //setting width and height
        width = 32;
        height = 48;

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
        fixtureDef.density = 10f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ENEMY_BIT;
        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }
    @Override
    public void killYourself(){
        objectManager.removeObject(this);
    }

    @Override
    public void onDirectContact(Player player) {
        player.onDamageTaken(new Value(stats.getCombinedDamage()));
    }

    @Override
    public void onDamageTaken(Value value) {
        stats.changeHealthPoints(-value.getValue() * stats.getDamageResistanceModifier());
        if(value.getValue()!=0) {
            Controller.addOnScreenMessage(Float.toString(value.getValue()), getBody().getPosition().x * PIXELS_TO_METERS,
                    getBody().getPosition().y * PIXELS_TO_METERS, 1.5f);
        }
    }

    @Override
    public void onHealingTaken(Value amount) {
        stats.changeHealthPoints(amount.getValue());
        if(amount.getValue()!=0) {
            Controller.addOnScreenMessage(new TextMessage(Float.toString(amount.getValue()), getBody().getPosition().x * PIXELS_TO_METERS,
                    getBody().getPosition().y * PIXELS_TO_METERS, 3f, Assets.greenFont));
        }
    }
}
