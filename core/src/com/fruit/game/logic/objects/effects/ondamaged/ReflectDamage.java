package com.fruit.game.logic.objects.effects.ondamaged;

import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.effects.OnDamageTakenEffect;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class ReflectDamage extends OnDamageTakenEffect {

    private Player player;
    private int count;

    public ReflectDamage(Player player, int count){
        this.count = count;
        this.player = player;
        setEffectID(OnDamageTakenEffect.REFLECT_DAMAGE);
    }
    @Override
    public void onDamageTaken(Character source, Value mod, Value original) {
        if(count > 0 || count == OnDamageTakenEffect.INFINITE_CHARGES) {
            if(source!=null) {
                source.onDamageTaken(player, original);
            }
            if(count!=OnDamageTakenEffect.INFINITE_CHARGES) {
                count--;
            }
        }else {
            player.removeOnDamageTakenEffect(this);
        }
    }

    @Override
    public void join(OnDamageTakenEffect onDamageTakenEffect) {
        ReflectDamage blockEffectReceived = (ReflectDamage)onDamageTakenEffect;
        if(((ReflectDamage) onDamageTakenEffect).count != OnDamageTakenEffect.INFINITE_CHARGES) {
            count += blockEffectReceived.count / 2;
        }else {
            count = onDamageTakenEffect.INFINITE_CHARGES;
        }
    }
}
