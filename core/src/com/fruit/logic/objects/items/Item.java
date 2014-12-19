package com.fruit.logic.objects.items;

import com.fruit.logic.objects.entities.GameObject;
import com.fruit.logic.objects.entities.player.Player;

public abstract class Item extends GameObject {
    //item types
    public static final int HEART = 1;
    public static final int DAMAGE_UP_1 =2;
    public static final int HEALTH_POTION = 3;
    public static final int SPHERE_OF_PROTECTION = 4;
    public static final int POISON_TOUCH = 5;
    //each item should have a String containing its description
    private String description;
    //concrete type of this item (must be one of the types declared at the beginning of this class)
    private int itemType;
    //every item should define what should happen when the player picks it up
    public abstract void onPickUp(Player player);

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
