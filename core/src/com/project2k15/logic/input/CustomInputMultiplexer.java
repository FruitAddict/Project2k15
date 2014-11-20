package com.project2k15.logic.input;

import com.badlogic.gdx.InputMultiplexer;
import com.project2k15.rendering.ui.GuiStage;

/**
 * Customized input multiplexer to handle input updaters.
 */
public class CustomInputMultiplexer extends InputMultiplexer {

    private WorldInputProcessor processor;
    public CustomInputMultiplexer(GuiStage stage, WorldInputProcessor processor){
        super();
        this.processor = processor;
        addProcessor(stage);
        addProcessor(processor);
    }

    public void updateInput(float delta){
        processor.update();
    }
}
