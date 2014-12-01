package com.fruit.logic.objects;

/**
 * Abstract character class. Contains method to determine object's facing based on the velocity
 * (For example, if the object's velocity on y axis is + and higher than on x axis, we can determine
 * that it's facing the north direction);
 */
public abstract class Character extends MovableGameObject {
    public boolean facingW, facingE, facingN, facingS,
                   facingNE, facingNW, facingSE, facingSW, idle;

    public void setFacings(boolean bool){
        //sets all the facing booleans to @bool.
        facingW     =  bool;
        facingN     =  bool;
        facingE     =  bool;
        facingS     =  bool;
        facingNE    =  bool;
        facingNW    =  bool;
        facingSE    =  bool;
        facingSW    =  bool;
        idle        =  bool;
    }

    public void updateFacing(){
        //updates facing booleans based on valocity
        if(body.getLinearVelocity().x <=0.55f && body.getLinearVelocity().x >=-0.55f && body.getLinearVelocity().y<=0.55f && body.getLinearVelocity().y >=-0.55f){
            //body is idle
            setFacings(false);
            idle = true;
        }else if(body.getLinearVelocity().y > maxVelocity/2 && body.getLinearVelocity().x < maxVelocity/2 && body.getLinearVelocity().x > -maxVelocity/2){
            //body is facing NORTH
            setFacings(false);
            facingN = true;
        }else if(body.getLinearVelocity().y>maxVelocity/2 && body.getLinearVelocity().x > maxVelocity/2){
            //body is facing NORTH-EAST
            setFacings(false);
            facingNE = true;
        }else if(body.getLinearVelocity().y>maxVelocity/2 && body.getLinearVelocity().x < -maxVelocity/2){
            //body is facing NORTH-WEST
            setFacings(false);
            facingNW = true;
        }else if(body.getLinearVelocity().x>maxVelocity/2 && body.getLinearVelocity().y <maxVelocity/2 && body.getLinearVelocity().y>-maxVelocity/2){
            //body is facing EAST
            setFacings(false);
            facingE=true;
        }else if(body.getLinearVelocity().x <= -maxVelocity/2 && body.getLinearVelocity().y < maxVelocity/2 && body.getLinearVelocity().y >-maxVelocity/2){
            //body is facing WEST
            setFacings(false);
            facingW = true;
        }else if(body.getLinearVelocity().x <= -maxVelocity/2 && body.getLinearVelocity().y <=-maxVelocity/2){
            //body is facing SOUTH-WEST
            setFacings(false);
            facingSW = true;
        }else if(body.getLinearVelocity().y < -maxVelocity/2 && body.getLinearVelocity().x < maxVelocity/2 && body.getLinearVelocity().x > -maxVelocity/2){
            //body is facing SOUTH
            setFacings(false);
            facingS = true;
        }else if(body.getLinearVelocity().x > maxVelocity/2 && body.getLinearVelocity().y < -maxVelocity/2){
            //body is facing SOUTH-EAST
            setFacings(false);
            facingSE=true;
        }
    }
}
