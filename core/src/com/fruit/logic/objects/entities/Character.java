package com.fruit.logic.objects.entities;

import com.badlogic.gdx.utils.Array;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.effects.PassiveEffect;

/**
 * Abstract character class. Contains method to determine object's facing based on the velocity
 * (For example, if the object's velocity on y axis is + and higher than on x axis, we can determine
 * that it's facing the north direction).
 * Every character should also contain array of effects (DoTs, HoTs, boosts, slows etc) and a method
 * to iterate through those effects and update them.
 * Contains easy methods to move the object around in the game world(logic)
 * e.g. moveEast(), moveWest(). etc. Those methods will move the object automatically
 * according to their speed and maxVelocity (the entity can still be pushed over its limit)
 */
public abstract class Character extends MovableGameObject {

    public boolean facingW, facingE, facingN, facingS,
                   facingNE, facingNW, facingSE, facingSW, idle;

    //array of passive effects, as both player and enemies can have those.
    private Array<PassiveEffect> effectArray = new Array<>();

    //every character must have its own status class to contain possible statuses (makes it easier to render effects)
    public CharacterStatus status = new CharacterStatus();

    //every character must also have stats class for the effects to work with.
    // a container for everything stats related. Provides easy getters for
    //calculated stats and de-clutters everything.
    public CharacterStats stats = new CharacterStats();

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
        if(body.getLinearVelocity().x <=0.55f && body.getLinearVelocity().x >=-0.55f && body.getLinearVelocity().y<=0.55f && body.getLinearVelocity().y >=-0.55f){
            //body is idle
            setFacings(false);
            idle = true;
        }else if(body.getLinearVelocity().y > stats.getMaxVelocity()/2 && body.getLinearVelocity().x < stats.getMaxVelocity()/2 && body.getLinearVelocity().x > -stats.getMaxVelocity()/2){
            //body is facing NORTH
            setFacings(false);
            facingN = true;
        }else if(body.getLinearVelocity().y>stats.getMaxVelocity()/2 && body.getLinearVelocity().x > stats.getMaxVelocity()/2){
            //body is facing NORTH-EAST
            setFacings(false);
            facingNE = true;
        }else if(body.getLinearVelocity().y>stats.getMaxVelocity()/2 && body.getLinearVelocity().x < -stats.getMaxVelocity()/2){
            //body is facing NORTH-WEST
            setFacings(false);
            facingNW = true;
        }else if(body.getLinearVelocity().x>stats.getMaxVelocity()/2 && body.getLinearVelocity().y <stats.getMaxVelocity()/2 && body.getLinearVelocity().y>-stats.getMaxVelocity()/2){
            //body is facing EAST
            setFacings(false);
            facingE=true;
        }else if(body.getLinearVelocity().x <= -stats.getMaxVelocity()/2 && body.getLinearVelocity().y < stats.getMaxVelocity()/2 && body.getLinearVelocity().y >-stats.getMaxVelocity()/2){
            //body is facing WEST
            setFacings(false);
            facingW = true;
        }else if(body.getLinearVelocity().x <= -stats.getMaxVelocity()/2 && body.getLinearVelocity().y <=-stats.getMaxVelocity()/2){
            //body is facing SOUTH-WEST
            setFacings(false);
            facingSW = true;
        }else if(body.getLinearVelocity().y < -stats.getMaxVelocity()/2 && body.getLinearVelocity().x < stats.getMaxVelocity()/2 && body.getLinearVelocity().x > -stats.getMaxVelocity()/2){
            //body is facing SOUTH
            setFacings(false);
            facingS = true;
        }else if(body.getLinearVelocity().x > stats.getMaxVelocity()/2 && body.getLinearVelocity().y < -stats.getMaxVelocity()/2){
            //body is facing SOUTH-EAST
            setFacings(false);
            facingSE=true;
        }
    }

    //what happens when character is damaged
    public abstract void onDamageTaken(Value value);

    //what happens when character is healed
    public abstract void onHealingTaken(Value amount);

    /**
     *
     * @param delta
     * Time between frames
     * @see com.fruit.logic.ObjectManager
     */
    public void updatePassiveEffects(float delta){
        for(PassiveEffect e :effectArray){
            e.update(delta);
        }
    }

    public void addPassiveEffect(PassiveEffect passiveEffect){
        for(PassiveEffect e: effectArray){
            if(e.getEffectType() == passiveEffect.getEffectType()){
                e.join(passiveEffect);
                return;
            }
        }
        effectArray.add(passiveEffect);
        passiveEffect.apply();
    }

    public void removePassiveEffect(PassiveEffect passiveEffect){
        if(effectArray.contains(passiveEffect,true)){
            effectArray.removeValue(passiveEffect,true);
        }
    }

    public void moveEast() {
        if (body.getLinearVelocity().x < stats.getMaxVelocity()) {
            body.setLinearVelocity(body.getLinearVelocity().x + stats.getSpeed(), body.getLinearVelocity().y);
        } else {
            body.setLinearVelocity(stats.getMaxVelocity(),body.getLinearVelocity().y);
        }
    }

    public void moveWest() {
        if (body.getLinearVelocity().x > -stats.getMaxVelocity()) {
            body.setLinearVelocity(body.getLinearVelocity().x - stats.getSpeed(), body.getLinearVelocity().y);
        } else {
            body.setLinearVelocity(-stats.getMaxVelocity(),body.getLinearVelocity().y);
        }
    }

    public void moveNorth() {
        if (body.getLinearVelocity().y < stats.getMaxVelocity()) {
            body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y + stats.getSpeed());
        } else {
            body.setLinearVelocity(body.getLinearVelocity().x, stats.getMaxVelocity());
        }
    }

    public void moveSouth() {
        if (body.getLinearVelocity().y > -stats.getMaxVelocity()) {
            body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y - stats.getSpeed());
        } else {
            body.setLinearVelocity(body.getLinearVelocity().x, -stats.getMaxVelocity());
        }
    }

    public void moveNorthEast() {
        moveNorth();
        moveEast();
    }

    public void moveNorthWest(){
        moveNorth();
        moveWest();
    }

    public void moveSouthEast(){
        moveSouth();
        moveEast();
    }

    public void moveSouthWest(){
        moveSouth();
        moveWest();
    }
}
