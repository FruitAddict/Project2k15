package com.fruit.visual.messages;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Quad;
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
    //alpha of this message
    private float alpha = 1;

    //position of this message;
    private float positionX;
    private float positionY;

    public TextMessage(String msg,float positionX, float positionY, float lifeSpan, BitmapFont bitmapFont) {
        this.message = msg;
        this.positionX = positionX;
        this.positionY = positionY;
        this.lifeSpan = lifeSpan;
        this.bitmapFont = bitmapFont;
        alpha = bitmapFont.getColor().a;
        startTween();
    }

    public TextMessage(String msg,float positionX, float positionY, float lifeSpan, int bitmapFontType){
        this.message = msg;
        this.positionX = positionX;
        this.positionY = positionY;
        this.lifeSpan = lifeSpan;
        switch(bitmapFontType){
            case TextMessage.BITMAPFONT_HELVETICA:{
                bitmapFont = Assets.redFont;
                break;
            }
            default:{
                bitmapFont = Assets.redFont;
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
        bitmapFont = Assets.redFont;
        startTween();
    }

    public void startTween(){
        Timeline.createSequence()
                .push(Tween.to(this, TextMessageAccessor.POSITION_Y,lifeSpan*2/3).target(positionY+75).ease(Quad.INOUT))
                .beginParallel()
                .push(Tween.to(this, TextMessageAccessor.POSITION_Y,lifeSpan*1/3).target(positionY))
                .push(Tween.to(this,TextMessageAccessor.ALPHA,lifeSpan*1/3).target(0f))
                .end()
                .start(TweenUtils.tweenManager);

    }

    public void render(SpriteBatch batch, float delta){
        bitmapFont.setColor(bitmapFont.getColor().r,bitmapFont.getColor().g,bitmapFont.getColor().b,alpha);
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

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
