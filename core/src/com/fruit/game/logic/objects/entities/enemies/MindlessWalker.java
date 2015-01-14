package com.fruit.game.logic.objects.entities.enemies;

import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Flee;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.effects.OnHitEffect;
import com.fruit.game.logic.objects.entities.Enemy;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.Projectile;
import com.fruit.game.logic.objects.entities.projectiles.MobProjectileWithEffect;

import java.util.Random;

public class MindlessWalker extends Enemy implements Constants {
    private World world;
    private ObjectManager objectManager;
    private float  lastAttack = 0;
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
    //to pass to anonymous class
    private MindlessWalker reference;


    public MindlessWalker(ObjectManager objectManager, float spawnX, float spawnY, int generationNumber){
        this.objectManager = objectManager;
        this.reference = this;
        lastKnownX = spawnX;
        lastKnownY = spawnY;
        setEntityID(GameObject.MINDLESS_WALKER);
        stats.setMaxVelocity(2f/generationNumber);
        stats.setSpeed(3f);
        setSaveInRooms(true);
        stats.setHealthPoints(20/generationNumber);
        stats.setBaseMaximumHealthPoints(20/generationNumber);
        stats.setAttackSpeed(0.75f);
        stats.setAttackSpeedModifier(1f);
        stats.setBaseDamage(2);
        stats.setBaseDamageModifier(1);

        attackDirectionNormalized = new Vector2();
        lastKnownPlayerPosition = new Vector2();

        width = 32*1.5f/generationNumber;
        height = 48*1.5f/generationNumber;


        steeringBehavior = new Wander<Vector2>(this).setWanderOffset(50).setWanderRadius(5).setFaceEnabled(false)
        .setWanderRate(0.5f);
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

    public void throwYourselfAtPlayer(){
        if(stateTime - lastAttack > stats.getCombinedAttackSpeed()) {
            //normalize the throwYourselfAtPlayer direction vector using new values
            attackDirectionNormalized.set(getBody().getPosition().x - lastKnownPlayerPosition.x,
                    getBody().getPosition().y - lastKnownPlayerPosition.y);
            attackDirectionNormalized.nor();
            attackDirectionNormalized.x*=-1;
            attackDirectionNormalized.y*=-1;
            MobProjectileWithEffect proj = new MobProjectileWithEffect(objectManager, getBody().getPosition().x, getBody().getPosition().y, attackDirectionNormalized, 5f, 1, new OnHitEffect() {
                @Override
                public void onHit(Projectile proj, Character enemy, Value damage) {
                    objectManager.addObject(new MindlessWalker(objectManager,enemy.getPosition().x,enemy.getPosition().y,3));
                    reference.stats.setHealthPoints(reference.stats.getBaseMaximumHealthPoints());
                }

                @Override
                public void join(OnHitEffect onHitEffect) {
                    //
                }
            });
            proj.setWidth(32);
            proj.setHeight(32);
            proj.setTypeID(Projectile.MINDLESS_PROJECTILE);
            objectManager.addObject(proj);
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
    @Override
    public void killYourself(){
        super.killYourself();
        Controller.getWorldRenderer().getSplatterRenderer().addMultiBloodSprite(body.getPosition(), 3, 0);
        Controller.getWorldUpdater().getPlayer().addSlainEnemy();
        dropAllLoot(objectManager);
        objectManager.getPlayer().addExperiencePoints(3);
        if(generationNumber<3){
            objectManager.removeObject(this);
            objectManager.addObject(new MindlessWalker(objectManager,body.getPosition().x+0.1f,body.getPosition().y,generationNumber+1));
            objectManager.addObject(new MindlessWalker(objectManager,body.getPosition().x,body.getPosition().y,generationNumber+1));
        }else {
            lastKnownPlayerPosition = Controller.getWorldUpdater().getPlayer().getPosition();
            objectManager.removeObject(this);
            throwYourselfAtPlayer();
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
            status.setAttackedByPlayer(true);
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
