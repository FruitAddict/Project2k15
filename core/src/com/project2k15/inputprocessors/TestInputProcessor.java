package com.project2k15.inputprocessors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by FruitAddict on 2014-11-04.
 */
public class TestInputProcessor implements InputProcessor {
    OrthographicCamera camera;
    boolean tLeft,tRight,tUp,tDown = false;

    public TestInputProcessor(OrthographicCamera cam){
        camera = cam;
    }


    @Override
    public boolean keyDown(int keycode) {
        switch(keycode){
            case Input.Keys.D: {
                tRight = true;
                return true;
            }
            case Input.Keys.A :{
                tLeft=true;
                return true;
            }
            case Input.Keys.W :{
                tUp=true;
                return true;
            }
            case Input.Keys.S :{
                tDown=true;
                return true;
            }

        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode){
            case Input.Keys.D: {
                tRight = false;
                return true;
            }
            case Input.Keys.A :{
                tLeft=false;
                return true;
            }
            case Input.Keys.W :{
                tUp=false;
                return true;
            }
            case Input.Keys.S :{
                tDown=false;
                return true;
            }

        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void translateCamera(){
        if(tRight){
            camera.translate(5,0);
        }
        if(tLeft){
            camera.translate(-5,0);
        }
        if(tUp){
            camera.translate(0,5);
        }
        if(tDown){
            camera.translate(0,-5);
        }
    }
}
