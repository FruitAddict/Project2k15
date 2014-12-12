package com.fruit.logic.objects.entities;

import com.fruit.logic.objects.player.Player;

public abstract class Enemy extends Character {
    /**
     * Every enemy Should implement onDirectContact method that will resolve direct collision between the enemy and the
     * player.
     * @param player
     * Player passed to this method to resolve the contact, for example, the enemy can invoke its attack method
     * and make the
     */
    public abstract void onDirectContact(Player player);
}
