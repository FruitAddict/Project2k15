package com.project2k15.entities.abstracted;

/**
 * Created by FruitAddict on 2014-11-13.
 */
public class Character extends MovableObject {
    public boolean facingLeft, facingRight, facingNorth, facingSouth, idle;

    @Override
    public void moveRight() {
        super.moveRight();
        facingRight = true;
        facingLeft = false;
        facingNorth = false;
        facingSouth = false;
    }

    public void moveLeft() {
        super.moveLeft();
        facingRight = false;
        facingLeft = true;
        facingNorth = false;
        facingSouth = false;
    }

    public void moveUp() {
        super.moveUp();
        facingRight = false;
        facingLeft = false;
        facingNorth = true;
        facingSouth = false;
    }

    public void moveDown() {
        super.moveDown();
        facingRight = false;
        facingLeft = false;
        facingNorth = false;
        facingSouth = true;
    }
}
