package com.project2k15.entities;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class MovingObject extends Entity {

    protected float maxVelocity = 200;
    protected float speed = 50;
    protected float width = 26;
    protected float height = 48;
    public boolean facingLeft, facingRight, facingNorth, facingSouth;
    protected float scalar;

    protected String resolveCollision(ArrayList<Rectangle> collisionRects) {
        String collisionsFound = "";

        if (collisionRectangles.size() > 0) {
            for (Rectangle r : collisionRectangles) {
                for (Rectangle cR : collisionRects) {
                    Rectangle intersection = new Rectangle();
                    Intersector.intersectRectangles(r, cR, intersection);
                    if (intersection.width > 0 || intersection.height > 0) {
                        if (intersection.x > r.x) {
                            //Intersects with right side
                            collisionsFound += "R";
                        }
                        if (intersection.y > r.y) {
                            //Intersects with top side
                            collisionsFound += "T";
                        }
                        if (intersection.x + intersection.width < r.x + r.width) {
                            //Intersects with left side
                            collisionsFound += "L";
                        }
                        if (intersection.y + intersection.height < r.y + r.height) {
                            //Intersects with bottom side
                            collisionsFound += "B";
                        }
                    }

                }
            }
        }
        return collisionsFound;
    }

    public void update(float delta, ArrayList<Rectangle> checkRectangles) {
        Vector2 newVeloc = velocity.cpy().scl(delta);
        Vector2 oldPosition = position.cpy();
        position.add(newVeloc);
        collisionRectangles.get(0).setPosition(position.x, position.y);
        String collisions = resolveCollision(checkRectangles);
        if (collisions.contains("R")) {
            position.x = oldPosition.x;
            velocity.x = 0;
        }
        if (collisions.contains("T")) {
            position.y = oldPosition.y;
            velocity.y = 0;
        }
        if (collisions.contains("L")) {
            position.x = oldPosition.x;
            velocity.x = 0;
        }
        if (collisions.contains("B")) {
            position.y = oldPosition.y;
            velocity.y = 0;
        }
        velocity.scl(scalar);
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

    public void setScalar(Float s) {
        scalar = s;
    }

    public float getScalar() {
        return scalar;
    }
}
