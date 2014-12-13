package com.fruit.logic.objects.effects;

import com.fruit.logic.objects.entities.Enemy;

/**t
 * @Author FruitAddict
 */
public abstract class OnHitEffect {
    //types of OnHitEffect
    public static int SPAWN_OBJECT_ON_HIT = 1;

    //what happens when enemy is hit
    public abstract void onHit(Enemy enemy);
}
