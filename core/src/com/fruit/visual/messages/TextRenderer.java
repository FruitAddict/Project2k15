package com.fruit.visual.messages;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.fruit.visual.tween.TextMessageAccessor;

public class TextRenderer {
    private Array<TextMessage> messageList;

    public TextRenderer(){
        messageList = new Array<>();
        //register new accessor
        Tween.registerAccessor(TextMessage.class, new TextMessageAccessor());
    }

    public void render(SpriteBatch batch, float delta){
        //check if the messages should disappear
        for(int i =0 ; i < messageList.size;i++){
            if(messageList.get(i).getStateTime() > messageList.get(i).getLifeSpan()){
                messageList.removeIndex(i);
            }
        }
        batch.begin();
        for(TextMessage tm : messageList){
            tm.render(batch,delta);
        }
        batch.end();
    }

    public void addMessage(String msg,float positionX, float positionY, float lifeSpan){
        messageList.add(new TextMessage(msg,positionX,positionY,lifeSpan));
    }

    public void addMessage(TextMessage message){
        messageList.add(message);
    }

    public void removeAll(){
        messageList.clear();
    }
}
