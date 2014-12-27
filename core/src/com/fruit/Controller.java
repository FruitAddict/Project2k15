package com.fruit;

import com.fruit.logic.WorldUpdater;
import com.fruit.screens.GameScreen;
import com.fruit.visual.messages.TextMessage;
import com.fruit.visual.renderer.WorldRenderer;
import com.fruit.visual.ui.UserInterface;

/**
 * Utility class contains static getter methods for all the main components of the game.
 * contains static methods to register the components. Used for communication
 * between logic and rendering.
 */
public class Controller {
    private static WorldUpdater worldUpdater;
    private static WorldRenderer worldRenderer;
    private static UserInterface userInterface;
    private static GameScreen gameScreen;

    public static WorldRenderer getWorldRenderer(){
        if(Controller.worldRenderer!=null){
            return Controller.worldRenderer;
        }else {
            throw new NullPointerException("No world renderer registered.");
        }
    }


    public static UserInterface getUserInterface(){
        if(Controller.userInterface!=null){
            return Controller.userInterface;
        }else {
            throw new NullPointerException("No User Interface registered.");
        }
    }

    public static WorldUpdater getWorldUpdater(){
        if(Controller.worldUpdater != null){
            return Controller.worldUpdater;
        }else {
            throw new NullPointerException("No world updater registered.");
        }
    }

    public static GameScreen getGameScreen(){
        if(Controller.gameScreen!=null){
            return Controller.gameScreen;
        }else {
            throw new NullPointerException("No game screen registered.");
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

    public static synchronized void pauseGame(){
        if(Controller.gameScreen!=null){
            Controller.gameScreen.paused = true;
            Controller.worldRenderer.pauseRendering(0.3f);
        }
    }

    public static synchronized void unpauseGame(){
        if(Controller.gameScreen!=null){
            Controller.gameScreen.paused = false;
            Controller.worldRenderer.unpauseRendering();
        }
    }

    public static void registerWorldUpdater(WorldUpdater worldUpdater){
        Controller.worldUpdater = worldUpdater;
    }

    public static void registerWorldRenderer(WorldRenderer worldRenderer){
        Controller.worldRenderer = worldRenderer;
    }

    public static void registerUserInterface(UserInterface userInterface){
        Controller.userInterface = userInterface;
    }

    public static void registerGameScreen(GameScreen gameScreen){
        Controller.gameScreen = gameScreen;
    }
}
