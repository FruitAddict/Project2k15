package com.fruit.game.logic.objects.entities;

//todo remove or rework this
public abstract class Enemy extends Character {
    /**
     * Every enemy Should implement onDirectContact method that will resolve direct collision between the enemy and the
     * player/other characters
     * @param character
     * Character passed to this method to resolve the contact, for example, the enemy can invoke its attack method
     */
    public abstract void onDirectContact(Character character);

}
