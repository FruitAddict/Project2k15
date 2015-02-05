package com.fruit.game.logic.objects.entities.enemies;

import com.badlogic.gdx.ai.steer.behaviors.Flee;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.limiters.LinearAccelerationLimiter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.entities.*;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.utilities.Utils;
import com.fruit.game.visual.renderer.ParticleRenderer;

/**
 * @Author FruitAddict
 */
public class Zombie extends Enemy {
    public static final int ZOMBIE_1 = 1;
    public static final int ZOMBIE_2 = 2;
    public static final int ZOMBIE_3 = 3;
    public static final int ZOMBIE_4 = 4;
    public static final int ZOMBIE_5 = 5;
    public static final int ZOMBIE_6 = 6;
    public static final int ZOMBIE_7 = 7;
    public static final int ZOMBIE_8 = 8;

    private ObjectManager objectManager;
    private int zombieType;
    public float stateTime;
    private boolean followingPlayer = false;

    public Zombie(ObjectManager objectManager, float spawnX, float spawnY){
        this.objectManager = objectManager;
        lastKnownX = spawnX;
        lastKnownY = spawnY;
        setEntityID(GameObject.ZOMBIE);
        setSaveInRooms(true);
        setZombieType(1+Utils.randomGenerator.nextInt(8));
        changeSteeringBehavior(new Wander<Vector2>(this) //
                .setFaceEnabled(false)
                .setWanderOffset(20) //
                .setWanderOrientation(10) //
                .setWanderRadius(5) //
                .setWanderRate(MathUtils.PI / 3));
        stats.setBaseMaximumHealthPoints(25);
        stats.setHealthPoints(25);
        stats.setMaxVelocity(2f);
        stats.setSpeed(0.01f);
        stats.setBaseDamage(4);
        stats.setAttackSpeed(0.75f);
        stats.setAttackSpeedModifier(1f);
        stats.setHealthPointPercentOfMax(1);
        float offsetWidth = Math.signum(Utils.randomGenerator.nextInt()) * Utils.randomGenerator.nextFloat()*6.4f;
        float offsetHeight = Math.signum(Utils.randomGenerator.nextInt()) * Utils.randomGenerator.nextFloat()*9.6f;
        width = 32+offsetWidth;
        height = 48+offsetHeight;
    }

    @Override
    public void onDirectContact(com.fruit.game.logic.objects.entities.Character character) {
        if(character.getEntityID() == GameObject.PLAYER){
            status.setAttackedByPlayer(true);
            if(!followingPlayer) {
                changeSteeringBehavior(new Seek<Vector2>(this, objectManager.getPlayer()));
                followingPlayer = true;
            }
            character.onDamageTaken(this,new Value(stats.getCombinedDamage(),Value.NORMAL_DAMAGE));
        }
    }

    @Override
    public void onContactWithTerrain(int direction) {

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(stats.getHealthPoints() <= 0) {
            killYourself();
        }
        updateFacing();
        updatePassiveEffects(delta);
        stateTime+=delta;
        steeringBehavior.calculateSteering(steeringOutput);
        applySteering(delta);
    }

    @Override
    public void addToBox2dWorld(World world) {
        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(lastKnownX,lastKnownY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = GLOBAL_MOVEMENT_DAMPING;
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

    public int getZombieType() {
        return zombieType;
    }

    public void setZombieType(int zombieType) {
        this.zombieType = zombieType;
    }

    @Override
    public void killYourself(){
        super.killYourself();
        Controller.getWorldRenderer().getSplatterRenderer().addMultiBloodSprite(body.getPosition(), 3, 0);
        Controller.getWorldUpdater().getPlayer().addSlainEnemy();
        Controller.getWorldRenderer().getParticleRenderer().addParticleEffect(this, ParticleRenderer.BLOOD);
        dropAllLoot(objectManager);
        objectManager.getPlayer().addExperiencePoints(5);
        objectManager.removeObject(this);
    }

    @Override
    public void onDamageTaken(Character source, Value value) {
        stats.changeHealthPoints(-value.getValue() * stats.getDamageResistanceModifier());
        if(value.getValue()!=0) {
            status.setAttackedByPlayer(true);
            if(!followingPlayer) {
                changeSteeringBehavior(new Seek<Vector2>(this, objectManager.getPlayer()));
                followingPlayer = true;
            }
            super.onDamageTaken(source,value);
        }
    }
}
