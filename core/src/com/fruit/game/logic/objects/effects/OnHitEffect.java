package com.fruit.game.logic.objects.effects;

import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.Projectile;

/**t
 * @Author FruitAddict
 * Those effects can generally trigger everything they want, including adding other effects to the character.
 */
public abstract class OnHitEffect {
    //types of OnHitEffect
    public static final int SPAWN_OBJECT_ON_HIT = 1;
    public static final int POISON_ON_HIT = 2;
    public static final int EXPLODE_ON_HIT = 3;
    public static final int HEALTH_DRAIN_ON_HIT = 4;
    public static final int FORK_ON_HIT = 5;
    public static final int SET_ON_FIRE = 6;
    //instance effect id
    private int effectID;
    //owner
    protected Projectile projectile;

    //what happens when enemy is hit
    public abstract void onHit(Projectile proj,Character enemy, Value damage);

    public abstract void join(OnHitEffect onHitEffect);

    public int getEffectID() {
        return effectID;
    }

    public void setEffectID(int effectID) {
        this.effectID = effectID;
    }
}
