package com.fruit.game.logic.objects.effects.passive;

import com.fruit.game.logic.objects.effects.PassiveEffect;
import com.fruit.game.logic.objects.entities.Character;

/**
 * @Author FruitAddict
 */
public class IncreaseKnockback extends PassiveEffect {
    private float amount;
    private Character character;

    public IncreaseKnockback(Character character, float amount){
        this.character = character;
        this.amount = amount;
        setEffectType(PassiveEffect.INCREASE_KNOCKBACK);
    }
    @Override
    public void update(float delta) {

    }

    @Override
    public void join(PassiveEffect passiveEffect) {
        IncreaseKnockback otherEffect = (IncreaseKnockback)passiveEffect;
        if(otherEffect.amount > amount){
            onRemove();
            amount = otherEffect.amount;
            apply();
        }
    }

    @Override
    public void apply() {
        character.stats.setKnockBack(character.stats.getKnockBack()+amount);
    }

    @Override
    public void onRemove() {
        character.stats.setKnockBack(character.stats.getKnockBack()-amount);
    }
}
