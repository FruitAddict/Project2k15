package com.fruit.logic.objects.effects;

import com.fruit.logic.objects.entities.Character;

/**
 * Abstract effect class. Must contain info for how long the effect should last (-1 is forever)
 * and what should happen to the entity that. Contains ID's of effects
 */
public abstract class Effect {
    public static final float INFINITY = -1;
    public static final int HEAL_OVER_TIME = 1;
    public static final int DAMAGE_OVER_TIME = 2;

    //duration of this effect, e.g. maximum time this effect should be on.
    protected float duration;
    //state time since the effect began
    protected float stateTime;
    //delay between updates (e.g. the effect of healing over time will kick in every 0.5s)
    protected float delay;
    //type id
    private int effectType;
    //last tick time
    protected float lastUpdateTime;

    //update method, must be implemented by concrete effects
    public abstract void update(Character character, float delta);

    //join method, specifies what happen if another effect of the same type gets applied to the player
    public abstract void join(Effect effect);

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public int getEffectType() {
        return effectType;
    }

    public void setEffectType(int effectType) {
        this.effectType = effectType;
    }
}
