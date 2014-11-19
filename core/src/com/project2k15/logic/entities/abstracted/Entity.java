package com.project2k15.logic.entities.abstracted;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.PropertyRectangle;

/**
 * Abstract entity class from which all in-game objects will inherit from. Contains position vector and
 * array of collision rectangles. (entity can have more than one hitbox, eg a player having different hitboxes for head
 * and torso, enables some complex mechanics later on
 */
public abstract class Entity {
    protected Vector2 position = new Vector2();
    protected PropertyRectangle collisionRectangle = new PropertyRectangle(0, 0, 0, 0, 0);

    public abstract void update(float delta, Array<PropertyRectangle> checkRectangles);

    /**
     * Setters and getters
     */
    public void setPosition(Vector2 pos) {
        position = pos;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setCollisionRectangle(PropertyRectangle rec) {
        collisionRectangle = rec;
    }

    public PropertyRectangle getCollisionRectangle() {
        return collisionRectangle;
    }
}