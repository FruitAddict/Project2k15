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
import com.fruit.logic.objects.entities.Character;
import com.fruit.logic.objects.entities.misc.Explosion;
import com.fruit.logic.objects.items.HealthPotion;
import com.fruit.utilities.Utils;


/**
 * @Author FruitAddict
 */
public class TheEye extends Enemy {

    private World world;
    private ObjectManager objectManager;
    public float stateTime;
    private float directionX;
    private float directionY;
    private boolean playerFound = false;


    public TheEye(ObjectManager objectManager, float spawnX, float spawnY){
        this.objectManager = objectManager;
        lastKnownX = spawnX;
        lastKnownY = spawnY;
        setEntityID(GameObject.THE_EYE);
        stats.setMaxVelocity(2.2f);
        stats.setSpeed(0.25f);
        setSaveInRooms(DO_SAVE);
        stats.setHealthPoints(10);
        stats.setBaseMaximumHealthPoints(10);
        stats.setAttackSpeed(0.75f);
        stats.setAttackSpeedModifier(1f);
        stats.setBaseDamage(2);
        stats.setBaseDamageModifier(1);
        width = 32;
        height = 48;
        directionX = Math.signum((Utils.randomGenerator.nextInt()));
        directionY = Math.signum((Utils.randomGenerator.nextInt()));
    }

    @Override
    public void onDirectContact(Character character) {
        killYourself();
        objectManager.addObject(new Explosion(objectManager,body.getPosition().x,body.getPosition().y,1.5f,1f,stats.getCombinedDamage()*2));
    }

    @Override
    public void onContactWithTerrain(int direction) {
        if(!playerFound) {
            switch (direction) {
                case NORTH_DIR: {
                    directionY *= -1;
                    break;
                }
                case SOUTH_DIR: {
                    directionY *= -1;
                    break;
                }
                case EAST_DIR: {
                    directionX *= -1;
                    break;
                }
                case WEST_DIR: {
                    directionX *= -1;
                    break;
                }
            }
        }
    }

    @Override
    public void update(float delta) {
        stateTime+=delta;
        if(stats.getHealthPoints() <= 0) {
            killYourself();
        }
        updateFacing();
        updatePassiveEffects(delta);
        if(!playerFound) {
            if (directionX == 1) {
                moveEast();
            } else {
                moveWest();
            }
            if (directionY == 1) {
                moveNorth();
            } else {
                moveSouth();
            }
        }else {
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
        shape.setRadius(Math.min(width, height)/2 / PIXELS_TO_UNITS);
        CircleShape detectorShape = new CircleShape();
        detectorShape.setRadius(Math.min(width,height)/PIXELS_TO_UNITS*2);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 100f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ENEMY_BIT;
        fixtureDef.filter.maskBits = AREA_OF_EFFECT_BIT | PLAYER_BIT |TERRAIN_BIT | CLUTTER_BIT | PLAYER_PROJECTILE_BIT | ITEM_BIT | TREASURE_BIT | PORTAL_BIT | ENEMY_BIT;

        FixtureDef detectorDef = new FixtureDef();
        detectorDef.filter.categoryBits = DETECTOR_BIT;
        detectorDef.filter.maskBits = PLAYER_BIT;
        detectorDef.shape = detectorShape;
        detectorDef.isSensor = true;

        body.createFixture(fixtureDef);
        body.createFixture(detectorDef);

        //dispose shape
        shape.dispose();
    }

    @Override
    public void onDamageTaken(Value value) {
        stats.changeHealthPoints(-value.getValue() * stats.getDamageResistanceModifier());
        if(value.getValue()!=0) {
            status.setAttackedByPlayer(true);
            onPlayerDetected();
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

    @Override
    public void killYourself() {
        if(!playerFound) {
            objectManager.removeObject(this);
        }else {
            objectManager.removeObject(this);
            objectManager.addObject(new Explosion(objectManager,body.getPosition().x,body.getPosition().y,1.5f,1f,stats.getCombinedDamage()));

        }
        Controller.getWorldRenderer().getSplatterRenderer().addMultiBloodSprite(body.getPosition(),0.7f, 1, 0);
        if( Utils.randomGenerator.nextInt(100)>75){
            objectManager.addObject(new HealthPotion(objectManager,body.getPosition().x,body.getPosition().y,32,32,5f,0.5f,2));
        }
        objectManager.getPlayer().addExperiencePoints(4);
    }

    public void onPlayerDetected(){
        playerFound = true;
    }
}
