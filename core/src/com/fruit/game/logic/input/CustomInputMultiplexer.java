package com.fruit.game.logic.input;


import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.fruit.game.Configuration;
import com.fruit.game.visual.ui.UserInterface;

/**
 * Customized input multiplexer to handle input updaters.
 */
public class CustomInputMultiplexer extends InputMultiplexer {

    private WorldInputProcessor processor;
    private CustomGestureProcessor listener;
    private boolean joyStickEnabled;

    public CustomInputMultiplexer(UserInterface userInterface, WorldInputProcessor processor){
        super();
        this.processor = processor;
        addProcessor(userInterface);
        addProcessor(processor);
    }
    public CustomInputMultiplexer(UserInterface userInterface, WorldInputProcessor processor,CustomGestureProcessor gestureListener){
        super();
        this.processor = processor;
        addProcessor(userInterface);
        joyStickEnabled = Configuration.joyStickSteeringEnabled;
        GestureDetector gestureDetector = new GestureDetector(gestureListener);
        gestureDetector.setTapSquareSize(10);
        if(!joyStickEnabled){
            this.listener = gestureListener;
            listener.setParent(gestureDetector);
            addProcessor(gestureDetector);
        }
        addProcessor(processor);
    }

    public void updateInput() {
        processor.update();
        if(!joyStickEnabled) {
            listener.update();
        }
    }
}
