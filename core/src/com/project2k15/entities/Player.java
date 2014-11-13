package com.project2k15.entities;

import com.badlogic.gdx.math.Rectangle;
import com.project2k15.entities.abstracted.Character;

/**
 * Test player class
 */
public class Player extends Character {

    private Rectangle colRect;
    public boolean holding = false;
    public boolean holdingSomething = false;

    public Player(float positionX, float positionY) {
        width = 24;
        height = 30;
        position.set(positionX, positionY);
        colRect = new Rectangle(positionX, positionY, width, height);
        collisionRectangles.add(colRect);
        speed = 20;
        maxVelocity = 100;
        scalar = 0.88f;
    }

}
