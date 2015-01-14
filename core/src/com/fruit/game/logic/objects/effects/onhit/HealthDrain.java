package com.fruit.game.logic.objects.effects.onhit;

import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.effects.OnHitEffect;
import com.fruit.game.logic.objects.entities.*;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class HealthDrain extends OnHitEffect {

    private Player player;

    public HealthDrain(Player player){
        this.player = player;
        setEffectID(OnHitEffect.HEALTH_DRAIN_ON_HIT);
    }
    @Override
    public void onHit(Projectile proj, com.fruit.game.logic.objects.entities.Character enemy, Value damage) {
        Value val = damage.cpy();
        val.setValue(Math.max(1, damage.getValue() / 4));
        val.setType(Value.HEALING);
        player.onHealingTaken(val);
    }

    @Override
    public void join(OnHitEffect onHitEffect) {

    }
}
