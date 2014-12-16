package com.fruit.logic.objects.entities;

/**
 * Movable game object abstract class. All moving objects will inherit from this.
 */
public abstract class MovableGameObject extends GameObject {
    //Width and height of this game object. Only exists to be used with translating from
    //logic coordinates to screen coordinates (Bodies origin point is at their center, textures origin
    //points are at their bottom left corner.
    protected float width, height;

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
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
}
