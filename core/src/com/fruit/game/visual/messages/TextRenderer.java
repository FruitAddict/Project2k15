package com.fruit.game.visual.messages;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.fruit.game.Configuration;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.utilities.TweenableValues;
import com.fruit.game.visual.tween.TextMessageAccessor;
import com.fruit.game.visual.tween.TweenableValuesAccessor;

/**
 * Text renderer. Takes care of rendering scrolling battle text, boss-taunts
 * and other texts on the screen that should be part of the world and not the GUI.
 */
public class TextRenderer implements Constants {
    //list of text messages that are currently processed
    private Array<TextMessage> messageList;
    //Pool for text message objects to be reused
    private Pool<TextMessage> messagePool;
    //available fonts
    public static BitmapFont redFont;
    public static BitmapFont greenFont;
    public static BitmapFont goldenFont;
    public static BitmapFont poisonGreenFont;

    static{
        //fonts
        redFont = new BitmapFont(Gdx.files.internal("fonts//souls.fnt"));
        redFont.setScale(0.7f, 0.7f);
        redFont.setColor(1.0f, 0.1f, 0.1f, 0.9f);

        greenFont = new BitmapFont(Gdx.files.internal("fonts//souls.fnt"));
        greenFont.setScale(0.7f, 0.7f);;
        greenFont.setColor(0.1f,1f,0.1f,0.9f);

        goldenFont = new BitmapFont(Gdx.files.internal("fonts//souls.fnt"));
        goldenFont.setScale(0.7f, 0.7f);
        goldenFont.setColor(1,215/255f,0f,0.9f);

        poisonGreenFont = new BitmapFont(Gdx.files.internal("fonts//souls.fnt"));
        poisonGreenFont.setScale(0.7f, 0.7f);
        poisonGreenFont.setColor(new Color(199 / 255f, 228 / 255f, 118 / 255f, 0.9f));
    }

    public TextRenderer(){
        messageList = new Array<>();
        messagePool = new Pool<TextMessage>(10,60) {
            @Override
            protected TextMessage newObject() {
                return new TextMessage();
            }
        };
        //TWEENS - register new accessors for static and dynamic text messages
        Tween.registerAccessor(TextMessage.class, new TextMessageAccessor());
        Tween.registerAccessor(TweenableValues.class, new TweenableValuesAccessor());
    }

    public void render(SpriteBatch batch, float delta){
        //System.out.println(messageList.size+" "+messagePool.getFree());
        //check if the messages should disappear
        for(int i =0 ; i < messageList.size;i++){
            //if the message statetime exceeds its lifespan, remove it
            if(messageList.get(i).getStateTime() > messageList.get(i).getLifeSpan()){
                messagePool.free(messageList.get(i));
                messageList.removeIndex(i);
            }
        }
        //draw all the messages.
        batch.begin();
        for(TextMessage tm : messageList){
            tm.render(batch,delta);
            //delta is passed to increase the state time in the message, all the
            //movement is handled by tween manager.
        }
        batch.end();
    }

    public void addMessage(String msg,float positionX, float positionY, float lifeSpan){
        //only accepts new messages if battle text is enabled in config
        if(Configuration.battleTextEnabled) {
            TextMessage mssg = messagePool.obtain();
            mssg.setMessage(msg);
            mssg.setPositionX(positionX);
            mssg.setPositionY(positionY);
            mssg.setLifeSpan(lifeSpan);
            mssg.setTweenType(TextMessage.FIXED_POINT_UP);
            mssg.startTween();
            messageList.add(mssg);
        }
    }
    public void addMessage(String msg,float positionX, float positionY, float lifeSpan, BitmapFont font, int tweenType){
        //only accepts new messages if battle text is enabled in config
        if(Configuration.battleTextEnabled) {
            TextMessage mssg = messagePool.obtain();
            mssg.setMessage(msg);
            mssg.setPositionX(positionX);
            mssg.setPositionY(positionY);
            mssg.setLifeSpan(lifeSpan);
            mssg.setBitmapFont(font);
            mssg.setTweenType(tweenType);
            mssg.startTween();
            messageList.add(mssg);
        }
    }

    /**
     * Dynamic message that needs to know what is its parent
     */
    public void addMessage(GameObject owner,String message, Vector2 parentPosition, float parentHeight, float lifeTime, BitmapFont font, int tweenType){
        //only accepts new messages if battle text is enabled in config
        if(Configuration.battleTextEnabled) {
            TextMessage msg = messagePool.obtain();
            msg.setMessage(message);
            msg.setParentPosition(parentPosition);
            msg.setLifeSpan(lifeTime);
            msg.setParentHeight(parentHeight);
            msg.setBitmapFont(font);
            msg.setTweenType(tweenType);
            msg.setParentObject(owner);
            msg.startTween();
            messageList.add(msg);
        }
    }

    public void removeAll(){
        messagePool.freeAll(messageList);
        messageList.clear();
    }

    public static void disposeAllFonts(){
        redFont.dispose();
        greenFont.dispose();
        goldenFont.dispose();
        poisonGreenFont.dispose(); //todo merge with assets
    }

    //removes dynamic messages by its owner
    public void removeMessageByOwner(GameObject o){
        for(int i =0 ;i<messageList.size;i++){
            if(messageList.get(i).getParentObject()==o){
                System.out.println("static upping msg");
                messageList.get(i).setParentPosition(o.getBody().getPosition().cpy());
                messageList.get(i).setParentObject(null);
            }
        }
    }

    public static void reloadFonts() {
        redFont.dispose();
        redFont = new BitmapFont(Gdx.files.internal("fonts//souls.fnt"));
        redFont.setScale(0.7f, 0.7f);
        redFont.setColor(1.0f, 0.1f, 0.1f, 0.9f);

        greenFont.dispose();
        greenFont = new BitmapFont(Gdx.files.internal("fonts//souls.fnt"));
        greenFont.setScale(0.7f, 0.7f);;
        greenFont.setColor(0.1f,1f,0.1f,0.9f);

        goldenFont.dispose();
        goldenFont = new BitmapFont(Gdx.files.internal("fonts//souls.fnt"));
        goldenFont.setScale(0.7f, 0.7f);
        goldenFont.setColor(1,215/255f,0f,0.9f);

        poisonGreenFont.dispose();
        poisonGreenFont = new BitmapFont(Gdx.files.internal("fonts//souls.fnt"));
        poisonGreenFont.setScale(0.7f, 0.7f);
        poisonGreenFont.setColor(new Color(199 / 255f, 228 / 255f, 118 / 255f, 0.9f));
    }
}
