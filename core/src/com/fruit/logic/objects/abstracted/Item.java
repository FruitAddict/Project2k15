package com.fruit.logic.objects.abstracted;

public abstract class Item extends MovableGameObject {
    //item types
    public static final int HEALTH_RENEWER = 1;
    public static final int COLLECTIBLE = 2;
    //each item should have a String containing its description
    private String description;
    //concrete type of this item (must be one of the types declared at the beginning of this class)
    private int itemType;

}
