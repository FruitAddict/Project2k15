package com.fruit.logic.objects.entities;


/**
 * @Author FruitAddict
 * Container for statuses, stats and everything related. Made to keep the player class relatively clean.
 */
public class CharacterStats {

    //Player stats - must contain base value and modifier
    private float timeBetweenAttacks=1f;
    private float timeBetweenAttacksModifier = 1f;

    private float baseDamage = 1f;
    private float baseDamageModifier =2f;

    private float damageResistanceModifier = 1f;

    private float healingModifier = 1f;

    public float getCombinedDamage(){
        return baseDamage * baseDamageModifier;
    }

    public float getCombinedAttackSpeed(){
        return timeBetweenAttacks*timeBetweenAttacksModifier;
    }

    public float getCombinedResistance(){
        return damageResistanceModifier;
    }

    /*
     * GETTERS AND SETTERS
     *      SECTION
     */
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
}
