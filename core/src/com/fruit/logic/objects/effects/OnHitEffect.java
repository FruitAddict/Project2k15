package com.fruit.logic.objects.effects;

import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.entities.Enemy;

/**t
 * @Author FruitAddict
 * Those effects can generally trigger everything they want, including adding other effects to the character.
 */
public abstract class OnHitEffect {
    //types of OnHitEffect
    public static int SPAWN_OBJECT_ON_HIT = 1;
    //instance effect id
    private int effectID;

    //what happens when enemy is hit
    public abstract void onHit(Enemy enemy, Value damage);

    public abstract void join(OnHitEffect onHitEffect);

    public int getEffectID() {
        return effectID;
    }

    public void setEffectID(int effectID) {
        this.effectID = effectID;
    }
}
