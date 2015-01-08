package com.fruit.game.logic.objects.entities.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.fruit.game.Controller;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.effects.OnDamageTakenEffect;
import com.fruit.game.logic.objects.effects.OnHitEffect;
import com.fruit.game.logic.objects.effects.onhit.ForkOnHit;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.projectiles.PlayerProjectile;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.utilities.Utils;
import com.fruit.game.visual.messages.TextMessage;
import com.fruit.game.visual.messages.TextRenderer;

public class Player extends Character implements Constants {
    //coordinates at which the player is first spawned.
    private float spawnCoordX;
    private float spawnCoordY;
    //object manager reference.
    private ObjectManager objectManager;
    //objects life time is stored here, used with determining whether player can throwYourselfAtPlayer again
    public float stateTime;
    //variables to help with attacking delay.
    private float lastAttack;
    //throwYourselfAtPlayer direction vector, so no new vectors can be created constantly every frame
    private Vector2 attackDirectionNormalized;

    //player level start with 1
    private int level, experiencePoints, nextLevelExpRequirement, statPoints;

    //Players on hit effects, items can result in attacks slowing enemies down etc, it is passed to
    //every newly created projectile
    private Array<OnHitEffect> onHitEffects;
    //Player on damage taken effects, for example, items could result in reduced dmg taken or minions
    //spawning around the player on-hit
    private Array<OnDamageTakenEffect> onDamageTakenEffects;
    //projectile spawn point offset when colliding with walls
    private float xOffset, yOffset;

    //slain enemies int
    private float slainEnemies;

    public Player(ObjectManager objectManager, float spawnCoordX, float spawnCoordY){
        //constructor of every entity should take object manager, initial spawn coords and width and height of this entity
        width = 36;
        height = 25;
        this.spawnCoordY = spawnCoordY;
        this.spawnCoordX = spawnCoordX;
        this.objectManager = objectManager;

        //setting important stuff
        setEntityID(GameObject.PLAYER);
        stats.setMaxVelocity(4);
        stats.setSpeed(0.25f);
        setSaveInRooms(true);
        stats.setHealthPoints(100);
        stats.setBaseMaximumHealthPoints(100);
        stats.setAttackSpeed(0.3f);
        stats.setAimSway(3);

        //initialize throwYourselfAtPlayer direction vector
        attackDirectionNormalized = new Vector2();
        //initialize onHit and onDamageTaken arrays
        onHitEffects = new Array<OnHitEffect>();
        onDamageTakenEffects = new Array<OnDamageTakenEffect>();

        //todo more
        nextLevelExpRequirement = 25;
        stats.setNumberOfProjectiles(1);
        stats.setKnockBack(5);


    }

    public void attack(float directionPercentX, float directionPercentY){
        //if player can throwYourselfAtPlayer again, set the vector to the values passed by ui controller
        if(stateTime - lastAttack > stats.getCombinedAttackSpeed()) {
            float sign = (float)Math.signum(Utils.randomGenerator.nextInt());
            float sway = Utils.randomGenerator.nextFloat()/stats.getAimSway()*sign;
            float lastDegreeDifference = 7f;
            //normalize the throwYourselfAtPlayer direction vector using new values
            attackDirectionNormalized.set(0 - directionPercentX, 0 - directionPercentY);
            attackDirectionNormalized.add(sway, sway);
            attackDirectionNormalized.nor();
            attackDirectionNormalized.x*=-1;
            attackDirectionNormalized.y*=-1;
            switch(stats.getNumberOfProjectiles()){
                case 1: {
                    objectManager.addObject(new PlayerProjectile(this, objectManager, getBody().getPosition().x+xOffset, getBody().getPosition().y+yOffset,
                            attackDirectionNormalized.cpy(), 6f));
                    break;
                }
                case 2: {
                    objectManager.addObject(new PlayerProjectile(this, objectManager, getBody().getPosition().x+xOffset, getBody().getPosition().y+yOffset,
                            attackDirectionNormalized.rotate(-5), 6f));
                    objectManager.addObject(new PlayerProjectile(this, objectManager, getBody().getPosition().x+xOffset, getBody().getPosition().y+yOffset,
                            attackDirectionNormalized.cpy().rotate(10), 6f));
                    break;
                }
                case 3:{
                    objectManager.addObject(new PlayerProjectile(this, objectManager, getBody().getPosition().x+xOffset, getBody().getPosition().y+yOffset,
                            attackDirectionNormalized, 6f));
                    objectManager.addObject(new PlayerProjectile(this, objectManager, getBody().getPosition().x+xOffset, getBody().getPosition().y+yOffset,
                            attackDirectionNormalized.cpy().rotate(7), 6f));
                    objectManager.addObject(new PlayerProjectile(this, objectManager, getBody().getPosition().x+xOffset, getBody().getPosition().y+yOffset,
                            attackDirectionNormalized.cpy().rotate(-7), 6f));
                    break;
                }
            }
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
        Value copied = value.cpy();
        for(OnDamageTakenEffect onDamageTakenEffect : onDamageTakenEffects){
            onDamageTakenEffect.onDamageTaken(copied);
        }
        if(value.getValue()!=0) {
            stats.changeHealthPoints(-copied.getValue() * stats.getCombinedResistance());
            super.onDamageTaken(copied);
        }

        Controller.getUserInterface().updateStatusBars(stats.getHealthPoints(),stats.getBaseMaximumHealthPoints(),experiencePoints,nextLevelExpRequirement,statPoints);
    }

    @Override
    public void onHealingTaken(Value amount) {
        Value copied = amount.cpy();
        if(amount.getValue()!=0) {
            stats.changeHealthPoints(copied.getValue() * stats.getHealingModifier());
            super.onHealingTaken(copied);
        }

        Controller.getUserInterface().updateStatusBars(stats.getHealthPoints(),stats.getBaseMaximumHealthPoints(),experiencePoints,nextLevelExpRequirement,statPoints);
    }

    @Override
    public void onContactWithTerrain(int direction) {
        System.out.println("offseting " + direction);
        switch(direction){
            case NORTH_DIR:{
                yOffset = 24f/PIXELS_TO_UNITS;
                break;
            }
            case SOUTH_DIR:{
                yOffset = -24f/PIXELS_TO_UNITS;
                break;
            }
            case EAST_DIR:{
                xOffset = 24f/PIXELS_TO_UNITS;
                break;
            }
            case WEST_DIR:{
                xOffset = -24f/PIXELS_TO_UNITS;
                break;
            }
        }
    }

    public void onContactWithTerrainEnded(){
        xOffset = 0;
        yOffset = 0;
    }

    @Override
    public void addToBox2dWorld(World world){
        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spawnCoordX,spawnCoordY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = GLOBAL_MOVEMENT_DAMPING;
        bodyDef.allowSleep = false;

        //create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        //Shape definiton
        CircleShape shape = new CircleShape();
        shape.setRadius(Math.min(width, height) / 2 / PIXELS_TO_UNITS);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1000f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = PLAYER_BIT;
        fixtureDef.filter.maskBits= PLAYER_BIT | PROJECTILE_BIT |CLUTTER_BIT | TERRAIN_BIT |ENEMY_BIT | PORTAL_BIT | ITEM_BIT |TREASURE_BIT |DETECTOR_BIT | AREA_OF_EFFECT_BIT;
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
        if (stats.getHealthPoints() < 0) {
            onDeath();
        }
        stateTime += delta;
        if(steeringBehavior!=null){
            steeringBehavior.calculateSteering(steeringOutput);
            steeringOutput.scl(-1);
            applySteering(delta);
        }
    }

    private void onDeath() {
        Controller.onPlayerDeath(this);
    }

    public ObjectManager getObjectManager(){
        return objectManager;
    }

    public Array<OnHitEffect> getOnHitEffects(){
        return onHitEffects;
    }


    public int getExperiencePoints() {
        return experiencePoints;
    }

    public int getNextLevelExpRequirement() {
        return nextLevelExpRequirement;
    }

    public void addExperiencePoints(int value){
        experiencePoints+=value;
        Controller.addOnScreenMessage(new TextMessage(value+" exp", getBody().getPosition().x * PIXELS_TO_UNITS,
                getBody().getPosition().y * PIXELS_TO_UNITS, 1.5f, TextRenderer.goldenFont,TextMessage.UP_AND_FALL));
        if(experiencePoints >= nextLevelExpRequirement){
            experiencePoints = experiencePoints-nextLevelExpRequirement;
            onLevelUp();
        }
        Controller.getUserInterface().updateStatusBars(stats.getHealthPoints(),stats.getBaseMaximumHealthPoints(),experiencePoints,nextLevelExpRequirement,statPoints);
    }

    public void onLevelUp(){
        Controller.getUserInterface().getMessageHandler().addMessage("Level up! +1 stat point",new Color(1,215/255f,0f,1f),2.5f);
        objectManager.getPlayer().onHealingTaken(new Value(15,Value.HEALING));
        nextLevelExpRequirement*=1.5f;
        status.setLeveledUp(true);
        level+=1;
        statPoints+=1;
        Controller.getUserInterface().updateStatusBars(stats.getHealthPoints(),stats.getBaseMaximumHealthPoints(),experiencePoints,nextLevelExpRequirement,statPoints);
        Controller.getUserInterface().updateIcon(statPoints);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStatPoints() {
        return statPoints;
    }

    public void setStatPoints(int value){
        this.statPoints = value;
    }

    public float getSlainEnemies() {
        return slainEnemies;
    }

    public void addSlainEnemy(){
        slainEnemies++;
    }
}
