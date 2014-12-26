package com.fruit.logic.objects.entities.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.fruit.Controller;
import com.fruit.logic.Constants;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.effects.OnDamageTakenEffect;
import com.fruit.logic.objects.effects.OnHitEffect;
import com.fruit.logic.objects.entities.GameObject;
import com.fruit.logic.objects.entities.projectiles.PlayerProjectile;
import com.fruit.utilities.Utils;
import com.fruit.visual.messages.TextMessage;
import com.fruit.visual.messages.TextRenderer;

public class Player extends com.fruit.logic.objects.entities.Character implements Constants {
    //coordinates at which the player is first spawned.
    private float spawnCoordX;
    private float spawnCoordY;
    //object manager reference.
    private ObjectManager objectManager;
    //objects life time is stored here, used with determining whether player can attack again
    public float stateTime;
    //variables to help with attacking delay.
    private float lastAttack;
    //attack direction vector, so no new vectors can be created constantly every frame
    private Vector2 attackDirectionNormalized;

    //player level start with 1
    private int level = 1;
    //player's experiance points
    private int experiencePoints;
    //next level exp req
    private int nextLevelExpRequirement;

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
        stats.setMaxVelocity(3);
        stats.setSpeed(0.25f);
        setSaveInRooms(DONT_SAVE);
        stats.setHealthPoints(100);
        stats.setBaseMaximumHealthPoints(100);
        stats.setTimeBetweenAttacks(0.25f);
        stats.setAimSway(10);

        //initialize attack direction vector
        attackDirectionNormalized = new Vector2();
        //initialize onHit and onDamageTaken arrays
        onHitEffects = new Array<OnHitEffect>();
        onDamageTakenEffects = new Array<OnDamageTakenEffect>();

        //todo more
        nextLevelExpRequirement = 15;
    }

    public void attack(float directionPercentX, float directionPercentY){
        //if player can attack again, set the vector to the values passed by ui controller
        if(stateTime - lastAttack > stats.getCombinedAttackSpeed()) {
            float sign = (float)Math.signum(Utils.randomGenerator.nextInt());
            float sway = Utils.randomGenerator.nextFloat()/stats.getAimSway()*sign;
            //normalize the attack direction vector using new values
            attackDirectionNormalized.set(0 - directionPercentX, 0 - directionPercentY);
            attackDirectionNormalized.add(sway, sway);
            attackDirectionNormalized.nor();
            attackDirectionNormalized.x*=-1;
            attackDirectionNormalized.y*=-1;
            objectManager.addObject(new PlayerProjectile(this,objectManager, getBody().getPosition().x, getBody().getPosition().y, attackDirectionNormalized,6f));
            lastAttack = stateTime;
        }
    }

    public void addOnDamageTakenEffect(OnDamageTakenEffect onDamageTakenEffect){
        for(OnDamageTakenEffect effect : onDamageTakenEffects){
            if(onDamageTakenEffect.getEffectID() == onDamageTakenEffect.getEffectID()){
                effect.join(onDamageTakenEffect);
                return;
            }
        }
        onDamageTakenEffects.add(onDamageTakenEffect);
    }

    public void removeOnDamageTakenEffect(OnDamageTakenEffect onDamageTakenEffect){
        if(onDamageTakenEffects.contains(onDamageTakenEffect,true)){
            onDamageTakenEffects.removeValue(onDamageTakenEffect,true);
        }
    }

    public void addOnHitEffect(OnHitEffect onHitEffect){
        for(OnHitEffect effect : onHitEffects){
            if(onHitEffect.getEffectID() == effect.getEffectID()){
                effect.join(onHitEffect);
                return;
            }
        }
        onHitEffects.add(onHitEffect);
    }

    public void removeOnHitEffect(OnHitEffect onHitEffect){
        if(onHitEffects.contains(onHitEffect,true)){
            onHitEffects.removeValue(onHitEffect,true);
        }
    }

    @Override
    public void onDamageTaken(Value value){
        for(OnDamageTakenEffect onDamageTakenEffect : onDamageTakenEffects){
            onDamageTakenEffect.onDamageTaken(value);
        }
        if(value.getValue()!=0) {
            stats.changeHealthPoints(-value.getValue() * stats.getCombinedResistance());
            super.onDamageTaken(value);
        }

        Controller.getUserInterface().updateStatusBars(stats.getHealthPoints(),stats.getBaseMaximumHealthPoints(),experiencePoints,nextLevelExpRequirement);
    }

    @Override
    public void onHealingTaken(Value amount) {
        if(amount.getValue()!=0) {
            stats.changeHealthPoints(amount.getValue() * stats.getHealingModifier());
            super.onHealingTaken(amount);
        }

        Controller.getUserInterface().updateStatusBars(stats.getHealthPoints(),stats.getBaseMaximumHealthPoints(),experiencePoints,nextLevelExpRequirement);
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
        CircleShape shape = new CircleShape();
        shape.setRadius(Math.min(width, height) / 2 / PIXELS_TO_METERS);

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


    public float getExperiencePoints() {
        return experiencePoints;
    }

    public int getNextLevelExpRequirement() {
        return nextLevelExpRequirement;
    }

    public void addExperiencePoints(int value){
        experiencePoints+=value;
        Controller.addOnScreenMessage(new TextMessage(value+" exp", getBody().getPosition().x * PIXELS_TO_METERS,
                getBody().getPosition().y * PIXELS_TO_METERS, 1.5f, TextRenderer.goldenFont,TextMessage.UP_AND_FALL));
        if(experiencePoints >= nextLevelExpRequirement){
            experiencePoints = experiencePoints-nextLevelExpRequirement;
            onLevelUp();
        }
        Controller.getUserInterface().updateStatusBars(stats.getHealthPoints(),stats.getBaseMaximumHealthPoints(),experiencePoints,nextLevelExpRequirement);
    }

    public void onLevelUp(){
        objectManager.getPlayer().onHealingTaken(new Value(10,Value.HEALING));
        nextLevelExpRequirement*=1.5f;
        status.setLeveledUp(true);
    }
}
