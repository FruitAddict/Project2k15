package com.fruit.logic.objects;

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
            setFacings(false);
            idle = true;
        }else if(body.getLinearVelocity().y > maxVelocity/2 && body.getLinearVelocity().x < maxVelocity/2 && body.getLinearVelocity().x > -maxVelocity/2){
            setFacings(false);
            facingN = true;
        }else if(body.getLinearVelocity().y>maxVelocity/2 && body.getLinearVelocity().x > maxVelocity/2){
            setFacings(false);
            facingNE = true;
        }else if(body.getLinearVelocity().y>maxVelocity/2 && body.getLinearVelocity().x < -maxVelocity/2){
            setFacings(false);
            facingNW = true;
        }else if(body.getLinearVelocity().x>maxVelocity/2 && body.getLinearVelocity().y <maxVelocity/2 && body.getLinearVelocity().y>-maxVelocity/2){
            setFacings(false);
            facingE=true;
        }else if(body.getLinearVelocity().x <= -maxVelocity/2 && body.getLinearVelocity().y < maxVelocity/2 && body.getLinearVelocity().y >-maxVelocity/2){
            setFacings(false);
            facingW = true;
        }else if(body.getLinearVelocity().x <= -maxVelocity/2 && body.getLinearVelocity().y <=-maxVelocity/2){
            setFacings(false);
            facingSW = true;
        }else if(body.getLinearVelocity().y < -maxVelocity/2 && body.getLinearVelocity().x < maxVelocity/2 && body.getLinearVelocity().x > -maxVelocity/2){
            setFacings(false);
            facingS = true;
        }else if(body.getLinearVelocity().x > maxVelocity/2 && body.getLinearVelocity().y < -maxVelocity/2){
            setFacings(false);
            facingSE=true;
        }
    }
}
