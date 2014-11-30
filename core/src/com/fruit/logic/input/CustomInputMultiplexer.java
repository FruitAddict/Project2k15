package com.fruit.logic.input;


import com.badlogic.gdx.InputMultiplexer;
import com.fruit.visual.ui.UserInterface;

/**
 * Customized input multiplexer to handle input updaters.
 */
public class CustomInputMultiplexer extends InputMultiplexer {

    private WorldInputProcessor processor;
    public CustomInputMultiplexer(UserInterface userInterface, WorldInputProcessor processor){
        super();
        this.processor = processor;
        addProcessor(userInterface);
        addProcessor(processor);
    }

    public void updateInput() {
        processor.update();
    }
}
