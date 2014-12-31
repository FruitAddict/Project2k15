package com.fruit.logic.objects.effects.onhit;

import com.fruit.logic.Constants;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.effects.OnHitEffect;
import com.fruit.logic.objects.entities.Enemy;
import com.fruit.logic.objects.entities.misc.Explosion;
import com.fruit.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class ExplodeOnHit extends OnHitEffect implements Constants {
    private int explosionCount;
    private Player player;

    public ExplodeOnHit(Player player, int charges){
        this.player = player;
        explosionCount = charges;
        setEffectID(OnHitEffect.EXPLODE_ON_HIT);
    }

    @Override
    public void onHit(Enemy enemy, Value damage) {
        if(explosionCount >0) {
            player.getObjectManager().addObject(new Explosion(player.getObjectManager(),enemy.getBody().getPosition().x,enemy.getBody().getPosition().y,1f,1f,2));
            explosionCount--;
        }else {
            player.removeOnHitEffect(this);
        }
    }

    @Override
    public void join(OnHitEffect onHitEffect) {
        ExplodeOnHit explodeEffectReceived = (ExplodeOnHit)onHitEffect;
        explosionCount +=explodeEffectReceived.explosionCount /2;
    }
}
