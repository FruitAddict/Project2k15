package com.fruit.visual.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.fruit.visual.GameCamera;

public class GameCameraAccessor implements TweenAccessor<GameCamera> {

    public static final int positionX = 0;
    public static final int positionY = 1;
    public static final int positionXY = 2;
    public static final int zoom = 3;

    @Override
    public int getValues(GameCamera gameCamera, int i, float[] floats) {
        switch(i){
            case positionX: {
                floats[0] = gameCamera.position.x;
                return 1;
            }
            case positionY:{
                floats[0] = gameCamera.position.y;
                return 1;
            }
            case positionXY:{
                floats[0] = gameCamera.position.x;
                floats[1] = gameCamera.position.y;
                return 2;
            }
            case zoom:{
                floats[0] = gameCamera.zoom;
                return 1;
            }
            default: {
                assert false : "Wrong value returned at "+getClass().getName();
                return -1;
            }
        }
    }

    @Override
    public void setValues(GameCamera gameCamera, int i, float[] floats) {
        switch(i){
            case positionX: {
                gameCamera.position.x = floats[0];
                break;
            }
            case positionY:{
                gameCamera.position.y = floats[0];
                break;
            }
            case positionXY:{
                gameCamera.position.x = floats[0];
                gameCamera.position.y = floats[1];
                break;
            }
            case zoom:{
                gameCamera.zoom = floats[0];
                break;
            }
            default: {
                assert false: "Wrong value set at "+getClass().getName();
                break;
            }
        }
    }
}
