package com.fruit.visual.messages;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.fruit.visual.tween.TextMessageAccessor;

/**
 * Text renderer. Takes care of rendering scrolling battle text, boss-taunts
 * and other texts on the screen that should be part of the world and not the GUI.
 */
public class TextRenderer {
    //list of text messages that are currently processed
    private Array<TextMessage> messageList;
    //available fonts
    public static BitmapFont redFont;
    public static BitmapFont greenFont;

    static{
        //fonts
        redFont = new BitmapFont();
        redFont.setScale(1.5f, 1f);
        redFont.setColor(1.0f, 0.1f, 0.1f, 0.9f);

        greenFont = new BitmapFont();
        greenFont.setScale(1.5f,1f);
        greenFont.setColor(0.1f,1f,0.1f,0.9f);
    }

    public TextRenderer(){
        messageList = new Array<>();
        //TWEENS - register new accessor for text messages
        Tween.registerAccessor(TextMessage.class, new TextMessageAccessor());
    }

    public void render(SpriteBatch batch, float delta){
        //check if the messages should disappear
        for(int i =0 ; i < messageList.size;i++){
            //if the message statetime exceeds its lifespan, remove it
            if(messageList.get(i).getStateTime() > messageList.get(i).getLifeSpan()){
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
        messageList.add(new TextMessage(msg,positionX,positionY,lifeSpan,1));
    }

    public void addMessage(TextMessage message){
        messageList.add(message);
    }

    public void removeAll(){
        messageList.clear();
    }
}
