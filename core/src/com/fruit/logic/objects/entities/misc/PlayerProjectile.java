package com.fruit.logic.objects.entities.misc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.effects.OnHitEffect;
import com.fruit.logic.objects.entities.Enemy;
import com.fruit.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class PlayerProjectile extends Projectile {

    //Player-created projectiles should have a reference to player's on hit effect list on their creation
    private Array<OnHitEffect> onHitEffects;

    public PlayerProjectile(Player player, ObjectManager objectManager, float spawnX, float spawnY, Vector2 dir, float velocity) {
        super(objectManager, spawnX, spawnY, dir, velocity);
        onHitEffects = player.getOnHitEffects();
        damage.setValue(player.stats.getCombinedDamage());
    }

    public void onHit(Enemy enemy){
        killYourself();
        for(OnHitEffect onHitEffect : onHitEffects ){
            onHitEffect.onHit(enemy,damage);
        }
        enemy.onDamageTaken(damage);
    }

}
