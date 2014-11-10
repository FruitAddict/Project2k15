package com.project2k15.inputprocessors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.project2k15.test.Player;

/**
 * Created by FruitAddict on 2014-11-04.
 */
public class TestInputProcessor implements InputProcessor {
    OrthographicCamera camera;
    Player player;
    boolean tLeft, tRight, tUp, tDown, zUp, zDown = false;

    public TestInputProcessor(OrthographicCamera cam, Player player) {
        camera = cam;
        this.player = player;
    }


    /*@Override
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
            case Input.Keys.UP :{
                zUp = true;
                return true;
            }
            case Input.Keys.DOWN :{
                zDown = true;
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
            case Input.Keys.UP :{
                zUp = false;
                return true;
            }
            case Input.Keys.DOWN :{
                zDown = false;
                return true;
            }

        }
        return false;
    }*/

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.D: {
                tRight = true;
                return true;
            }
            case Input.Keys.A: {
                tLeft = true;
                return true;
            }
            case Input.Keys.W: {
                tUp = true;
                return true;
            }
            case Input.Keys.S: {
                tDown = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.D: {
                tRight = false;
                return true;
            }
            case Input.Keys.A: {
                tLeft = false;
                return true;
            }
            case Input.Keys.W: {
                tUp = false;
                return true;
            }
            case Input.Keys.S: {
                tDown = false;
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
        camera.position.set(player.getPosition(), 0.5f);

        if(tRight){
            player.moveRight();
        }
        if(tLeft){
            player.moveLeft();
        }
        if(tUp){
            player.moveUp();
        }
        if(tDown){
            player.moveDown();
        }
        if (zUp) {
            camera.zoom -= 0.03;
        }
        if (zDown) {
            camera.zoom += 0.03;
        }
    }
}
