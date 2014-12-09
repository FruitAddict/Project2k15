package com.fruit.logic.objects.entities;

import com.fruit.logic.objects.entities.GameObject;

/**
 * Movable game object abstract class. All moving objects will inherit from this.
 * Contains easy methods to move the object around in the game world(logic)
 * e.g. moveEast(), moveWest(). etc. Those methods will move the object automatically
 * according to their speed and maxVelocity (the entity can still be pushed over its limit)
 */
public abstract class MovableGameObject extends GameObject {
    //Width and height of this game object. Only exists to be used with translating from
    //logic coordinates to screen coordinates (Bodies origin point is at their center, textures origin
    //points are at their bottom left corner.
    protected float width, height;
    //Maximum allowed velocity for this object.
    protected float maxVelocity;
    //Speed of this object. This value is added to the velocity of this object on worldstep updates.
    protected float speed;

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }


    public float getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(float maxVelocity) {
        this.maxVelocity = maxVelocity;
    }


    public float getDamping() {
        return body.getLinearDamping();
    }

    public void setDamping(float damping) {
        //damping for movement. Needs to be in 0-1 range. 1 = object never loses velocity. 0
        //0= object loses all velocity in the next world step
        if(damping>=0 && damping <=1) {
            body.setLinearDamping(damping);
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void moveEast() {
        if (body.getLinearVelocity().x < maxVelocity) {
            body.setLinearVelocity(body.getLinearVelocity().x + speed, body.getLinearVelocity().y);
        } else {
            body.setLinearVelocity(maxVelocity,body.getLinearVelocity().y);
        }
    }

    public void moveWest() {
        if (body.getLinearVelocity().x > -maxVelocity) {
            body.setLinearVelocity(body.getLinearVelocity().x - speed, body.getLinearVelocity().y);
        } else {
            body.setLinearVelocity(-maxVelocity,body.getLinearVelocity().y);
        }
    }

    public void moveNorth() {
        if (body.getLinearVelocity().y < maxVelocity) {
            body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y + speed);
        } else {
            body.setLinearVelocity(body.getLinearVelocity().x, maxVelocity);
        }
    }

    public void moveSouth() {
        if (body.getLinearVelocity().y > -maxVelocity) {
            body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y - speed);
        } else {
            body.setLinearVelocity(body.getLinearVelocity().x, -maxVelocity);
        }
    }

    public void moveNorthEast() {
        moveNorth();
        moveEast();
    }

    public void moveNorthWest(){
        moveNorth();
        moveWest();
    }

    public void moveSouthEast(){
        moveSouth();
        moveEast();
    }

    public void moveSouthWest(){
        moveSouth();
        moveWest();
    }

}
