package com.fruit.logic.objects.entities.enemies;

import com.badlogic.gdx.math.Vector2;
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
import com.fruit.logic.objects.entities.projectiles.MobProjectile;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.visual.Assets;
import com.fruit.visual.messages.TextMessage;
import com.fruit.visual.messages.TextRenderer;

import java.util.Random;

public class MindlessWalker extends Enemy implements Constants{
    private World world;
    private ObjectManager objectManager;
    private float timeSpentDoingShit, stateTime, lastAttack = 0;
    private Random rng = new Random();
    private int random;
    private boolean angered=false;
    private Vector2 attackDirectionNormalized;

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
        stats.setTimeBetweenAttacks(0.75f);
        stats.setTimeBetweenAttacksModifier(1f);
        stats.setBaseDamage(1.5f);
        stats.setBaseDamageModifier(1f);

        attackDirectionNormalized = new Vector2();
    }

    @Override
    public void update(float delta) {
        stateTime += delta;
        if(stats.getHealthPoints() <= 0){
            killYourself();
        }else if(!angered) {
            if (timeSpentDoingShit == 0) {
                random = rng.nextInt(4);
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
        }else {
            //TODO abstract out the AI and make it better but get done with the map generator first .
            float distance = (float) (Math.sqrt(Math.pow(getBody().getPosition().x - objectManager.getPlayer().getBody().getPosition().x, 2) + Math.pow(getBody().getPosition().y - objectManager.getPlayer().getBody().getPosition().y, 2)));
            if (distance > 2.5) {
                if (objectManager.getPlayer().getBody().getPosition().x > getBody().getPosition().x) {
                    moveEast();
                } else if (objectManager.getPlayer().getBody().getPosition().x < getBody().getPosition().x) {
                    moveWest();
                }
                if (objectManager.getPlayer().getBody().getPosition().y > getBody().getPosition().y) {
                    moveNorth();
                } else if (objectManager.getPlayer().getBody().getPosition().y < getBody().getPosition().y) {
                    moveSouth();
                }
            }
            if(distance < 4.5){
                attack();
            }
        }
        updateFacing();
        updatePassiveEffects(delta);
    }

    public void attack(){
        if(stateTime - lastAttack > stats.getCombinedAttackSpeed()) {
            //normalize the attack direction vector using new values
            attackDirectionNormalized.set(getBody().getPosition().x - objectManager.getPlayer().getBody().getPosition().x,
                    getBody().getPosition().y - objectManager.getPlayer().getBody().getPosition().y);
            attackDirectionNormalized.nor();
            attackDirectionNormalized.x*=-1;
            attackDirectionNormalized.y*=-1;
            objectManager.addObject(new MobProjectile(objectManager, getBody().getPosition().x, getBody().getPosition().y, attackDirectionNormalized,5f));
            lastAttack = stateTime;
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
            angered = true;
            Controller.addOnScreenMessage(new TextMessage(Float.toString(value.getValue()), getBody().getPosition().x * PIXELS_TO_METERS,
                    getBody().getPosition().y * PIXELS_TO_METERS, 1.5f, TextRenderer.redFont,TextMessage.UP));
        }
    }

    @Override
    public void onHealingTaken(Value amount) {
        stats.changeHealthPoints(amount.getValue());
        if(amount.getValue()!=0) {
            Controller.addOnScreenMessage(new TextMessage(Float.toString(amount.getValue()), getBody().getPosition().x * PIXELS_TO_METERS,
                    getBody().getPosition().y * PIXELS_TO_METERS, 1.5f, TextRenderer.greenFont,TextMessage.UP));
        }
    }
}
