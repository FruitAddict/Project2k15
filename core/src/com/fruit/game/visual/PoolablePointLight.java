package com.fruit.game.visual;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.fruit.game.logic.objects.entities.GameObject;

/**
 * @Author FruitAddict
 * Same as point light, but implementing Poolable interface as creating new lights constantly is costly.
 * Disabled lights have no resource cost.
 */
public class PoolablePointLight extends PointLight implements Poolable {
    private GameObject parentObject;

    /**
     * @param rayHandler
     * @param rays
     * Both are passed into 2-arg constructor of the super class.
     * (It's the minimal number of args required by it)
     */
    public PoolablePointLight(RayHandler rayHandler, int rays){
        super(rayHandler,rays);
    }
    @Override
    public void reset() {
        setActive(false);
        setStaticLight(false);
        setDistance(0);
        setParentObject(null);
    }

    public GameObject getParentObject() {
        return parentObject;
    }

    public void setParentObject(GameObject parentObject) {
        this.parentObject = parentObject;
    }
}
