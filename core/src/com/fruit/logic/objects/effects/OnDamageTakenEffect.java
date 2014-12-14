package com.fruit.logic.objects.effects;

import com.fruit.logic.objects.Value;

/**
 * @Author FruitAddict
 */
public abstract class OnDamageTakenEffect {
    public final static int BLOCK_ATTACKS = 1;
    //effect id
    private int effectID;
    //what happens when damage is taken.
    public abstract void onDamageTaken(Value value);

    public int getEffectID() {
        return effectID;
    }

    public void setEffectID(int effectID) {
        this.effectID = effectID;
    }
}
