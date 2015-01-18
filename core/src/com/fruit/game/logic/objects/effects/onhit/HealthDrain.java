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
    private Value value;

    public HealthDrain(Player player){
        this.player = player;
        setEffectID(OnHitEffect.HEALTH_DRAIN_ON_HIT);
        value = new Value(1,Value.HEALING);
    }
    @Override
    public void onHit(Projectile proj, com.fruit.game.logic.objects.entities.Character enemy, Value damage) {
        player.onHealingTaken(value);
    }

    @Override
    public void join(OnHitEffect onHitEffect) {

    }
}
