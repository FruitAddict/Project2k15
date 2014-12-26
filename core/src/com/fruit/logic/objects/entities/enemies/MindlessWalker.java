package com.fruit.logic.objects.entities.enemies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fruit.Controller;
import com.fruit.logic.Constants;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.entities.Enemy;
import com.fruit.logic.objects.entities.GameObject;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.logic.objects.entities.projectiles.MobProjectile;
import com.fruit.utilities.Utils;
import com.fruit.visual.messages.TextMessage;
import com.fruit.visual.messages.TextRenderer;
import com.fruit.visual.renderer.SplatterRenderer;

import java.util.Random;

public class MindlessWalker extends Enemy implements Constants{
    private World world;
    private ObjectManager objectManager;
    private float timeSpentDoingShit, lastAttack = 0;
    private Random rng = new Random();
    private int random;
    private Vector2 attackDirectionNormalized;
    private Vector2 lastKnownPlayerPosition;
    boolean playerInSight=false;
    private float lastRayCastCheck = 0;
    public float stateTime;


    public MindlessWalker(ObjectManager objectManager, float spawnX, float spawnY){
        this.objectManager = objectManager;
        lastKnownX = spawnX;
        lastKnownY = spawnY;
        setEntityID(GameObject.MINDLESS_WALKER);
        stats.setMaxVelocity(1.5f);
        stats.setSpeed(0.2f);
        setSaveInRooms(DO_SAVE);
        stats.setHealthPoints(10);
        stats.setBaseMaximumHealthPoints(10);
        stats.setTimeBetweenAttacks(0.75f);
        stats.setTimeBetweenAttacksModifier(1f);
        stats.setBaseDamage(2);
        stats.setBaseDamageModifier(1);

        attackDirectionNormalized = new Vector2();
        lastKnownPlayerPosition = new Vector2();

        width = 32*2;
        height = 48*2;
    }

    @Override
    public void update(float delta) {
        stateTime += delta;
        if(stats.getHealthPoints() <= 0){
            killYourself();
        }else if(!status.isEnraged()) {
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
        }else if(stateTime - lastRayCastCheck >1) {
            lastRayCastCheck = stateTime;
            playerInSight = false;
            RayCastCallback rayCastCallback = new RayCastCallback() {

                @Override
                public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                    if(fixture.getFilterData().categoryBits==PLAYER_BIT) {
                        playerInSight = true;
                        return 0;
                    }else {
                        return 0;
                    }
                }
            };
            world.rayCast(rayCastCallback, body.getPosition(), objectManager.getPlayer().getBody().getPosition());
            if(playerInSight){
                lastKnownPlayerPosition.set(objectManager.getPlayer().getBody().getPosition());
            }
        }
        if(playerInSight){
            float distance = (float) (Math.sqrt(Math.pow(getBody().getPosition().x - lastKnownPlayerPosition.x, 2) + Math.pow(getBody().getPosition().y - lastKnownPlayerPosition.y, 2)));
            if (distance > 2f) {
                if (lastKnownPlayerPosition.x > getBody().getPosition().x) {
                    moveEast();
                } else if (lastKnownPlayerPosition.x < getBody().getPosition().x) {
                    moveWest();
                }
                if (lastKnownPlayerPosition.y > getBody().getPosition().y) {
                    moveNorth();
                } else if (lastKnownPlayerPosition.y < getBody().getPosition().y) {
                    moveSouth();
                }
            }
            if(distance < 3f){
                attack();
            }
        }
        updateFacing();
        updatePassiveEffects(delta);
    }

    public void attack(){
        if(stateTime - lastAttack > stats.getCombinedAttackSpeed()) {
            //normalize the attack direction vector using new values
            attackDirectionNormalized.set(getBody().getPosition().x - lastKnownPlayerPosition.x,
                    getBody().getPosition().y - lastKnownPlayerPosition.y);
            attackDirectionNormalized.nor();
            attackDirectionNormalized.x*=-1;
            attackDirectionNormalized.y*=-1;
            objectManager.addObject(new MobProjectile(objectManager, getBody().getPosition().x, getBody().getPosition().y, attackDirectionNormalized,5f,1));
            lastAttack = stateTime;
            }
    }

    @Override
    public void addToBox2dWorld(World world) {
        this.world = world;

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
        shape.setRadius(Math.min(width, height)/2 / PIXELS_TO_METERS);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 100f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ENEMY_BIT;
        fixtureDef.filter.maskBits = PLAYER_BIT |TERRAIN_BIT | CLUTTER_BIT | PLAYER_PROJECTILE_BIT | ITEM_BIT | TREASURE_BIT | PORTAL_BIT;
        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }
    @Override
    public void killYourself(){
        objectManager.removeObject(this);
        Controller.getWorldRenderer().getSplatterRenderer().addMultiSplatter(body.getPosition(),3,1);
        objectManager.getPlayer().addExperiencePoints(3);
    }

    @Override
    public void onDirectContact(Player player) {
        player.onDamageTaken(new Value(stats.getCombinedDamage(),Value.NORMAL_DAMAGE));
    }

    @Override
    public void onDamageTaken(Value value) {
        stats.changeHealthPoints(-value.getValue() * stats.getDamageResistanceModifier());
        if(value.getValue()!=0) {
            status.setEnraged(true);
            super.onDamageTaken(value);
        }
    }

    @Override
    public void onHealingTaken(Value value) {
        stats.changeHealthPoints(value.getValue());
        if(value.getValue()!=0) {
            super.onHealingTaken(value);
        }
    }

}
