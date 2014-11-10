package com.project2k15.test;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Test player class
 */
public class Player extends MovingObject {
    final float width = 26;
    final float height = 48;
    private Rectangle colRect;

    public Player(float positionX, float positionY) {
        position.set(positionX, positionY);
        colRect = new Rectangle(positionX, positionY, width, height);
        collisionRectangles.add(colRect);
        speed = 100;
        maxVelocity = 400;

    }

    @Override
    public void update(float delta, ArrayList<Rectangle> checkRectangles) {
        super.update(delta, checkRectangles);
        Vector2 oldPosition = position.cpy();
        position.add(velocity);
        colRect.setPosition(position.x, position.y);
        if (resolveCollision(checkRectangles)) {
            position = oldPosition;
            colRect.setPosition(oldPosition.x, oldPosition.y);
            velocity.set(0, 0);
        }
        velocity.scl(0.88f);
    }

}
