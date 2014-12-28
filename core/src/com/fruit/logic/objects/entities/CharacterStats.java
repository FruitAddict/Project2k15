package com.fruit.logic.objects.entities;


/**
 * @Author FruitAddict
 * Container for statuses, stats and everything related. Made to keep the player class relatively clean.
 */
public class CharacterStats {

    //Player stats - must contain base value and modifier

    protected int healthPoints;
    protected int baseMaximumHealthPoints;
    protected double maximumHealthPointsMultiplier = 1;
    //helper variable, should berecreated each time entity's health is changed
    private float healthPointPercentOfMax;

    private float attackSpeed =1f;
    private float attackSpeedModifier = 1f;

    private int baseDamage = 1;
    private int baseDamageModifier =1;

    private float damageResistanceModifier = 1f;

    private float healingModifier = 1f;

    private float speed, maxVelocity;

    private float criticalHitBase, criticalHitModifier = 1;

    private int numberOfProjectiles = 1;

    private boolean piercingProjectiles = false;
    //how accurate the character is, the bigger the better
    private float aimSway = 10f;

    public int getCombinedDamage(){
        return baseDamage * baseDamageModifier;
    }

    public float getCombinedAttackSpeed(){
        return attackSpeed * attackSpeedModifier;
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

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getBaseMaximumHealthPoints() {
        return baseMaximumHealthPoints;
    }

    public void setBaseMaximumHealthPoints(int baseMaximumHealthPoints) {
        this.baseMaximumHealthPoints = baseMaximumHealthPoints;
    }

    public double getMaximumHealthPointsMultiplier() {
        return maximumHealthPointsMultiplier;
    }

    public void setMaximumHealthPointsMultiplier(double maximumHealthPointsMultiplier) {
        this.maximumHealthPointsMultiplier = maximumHealthPointsMultiplier;
    }

    public float getHealthPointPercentOfMax() {
        return healthPointPercentOfMax;
    }

    public void setHealthPointPercentOfMax(float healthPointPercentOfMax) {
        this.healthPointPercentOfMax = healthPointPercentOfMax;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public float getAttackSpeedModifier() {
        return attackSpeedModifier;
    }

    public void setAttackSpeedModifier(float attackSpeedModifier) {
        this.attackSpeedModifier = attackSpeedModifier;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
    }

    public int getBaseDamageModifier() {
        return baseDamageModifier;
    }

    public void setBaseDamageModifier(int baseDamageModifier) {
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

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(float maxVelocity) {
        this.maxVelocity = maxVelocity;
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

    public float getAimSway() {
        return aimSway;
    }

    public void setAimSway(float aimSway) {
        this.aimSway = aimSway;
    }

    public int getNumberOfProjectiles() {
        return numberOfProjectiles;
    }

    public void setNumberOfProjectiles(int numberOfProjectiles) {
        this.numberOfProjectiles = numberOfProjectiles;
    }

}
