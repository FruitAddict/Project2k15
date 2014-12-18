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
    private float poisonCount = 5;
    private Player player;

    public PoisonOnHit(Player player){
        this.player = player;
    }

    @Override
    public void onHit(Enemy enemy, Value damage) {
        if(poisonCount>0) {
            enemy.addPassiveEffect(new DamageOverTime(enemy, 5f, 0.5f, new Value(damage.getValue() / 8), DamageOverTime.POISONED));
            damage.setValue(0);
            Controller.addOnScreenMessage(new TextMessage("Poisoned!", enemy.getBody().getPosition().x * PIXELS_TO_METERS,
                    enemy.getBody().getPosition().y * PIXELS_TO_METERS, 1.5f, TextRenderer.redFont,TextMessage.UP_AND_FALL));
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
