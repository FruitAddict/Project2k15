package com.project2k15.utilities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.project2k15.entities.Player;

/**
 * Created by FruitAddict on 2014-11-04.
 */
public class TestInputProcessor implements InputProcessor {
    OrthographicCamera camera;
    Player player;
    boolean tLeft, tRight, tUp, tDown, zUp, zDown, sUp, sDown = false;

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
            case Input.Keys.UP: {
                zUp = true;
                return true;
            }
            case Input.Keys.DOWN: {
                zDown = true;
                return true;
            }
            case Input.Keys.LEFT: {
                sUp = true;
                return true;
            }
            case Input.Keys.RIGHT: {
                sDown = true;
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
            case Input.Keys.UP: {
                zUp = false;
                return true;
            }
            case Input.Keys.DOWN: {
                zDown = false;
                return true;
            }
            case Input.Keys.LEFT: {
                sUp = false;
                return true;
            }
            case Input.Keys.RIGHT: {
                sDown = false;
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        switch (character) {
            case 'g': {
                player.holding = !player.holding;
            }
        }
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
        camera.position.set(player.getPosition().x + player.getWidth() / 2, player.getPosition().y + player.getHeight() / 2, camera.zoom);

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
            if (camera.zoom > 0.1)
            camera.zoom -= 0.03;
        }
        if (zDown) {
            camera.zoom += 0.03;
        }
        if (sUp) {
            if (player.getScalar() < 1.0f)
                player.setScalar(player.getScalar() + 0.005f);
        }
        if (sDown) {
            if (player.getScalar() > 0)
                player.setScalar(player.getScalar() - 0.005f);
        }
    }
}
