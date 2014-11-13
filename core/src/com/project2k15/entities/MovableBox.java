package com.project2k15.entities;

import com.badlogic.gdx.math.Rectangle;
import com.project2k15.entities.abstracted.MovableObject;

import java.util.ArrayList;

/**
 * Created by FruitAddict on 2014-11-13.
 */
public class MovableBox extends MovableObject {
    Player player;
    boolean beingHold = false;

    public MovableBox(Player player, float x, float y) {
        this.player = player;
        position.x = x;
        position.y = y;
        width = 32;
        height = 32;
        collisionRectangles.add(new Rectangle(position.x, position.y, width, height));
        maxVelocity = player.getMaxVelocity();
        speed = player.getSpeed();
        scalar = player.getScalar();
    }

    @Override
    public void update(float delta, ArrayList<Rectangle> test) {
        super.update(delta, test);

        if (player.getCollisionRectangles().get(0).overlaps(getCollisionRectangles().get(0))) {
            System.out.println("Overlapping");

            if (player.facingNorth) {
                moveUp();
            }
            if (player.facingSouth) {
                moveDown();
            }
            if (player.facingLeft) {
                moveLeft();
            }
            if (player.facingRight) {
                moveRight();
            }

        }
        if (!player.holding) {
            beingHold = false;
            player.holdingSomething = false;
        }


    }
}
