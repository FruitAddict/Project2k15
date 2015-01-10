package com.fruit.game.logic.objects.effects;

/**
 * Abstract passive effect class. Must contain info for how long the effect should last (-1 is forever)
 * and what should happen to the entity that. Contains ID's of effects. Can be temporary or permanent
 * (from items etc)
 */
public abstract class PassiveEffect {
    public static final float INFINITY = -1;
    //Effect ID's
    public static final int HEAL_OVER_TIME = 1;
    public static final int DAMAGE_OVER_TIME = 2;
    public static final int DAMAGE_UP = 3;
    public static final int MORE_PROJECTILES = 4;
    public static final int INCREASE_KNOCKBACK = 5;

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
    public abstract void update(float delta);

    //join method, specifies what happen if another effect of the same type gets applied to the player
    public abstract void join(PassiveEffect passiveEffect);

    //apply method, on buffs that modify character stats
    public abstract void apply();

    //remove method, should do n
    public abstract void onRemove();

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
