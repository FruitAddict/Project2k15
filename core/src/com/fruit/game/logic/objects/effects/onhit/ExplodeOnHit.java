package com.fruit.game.logic.objects.effects.onhit;


import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.effects.OnHitEffect;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.Projectile;
import com.fruit.game.logic.objects.entities.misc.Explosion;
import com.fruit.game.logic.objects.entities.player.Player;

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
    public void onHit(Projectile proj, Character enemy, Value damage) {
        if(explosionCount >0) {
            player.getObjectManager().addObject(new Explosion(player.getObjectManager(),enemy.getBody().getPosition().x,enemy.getBody().getPosition().y,1f,0.5f,2));
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
