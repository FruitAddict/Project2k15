package com.fruit.logic.objects.effects.onhit;

import com.fruit.Controller;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.effects.OnHitEffect;
import com.fruit.logic.objects.effects.passive.DamageOverTime;
import com.fruit.logic.objects.entities.Enemy;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.visual.Assets;
import com.fruit.visual.messages.TextMessage;

/**
 * @Author FruitAddict
 * TODO textrenderer fonts
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
                    enemy.getBody().getPosition().y * PIXELS_TO_METERS, 1.5f, Assets.redFont));
            poisonCount--;
        }else {
            player.removeOnHitEffect(this);
        }
    }
}