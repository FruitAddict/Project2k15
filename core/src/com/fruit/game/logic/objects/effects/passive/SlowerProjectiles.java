package com.fruit.game.logic.objects.effects.passive;

import com.fruit.game.logic.objects.effects.PassiveEffect;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class SlowerProjectiles extends PassiveEffect {
    private Character character;

    public SlowerProjectiles(Character character){
        this.character = character;
        setEffectType(PassiveEffect.INCREASE_KNOCKBACK);
    }
    @Override
    public void update(float delta) {

    }

    @Override
    public void join(PassiveEffect passiveEffect) {
        //do nothing
    }

    @Override
    public void apply() {
        Player p  = (Player)character;
        p.setProjectileVelocity(p.getProjectileVelocity()/3);
    }

    @Override
    public void onRemove() {
        Player p  = (Player)character;
        p.setProjectileVelocity(p.getProjectileVelocity()*3);
    }
}
