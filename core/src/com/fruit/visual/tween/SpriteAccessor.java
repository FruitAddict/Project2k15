package com.fruit.visual.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
@SuppressWarnings("all")
public class SpriteAccessor implements TweenAccessor<Sprite> {

    public static final int ALPHA = 0;
    public static final int POSITION_X = 1;
    public static final int POSITION_Y = 2;

    @Override
    public int getValues(Sprite sprite, int i, float[] floats) {
        switch(i){
            case ALPHA: {
                floats[0] = sprite.getColor().a;
                return 1;
            }
            case POSITION_X:{
                floats[0] = sprite.getX();
                return 2;
            }
            case POSITION_Y:{
                floats[0] = sprite.getY();
                return 3;
            }
            default: {
                assert false : "Wrong value returned at "+getClass().getName();
                return -1;
            }
        }
    }

    @Override
    public void setValues(Sprite sprite, int i, float[] floats) {
        switch(i){
            case ALPHA:{
                sprite.setColor(sprite.getColor().r,sprite.getColor().g,sprite.getColor().b,floats[0]);
                break;
            }
            case POSITION_X:{
                sprite.setPosition(floats[0],sprite.getY());
                break;
            }
            case POSITION_Y:{
                sprite.setPosition(sprite.getX(),floats[0]);
                break;
            }
            default:
                assert false: "Wrong value set at "+getClass().getName();
                break;
        }
    }
}