package com.fruit.game.logic.objects.effects.passive;


import com.fruit.game.Controller;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.effects.PassiveEffect;
import com.fruit.game.visual.messages.TextMessage;
import com.fruit.game.visual.messages.TextRenderer;
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
    private Character character;

    public DamageOverTime(Character character, float duration, float delay, Value amount, int dOTType){
        this.duration = duration;
        this.delay = delay;
        this.amount = amount;
        this.character = character;
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
                character.onDamageTaken(amount);
                lastUpdateTime=0;
            }else {
                lastUpdateTime+=delta;
            }
        }
    }

    @Override
    public void apply(){
        if(damageOverTimeType==BURNING) {
            character.status.setBurning(true);
            Controller.addOnScreenMessage(
                    new TextMessage("Burning!",character.getPosition(),character.getHeight(),2.0f,TextRenderer.redFont,TextMessage.DYNAMIC_UPFALL));
        }else if(damageOverTimeType ==POISONED){
            character.status.setPoisoned(true);
            Controller.addOnScreenMessage(
                    new TextMessage("Poisoned!",character.getPosition(),character.getHeight(),2.0f,TextRenderer.poisonGreenFont,TextMessage.DYNAMIC_UPFALL));
        }
    }

    @Override
    public void onRemove() {
        if(damageOverTimeType==BURNING) {
            character.status.setBurning(false);
        }else if(damageOverTimeType == POISONED){
            character.status.setPoisoned(false);
        }
        character.removePassiveEffect(this);
    }


    @Override
    public void join(PassiveEffect passiveEffect){
        DamageOverTime temp = (DamageOverTime)passiveEffect;
        duration = (duration+ temp.getDuration())/2;
        amount.setValue(amount.getValue()+temp.amount.getValue()/2);
    }
}
