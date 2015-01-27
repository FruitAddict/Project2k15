package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;

public abstract class Item extends GameObject {
    //item types
    public static final int HEART = 1;
    public static final int DAMAGE_UP_1 =2;
    public static final int HEALTH_POTION = 3;
    public static final int SPHERE_OF_PROTECTION = 4;
    public static final int POISON_TOUCH = 5;
    public static final int PIERCING_PROJECTILE = 6;
    public static final int MORE_PROJECTILES = 7;
    public static final int MICHAEL_BAY = 8;
    public static final int FORKING_PROJECTILES = 9;
    public static final int INCREASED_KNOCKBACK_HAMMER = 10;
    public static final int BLOOD_AMULET = 11;
    public static final int VOODO_DOLL = 12;
    public static final int SLOWER_PROJECTILE_RING = 13;
    //each item should have a String containing its description
    protected String description;
    //concrete type of this item (must be one of the types declared at the beginning of this class)
    private int itemType;
    private String itemName;
    //every item should define what should happen when the player picks it up
    public abstract void onPickUp(Player player);

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getDescription(){
        return description;
    }

    @Override
    public void addToBox2dWorld(World world){
        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(lastKnownX,lastKnownY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 1.0f;
        bodyDef.fixedRotation = true;

        //create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        //Shape definiton
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/ PIXELS_TO_UNITS /2,height/ PIXELS_TO_UNITS /2);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 50f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ITEM_BIT;
        fixtureDef.filter.maskBits = PLAYER_BIT | TERRAIN_BIT | PORTAL_BIT | ITEM_BIT | ENEMY_BIT | CLUTTER_BIT;
        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }

    @Override
    public void killYourself(){
        Controller.getWorldRenderer().getLightRenderer().freeAttachedLight(this);
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
