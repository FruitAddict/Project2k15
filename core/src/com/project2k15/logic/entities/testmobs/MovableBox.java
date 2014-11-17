package com.project2k15.logic.entities.testmobs;

import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.entities.Player;
import com.project2k15.logic.entities.abstracted.MovableObject;
import com.project2k15.logic.quadtree.PropertyRectangle;

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
        collisionRectangles.add(new PropertyRectangle(position.x, position.y, width, height, PropertyRectangle.MOVING_OBJECT));
        maxVelocity = player.getMaxVelocity();
        speed = player.getSpeed();
        clamping = player.getClamping();
    }

    @Override
    public void update(float delta, Array<PropertyRectangle> test) {
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
