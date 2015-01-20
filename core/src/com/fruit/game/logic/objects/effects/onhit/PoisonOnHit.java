package com.fruit.game.logic.objects.effects.onhit;

import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.effects.OnHitEffect;
import com.fruit.game.logic.objects.effects.passive.DamageOverTime;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.Projectile;
import com.fruit.game.logic.objects.entities.player.Player;

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
    public void onHit(Projectile proj, Character enemy, Value damage) {
        if(poisonCount>0) {
            enemy.addPassiveEffect(new DamageOverTime(player,enemy, 5f, 0.5f, new Value(Math.max(1,damage.getValue() / 2), Value.POISON_DAMAGE), DamageOverTime.POISONED));
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
