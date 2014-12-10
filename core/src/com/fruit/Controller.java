package com.fruit;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.fruit.logic.WorldUpdater;
import com.fruit.visual.messages.TextMessage;
import com.fruit.visual.renderer.WorldRenderer;

/**
 * Utility class contains static getter methods for all the main components of the game.
 * constains static methods to register the components. Used for communication
 * between logic and rendering.
 */
public class Controller {
    public static WorldUpdater worldUpdater;
    public static WorldRenderer worldRenderer;

    public static WorldRenderer getWorldRenderer(){
        if(Controller.worldRenderer!=null){
            return Controller.worldRenderer;
        }else {
            throw new NullPointerException("No world renderer registered.");
        }
    }

    public static WorldUpdater getWorldUpdater(){
        if(Controller.worldUpdater != null){
            return Controller.worldUpdater;
        }else {
            throw new NullPointerException("No world updater registered.");
        }
    }

    public static void addOnScreenMessage(String msg, float positionX, float positionY, float lifeSpan){
        if(Controller.worldRenderer!=null){
            worldRenderer.getTextRenderer().addMessage(msg,positionX,positionY,lifeSpan);
        }
    }

    public static void addOnScreenMessage(TextMessage message){
        if(Controller.worldRenderer!=null){
            worldRenderer.getTextRenderer().addMessage(message);
        }
    }

    public static void registerWorldUpdater(WorldUpdater worldUpdater){
        Controller.worldUpdater = worldUpdater;
    }

    public static void registerWorldRenderer(WorldRenderer worldRenderer){
        Controller.worldRenderer = worldRenderer;
    }
}
