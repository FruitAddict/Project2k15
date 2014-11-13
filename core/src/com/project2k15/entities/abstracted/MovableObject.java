package com.project2k15.entities.abstracted;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class MovableObject extends Entity {

    protected float width = 26;
    protected float height = 48;
    public boolean facingLeft, facingRight, facingNorth, facingSouth;
    protected float scalar;
    protected float maxVelocity = 200;
    protected float speed = 50;

    protected boolean[] resolveCollision(ArrayList<Rectangle> collisionRects) {
        boolean[] result = new boolean[4];
        for (int i = 0; i < 4; i++) {
            result[i] = false;
        }

        if (collisionRectangles.size() > 0) {
            for (Rectangle r : collisionRectangles) {
                for (Rectangle cR : collisionRects) {
                    Rectangle intersection = new Rectangle();
                    Intersector.intersectRectangles(r, cR, intersection);
                    if (intersection.width > 0 || intersection.height > 0) {
                        if (intersection.x > r.x) {
                            //Intersects with right side
                            result[0] = true;
                        }
                        if (intersection.y > r.y) {
                            //Intersects with top side
                            result[1] = true;
                        }
                        if (intersection.x + intersection.width < r.x + r.width) {
                            //Intersects with left side
                            result[2] = true;
                        }
                        if (intersection.y + intersection.height < r.y + r.height) {
                            //Intersects with bottom side
                            result[3] = true;
                        }
                    }

                }
            }
        }
        return result;
    }

    public void update(float delta, ArrayList<Rectangle> checkRectangles) {
        Vector2 newVeloc = velocity.cpy().scl(delta);
        Vector2 oldPosition = position.cpy();
        position.add(newVeloc);
        collisionRectangles.get(0).setPosition(position.x, position.y);
        boolean[] collisions = resolveCollision(checkRectangles);
        if (collisions[0] && collisions[1]) {
            //right-top
            if (velocity.x > 0 && velocity.y < 0) {
                velocity.x = 0;
                position.x = oldPosition.x;
            } else if (velocity.y > 0 && velocity.x < 0) {
                velocity.y = 0;
                position.y = oldPosition.y;
            } else {
                velocity.x = 0;
                velocity.y = 0;
                position = oldPosition;
            }
        } else if (collisions[0] && collisions[3]) {
            //right-bottom
            if (velocity.x > 0 && velocity.y > 0) {
                velocity.x = 0;
                position.x = oldPosition.x;
            } else if (velocity.y < 0 && velocity.x < 0) {
                velocity.y = 0;
                position.y = oldPosition.y;
            } else {
                velocity.x = 0;
                velocity.y = 0;
                position = oldPosition;
            }

        } else if (collisions[2] && collisions[1]) {
            //left-top
            if (velocity.x < 0 && velocity.y < 0) {
                velocity.x = 0;
                position.x = oldPosition.x;
            } else if (velocity.y > 0 && velocity.x > 0) {
                velocity.y = 0;
                position.y = oldPosition.y;
            } else {
                velocity.x = 0;
                velocity.y = 0;
                position = oldPosition;
            }
        } else if (collisions[2] && collisions[3]) {
            //left-bottom
            if (velocity.x < 0 && velocity.y > 0) {
                velocity.x = 0;
                position.x = oldPosition.x;
            } else if (velocity.y < 0 && velocity.x > 0) {
                velocity.y = 0;
                position.y = oldPosition.y;
            } else {
                velocity.x = 0;
                velocity.y = 0;
                position = oldPosition;
            }

        } else {
            if (collisions[0]) {
                //right
                position.x = oldPosition.x;
                velocity.x = 0;
            }
            if (collisions[1]) {
                //top
                position.y = oldPosition.y;
                velocity.y = 0;
            }
            if (collisions[2]) {
                //left
                position.x = oldPosition.x;
                velocity.x = 0;
            }
            if (collisions[3]) {
                //bottom
                position.y = oldPosition.y;
                velocity.y = 0;
            }
        }
        velocity.scl(scalar);
    }


    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    public void moveRight() {
        facingRight = true;
        facingLeft = false;
        facingNorth = false;
        facingSouth = false;
        if (velocity.x < maxVelocity)
            velocity.add(speed, 0);
    }

    public void moveLeft() {
        facingRight = false;
        facingLeft = true;
        facingNorth = false;
        facingSouth = false;
        if (velocity.x > -maxVelocity)
            velocity.add(-speed, 0);
    }

    public void moveUp() {
        facingRight = false;
        facingLeft = false;
        facingNorth = true;
        facingSouth = false;
        if (velocity.y < maxVelocity) {
            velocity.add(0, speed);
        }
    }

    public void moveDown() {
        facingRight = false;
        facingLeft = false;
        facingNorth = false;
        facingSouth = true;
        if (velocity.y > -maxVelocity) {
            velocity.add(0, -speed);
        }
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
