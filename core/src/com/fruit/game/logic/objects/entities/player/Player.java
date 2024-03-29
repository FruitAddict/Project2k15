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
import com.fruit.game.logic.objects.effects.ondamaged.ReflectDamage;
import com.fruit.game.logic.objects.effects.passive.HealOverTime;
import com.fruit.game.logic.objects.entities.Enemy;
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
    private int level, experiencePoints, nextLevelExpRequirement, statPoints, expAccumulator, healingAccumulator;
    private float lastHealingUpdate, lastAttackUpdate, justHitTimer;

    //Players on hit effects, items can result in attacks slowing enemies down etc, it is passed to
    //every newly created projectile
    private Array<OnHitEffect> onHitEffects;
    //Player on damage taken effects, for example, items could result in reduced dmg taken or minions
    //spawning around the player on-hit
    private Array<OnDamageTakenEffect> onDamageTakenEffects;
    //projectile spawn point offset when colliding with walls
    private float xOffset, yOffset;

    /**
     * PLAYER-RELEATED STATS SECTION
     */
    //slain enemies int
    private float slainEnemies, projectileRadius, projectileVelocity, invicibilityPeriod;
    private Color projectileTint;

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
        stats.setHealthPoints(50);
        stats.setBaseMaximumHealthPoints(50);
        stats.setAttackSpeed(0.3f);
        stats.setAimSway(3);
        setProjectileRadius(6);
        setProjectileVelocity(6);
        setProjectileTint(new Color(1,1,1,1));
        setInvicibilityPeriod(0.5f);

        //initialize throwYourselfAtPlayer direction vector
        attackDirectionNormalized = new Vector2();
        //initialize onHit and onDamageTaken arrays
        onHitEffects = new Array<OnHitEffect>();
        onDamageTakenEffects = new Array<OnDamageTakenEffect>();

        //todo more
        nextLevelExpRequirement = 25;
        stats.setNumberOfProjectiles(1);
        stats.setKnockBack(0);

    }

    public void attack(float directionPercentX, float directionPercentY){
        //if player can throwYourselfAtPlayer again, set the vector to the values passed by ui controller
        if(stateTime - lastAttack > stats.getCombinedAttackSpeed()) {
            float sign = (float)Math.signum(Utils.randomGenerator.nextInt());
            float sway = Utils.randomGenerator.nextFloat()/stats.getAimSway()*sign;
            //normalize the throwYourselfAtPlayer direction vector using new values
            attackDirectionNormalized.set(0 - directionPercentX, 0 - directionPercentY);
            attackDirectionNormalized.add(sway, sway);
            attackDirectionNormalized.nor();
            attackDirectionNormalized.x*=-1;
            attackDirectionNormalized.y*=-1;
            switch(stats.getNumberOfProjectiles()){
                case 1: {
                    objectManager.addObject(new PlayerProjectile(this, objectManager, getBody().getPosition().x+xOffset, getBody().getPosition().y+yOffset,
                            attackDirectionNormalized.cpy()));
                    break;
                }
                case 2: {
                    objectManager.addObject(new PlayerProjectile(this, objectManager, getBody().getPosition().x+xOffset, getBody().getPosition().y+yOffset,
                            attackDirectionNormalized.rotate(-5)));
                    objectManager.addObject(new PlayerProjectile(this, objectManager, getBody().getPosition().x+xOffset, getBody().getPosition().y+yOffset,
                            attackDirectionNormalized.cpy().rotate(10)));
                    break;
                }
                case 3:{
                    objectManager.addObject(new PlayerProjectile(this, objectManager, getBody().getPosition().x+xOffset, getBody().getPosition().y+yOffset,
                            attackDirectionNormalized));
                    objectManager.addObject(new PlayerProjectile(this, objectManager, getBody().getPosition().x+xOffset, getBody().getPosition().y+yOffset,
                            attackDirectionNormalized.cpy().rotate(7)));
                    objectManager.addObject(new PlayerProjectile(this, objectManager, getBody().getPosition().x+xOffset, getBody().getPosition().y+yOffset,
                            attackDirectionNormalized.cpy().rotate(-7)));
                    break;
                }
            }
            lastAttack = stateTime;
        }
    }

    public void addOnDamageTakenEffect(OnDamageTakenEffect onDamageTakenEffect){
        for(OnDamageTakenEffect effect : onDamageTakenEffects){
            if(effect.getEffectID() == onDamageTakenEffect.getEffectID()){
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
    public void onDamageTaken(Character source, Value value){
        if(!status.isJustHit()) { //if invicibility period is off
            Value copied = value.cpy();
            status.setJustHit(true);
            for (OnDamageTakenEffect onDamageTakenEffect : onDamageTakenEffects) {
                onDamageTakenEffect.onDamageTaken(source, copied, value);
            }
            if (copied.getValue() > 0) {
                stats.changeHealthPoints(-copied.getValue() * stats.getCombinedResistance());
                super.onDamageTaken(source, copied);
                //change light distance
                Controller.getWorldRenderer().getLightRenderer().scalePlayerLight(stats.getHealthPointPercentOfMax());
            }

            Controller.getUserInterface().updateStatusBars(stats.getHealthPoints(), stats.getBaseMaximumHealthPoints(), experiencePoints, nextLevelExpRequirement, statPoints);
        }
    }

    @Override
    public void onHealingTaken(Value amount) {
        Value copied = amount.cpy();
        if(amount.getValue()!=0) {
            stats.changeHealthPoints(copied.getValue() * stats.getHealingModifier());
            healingAccumulator+= copied.getValue() * stats.getHealingModifier();
            stats.setHealthPointPercentOfMax(stats.getHealthPoints()/(float)stats.getBaseMaximumHealthPoints()); //todo in char stats
            //change light distance
            Controller.getWorldRenderer().getLightRenderer().scalePlayerLight(stats.getHealthPointPercentOfMax());
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
        super.killYourself();
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
        updateInfoBuffers(delta);

    }

    public void updateInfoBuffers(float delta){
        if( expAccumulator >0 && stateTime -lastAttackUpdate> 4f){
            Controller.getUserInterface().getMessageHandler().addMessage("+"+expAccumulator+" EXP",new Color(1,215/255f,0f,1f),2.5f);
            expAccumulator = 0;
            lastAttackUpdate = stateTime;
        }
        if(healingAccumulator> 0 && stateTime - lastHealingUpdate > 0.5f){
            Controller.addOnScreenMessage(this, Integer.toString(healingAccumulator) + "HP", getPosition(), getHeight(), 1.5f, TextRenderer.greenFont, TextMessage.DYNAMIC_UP);
            lastHealingUpdate = stateTime;
            healingAccumulator = 0;
        }
        if(status.isJustHit()) {
            justHitTimer += delta;
            if (justHitTimer > invicibilityPeriod) {
                status.setJustHit(false);
                justHitTimer = 0;
            }
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
        expAccumulator+=value;
        //Controller.addOnScreenMessage(value+" EXP", getBody().getPosition().x * PIXELS_TO_UNITS,
        //               getBody().getPosition().y * PIXELS_TO_UNITS, 2.0f, TextRenderer.goldenFont, TextMessage.FIXED_POINT_UPFALL);
        if(experiencePoints >= nextLevelExpRequirement){
            experiencePoints = experiencePoints-nextLevelExpRequirement;
            onLevelUp();
        }
        Controller.getUserInterface().updateStatusBars(stats.getHealthPoints(),stats.getBaseMaximumHealthPoints(),experiencePoints,nextLevelExpRequirement,statPoints);
    }

    public void onLevelUp(){
        Controller.getUserInterface().getMessageHandler().addMessage("Level up! +1 stat point",new Color(1,215/255f,0f,1f),2.5f);
        objectManager.getPlayer().addPassiveEffect(new HealOverTime(this,5,0.1f,new Value(stats.getBaseMaximumHealthPoints()/50,Value.HEALING)));
        nextLevelExpRequirement*=1.5f;
        status.setLeveledUp(true);
        level+=1;
        statPoints+=1;
        Controller.getUserInterface().updateStatusBars(stats.getHealthPoints(),stats.getBaseMaximumHealthPoints(),experiencePoints,nextLevelExpRequirement,statPoints);
        Controller.getUserInterface().updateIcon(statPoints);
    }

    public float getBodyWidth(){
        return width;
    }
    public float getBodyHeight(){
        //todo make it logic
        return height;
    }
    @Override
    public float getHeight(){
        return height+62;
        //real height of the player + head;
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

    public float getProjectileRadius() {
        return projectileRadius;
    }

    public void setProjectileRadius(float projectileRadius) {
        this.projectileRadius = projectileRadius;
    }

    public Color getProjectileTint() {
        return projectileTint;
    }

    public void setProjectileTint(Color projectileTint) {
        this.projectileTint = projectileTint;
    }

    public float getProjectileVelocity() {
        return projectileVelocity;
    }

    public void setProjectileVelocity(float projectileVelocity) {
        this.projectileVelocity = projectileVelocity;
    }

    public float getInvicibilityPeriod() {
        return invicibilityPeriod;
    }

    public void setInvicibilityPeriod(float invicibilityPeriod) {
        this.invicibilityPeriod = invicibilityPeriod;
    }
}
