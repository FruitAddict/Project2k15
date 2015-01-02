package com.fruit.game.visual;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * @Author FruitAddict
 * Same as point light, but implementing Poolable interface as creating new lights constantly is costly.
 * Disabled lights have no resource cost.
 */
public class PoolablePointLight extends PointLight implements Poolable {

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
    }
}
