package com.project2k15.test;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class MovingObject extends Entity {

    protected float maxVelocity = 200;
    protected float speed = 50;

    protected boolean resolveCollision(ArrayList<Rectangle> collisionRects) {
        if (collisionRectangles.size() > 0) {
            System.out.println("Checking collisions..");
            for (Rectangle r : collisionRectangles) {
                for (Rectangle cR : collisionRects) {
                    if (Intersector.overlaps(r, cR)) {
                        return true;
                    }
                }
            }
        } else {
            return false; //nothing
        }
        return false;
    }

    public void update(float delta, ArrayList<Rectangle> checkRectangles) {
        velocity.scl(delta);
    }

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
}
