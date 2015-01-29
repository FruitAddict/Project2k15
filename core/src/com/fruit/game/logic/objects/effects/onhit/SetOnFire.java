package com.fruit.game.logic.objects.effects.onhit;

import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.effects.OnHitEffect;
import com.fruit.game.logic.objects.effects.passive.DamageOverTime;
import com.fruit.game.logic.objects.entities.*;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class SetOnFire extends OnHitEffect {
    private Player player;
    private Value value;

    public SetOnFire(Player player){
        this.player = player;
        setEffectID(OnHitEffect.SET_ON_FIRE);
        value = new Value(1,Value.BURNING_DAMAGE);
    }

    @Override
    public void onHit(Projectile proj, Character enemy, Value damage) {
        value.setValue(Math.max(1,damage.getValue()/2));
        enemy.addPassiveEffect(new DamageOverTime(player,enemy,5f,0.5f,value,DamageOverTime.BURNING));
    }

    @Override
    public void join(OnHitEffect onHitEffect) {
    }
}
