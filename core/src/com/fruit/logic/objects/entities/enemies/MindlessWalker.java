package com.fruit.logic.objects.entities.enemies;

import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Flee;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fruit.Controller;
import com.fruit.logic.Constants;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.entities.Enemy;
import com.fruit.logic.objects.entities.GameObject;
import com.fruit.logic.objects.entities.Character;
import com.fruit.logic.objects.entities.projectiles.MobProjectile;
import com.fruit.logic.objects.items.HealthPotion;
import com.fruit.utilities.Utils;

import java.util.Random;

public class MindlessWalker extends Enemy implements Constants{
    private World world;
    private ObjectManager objectManager;
    private float timeSpentDoingShit, lastAttack = 0;
    private Random rng = new Random();
    private int random, generationNumber;
    private Vector2 attackDirectionNormalized;
    private Vector2 lastKnownPlayerPosition;
    boolean playerInSight=false;
    private float lastRayCastCheck = 0;
    public float stateTime;
    private boolean followingPlayer = false;
    private boolean fleeing = false;
    //test

    private SteeringBehavior<Vector2> steeringBehavior;


    public MindlessWalker(ObjectManager objectManager, float spawnX, float spawnY, int generationNumber){
        this.objectManager = objectManager;
        lastKnownX = spawnX;
        lastKnownY = spawnY;
        setEntityID(GameObject.MINDLESS_WALKER);
        stats.setMaxVelocity(2f/generationNumber);
        stats.setSpeed(3f);
        setSaveInRooms(DO_SAVE);
        stats.setHealthPoints(20/generationNumber);
        stats.setBaseMaximumHealthPoints(20/generationNumber);
        stats.setAttackSpeed(0.75f);
        stats.setAttackSpeedModifier(1f);
        stats.setBaseDamage(2);
        stats.setBaseDamageModifier(1);

        attackDirectionNormalized = new Vector2();
        lastKnownPlayerPosition = new Vector2();

        width = 32*2/generationNumber;
        height = 48*2/generationNumber;


        steeringBehavior = new Wander<Vector2>(this).setWanderRadius(4).setWanderOrientation(15f);
        this.generationNumber = generationNumber;
    }
    @Override
    public void update(float delta){
        if(stats.getHealthPoints() <= 0) {
            killYourself();
        }
        updateFacing();
        updatePassiveEffects(delta);
        stateTime+=delta;
        steeringBehavior.calculateSteering(steeringOutput);
        applySteering(delta);
    }
    /*
    public void update(float delta, String deprecated) {
        stateTime += delta;
        if(stats.getHealthPoints() <= 0){
            killYourself();
        }else if(!status.isAttackedByPlayer()) {
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
    */

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
        bodyDef.linearDamping = 3.0f;
        bodyDef.allowSleep = false;

        //create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        //Shape definiton
        CircleShape shape = new CircleShape();
        shape.setRadius(Math.min(width, height)/2 / PIXELS_TO_UNITS);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 100f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ENEMY_BIT;
        fixtureDef.filter.maskBits = AREA_OF_EFFECT_BIT | ENEMY_BIT |PLAYER_BIT |TERRAIN_BIT | CLUTTER_BIT | PLAYER_PROJECTILE_BIT | ITEM_BIT | TREASURE_BIT | PORTAL_BIT;
        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }
    @Override
    public void killYourself(){
        objectManager.removeObject(this);
        Controller.getWorldRenderer().getSplatterRenderer().addMultiBloodSprite(body.getPosition(), 3, 0);
        if( Utils.randomGenerator.nextInt(100)>75 && generationNumber == 3){
            objectManager.addObject(new HealthPotion(objectManager,body.getPosition().x,body.getPosition().y,32,32,5f,0.5f,2));
        }
        objectManager.getPlayer().addExperiencePoints(3);
        if(generationNumber<3){
            objectManager.addObject(new MindlessWalker(objectManager,body.getPosition().x+0.1f,body.getPosition().y,generationNumber+1));
            objectManager.addObject(new MindlessWalker(objectManager,body.getPosition().x,body.getPosition().y,generationNumber+1));
        }
    }

    @Override
    public void onDirectContact(Character character) {
        character.onDamageTaken(new Value(stats.getCombinedDamage(), Value.NORMAL_DAMAGE));
    }

    @Override
    public void onContactWithTerrain(int direction) {
        switch(direction){
            case NORTH_DIR:{
                break;
            }
            case SOUTH_DIR:{
                break;
            }
            case EAST_DIR:{
                break;
            }
            case WEST_DIR:{

            }
        }
    }

    @Override
    public void onDamageTaken(Value value) {
        stats.changeHealthPoints(-value.getValue() * stats.getDamageResistanceModifier());
        if(value.getValue()!=0) {
            if(generationNumber==1) {
                status.setAttackedByPlayer(true);
            }
            if(!followingPlayer) {
                changeSteeringBehavior(new Seek<Vector2>(this, objectManager.getPlayer()));
                followingPlayer = true;
            }
            super.onDamageTaken(value);
        }
        if(stats.getHealthPoints()> 0 && stats.getHealthPoints() < stats.getBaseMaximumHealthPoints()/5 && !fleeing){
            changeSteeringBehavior(new Flee<Vector2>(this,objectManager.getPlayer()));
            fleeing = true;
        }
        if(generationNumber>1 && !fleeing){
            changeSteeringBehavior(new Flee<Vector2>(this,objectManager.getPlayer()));
            status.setAttackedByPlayer(true);
            fleeing=true;
        }

    }

    @Override
    public void onHealingTaken(Value value) {
        stats.changeHealthPoints(value.getValue());
        if(value.getValue()!=0) {
            super.onHealingTaken(value);
        }
    }

    public void changeSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior){
        this.steeringBehavior = steeringBehavior;
    }
}
