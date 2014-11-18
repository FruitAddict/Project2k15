package com.project2k15.logic.entities.abstracted;

public abstract class Character extends MovableObject {
    /**
     * Abstract character class, contains facing booleans for use with drawing,
     * and health points. ( Please notice the oxford coma here).
     */
    public boolean facingLeft, facingRight, facingNorth, facingSouth, idle;

    protected float healthPoints;

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

    public float getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(float healthPoints) {
        this.healthPoints = healthPoints;
    }
}
