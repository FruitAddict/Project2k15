package com.fruit.logic.objects.effects.passive;

import com.fruit.logic.objects.effects.PassiveEffect;
import com.fruit.logic.objects.entities.Character;

/**
 * @Author FruitAddict
 */
public class DamageUp extends PassiveEffect {
    private Character character;
    private int amount;

    public DamageUp(Character character, int amount){
        this.character = character;
        this.amount = amount;
        setEffectType(PassiveEffect.DAMAGE_UP);
    }
    @Override
    public void update(float delta) {

    }

    @Override
    public void join(PassiveEffect passiveEffect) {
        onRemove();
        amount = Math.max(((DamageUp)passiveEffect).amount, amount);
        character.stats
                .setBaseDamageModifier(character.stats.getBaseDamageModifier()+amount);
    }

    @Override
    public void apply() {
        character.stats.setBaseDamageModifier(character.stats.getBaseDamageModifier()+amount);
    }

    @Override
    public void onRemove() {
        character.stats.setBaseDamageModifier(character.stats.getBaseDamageModifier()-amount);
    }
}
