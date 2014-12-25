package com.fruit.logic.objects;

/**
 * @Author FruitAddict
 * Values needs to be passed around as an object very often, as java doesn't allow passing primitives by reference.
 * Used for damage/heal
 */
public class Value {

    public static final int NORMAL_DAMAGE = 1;
    public static final int BURNING_DAMAGE = 2;
    public static final int POISON_DAMAGE = 3;
    public static final int HEALING = 4;
    public static final int VELOCITY = 5;

    private float value;
    private int type;

    public Value(float value, int type){
        this.value = value;
        this.type = type;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getType(){
        return type;
    }
}
