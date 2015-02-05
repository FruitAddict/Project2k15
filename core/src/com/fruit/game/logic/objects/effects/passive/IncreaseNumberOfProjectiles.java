package com.fruit.game.logic.objects.effects.passive;

import com.fruit.game.logic.objects.effects.PassiveEffect;
import com.fruit.game.logic.objects.entities.Character;

/**
 * @Author FruitAddict
 */
public class IncreaseNumberOfProjectiles extends PassiveEffect {
    private Character character;
    private int amount;

    public IncreaseNumberOfProjectiles(Character character, int amount){
        this.character = character;
        this.amount = amount;
        setEffectType(PassiveEffect.MORE_PROJECTILES);
    }
    @Override
    public void update(float delta) {
    }

    @Override
    public void join(PassiveEffect passiveEffect) {
        onRemove();
        amount += ((IncreaseNumberOfProjectiles)passiveEffect).amount;
        if(amount>2){
            amount = 2;
        }
        character.stats.setNumberOfProjectiles(character.stats.getNumberOfProjectiles()+amount);
        character.stats.setAttackSpeedModifier(character.stats.getAttackSpeedModifier()*(amount+1));
    }

    @Override
    public void apply() {
        character.stats.setNumberOfProjectiles(character.stats.getNumberOfProjectiles()+amount);
        character.stats.setAttackSpeedModifier(character.stats.getAttackSpeedModifier()*(amount+1));
    }

    @Override
    public void onRemove() {
        character.stats.setNumberOfProjectiles(character.stats.getNumberOfProjectiles()-amount);
        character.stats.setAttackSpeedModifier(character.stats.getAttackSpeedModifier()*1/(amount+1));
    }
}
