package com.project2k15.logic.entities.abstracted;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.PropertyRectangle;

/**
 * Abstract MovableObject class. Contains methods to move the entity around
 * ,update method that checks for terrain collisions
 */
public abstract class MovableObject extends Entity {

    /**
     * Velocity vector used with collision checking alghorithm
     * width and height of the entity that will be used with rendering
     * damping with which the velocity vector will be multiplied every frame, 1 = sliding around 0.5 = realistic feel etc
     * speed - number that is added to velocity vector when move[position]() method is called
     */
    protected Vector2 velocity = new Vector2();
    protected float width = 26;
    protected float height = 48;
    protected float clamping;
    protected float maxVelocity = 200;
    protected float speed = 50;

    /**
     * Set of methods to move this entity around. When invoked, checks if the velocity is below the max velocity
     * and then adds the speed variable to the current velocity.
     */

    public void moveRight() {
        if (velocity.x < maxVelocity)
            velocity.add(speed, 0);
    }

    public void moveLeft() {
        if (velocity.x > -maxVelocity)
            velocity.add(-speed, 0);
    }

    public void moveUp() {
        if (velocity.y < maxVelocity) {
            velocity.add(0, speed);
        }
    }

    public void moveDown() {
        if (velocity.y > -maxVelocity) {
            velocity.add(0, -speed);
        }
    }

    /**
     * Getters and setters
     */

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getMaxVelocity() {
        return maxVelocity;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float s) {
        speed = s;
    }

    public void setClamping(Float s) {
        clamping = s;
    }

    public float getClamping() {
        return clamping;
    }

    public void setVelocity(Vector2 vel) {
        velocity = vel;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setWidth(float w){
        width=w;
    }
    public void setHeight(float h){
        height=h;
    }

}
