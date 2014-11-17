package com.project2k15.logic.entities.abstracted;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.quadtree.PropertyRectangle;

/**
 * Abstract entity class from which all in-game objects will inherit from. Contains position vector and
 * array of collision rectangles. (entity can have more than one hitbox, eg a player having different hitboxes for head
 * and torso, enables some complex mechanics later on
 */
public abstract class Entity {
    protected Vector2 position = new Vector2();
    protected Array<PropertyRectangle> collisionRectangles = new Array<PropertyRectangle>();

    /**
     * Setters and getters
     */
    public void setPosition(Vector2 pos) {
        position = pos;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setCollisionRectangles(Array<PropertyRectangle> recs) {
        collisionRectangles = recs;
    }

    public Array<PropertyRectangle> getCollisionRectangles() {
        return collisionRectangles;
    }
}
