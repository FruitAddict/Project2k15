package com.fruit.game.visual.messages;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Quad;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.utilities.TweenableValues;
import com.fruit.game.visual.tween.TextMessageAccessor;
import com.fruit.game.visual.tween.TweenUtils;
import com.fruit.game.visual.tween.TweenableValuesAccessor;


/**
 * Text message.
 * On creation it starts the basic tween (go up 2/3 of the lifespan, then go down and set the alpha to 0)
 * Contains many overloaded constructors for different situations.
 * TODO make them poolable and vector resetting
 */
public class TextMessage implements Constants {
    //tween types
    public static final int FIXED_POINT_UPFALL = 1;
    public static final int FIXED_POINT_UP = 2;
    public static final int DYNAMIC_UPFALL = 3;
    public static final int DYNAMIC_UP = 4;

    //font of this message
    private BitmapFont bitmapFont;
    //tween type
    private int tweenType;
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
    //parent object position if its specified in the constructor ( only usable for dynamic tween types that follow
    //the parent object and use offset to move)
    private Vector2 parentPosition;
    //parent height for dynamic tweens
    private float parentHeight;
    //tweenable values to be used as offset in dynamic messages
    private TweenableValues tweenableValues;

    //position of this message;
    private float positionX;
    private float positionY;

    public TextMessage(String msg,float positionX, float positionY, float lifeSpan, BitmapFont bitmapFont,int tweenType) {
        //constructor for fixed-point tweened message
        this.message = msg;
        this.positionX = positionX;
        this.positionY = positionY;
        this.lifeSpan = lifeSpan;
        this.bitmapFont = bitmapFont;
        alpha = bitmapFont.getColor().a;
        startTween(tweenType);
        this.tweenType = tweenType;
    }

    public TextMessage(String msg,float positionX, float positionY, float lifeSpan,int tweenType){
        //fixed point tweened message with default font
        this.message = msg;
        this.positionX = positionX;
        this.positionY = positionY;
        this.lifeSpan = lifeSpan;
        bitmapFont = TextRenderer.redFont;
        startTween(tweenType);
        this.tweenType = tweenType;
    }

    public TextMessage(String msg, Vector2 parentPosition, float objectHeight, float lifeSpan, int tweenType){
        //dynamic tween following the game object with default red font
        this.message = msg;
        this.lifeSpan = lifeSpan;
        bitmapFont = TextRenderer.redFont;
        tweenableValues = new TweenableValues();
        this.parentPosition = parentPosition;
        this.tweenType = tweenType;
        this.parentHeight = objectHeight;
        startTween(tweenType);
    }

    public TextMessage(String msg, Vector2 parentPosition, float objectHeight, float lifeSpan, BitmapFont font, int tweenType){
        //dynamic tween following the game object
        this.message = msg;
        this.lifeSpan = lifeSpan;
        bitmapFont = font;
        tweenableValues = new TweenableValues();
        this.parentPosition = parentPosition;
        this.parentHeight = objectHeight;
        this.tweenType = tweenType;
        startTween(tweenType);
    }

    public void startTween(int type){
        switch(type) {
            //starts the tweening sequence
            case FIXED_POINT_UPFALL: {
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
            case FIXED_POINT_UP: {
                Timeline.createSequence()
                        .push(Tween.set(this, TextMessageAccessor.ALPHA).target(0.7f))
                        .beginParallel()
                        .push(Tween.to(this, TextMessageAccessor.POSITION_Y, lifeSpan).target(positionY + 75).ease(Quad.INOUT))
                        .push(Tween.to(this, TextMessageAccessor.ALPHA, lifeSpan).target(0f))
                        .end()
                        .start(TweenUtils.tweenManager);
                break;
            }
            case DYNAMIC_UPFALL: {
                Timeline.createSequence()
                        .push(Tween.set(tweenableValues, TweenableValuesAccessor.ALPHA_VALUE).target(0.7f))
                        .push(Tween.set(tweenableValues, TweenableValuesAccessor.Y_VALUE).target(0f))
                        .push(Tween.to(tweenableValues, TweenableValuesAccessor.Y_VALUE, lifeSpan * 2 / 3).target(75).ease(Quad.INOUT))
                        .beginParallel()
                        .push(Tween.to(tweenableValues, TweenableValuesAccessor.Y_VALUE, lifeSpan * 1 / 3).target(0).ease(Quad.INOUT))
                        .push(Tween.to(tweenableValues, TweenableValuesAccessor.ALPHA_VALUE, lifeSpan * 1 / 3).target(0f))
                        .end()
                        .start(TweenUtils.tweenManager);
                break;
            }
            case DYNAMIC_UP: {
                Timeline.createSequence()
                        .push(Tween.set(tweenableValues, TweenableValuesAccessor.ALPHA_VALUE).target(0.7f))
                        .push(Tween.set(tweenableValues,TweenableValuesAccessor.Y_VALUE).target(0f))
                        .beginParallel()
                        .push(Tween.to(tweenableValues,  TweenableValuesAccessor.Y_VALUE, lifeSpan).target(75).ease(Quad.INOUT))
                        .push(Tween.to(tweenableValues, TweenableValuesAccessor.ALPHA_VALUE, lifeSpan).target(0f))
                        .end()
                        .start(TweenUtils.tweenManager);
                break;
            }
        }

    }

    public void render(SpriteBatch batch, float delta){
        //renders this message with respect to this messages own alpha value.
        if(tweenType== FIXED_POINT_UP || tweenType == FIXED_POINT_UPFALL) {
            bitmapFont.setColor(bitmapFont.getColor().r, bitmapFont.getColor().g, bitmapFont.getColor().b, alpha);
            bitmapFont.draw(batch, message, positionX, positionY);
        }
        else if (tweenType == DYNAMIC_UP || tweenType == DYNAMIC_UPFALL){
            bitmapFont.setColor(bitmapFont.getColor().r, bitmapFont.getColor().g, bitmapFont.getColor().b, tweenableValues.getAlpha());
            bitmapFont.draw(batch, message, (parentPosition.x*PIXELS_TO_UNITS)+tweenableValues.getX(),
                    (parentPosition.y*PIXELS_TO_UNITS)+tweenableValues.getY()+parentHeight);
        }
        stateTime+=delta;
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
