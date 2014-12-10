package com.fruit.logic.objects.effects;

import com.fruit.logic.objects.entities.Character;


public class HealOverTime extends Effect {
    //amount of healing every tick.
    private float amount;

    public HealOverTime(float duration, float delay, float amount){
        this.duration = duration;
        this.delay = delay;
        this.amount = amount;
        setEffectType(Effect.HEAL_OVER_TIME);
    }
    @Override
    public void update(Character character, float delta) {
        stateTime+=delta;
        if(stateTime>=duration && duration != Effect.INFINITY){
            character.removeEffect(this);
        }else{
            if(lastUpdateTime>=delay){
                character.changeHealthPoints(amount);
                lastUpdateTime=0;
            }else {
                lastUpdateTime+=delta;
            }
        }
    }

    @Override
    public void apply(){
    //does nothing
    }

    @Override
    public void join(Effect effect){
        duration = (duration+effect.getDuration())/2;
    }
}
