package com.project2k15.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Test player class
 */
public class Player extends MovingObject {

    private Rectangle colRect;
    private float scalar;

    public Player(float positionX, float positionY) {
        position.set(positionX, positionY);
        colRect = new Rectangle(positionX, positionY, width, height);
        collisionRectangles.add(colRect);
        speed = 20;
        maxVelocity = 100;
        scalar = 0.88f;

    }

    @Override
    public void update(float delta, ArrayList<Rectangle> checkRectangles) {
        Vector2 newVeloc = velocity.cpy().scl(delta);
        Vector2 oldPosition = position.cpy();
        position.add(newVeloc);
        colRect.setPosition(position.x, position.y);
        String collisions = resolveCollision(checkRectangles);
        if (collisions.contains("R")) {
            position.x = oldPosition.x;
            velocity.x = 0;
            System.out.println("Collision right");
        }
        if (collisions.contains("T")) {
            position.y = oldPosition.y;
            velocity.y = 0;
            System.out.println("Collision top");
        }
        if (collisions.contains("L")) {
            position.x = oldPosition.x;
            velocity.x = 0;
            System.out.println("Collision left");
        }
        if (collisions.contains("B")) {
            position.y = oldPosition.y;
            velocity.y = 0;
            System.out.println("Collision bottom");
        }
        velocity.scl(scalar);

    }

    public void setScalar(Float s) {
        scalar = s;
    }

    public float getScalar() {
        return scalar;
    }

}
