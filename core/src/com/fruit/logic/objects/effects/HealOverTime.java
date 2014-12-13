package com.fruit.logic.objects.effects;

import com.fruit.logic.objects.entities.Character;


public class HealOverTime extends PassiveEffect {
    //amount of healing every tick.
    private float amount;
    private Character character;

    public HealOverTime(Character character, float duration, float delay, float amount){
        this.duration = duration;
        this.delay = delay;
        this.amount = amount;
        this.character = character;
        setEffectType(PassiveEffect.HEAL_OVER_TIME);
    }
    @Override
    public void update(float delta) {
        stateTime+=delta;
        if(stateTime>=duration && duration != PassiveEffect.INFINITY){
            character.removePassiveEffect(this);
            onRemove();
        }else{
            if(lastUpdateTime>=delay){
                character.onHealingTaken(amount);
                lastUpdateTime=0;
            }else {
                lastUpdateTime+=delta;
            }
        }
    }

    @Override
    public void apply(){
        character.status.setHealing(true);
    }

    @Override
    public void onRemove() {
        character.status.setHealing(false);
    }

    @Override
    public void join(PassiveEffect passiveEffect){
        duration = (duration+ passiveEffect.getDuration())/2;
    }
}
