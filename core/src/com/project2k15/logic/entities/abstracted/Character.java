package com.project2k15.logic.entities.abstracted;

public abstract class Character extends MovableObject {
    /**
     * Abstract character class, contains facing booleans for use with drawing,
     * and health points. ( Please notice the oxford coma here).
     * Will contain hooks for operating on AI objects etc
     */
    public boolean facingW, facingE, facingN, facingS,
                    facingNE, facingNW, facingSE, facingSW, idle;

    protected float healthPoints;

    public boolean hitByPlayer=false;

    public float getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(float healthPoints) {
        this.healthPoints = healthPoints;
    }

    public void addHealthPoints(float amount){
        this.healthPoints += amount;
    }

    public void setFacings(boolean bool){
        //sets all the facing booleans to @bool.
        facingW = bool;
        facingN = bool;
        facingE = bool;
        facingS = bool;
        facingNE = bool;
        facingNW = bool;
        facingSE = bool;
        facingSW = bool;
        idle = bool;
    }

    public void updateFacing(){
        //updates facing booleans based on valocity
        if(velocity.x <=25 && velocity.x >=-25 && velocity.y<=25 && velocity.y >=-25){
            setFacings(false);
            idle = true;
        }else if(velocity.y > maxVelocity/2 && velocity.x < maxVelocity/2 && velocity.x > -maxVelocity/2){
            setFacings(false);
            facingN = true;
        }else if(velocity.y>maxVelocity/2 && velocity.x > maxVelocity/2){
            setFacings(false);
            facingNE = true;
        }else if(velocity.y>maxVelocity/2 && velocity.x < -maxVelocity/2){
            setFacings(false);
            facingNW = true;
        }else if(velocity.x>maxVelocity/2 && velocity.y <maxVelocity/2 && velocity.y>-maxVelocity/2){
            setFacings(false);
            facingE=true;
        }else if(velocity.x <= -maxVelocity/2 && velocity.y < maxVelocity/2 && velocity.y >-maxVelocity/2){
            setFacings(false);
            facingW = true;
        }else if(velocity.x <= -maxVelocity/2 && velocity.y <=-maxVelocity/2){
            setFacings(false);
            facingSW = true;
        }else if(velocity.y < -maxVelocity/2 && velocity.x < maxVelocity/2 && velocity.x > -maxVelocity/2){
            setFacings(false);
            facingS = true;
        }else if(velocity.x > maxVelocity/2 && velocity.y < -maxVelocity/2){
            setFacings(false);
            facingSE=true;
        }
    }

}
