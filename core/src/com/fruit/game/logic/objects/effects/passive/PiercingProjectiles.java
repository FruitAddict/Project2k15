package com.fruit.game.logic.objects.effects.passive;

import com.fruit.game.logic.objects.effects.PassiveEffect;
import com.fruit.game.logic.objects.entities.Character;

/**
 * @Author FruitAddict
 */
public class PiercingProjectiles extends PassiveEffect {

    private Character chararacter;

    public PiercingProjectiles(Character character){
        this.chararacter = character;
    }
    @Override
    public void update(float delta) {

    }

    @Override
    public void join(PassiveEffect passiveEffect) {

    }

    @Override
    public void apply() {
        chararacter.stats.setPiercingProjectiles(true);
    }

    @Override
    public void onRemove() {
        chararacter.stats.setPiercingProjectiles(false);
    }
}
