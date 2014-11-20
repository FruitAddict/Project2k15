package com.project2k15.logic.entities.abstracted;

public abstract class Character extends MovableObject {
    /**
     * Abstract character class, contains facing booleans for use with drawing,
     * and health points. ( Please notice the oxford coma here).
     */
    public boolean facingLeft, facingRight, facingUp, facingDown, idle;

    protected float healthPoints;

    @Override
    public void moveRight() {
        super.moveRight();
        setFacings(false);
        facingRight=true;
    }

    public void moveLeft() {
        super.moveLeft();
        setFacings(false);
        facingLeft=true;
    }

    public void moveUp() {
        super.moveUp();
        setFacings(false);
        facingUp =true;
    }

    public void moveDown() {
        super.moveDown();
        setFacings(false);
        facingDown =true;
    }

    public float getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(float healthPoints) {
        this.healthPoints = healthPoints;
    }

    public void setFacings(boolean bool){
        facingLeft = bool;
        facingUp = bool;
        facingRight = bool;
        facingDown = bool;
    }
}
