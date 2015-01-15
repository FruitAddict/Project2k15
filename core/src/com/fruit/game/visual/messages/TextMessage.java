package com.fruit.game.visual.messages;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Quad;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.utilities.TweenableValues;
import com.fruit.game.utilities.Utils;
import com.fruit.game.visual.tween.TextMessageAccessor;
import com.fruit.game.visual.tween.TweenUtils;
import com.fruit.game.visual.tween.TweenableValuesAccessor;
import org.w3c.dom.Text;


/**
 * Text message.
 * On creation it starts the basic tween (go up 2/3 of the lifespan, then go down and set the alpha to 0)
 * Contains many overloaded constructors for different situations.
 * TODO make them poolable and vector resetting
 */
public class TextMessage implements Constants, Pool.Poolable {
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
    private GameObject parentObject;
    private Vector2 parentPosition;
    //parent height for dynamic tweens
    private float parentHeight;
    //tweenable values to be used as offset in dynamic messages
    private TweenableValues tweenableValues;
    //left or right tween alignment for dynamic msgs
    private float tweenSign;

    //position of this message;
    private float positionX;
    private float positionY;

    public TextMessage(){
        parentPosition = new Vector2();
        tweenableValues = new TweenableValues();
        bitmapFont = TextRenderer.redFont;
        tweenSign = Math.signum(Utils.randomGenerator.nextInt());
    }

    public void startTween(){
        switch(tweenType) {
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
                        .push(Tween.set(tweenableValues, TweenableValuesAccessor.X_VALUE).target(0f))
                        .push(Tween.set(tweenableValues, TweenableValuesAccessor.Y_VALUE).target(0f))
                        .push(Tween.set(tweenableValues, TweenableValuesAccessor.Z_VALUE).target(90))
                        .push(Tween.to(tweenableValues, TweenableValuesAccessor.Y_VALUE, lifeSpan * 2 / 3).target(75).ease(Quad.INOUT))
                        .push(Tween.to(tweenableValues,TweenableValuesAccessor.Z_VALUE,lifeSpan*2/3).target(0).ease(Quad.INOUT))
                        .push(Tween.to(tweenableValues, TweenableValuesAccessor.X_VALUE, lifeSpan * 2 / 3).target(50).ease(Quad.INOUT))
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
                        .push(Tween.set(tweenableValues, TweenableValuesAccessor.Y_VALUE).target(0f))
                        .push(Tween.set(tweenableValues, TweenableValuesAccessor.Z_VALUE).target(90))
                        .push(Tween.set(tweenableValues, TweenableValuesAccessor.X_VALUE).target(0f))
                        .beginParallel()
                        .push(Tween.to(tweenableValues, TweenableValuesAccessor.Y_VALUE, lifeSpan).target(75).ease(Quad.INOUT))
                        .push(Tween.to(tweenableValues, TweenableValuesAccessor.Z_VALUE, lifeSpan).target(0).ease(Quad.INOUT))
                        .push(Tween.to(tweenableValues, TweenableValuesAccessor.X_VALUE, lifeSpan).target(50).ease(Quad.INOUT))
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
            float x,y;
            if(parentObject!=null) {
                x = parentObject.getBody().getPosition().x * PIXELS_TO_UNITS;
                y = parentObject.getBody().getPosition().y * PIXELS_TO_UNITS;
            }else {
                x = parentPosition.x*PIXELS_TO_UNITS;
                y= parentPosition.y*PIXELS_TO_UNITS;
            }
            bitmapFont.setColor(bitmapFont.getColor().r, bitmapFont.getColor().g, bitmapFont.getColor().b, tweenableValues.getAlpha());
            bitmapFont.draw(batch, message,(x)+(tweenSign*tweenableValues.getX()* MathUtils.cosDeg(tweenableValues.getZ())),
                    (y)+parentHeight+(tweenableValues.getY()* MathUtils.sinDeg(tweenableValues.getZ())));
        }
        stateTime+=delta;
    }

    @Override
    public void reset() {
        TweenUtils.tweenManager.killTarget(tweenableValues);
        setAlpha(0);
        setPositionX(-100);
        setPositionY(-100);
        stateTime = 0;
        tweenableValues.setAlpha(0);
        tweenableValues.setX(0);
        tweenableValues.setY(0);
        tweenableValues.setZ(0);
        getParentPosition().set(0,0);
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector2 getParentPosition() {
        return parentPosition;
    }

    public void setParentPosition(Vector2 parentPosition) {
        this.parentPosition = parentPosition;
    }

    public float getParentHeight() {
        return parentHeight;
    }

    public void setParentHeight(float parentHeight) {
        this.parentHeight = parentHeight;
    }

    public TweenableValues getTweenableValues() {
        return tweenableValues;
    }

    public void setTweenableValues(TweenableValues tweenableValues) {
        this.tweenableValues = tweenableValues;
    }
    public int getTweenType() {
        return tweenType;
    }

    public void setTweenType(int tweenType) {
        this.tweenType = tweenType;
    }

    public void setLifeSpan(float lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public BitmapFont getBitmapFont() {
        return bitmapFont;
    }

    public void setBitmapFont(BitmapFont bitmapFont) {
        this.bitmapFont = bitmapFont;
    }

    public GameObject getParentObject() {
        return parentObject;
    }

    public void setParentObject(GameObject parentObject) {
        this.parentObject = parentObject;
    }
}
