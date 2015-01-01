package com.fruit.logic.objects.effects.onhit;

import com.fruit.Controller;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.effects.OnHitEffect;
import com.fruit.logic.objects.effects.passive.DamageOverTime;
import com.fruit.logic.objects.entities.Enemy;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.visual.messages.TextMessage;
import com.fruit.visual.messages.TextRenderer;

/**
 * @Author FruitAddict
 */
public class PoisonOnHit extends OnHitEffect implements Constants {
    private int poisonCount;
    private Player player;

    public PoisonOnHit(Player player, int charges){
        this.player = player;
        poisonCount = charges;
        setEffectID(OnHitEffect.POISON_ON_HIT);
    }

    @Override
    public void onHit(Enemy enemy, Value damage) {
        if(poisonCount>0) {
            enemy.addPassiveEffect(new DamageOverTime(enemy, 5f, 0.5f, new Value(Math.max(1,damage.getValue() / 2), Value.POISON_DAMAGE), DamageOverTime.POISONED));
            poisonCount--;
        }else {
            player.removeOnHitEffect(this);
        }
    }

    @Override
    public void join(OnHitEffect onHitEffect) {
        PoisonOnHit poisonEffectReceived = (PoisonOnHit)onHitEffect;
        poisonCount+=poisonEffectReceived.poisonCount/2;
    }
}
