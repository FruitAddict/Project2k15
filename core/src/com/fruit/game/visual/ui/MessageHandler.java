package com.fruit.game.visual.ui;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Quad;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.fruit.game.Controller;
import com.fruit.game.visual.tween.OnScreenMessageAccessor;
import com.fruit.game.visual.tween.TweenUtils;
import pl.kotcrab.vis.ui.widget.VisLabel;

import java.util.PriorityQueue;

public class MessageHandler {
        private VisLabel messageLabel;
        private PriorityQueue<OnScreenMessage> messageQueue;
        private Stage stage;
        private float messageTimer;
        private OnScreenMessage currentMsg;

        public MessageHandler(Stage stage){
            Tween.registerAccessor(OnScreenMessage.class, new OnScreenMessageAccessor());
            messageLabel = new VisLabel();
            messageQueue = new PriorityQueue<>();
            messageLabel.setAlignment(Align.bottom);
            messageLabel.setFillParent(true);
            stage.addActor(messageLabel);
            this.stage = stage;
        }

        public void update(float delta){
            if(currentMsg != null && messageTimer > currentMsg.time){
                if(!messageQueue.isEmpty()){
                    System.out.println("setting new message");
                    currentMsg = messageQueue.poll();
                    messageLabel.setText(currentMsg.message);
                    messageTimer = 0;
                    startTween();
                }else {
                    currentMsg = null;
                    messageLabel.setText("");
                    messageTimer = 0;
                }
            }else if(currentMsg == null && !messageQueue.isEmpty()){
                System.out.println("setting new message");
                currentMsg = messageQueue.poll();
                messageLabel.setText(currentMsg.message);
                messageTimer = 0;
                startTween();
            }
            if(currentMsg!=null){
                messageTimer+=delta;
                messageLabel.setColor(currentMsg.getColor().r,currentMsg.getColor().g,currentMsg.getColor().b,currentMsg.getAlpha());
                messageLabel.setPosition(messageLabel.getX(),currentMsg.posY);
                messageLabel.setFontScale(currentMsg.getScale());
            }
        }

        public void startTween() {
                //starts the tweening sequence
            Timeline.createSequence()
                    .push(Tween.set(currentMsg, OnScreenMessageAccessor.ALPHA).target(0f))
                    .push(Tween.set(currentMsg, OnScreenMessageAccessor.POSITION_Y).target(currentMsg.getPosY()))
                    .push(Tween.set(currentMsg,OnScreenMessageAccessor.SCALE).target(0.9f))
                    .beginParallel()
                    .push(Tween.to(currentMsg, OnScreenMessageAccessor.POSITION_Y, currentMsg.time * 2 / 3).target(currentMsg.getPosY() + 35).ease(Quad.INOUT))
                    .push(Tween.to(currentMsg, OnScreenMessageAccessor.ALPHA, currentMsg.time * 2 / 3).target(1f))
                    .push(Tween.to(currentMsg, OnScreenMessageAccessor.SCALE, currentMsg.time * 2 / 3).target(1f))
                    .end()
                    .beginParallel()
                    .push(Tween.to(currentMsg, OnScreenMessageAccessor.POSITION_Y, currentMsg.time * 1 / 3).target(currentMsg.getPosY()).ease(Quad.INOUT))
                    .push(Tween.to(currentMsg, OnScreenMessageAccessor.ALPHA, currentMsg.time * 1 / 3).target(0f))
                    .push(Tween.to(currentMsg, OnScreenMessageAccessor.SCALE, currentMsg.time * 1 / 3).target(0.9f))
                    .end()
                    .start(TweenUtils.tweenManager);
        }


        public void addMessage(String message, Color color, float lifespan){
            messageQueue.offer(new OnScreenMessage(message,color,lifespan));
        }

        public class OnScreenMessage implements Comparable<OnScreenMessage>{
            private String message;
            private Color color;
            private float time;
            private float alpha;
            private float posX, posY;
            private int priority;
            private float scale;

            public OnScreenMessage(String msg, Color color, float time, int priority){
                this.message = msg;
                this.color = color;
                this.time = time;
                this.priority = priority;
                posY = Controller.getUserInterface().getViewport().getScreenY()/3f;
                scale = 1;
            }

            public OnScreenMessage(String msg, Color color, float time){
                this.message = msg;
                this.color = color;
                this.time = time;
                this.priority = 1;
                scale = 1;
                posY = Controller.getUserInterface().getViewport().getScreenY()/3f;
                System.out.println(Controller.getUserInterface().getHeight() / 3f);
                System.out.println(Controller.getUserInterface().getHeight() / 2f);
                System.out.println(Controller.getUserInterface().getHeight());
            }

            public float getAlpha() {
                return alpha;
            }

            public void setAlpha(float alpha) {
                this.alpha = alpha;
            }

            public float getPosX() {
                return posX;
            }

            public void setPosX(float posX) {
                this.posX = posX;
            }

            public float getPosY() {
                return posY;
            }

            public void setPosY(float posY) {
                this.posY = posY;
            }

            public float getScale() {
                return scale;
            }

            public void setScale(float scale) {
                this.scale = scale;
            }

            @Override
            public int compareTo(OnScreenMessage another) {
                if(priority > another.priority){
                    return 1;
                }
                else if( priority == another.priority){
                    return 0;
                } else {
                    return -1;
                }
            }

            public Color getColor() {
                return color;
            }
        }

    }