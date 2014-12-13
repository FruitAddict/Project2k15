package com.fruit.logic.objects.effects;

import com.fruit.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 * TODO CHANGE PLAYER STATS TO GENERAL STATS
 */
public class DamageUP extends PassiveEffect{
    private Player character;

    public DamageUP(Player character){
        this.character = character;
        setEffectType(PassiveEffect.DAMAGE_UP);
    }
    @Override
    public void update(float delta) {

    }

    @Override
    public void join(PassiveEffect passiveEffect) {
        //do nuthin
    }

    @Override
    public void apply() {
        character.stats.setBaseDamage(character.stats.getBaseDamage()+1);
    }

    @Override
    public void onRemove() {
        character.stats.setBaseDamage(character.stats.getBaseDamage()-1);
    }
}
