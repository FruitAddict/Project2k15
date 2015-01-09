package com.fruit.game.visual.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.fruit.game.utilities.TweenableValues;
/**
 * @Author FruitAddict
 */
public class TweenableValuesAccessor implements TweenAccessor<TweenableValues> {
    public static final int X_VALUE = 1;
    public static final int Y_VALUE = 2;
    public static final int Z_VALUE = 3;
    public static final int ALPHA_VALUE = 4;

    @Override
    public int getValues(TweenableValues tweenableValues, int i, float[] floats) {
        switch(i){
            case X_VALUE:{
                floats[0] = tweenableValues.getX();
                return 1;
            }
            case Y_VALUE:{
                floats[0] = tweenableValues.getY();
                return 1;
            }
            case Z_VALUE:{
                floats[0] = tweenableValues.getZ();
                return 1;
            }
            case ALPHA_VALUE:{
                floats[0] = tweenableValues.getAlpha();
                return 1;
            }
            default: {
                return -1;
            }
        }
    }

    @Override
    public void setValues(TweenableValues tweenableValues, int i, float[] floats) {
        switch(i){
            case X_VALUE:{
                tweenableValues.setX(floats[0]);
                break;
            }
            case Y_VALUE:{
                tweenableValues.setY(floats[0]);
                break;
            }
            case Z_VALUE:{
                tweenableValues.setZ(floats[0]);
                break;
            }
            case ALPHA_VALUE:{
                tweenableValues.setAlpha(floats[0]);
                break;
            }
            default: {
                break;
            }
        }
    }
}
