package com.fruit.logic.objects.entities;

import com.fruit.logic.objects.entities.player.Player;

public abstract class Enemy extends Character {

    protected float damageResistanceModifier = 1f;
    /**
     * Every enemy Should implement onDirectContact method that will resolve direct collision between the enemy and the
     * player.
     * @param player
     * Player passed to this method to resolve the contact, for example, the enemy can invoke its attack method
     * and make the
     */
    public abstract void onDirectContact(Player player);

    public float getDamageResistanceModifier() {
        return damageResistanceModifier;
    }

    public void setDamageResistanceModifier(float damageResistanceModifier) {
        this.damageResistanceModifier = damageResistanceModifier;
    }
}
