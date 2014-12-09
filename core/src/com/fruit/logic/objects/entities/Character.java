package com.fruit.logic.objects.entities;

import com.badlogic.gdx.utils.Array;
import com.fruit.logic.objects.effects.Effect;

/**
 * Abstract character class. Contains method to determine object's facing based on the velocity
 * (For example, if the object's velocity on y axis is + and higher than on x axis, we can determine
 * that it's facing the north direction).
 * Every character should also contain array of effects (DoTs, HoTs, boosts, slows etc) and a method
 * to iterate through those effects and update them.
 */
public abstract class Character extends MovableGameObject {

    protected float healthPoints;
    protected float maximumHealthPoints;

    public boolean facingW, facingE, facingN, facingS,
                   facingNE, facingNW, facingSE, facingSW, idle;

    private Array<Effect> effectArray = new Array<>();

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

    public void updateEffects(float delta){
        for(Effect e :effectArray){
            e.update(this,delta);
        }
    }

    public void addEffect(Effect effect){
        for(Effect e: effectArray){
            if(e.getEffectType() == effect.getEffectType()){
                e.join(effect);
                return;
            }
        }
        effectArray.add(effect);
    }

    public int getEffectIDS(){
        //returns effect ids currently on the character
        for(Effect e :effectArray){
            if(e.getEffectType()==Effect.HEAL_OVER_TIME){
                return 1;
            }
        }
        return 0;
    }

    public void removeEffect(Effect effect){
        if(effectArray.contains(effect,true)){
            effectArray.removeValue(effect,true);
        }
    }

    public void changeHealthPoints(float amount){
        //every character must be damagable or healable.
        if(healthPoints+amount < maximumHealthPoints) {
            healthPoints += amount;
        }else {
            healthPoints = maximumHealthPoints;
        }
    }

    public void setHealthPoints(float value){
        //set health points
        healthPoints = value;
    }

    public float getHealthPoints(){
        return healthPoints;
    }

    public void setMaximumHealthPoints(float value){
        maximumHealthPoints = value;
    }

    public float getMaximumHealthPoints(){
        return maximumHealthPoints;
    }
}
