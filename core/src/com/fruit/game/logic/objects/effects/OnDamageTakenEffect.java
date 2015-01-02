package com.fruit.game.logic.objects.effects;


import com.fruit.game.logic.objects.Value;

/**
 * @Author FruitAddict
 * Those effects can generally trigger everything they want, including adding other effects to the character.
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

    public abstract void join(OnDamageTakenEffect onDamageTakenEffect);
}
