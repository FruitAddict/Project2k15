package com.fruit.game.visual;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.fruit.game.Controller;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.visual.tween.GameCameraAccessor;

/**
 * @author FruitAddict
 * Custom camer class. Includes updateCameraMovement() method and supports 2 modes:
 * -follow a game object using lerp alghorithm for smooth movements
 * -free camera (so it can be manipulated by some pre-programmed methods like map changing stuff
 * @see com.fruit.game.visual.renderer.WorldRenderer
 */
public class GameCamera extends OrthographicCamera implements Constants {
    private boolean freeCamera = false;
    private float lerpValue = 0.1f;
    private GameObject followedObject;
    public TweenCallback followObjectCallback;
    private Vector3 lerpVector;
    private boolean paused;

    public GameCamera(){
        //call super default constructor of the orthographic camera class
        super();
        //register gamecamera accessor
        Tween.registerAccessor(GameCamera.class, new GameCameraAccessor());
        //tween callback to reset the camera ( following the object) used with map transition tweens.
        //see WorldRenderer
        followObjectCallback = new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                freeCamera = false;
                System.out.println("callback reached");
            }
        };
        //init lerp vector
        lerpVector = new Vector3();
        //set zoom to 0.65
        zoom = 0.75f;
    }

    public void updateCameraMovement(float delta){
        if(!freeCamera && followedObject!= null && !paused){
            float mapWidth = Controller.getWorldUpdater().getMapManager().getCurrentMapWidth();
            float mapHeight = Controller.getWorldUpdater().getMapManager().getCurrentMapHeight();

             //Camera translating algorithm. Initially lerps the camera to the player position (centered), then checks whether the current camera
             //position overlaps the map boundaries. If so, sets it to the corner

            lerpVector.set((((followedObject.getBody().getPosition().x) * PIXELS_TO_UNITS)),
                    (followedObject.getBody().getPosition().y * PIXELS_TO_UNITS), 0);

            position.lerp(lerpVector, lerpValue);

            float cameraLeft = position.x - viewportWidth * zoom / 2;
            float cameraRight = position.x + viewportWidth * zoom / 2;
            float cameraBottom = position.y - viewportHeight * zoom / 2;
            float cameraTop = position.y + viewportHeight * zoom / 2;

            // Horizontal axis
            if (cameraLeft <= 0) {
                position.x = viewportWidth * zoom / 2;
            } else if (cameraRight >= mapWidth) {
                position.x = mapWidth - viewportWidth * zoom / 2;
            }

            // Vertical axis
            if (cameraBottom <= 0) {
                position.y = viewportHeight * zoom / 2;
            } else if (cameraTop >= mapHeight) {
                position.y = mapHeight - viewportHeight * zoom / 2;
            }
        }
    }

    public void setObjectToFollow(GameObject o){
        //if object specified exists in the game world, camera will follow it.
        if(Controller.getWorldUpdater().getObjectManager().getGameObjects().contains(o,true)){
            followedObject = o;
        } else {
            followedObject = Controller.getWorldUpdater().getObjectManager().getPlayer();
        }
    }

    public void setFreeCamera(boolean truefalse){
        freeCamera = truefalse;
    }
    public float getLerpValue() {
        return lerpValue;
    }

    public void setLerpValue(float lerpValue) {
        this.lerpValue = lerpValue;
    }

    public void pauseTransition() {
        paused = true;
    }

    public void unpauseTransition(){
        paused = false;
    }
}
