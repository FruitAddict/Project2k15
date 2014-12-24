package com.fruit.logic.objects.entities;


/**
 * @Author FruitAddict
 * Container for statuses, stats and everything related. Made to keep the player class relatively clean.
 */
public class CharacterStats {

    //Player stats - must contain base value and modifier

    protected float healthPoints;
    protected float baseMaximumHealthPoints;
    protected float maximumHealthPointsMultiplier = 1;

    private float timeBetweenAttacks=1f;
    private float timeBetweenAttacksModifier = 1f;

    private float baseDamage = 1f;
    private float baseDamageModifier =1f;

    private float damageResistanceModifier = 1f;

    private float healingModifier = 1f;

    private float speed, maxVelocity;

    private float criticalHitBase, criticalHitModifier = 1;

    private boolean piercingProjectiles = false;

    public float getCombinedDamage(){
        return baseDamage * baseDamageModifier;
    }

    public float getCombinedAttackSpeed(){
        return timeBetweenAttacks*timeBetweenAttacksModifier;
    }

    public float getCombinedResistance(){
        return damageResistanceModifier;
    }

    public float getCombinedCriticalHitChance(){
        return criticalHitBase*criticalHitModifier;
    }

    public void changeHealthPoints(float amount){
        //every character must be damageable or healable.
        if(healthPoints+amount < baseMaximumHealthPoints) {
            healthPoints += amount;
        }else if(healthPoints + amount < 0 ){
            healthPoints=0;
        } else{
            healthPoints = baseMaximumHealthPoints;
        }
    }

    /*
     * GETTERS AND SETTERS
     *      SECTION
     */
    public void setHealthPoints(float value){
        //set health points
        healthPoints = value;
    }

    public float getTimeBetweenAttacks() {
        return timeBetweenAttacks;
    }

    public void setTimeBetweenAttacks(float timeBetweenAttacks) {
        this.timeBetweenAttacks = timeBetweenAttacks;
    }

    public float getTimeBetweenAttacksModifier() {
        return timeBetweenAttacksModifier;
    }

    public void setTimeBetweenAttacksModifier(float timeBetweenAttacksModifier) {
        this.timeBetweenAttacksModifier = timeBetweenAttacksModifier;
    }

    public float getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }

    public float getBaseDamageModifier() {
        return baseDamageModifier;
    }

    public void setBaseDamageModifier(float baseDamageModifier) {
        this.baseDamageModifier = baseDamageModifier;
    }

    public float getDamageResistanceModifier() {
        return damageResistanceModifier;
    }

    public void setDamageResistanceModifier(float damageResistanceModifier) {
        this.damageResistanceModifier = damageResistanceModifier;
    }

    public float getHealingModifier() {
        return healingModifier;
    }

    public void setHealingModifier(float healingModifier) {
        this.healingModifier = healingModifier;
    }

    public float getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(float maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getHealthPoints(){
        return healthPoints;
    }

    public void setBaseMaximumHealthPoints(float value){
        baseMaximumHealthPoints = value;
    }

    public float getBaseMaximumHealthPoints(){
        return baseMaximumHealthPoints;
    }

    public void resetMaxHealthPointsMultiplier(){
        maximumHealthPointsMultiplier = 1;
    }

    public float getCriticalHitBase() {
        return criticalHitBase;
    }

    public void setCriticalHitBase(float criticalHitBase) {
        this.criticalHitBase = criticalHitBase;
    }

    public float getCriticalHitModifier() {
        return criticalHitModifier;
    }

    public void setCriticalHitModifier(float criticalHitModifier) {
        this.criticalHitModifier = criticalHitModifier;
    }

    public boolean isPiercingProjectiles() {
        return piercingProjectiles;
    }

    public void setPiercingProjectiles(boolean piercingProjectiles) {
        this.piercingProjectiles = piercingProjectiles;
    }
}
