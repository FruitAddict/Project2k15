package com.fruit.logic.objects.entities;

import com.badlogic.gdx.math.Vector2;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.Value;

/**
 * Abstract projectile class.
 */
public abstract class Projectile extends MovableGameObject {
    //projectile types
    public static final int PLAYER_PROJECTILE = 1;
    public static final int MOB_PROJECTILE = 2;
    //like with every game object it needs access to this to kill itself
    protected ObjectManager objectManager;
    //every projectile should know its direction
    protected Vector2 direction;
    //spawn coordinates
    protected float spawnX;
    protected float spawnY;

    //projectile type
    protected int typeID;

    //damage carried by this projectile. Defaulted to 0 unless set.
    protected Value damage;
    //speed of this velocity in logic units/s
    protected Value velocity;
    //radius of the projectile
    protected float radius;

    public abstract void onHit(Character character);

    public abstract void update(float delta);

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

}