package com.project2k15.entities;

import com.badlogic.gdx.math.Rectangle;

/**
 * Test player class
 */
public class Player extends MovingObject {

    private Rectangle colRect;

    public Player(float positionX, float positionY) {
        position.set(positionX, positionY);
        colRect = new Rectangle(positionX, positionY, width, height);
        collisionRectangles.add(colRect);
        speed = 20;
        maxVelocity = 100;
        scalar = 0.88f;
    }

}
