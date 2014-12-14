package com.fruit.logic.objects.entities.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.fruit.Controller;
import com.fruit.logic.Constants;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.effects.OnDamageTakenEffect;
import com.fruit.logic.objects.effects.OnHitEffect;
import com.fruit.logic.objects.entities.*;
import com.fruit.logic.objects.entities.misc.PlayerProjectile;
import com.fruit.visual.Assets;
import com.fruit.visual.messages.TextMessage;

public class Player extends com.fruit.logic.objects.entities.Character implements Constants {
    //coordinates at which the player is first spawned.
    private float spawnCoordX;
    private float spawnCoordY;
    //object manager reference.
    private ObjectManager objectManager;
    //objects life time is stored here, used with determining whether player can attack again
    private float stateTime;
    //variables to help with attacking delay.
    private float lastAttack;
    //attack direction vector, so no new vectors can be created constantly every frame
    private Vector2 attackDirectionNormalized;

    //Players on hit effects, items can result in attacks slowing enemies down etc, it is passed to
    //every newly created projectile
    private Array<OnHitEffect> onHitEffects;
    //Player on damage taken effects, for example, items could result in reduced dmg taken or minions
    //spawning around the player on-hit
    private Array<OnDamageTakenEffect> onDamageTakenEffects;

    public Player(ObjectManager objectManager, float spawnCoordX, float spawnCoordY,float width, float height){
        //constructor of every entity should take object manager, initial spawn coords and width and height of this entity
        this.width = width;
        this.height = height;
        this.spawnCoordY = spawnCoordY;
        this.spawnCoordX = spawnCoordX;
        this.objectManager = objectManager;

        //setting important stuff
        setEntityID(GameObject.PLAYER);
        setMaxVelocity(3);
        setSpeed(0.25f);
        setSaveInRooms(DONT_SAVE);
        setHealthPoints(3);
        setBaseMaximumHealthPoints(3);

        //initialize attack direction vector
        attackDirectionNormalized = new Vector2();
        //initialize onHit and onDamageTaken arrays
        onHitEffects = new Array<OnHitEffect>();
        onDamageTakenEffects = new Array<OnDamageTakenEffect>();
    }

    public void attack(float directionPercentX, float directionPercentY){
        //if player can attack again, set the vector to the values passed by ui controller
        if(stateTime - lastAttack > stats.getCombinedAttackSpeed()) {
            //normalize the attack direction vector using new values
            attackDirectionNormalized.set(0 - directionPercentX, 0 - directionPercentY);
            attackDirectionNormalized.nor();
            attackDirectionNormalized.x*=-1;
            attackDirectionNormalized.y*=-1;
            objectManager.addObject(new PlayerProjectile(this,objectManager, getBody().getPosition().x, getBody().getPosition().y, attackDirectionNormalized));
            lastAttack = stateTime;
        }
    }

    public void addOnDamageTakenEffect(OnDamageTakenEffect onDamageTakenEffect){
        //TODO LOGIC CHECKING FOR TYPES
        onDamageTakenEffects.add(onDamageTakenEffect);
    }

    public void removeOnDamageTakenEffect(OnDamageTakenEffect onDamageTakenEffect){
        if(onDamageTakenEffects.contains(onDamageTakenEffect,true)){
            onDamageTakenEffects.removeValue(onDamageTakenEffect,true);
        }
    }

    @Override
    public void onDamageTaken(Value value){
        for(OnDamageTakenEffect onDamageTakenEffect : onDamageTakenEffects){
            onDamageTakenEffect.onDamageTaken(value);
        }
        if(value.getValue()!=0) {
            changeHealthPoints(-value.getValue() * stats.getCombinedResistance());
            Controller.addOnScreenMessage(Float.toString(value.getValue()), getBody().getPosition().x * PIXELS_TO_METERS,
                    getBody().getPosition().y * PIXELS_TO_METERS, 1.5f);
        }
    }

    @Override
    public void onHealingTaken(Value amount) {
        if(amount.getValue()!=0) {
            changeHealthPoints(amount.getValue() * stats.getHealingModifier());
            Controller.addOnScreenMessage(new TextMessage(Float.toString(amount.getValue()), getBody().getPosition().x * PIXELS_TO_METERS,
                    getBody().getPosition().y * PIXELS_TO_METERS, 1.5f, Assets.greenFont));
        }
    }

    @Override
    public void addToBox2dWorld(World world){
        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spawnCoordX,spawnCoordY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = 3.0f;
        bodyDef.allowSleep = false;

        //create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        //Shape definiton
        PolygonShape shape = new PolygonShape();
        //setAsBox take's half of width and height
        shape.setAsBox(width/PIXELS_TO_METERS/2,height/PIXELS_TO_METERS/2);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1000f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = PLAYER_BIT;
        fixtureDef.filter.maskBits= PROJECTILE_BIT |CLUTTER_BIT | TERRAIN_BIT |ENEMY_BIT | PORTAL_BIT | ITEM_BIT |TREASURE_BIT;
        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
    }

    @Override
    public void update(float delta) {
        updatePassiveEffects(delta);
        updateFacing();
        stateTime+=delta;
    }

    public void addLinearVelocity(float velX,float velY){
        body.setLinearVelocity(body.getLinearVelocity().x + velX, body.getLinearVelocity().y);
        body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y+velY);
    }

    public ObjectManager getObjectManager(){
        return objectManager;
    }

    public Array<OnHitEffect> getOnHitEffects(){
        return onHitEffects;
    }



}
