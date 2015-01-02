package com.fruit.game.logic.objects.effects.passive;

import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.effects.PassiveEffect;
import com.fruit.game.logic.objects.entities.Character;

public class HealOverTime extends PassiveEffect {
    //amount of healing every tick.
    private Value amount;
    private Character character;

    public HealOverTime(Character character, float duration, float delay, Value amount){
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
        character.removePassiveEffect(this);
    }

    @Override
    public void join(PassiveEffect passiveEffect){
        HealOverTime temp = (HealOverTime)passiveEffect;
        duration = (duration+ temp.getDuration())/2;
        amount.setValue(amount.getValue()+temp.amount.getValue()/2);
    }
}
