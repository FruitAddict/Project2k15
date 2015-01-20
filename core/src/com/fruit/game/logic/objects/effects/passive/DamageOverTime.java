package com.fruit.game.logic.objects.effects.passive;


import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.effects.PassiveEffect;
import com.fruit.game.logic.objects.entities.Character;

/**
 * @Author FruitAddict
 */
public class DamageOverTime extends PassiveEffect implements Constants {
    //DoT types
    public static final int BURNING = 1;
    public static final int POISONED = 2;
    //amount of healing every tick.
    private Value amount;
    private int damageOverTimeType;
    private Character target;
    private Character source;

    public DamageOverTime(Character source, Character target, float duration, float delay, Value amount, int dOTType){
        this.duration = duration;
        this.delay = delay;
        this.amount = amount;
        this.target = target;
        setEffectType(PassiveEffect.DAMAGE_OVER_TIME);
        damageOverTimeType = dOTType;
    }
    @Override
    public void update(float delta) {
        stateTime+=delta;
        if(stateTime>=duration && duration != PassiveEffect.INFINITY){
            onRemove();
        }else{
            if(lastUpdateTime>=delay){
                target.onDamageTaken(null, amount);
                lastUpdateTime=0;
            }else {
                lastUpdateTime+=delta;
            }
        }
    }

    @Override
    public void apply(){
        if(damageOverTimeType==BURNING) {
            target.status.setBurning(true);
        }else if(damageOverTimeType ==POISONED){
            target.status.setPoisoned(true);
            //Controller.addOnScreenMessage(
            //       new TextMessage("Poisoned!",target.getPosition(),target.getHeight(),2.0f,TextRenderer.poisonGreenFont,TextMessage.DYNAMIC_UPFALL));
        }
    }

    @Override
    public void onRemove() {
        if(damageOverTimeType==BURNING) {
            target.status.setBurning(false);
        }else if(damageOverTimeType == POISONED){
            target.status.setPoisoned(false);
        }
        target.removePassiveEffect(this);
    }


    @Override
    public void join(PassiveEffect passiveEffect){
        DamageOverTime temp = (DamageOverTime)passiveEffect;
        duration = (duration+ temp.getDuration())/2;
        amount.setValue(amount.getValue()+temp.amount.getValue()/2);
    }
}
