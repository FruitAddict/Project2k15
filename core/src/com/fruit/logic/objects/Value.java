package com.fruit.logic.objects;

/**
 * @Author FruitAddict
 * Values needs to be passed around as an object very often, as java doesn't allow passing primitives by reference.
 */
public class Value {

    private float value;

    public Value(float value){
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
