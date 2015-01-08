package com.fruit.game.logic.objects.entities;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fruit.game.Controller;
import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.effects.PassiveEffect;
import com.fruit.game.visual.messages.TextMessage;
import com.fruit.game.visual.messages.TextRenderer;

/**
 * Abstract character class. Contains method to determine object's facing based on the velocity
 * (For example, if the object's velocity on y axis is + and higher than on x axis, we can determine
 * that it's facing the north direction).
 * Every character should also contain array of effects (DoTs, HoTs, boosts, slows etc) and a method
 * to iterate through those effects and update them.
 * Contains easy methods to move the object around in the game world(logic)
 * e.g. moveEast(), moveWest(). etc. Those methods will move the object automatically
 * according to their speed and maxVelocity (the entity can still be pushed over its limit)
 *
 * Every character (including the player) should be Steerable for use with gdx-ai.
 */
public abstract class Character extends GameObject implements Steerable<Vector2> {

    public boolean facingW, facingE, facingN, facingS, idle;

    //array of passive effects, as both player and enemies can have those.
    private Array<PassiveEffect> effectArray = new Array<>();

    //every character must have its own status class to contain possible statuses (makes it easier to render effects)
    public CharacterStatus status = new CharacterStatus();

    //every character must also have stats class for the effects to work with.
    // a container for everything stats related. Provides easy getters for
    //calculated stats and de-clutters everything.
    public CharacterStats stats = new CharacterStats();
    //steering output that is used by ai
    protected final SteeringAcceleration<Vector2> steeringOutput =
            new SteeringAcceleration<Vector2>(new Vector2());
    //temporary speed vector
    private Vector2 newVelocity = new Vector2();
    protected SteeringBehavior<Vector2> steeringBehavior;

    public void setFacings(boolean bool){
        //sets all the facing booleans to @bool.
        facingW = bool;
        facingN = bool;
        facingE = bool;
        facingS = bool;
        idle = bool;
    }

    public void updateFacing(){
        //updates facing booleans based on valocity
        float angle = body.getLinearVelocity().angle();
        if(body.getLinearVelocity().x <= stats.getMaxVelocity()/9 && body.getLinearVelocity().x >=-stats.getMaxVelocity()/9 && body.getLinearVelocity().y<=stats.getMaxVelocity()/9 && body.getLinearVelocity().y >=-stats.getMaxVelocity()/9){
            //body is idle
            setFacings(false);
            idle = true;
        }
        else if(angle > 45 && angle <= 135){
            setFacings(false);
            facingN = true;
        }
        else if(angle>135 && angle <= 225){
            setFacings(false);
            facingW = true;
        }
        else if(angle>225 && angle <= 315){
            setFacings(false);
            facingS = true;
        }
        else if(angle > 315 || angle <= 45){
            setFacings(false);
            facingE = true;
        }
    }

    //what happens when character is damaged, child classes should call this after all the logic is done.
    //Handles only rendering to the screen using Text Renderer
    public void onDamageTaken(Value value){
        //render to the screen as scrolling battle text based on the type
        switch(value.getType()) {
            case Value.NORMAL_DAMAGE: {
                Controller.addOnScreenMessage(new TextMessage(Integer.toString(value.getValue()), getBody().getPosition().x * PIXELS_TO_UNITS,
                        getBody().getPosition().y * PIXELS_TO_UNITS, 1.5f, TextRenderer.redFont, TextMessage.UP));
                break;
            }
            case Value.BURNING_DAMAGE: {
                Controller.addOnScreenMessage(new TextMessage(Integer.toString(value.getValue()), getBody().getPosition().x * PIXELS_TO_UNITS,
                        getBody().getPosition().y * PIXELS_TO_UNITS, 1.5f, TextRenderer.redFont, TextMessage.UP));
                break;
            }
            case Value.POISON_DAMAGE: {
                Controller.addOnScreenMessage(new TextMessage(Integer.toString(value.getValue()), getBody().getPosition().x * PIXELS_TO_UNITS,
                        getBody().getPosition().y * PIXELS_TO_UNITS, 1.5f, TextRenderer.poisonGreenFont, TextMessage.UP));
                break;
            }
        }
        //update the %/max of player's hp (GUI uses that)
        stats.setHealthPointPercentOfMax(stats.getHealthPoints()/(float)stats.getBaseMaximumHealthPoints());

    }

    //what happens when character is healed
    public void onHealingTaken(Value amount){
        switch(amount.getType()) {
            case Value.HEALING: {
                Controller.addOnScreenMessage(new TextMessage(Integer.toString(amount.getValue())+" HP", getBody().getPosition().x * PIXELS_TO_UNITS,
                        getBody().getPosition().y * PIXELS_TO_UNITS, 1.5f, TextRenderer.greenFont, TextMessage.UP));
                break;
            }
        }
        //update the %/max of players hp again
        stats.setHealthPointPercentOfMax(stats.getHealthPoints()/(float)stats.getBaseMaximumHealthPoints());
    }

    /**
     * Every character should know what to do when it hits a wall
     * @param direction
     */
    public abstract void onContactWithTerrain(int direction);

    /**
     *
     * @param delta
     * Time between frames
     * @see com.fruit.game.logic.ObjectManager
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

    public void addLinearVelocity(float velX,float velY){
        newVelocity.set(body.getLinearVelocity().add(velX,velY));
        if(newVelocity.x>stats.getMaxVelocity()){
            newVelocity.x = stats.getMaxVelocity();
        }
        else if(newVelocity.x < -stats.getMaxVelocity()){
            newVelocity.x = -stats.getMaxVelocity();
        }
        if(newVelocity.y>stats.getMaxVelocity()){
            newVelocity.y = stats.getMaxVelocity();
        }
        else if(newVelocity.y < -stats.getMaxVelocity()){
            newVelocity.y = -stats.getMaxVelocity();
        }
        body.setLinearVelocity(newVelocity);
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getLinearVelocity().angle();
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return Math.min(width, height)/2 / PIXELS_TO_UNITS;
    }

    @Override
    public boolean isTagged() {
        return status.isTagged();
    }

    @Override
    public void setTagged(boolean b) {
        status.setTagged(b);
    }

    @Override
    public Vector2 newVector() {
        return new Vector2();
    }

    @Override
    public float vectorToAngle(Vector2 vector2) {
        return (float)Math.toRadians(vector2.angle());
    }

    @Override
    public Vector2 angleToVector(Vector2 vector2, float v) {
        vector2.set(1,0);
        vector2.rotate(v);
        return vector2;
    }

    @Override
    public float getMaxLinearSpeed() {
        return stats.getMaxVelocity();
    }

    @Override
    public void setMaxLinearSpeed(float v) {
        stats.setMaxVelocity(v);
    }

    @Override
    public float getMaxLinearAcceleration() {
        return 10;
    }

    @Override
    public void setMaxLinearAcceleration(float v) {
        stats.setSpeed(v);
    }

    @Override
    public float getMaxAngularSpeed() {
        return 0;
    }

    @Override
    public void setMaxAngularSpeed(float v) {

    }

    @Override
    public float getMaxAngularAcceleration() {
        return 0;
    }

    @Override
    public void setMaxAngularAcceleration(float v) {

    }

    public void changeSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior){
        this.steeringBehavior = steeringBehavior;
    }
    protected void applySteering (float deltaTime){
        addLinearVelocity(steeringOutput.linear.x*deltaTime , steeringOutput.linear.y*deltaTime);
    }
}
