package com.project2k15.logic.entities.abstracted;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.PropertyRectangle;

/**
 * Abstract entity class from which all in-game objects will inherit from. Contains position vector and
 * array of collision rectangles. Each child class should have one unique id (for example, player = 10,
 * mob1 = 11, projectileA =12, mob2 = 13 etc. So
 */
public abstract class Entity {
    protected Vector2 position = new Vector2();
    //default empty collision rectangle.
    protected PropertyRectangle collisionRectangle = new PropertyRectangle(0, 0, 0, 0, 0);
    protected int typeID;

    public abstract void update(float delta, Array<PropertyRectangle> checkRectangles);

    public void setPosition(Vector2 pos) {
        position = pos;
    }

    public Vector2 getPosition() {
        return position;
    }

    public PropertyRectangle getCollisionRectangle() {
        return collisionRectangle;
    }

    public void setTypeID(int type){
        this.typeID = type;
    }

    public int getTypeID(){
        return typeID;
    }
}
