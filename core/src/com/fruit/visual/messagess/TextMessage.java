package com.fruit.visual.messagess;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fruit.visual.Assets;
import com.fruit.visual.tween.TextMessageAccessor;
import com.fruit.visual.tween.TweenUtils;

public class TextMessage {
    //font types
    public static final int BITMAPFONT_HELVETICA = 1;

    //font of this message
    private BitmapFont bitmapFont;
    //lifespan max
    private float lifeSpan;
    //statetime
    private float stateTime =0;
    //content of this msg
    private String message;
    //color of this message;
    private Color color;

    //position of this message;
    private float positionX;
    private float positionY;

    public TextMessage(String msg,float positionX, float positionY, float lifeSpan, BitmapFont bitmapFont) {
        this.message = msg;
        this.positionX = positionX;
        this.positionY = positionY;
        this.lifeSpan = lifeSpan;
        this.bitmapFont = bitmapFont;
        startTween();
    }

    public TextMessage(String msg,float positionX, float positionY, float lifeSpan, int bitmapFontType){
        this.message = msg;
        this.positionX = positionX;
        this.positionY = positionY;
        this.lifeSpan = lifeSpan;
        switch(bitmapFontType){
            case TextMessage.BITMAPFONT_HELVETICA:{
                bitmapFont = Assets.bitmapFont;
                break;
            }
            default:{
                bitmapFont = Assets.bitmapFont;
                break;
            }
        }
        startTween();
    }

    public TextMessage(String msg,float positionX, float positionY, float lifeSpan){
        this.message = msg;
        this.positionX = positionX;
        this.positionY = positionY;
        this.lifeSpan = lifeSpan;
        bitmapFont = Assets.bitmapFont;
        startTween();
    }

    public void startTween(){
        Timeline.createSequence().push(
                Tween.to(this, TextMessageAccessor.POSITION_Y,lifeSpan*2/3).target(positionY+75)).push(
                Tween.to(this, TextMessageAccessor.POSITION_Y,lifeSpan*1/3).target(positionY)
        ).start(TweenUtils.tweenManager);
    }

    public void render(SpriteBatch batch, float delta){
        bitmapFont.draw(batch,message,positionX,positionY);
        stateTime+=delta;
    }

    public BitmapFont getBitmapFont() {
        return bitmapFont;
    }

    public float getLifeSpan() {
        return lifeSpan;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public float getStateTime() {
        return stateTime;
    }
}
