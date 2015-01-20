package com.fruit.game.logic.objects.effects;


import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.entities.Character;

/**
 * @Author FruitAddict
 * Those effects can generally trigger everything they want, including adding other effects to the character.
 */
public abstract class OnDamageTakenEffect {
    public static final int INFINITE_CHARGES = -1;
    public final static int BLOCK_ATTACKS = 1;
    public final static int REFLECT_DAMAGE = 2;
    //effect id
    private int effectID;
    //what happens when damage is taken, takes source of damage (must check for null if the source
    //doesnt exist anymore (for example a mob left a dot on player, and died afterwards),
    //modifiable value that should be changed and original value to lookup (like for reflect, we dont know
    //if the value passed will be zeroed out by another effect at the moment it takes it)
    public abstract void onDamageTaken(Character source, Value modifiable, Value original);

    public int getEffectID() {
        return effectID;
    }

    public void setEffectID(int effectID) {
        this.effectID = effectID;
    }

    public abstract void join(OnDamageTakenEffect onDamageTakenEffect);
}
