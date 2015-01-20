package com.fruit.game.logic.objects.entities.bosses;

import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.MatchVelocity;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.effects.OnHitEffect;
import com.fruit.game.logic.objects.entities.*;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.enemies.Slime;
import com.fruit.game.logic.objects.entities.projectiles.MobProjectile;
import com.fruit.game.logic.objects.entities.projectiles.MobProjectileWithEffect;

/**
 * @Author FruitAddict
 */
public class EnormousGlutton extends Enemy {

    private World world;
    private ObjectManager objectManager;
    public float stateTime;
    private SteeringBehavior<Vector2> steeringBehavior;
    private float lastVomit;
    private float lastThrowMobs;
    private Vector2 attackDirectionNormalized;
    private boolean enragedBehavior = false;
    private boolean barAdded = false;

    public EnormousGlutton(ObjectManager objectManager, float spawnX, float spawnY){
        this.objectManager = objectManager;
        lastKnownX = spawnX;
        lastKnownY = spawnY;
        width = 128;
        height = 128;
        setEntityID(GameObject.ENORMOUS_GLUTTON);
        setSaveInRooms(true);
        stats.setHealthPoints(1000);
        stats.setBaseMaximumHealthPoints(1000);
        stats.setMaxVelocity(1);
        stats.setSpeed(1);
        stats.setAttackSpeedModifier(1);
        stats.setAttackSpeed(2f);
        stats.setBaseDamage(5);
        stats.setKnockBack(10);
        steeringBehavior = new Wander<>(this).setWanderRadius(4).setWanderOrientation(15f);
        attackDirectionNormalized = new Vector2();
    }

    @Override
    public void onDirectContact(com.fruit.game.logic.objects.entities.Character character) {
        character.onDamageTaken(this, new Value(stats.getCombinedDamage(),Value.NORMAL_DAMAGE));
    }

    @Override
    public void onContactWithTerrain(int direction) {

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(!barAdded){
            Controller.getUserInterface().addBossHealthBar("Enormous Glutton");
            barAdded=true;
        }
        if(stats.getHealthPoints()<0){
            killYourself();
            Controller.getUserInterface().removeBossBar();
        }
        Controller.getUserInterface().updateBossBar(stats.getHealthPoints(),stats.getBaseMaximumHealthPoints(),stats.getHealthPointPercentOfMax());
        updatePassiveEffects(delta);
        updateFacing();
        stateTime+=delta;
        steeringBehavior.calculateSteering(steeringOutput);
        steeringOutput.linear.scl(-1);
        applySteering(delta);

        if(status.isAttackedByPlayer()){
            vomitProjectiles();
        }
        if(stats.getHealthPointPercentOfMax()<75){
            throwMobs();
        }
    }

    public void throwMobs(){
        if(stateTime - lastThrowMobs > 5) {
            //normalize the throwYourselfAtPlayer direction vector using new values
            attackDirectionNormalized.set(getBody().getPosition().x - Controller.getWorldUpdater().getPlayer().getPosition().x,
                    getBody().getPosition().y - Controller.getWorldUpdater().getPlayer().getPosition().y);
            attackDirectionNormalized.nor();
            attackDirectionNormalized.x*=-1;
            attackDirectionNormalized.y*=-1;
            OnHitEffect spawnShit = new OnHitEffect() {
                @Override
                public void onHit(Projectile projectile1, Character enemy, Value damage) {
                    objectManager.addObject(new Slime(objectManager,enemy.getPosition().x,enemy.getPosition().y,3));
                }

                @Override
                public void join(OnHitEffect onHitEffect) {

                }
            };
            MobProjectileWithEffect proj = new MobProjectileWithEffect(this,objectManager, getBody().getPosition().x, getBody().getPosition().y, attackDirectionNormalized, 9f, stats.getCombinedDamage(),spawnShit);
            MobProjectileWithEffect proj2 = new MobProjectileWithEffect(this,objectManager, getBody().getPosition().x, getBody().getPosition().y, attackDirectionNormalized.cpy().rotate(-7),9f, stats.getCombinedDamage(),spawnShit);
            MobProjectileWithEffect proj3 = new MobProjectileWithEffect(this,objectManager, getBody().getPosition().x, getBody().getPosition().y, attackDirectionNormalized.cpy().rotate(7), 9f, stats.getCombinedDamage(),spawnShit);

            proj.setTypeID(Projectile.MINDLESS_PROJECTILE);
            proj2.setTypeID(Projectile.MINDLESS_PROJECTILE);
            proj3.setTypeID(Projectile.MINDLESS_PROJECTILE);
            objectManager.addObject(proj);
            objectManager.addObject(proj2);
            objectManager.addObject(proj3);
            lastThrowMobs = stateTime;
        }
    }

    public void vomitProjectiles(){
        if(stateTime - lastVomit > stats.getCombinedAttackSpeed()) {
            //normalize the throwYourselfAtPlayer direction vector using new values
            attackDirectionNormalized.set(getBody().getPosition().x - Controller.getWorldUpdater().getPlayer().getPosition().x,
                    getBody().getPosition().y - Controller.getWorldUpdater().getPlayer().getPosition().y);
            attackDirectionNormalized.nor();
            attackDirectionNormalized.x*=-1;
            attackDirectionNormalized.y*=-1;
            MobProjectile proj = new MobProjectile(this,objectManager, getBody().getPosition().x, getBody().getPosition().y, attackDirectionNormalized, 7f, stats.getCombinedDamage(),stats.getKnockBack());
            MobProjectile proj2 = new MobProjectile(this,objectManager, getBody().getPosition().x, getBody().getPosition().y, attackDirectionNormalized.cpy().rotate(-7), 5f, stats.getCombinedDamage(),stats.getKnockBack());
            MobProjectile proj3 = new MobProjectile(this,objectManager, getBody().getPosition().x, getBody().getPosition().y, attackDirectionNormalized.cpy().rotate(7), 5f, stats.getCombinedDamage(),stats.getKnockBack());
            MobProjectile proj4 = new MobProjectile(this,objectManager, getBody().getPosition().x, getBody().getPosition().y, attackDirectionNormalized.cpy().rotate(-14), 7f, stats.getCombinedDamage(),stats.getKnockBack());
            MobProjectile proj5 = new MobProjectile(this,objectManager, getBody().getPosition().x, getBody().getPosition().y, attackDirectionNormalized.cpy().rotate(14), 7f, stats.getCombinedDamage(),stats.getKnockBack());

            objectManager.addObject(proj);
            objectManager.addObject(proj2);
            objectManager.addObject(proj3);
            objectManager.addObject(proj4);
            objectManager.addObject(proj5);
            lastVomit = stateTime;
        }
    }

    @Override
    public void addToBox2dWorld(World world) {
        this.world = world;
        barAdded = false;
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

        Controller.getWorldRenderer().getLightRenderer().attachPointLightToBody(this, com.badlogic.gdx.graphics.Color.PURPLE,5f);
    }
    @Override
    public void onDamageTaken(Character source, Value value) {
        stats.changeHealthPoints(-value.getValue() * stats.getDamageResistanceModifier());
        if(value.getValue()!=0) {
            status.setAttackedByPlayer(true);
            super.onDamageTaken(source,value);
            if(!enragedBehavior){
                changeSteeringBehavior(new MatchVelocity<Vector2>(this,Controller.getWorldUpdater().getPlayer()));
                enragedBehavior = true;
            }
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


    @Override
    public void killYourself() {
        super.killYourself();
        objectManager.removeObject(this);
        Controller.getWorldUpdater().getPlayer().addExperiencePoints(250);
        Controller.getWorldRenderer().getSplatterRenderer().addMultiBloodSprite(getPosition(),10,2);
        Controller.getWorldRenderer().getLightRenderer().freeAttachedLight(this);
    }
}
