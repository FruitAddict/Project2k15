package com.fruit.game.visual.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.fruit.game.visual.ui.MessageHandler;
/**
 * @Author FruitAddict
 */
public class OnScreenMessageAccessor implements TweenAccessor<MessageHandler.OnScreenMessage> {
    public static final int POSITION_X = 0;
    public static final int POSITION_Y = 1;
    public static final int ALPHA = 2;
    public static final int SCALE = 3;

    @Override
    public int getValues(MessageHandler.OnScreenMessage textMessage, int i, float[] floats) {
        switch(i){
            case POSITION_X:{
                floats[0] = textMessage.getPosX();
                return 1;
            }
            case POSITION_Y:{
                floats[0] = textMessage.getPosY();
                return 1;
            }
            case ALPHA: {
                floats[0] = textMessage.getAlpha();
                return 1;
            }
            case SCALE:{
                floats[0] = textMessage.getScale();
                return 1;
            }
            default: {
                return -1;
            }
        }
    }

    @Override
    public void setValues(MessageHandler.OnScreenMessage textMessage, int i, float[] floats) {
        switch(i){
            case POSITION_X:{
                textMessage.setPosX(floats[0]);
                break;
            }
            case POSITION_Y:{
                textMessage.setPosY(floats[0]);
                break;
            }
            case ALPHA:{
                textMessage.setAlpha(floats[0]);
                break;
            }
            case SCALE:{
                textMessage.setScale(floats[0]);
                break;
            }
            default:
                break;
        }
    }
}
