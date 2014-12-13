package com.fruit.logic.objects.entities.misc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.effects.OnHitEffect;
import com.fruit.logic.objects.entities.Enemy;
import com.fruit.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class PlayerProjectile extends Projectile {

    //Player-created projectiles should have a reference
    private Array<OnHitEffect> onHitEffects;

    public PlayerProjectile(Player player, ObjectManager objectManager, float spawnX, float spawnY, Vector2 dir) {
        super(objectManager, spawnX, spawnY, dir);
        onHitEffects = player.getOnHitEffects();
        damage = player.stats.getPlayerDamage();
    }

    public void onHit(Enemy enemy){
        killYourself();
        enemy.onDamageTaken(damage);
    }

}
