package com.project2k15.test;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by FruitAddict on 2014-11-10.
 */
public abstract class Entity {
    protected Vector2 velocity = new Vector2();
    protected Vector2 position = new Vector2();
    protected ArrayList<Rectangle> collisionRectangles = new ArrayList<Rectangle>();

    public void setVelocity(Vector2 vel) {
        velocity = vel;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setPosition(Vector2 pos) {
        position = pos;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setCollisionRectangles(ArrayList<Rectangle> recs) {
        collisionRectangles = recs;
    }
}
