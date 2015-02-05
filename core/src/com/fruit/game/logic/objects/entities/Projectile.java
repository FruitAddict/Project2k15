package com.fruit.game.logic.objects.entities;

import com.badlogic.gdx.math.Vector2;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.Value;

/**
 * Abstract projectile class.
 */
public abstract class Projectile extends GameObject {
    //projectile types
    public static final int PLAYER_PROJECTILE = 1;
    public static final int MOB_PROJECTILE = 2;
    public static final int MINDLESS_PROJECTILE = 3;
    //like with every game object it needs access to this to kill itself
    protected ObjectManager objectManager;
    //every projectile should know its direction
    protected Vector2 direction;
    //spawn coordinates
    protected float spawnX;
    protected float spawnY;
    //state time
    public float stateTime;
    //projectile type
    protected int typeID;

    //damage carried by this projectile. Defaulted to 0 unless set.
    protected Value damage;
    //speed of this velocity in logic units/s
    protected float velocity;

    //radius of the projectile
    protected float radius;
    //boolean marking projecitle as forked
    private boolean forked = false;

    public abstract void onHit(Character character);

    public abstract void update(float delta);

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }
    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }


    public boolean isForked() {
        return forked;
    }

    public void setForked(boolean forked) {
        this.forked = forked;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Value getDamage() {
        return damage;
    }

    public void setDamage(Value damage) {
        this.damage = damage;
    }


}