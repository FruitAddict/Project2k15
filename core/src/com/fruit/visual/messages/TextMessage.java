package com.fruit.visual.messages;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Quad;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fruit.visual.tween.TextMessageAccessor;
import com.fruit.visual.tween.TweenUtils;

/**
 * Text message.
 * On creation it starts the basic tween (go up 2/3 of the lifespan, then go down and set the alpha to 0)
 * Contains many overloaded constructors for different situations.
 */
public class TextMessage {
    //tween types
    public static final int UP_AND_FALL = 1;
    public static final int UP = 2;

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

    public TextMessage(String msg,float positionX, float positionY, float lifeSpan, BitmapFont bitmapFont,int tweenType) {
        this.message = msg;
        this.positionX = positionX;
        this.positionY = positionY;
        this.lifeSpan = lifeSpan;
        this.bitmapFont = bitmapFont;
        alpha = bitmapFont.getColor().a;
        startTween(tweenType);
    }

    public TextMessage(String msg,float positionX, float positionY, float lifeSpan,int tweenType){
        this.message = msg;
        this.positionX = positionX;
        this.positionY = positionY;
        this.lifeSpan = lifeSpan;
        bitmapFont = TextRenderer.redFont;
        startTween(tweenType);
    }

    public void startTween(int type){
        switch(type) {
            //starts the tweening sequence
            case UP_AND_FALL: {
                Timeline.createSequence()
                        .push(Tween.set(this, TextMessageAccessor.ALPHA).target(0.7f))
                        .push(Tween.to(this, TextMessageAccessor.POSITION_Y, lifeSpan * 2 / 3).target(positionY + 75).ease(Quad.INOUT))
                        .beginParallel()
                        .push(Tween.to(this, TextMessageAccessor.POSITION_Y, lifeSpan * 1 / 3).target(positionY).ease(Quad.INOUT))
                        .push(Tween.to(this, TextMessageAccessor.ALPHA, lifeSpan * 1 / 3).target(0f))
                        .end()
                        .start(TweenUtils.tweenManager);
                break;
            }
            case UP: {
                Timeline.createSequence()
                        .push(Tween.set(this, TextMessageAccessor.ALPHA).target(0.7f))
                        .beginParallel()
                        .push(Tween.to(this, TextMessageAccessor.POSITION_Y, lifeSpan).target(positionY + 75).ease(Quad.INOUT))
                        .push(Tween.to(this, TextMessageAccessor.ALPHA, lifeSpan).target(0f))
                        .end()
                        .start(TweenUtils.tweenManager);
                break;
            }
        }

    }

    public void render(SpriteBatch batch, float delta){
        //renders this message with respect to this messages own alpha value.
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
