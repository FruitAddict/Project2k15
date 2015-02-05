package com.fruit.game.visual.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.fruit.game.visual.messages.TextMessage;

public class TextMessageAccessor implements TweenAccessor<TextMessage> {

    public static final int POSITION_X = 0;
    public static final int POSITION_Y = 1;
    public static final int ALPHA = 2;

    @Override
    public int getValues(TextMessage textMessage, int i, float[] floats) {
        switch(i){
            case POSITION_X:{
                floats[0] = textMessage.getPositionX();
                return 1;
            }
            case POSITION_Y:{
                floats[0] = textMessage.getPositionY();
                return 1;
            }
            case ALPHA: {
                floats[0] = textMessage.getAlpha();
                return 1;
            }
            default: {
                return -1;
            }
        }
    }

    @Override
    public void setValues(TextMessage textMessage, int i, float[] floats) {
        switch(i){
            case POSITION_X:{
                textMessage.setPositionX(floats[0]);
                break;
            }
            case POSITION_Y:{
                textMessage.setPositionY(floats[0]);
                break;
            }
            case ALPHA:{
                textMessage.setAlpha(floats[0]);
                break;
            }
            default:
                break;
        }
    }
}
