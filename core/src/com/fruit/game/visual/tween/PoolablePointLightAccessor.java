package com.fruit.game.visual.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.fruit.game.visual.PoolablePointLight;

/**
 * @Author FruitAddict
 */
public class PoolablePointLightAccessor implements TweenAccessor<PoolablePointLight> {

    public static final int DISTANCE = 1;

    @Override
    public int getValues(PoolablePointLight poolablePointLight, int i, float[] floats) {
        switch(i){
            case DISTANCE:{
                floats[0] = poolablePointLight.getDistance();
                return 1;
            }
            default: {
                return -1;
            }

        }
    }

    @Override
    public void setValues(PoolablePointLight poolablePointLight, int i, float[] floats) {
        switch(i){
            case DISTANCE:{
                poolablePointLight.setDistance(floats[0]);
                break;
            }
        }
    }
}
