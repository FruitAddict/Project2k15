package com.fruit.game;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.fruit.game.logic.WorldUpdater;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;
import com.fruit.game.screens.GameScreen;
import com.fruit.game.visual.messages.TextMessage;
import com.fruit.game.visual.renderer.WorldRenderer;
import com.fruit.game.visual.ui.UserInterface;

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
    private static MainGame mainGame;

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

    public static MainGame getMainGame(){
        if(Controller.mainGame!=null){
            return Controller.mainGame;
        }else {
            throw new NullPointerException("No main game class registered.");
        }
    }

    public static void addOnScreenMessage(String msg, float positionX, float positionY, float lifeSpan){
        if(Controller.worldRenderer!=null){
            worldRenderer.getTextRenderer().addMessage(msg,positionX,positionY,lifeSpan);
        }
    }

    public static void addOnScreenMessage(String msg, float positionX, float positionY, float lifeSpan, BitmapFont font, int tweenType){
        if(Controller.worldRenderer!=null){
            worldRenderer.getTextRenderer().addMessage(msg,positionX,positionY,lifeSpan,font,tweenType);
        }
    }

    public static void addOnScreenMessage(GameObject owner, String message, Vector2 parentPosition, float parentHeight, float lifeTime, BitmapFont font, int tweenType){
        if(Controller.worldRenderer!=null){
            worldRenderer.getTextRenderer().addMessage(owner,message,parentPosition,parentHeight,lifeTime,font,tweenType);
        }
    }

    public static synchronized void pauseGame(){
        if(Controller.gameScreen!=null){
            Controller.getGameScreen().paused = true;
            Controller.worldRenderer.pauseRendering();
            Controller.getUserInterface().paused = true;
            Controller.getGameScreen().getWorldInputProcessor().resetAll();
        }
    }

    public static synchronized void unpauseGame(){
        if(Controller.getGameScreen()!=null){
            Controller.getGameScreen().paused = false;
            Controller.getUserInterface().paused = false;
            Controller.getWorldRenderer().unpauseRendering();
            Controller.getGameScreen().getWorldInputProcessor().resetAll();
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

    public static void registerMainGame(MainGame mainGame) {
        Controller.mainGame = mainGame;
    }

    public static void onPlayerDeath(Player player) {
        Controller.userInterface.addOnDeathDialog(player);
    }
}
