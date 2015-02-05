package com.fruit.game.logic.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.fruit.game.Controller;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * Game world input processor. Takes care of steering, attacking and other things not releated to GUI
 */
public class WorldInputProcessor implements InputProcessor, Constants {
    protected Player player;
    protected float mapWidth;
    protected float mapHeight;
    private Vector2 touchpad1;
    private boolean movingDown,movingUp,movingLeft,movingRight = false;
    private boolean attackingLeft,attackingRight,attackingUp,attackingDown = false;

    public WorldInputProcessor(Player player) {
        //initiators

        touchpad1 = new Vector2();
        this.player = player;
        setMapSize(2048,2048);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode){
            case Input.Keys.W:{
                movingUp = true;
                break;
            }
            case Input.Keys.S:{
                movingDown = true;
                break;
            }
            case Input.Keys.A:{
                movingLeft = true;
                break;
            }
            case Input.Keys.D:{
                movingRight = true;
                break;
            }
            case Input.Keys.UP:{
                attackingUp = true;
                break;
            }
            case Input.Keys.DOWN:{
                attackingDown = true;
                break;
            }
            case Input.Keys.RIGHT:{
                attackingRight=true;
                break;
            }
            case Input.Keys.LEFT:{
                attackingLeft = true;
                break;
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode){
            case Input.Keys.W:{
                movingUp = false;
                break;
            }
            case Input.Keys.S:{
                movingDown = false;
                break;
            }
            case Input.Keys.A:{
                movingLeft = false;
                break;
            }
            case Input.Keys.D:{
                movingRight = false;
                break;
            }
            case Input.Keys.UP:{
                attackingUp = false;
                break;
            }
            case Input.Keys.DOWN:{
                attackingDown = false;
                break;
            }
            case Input.Keys.RIGHT:{
                attackingRight=false;
                break;
            }
            case Input.Keys.LEFT:{
                attackingLeft = false;
                break;
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
        //naive check if the pointer is on the left or right side of the screen
        if(screenX < Gdx.graphics.getWidth()/2) {
            touchpad1.set(Controller.getUserInterface().screenToStageCoordinates(touchpad1.set(screenX, screenY)));
            Controller.getUserInterface().getTouchpadMove().setPosition(touchpad1.x - Controller.getUserInterface().getTouchpadMove().getWidth() / 2, touchpad1.y -
                    +Controller.getUserInterface().getTouchpadMove().getHeight() / 2);
            InputEvent fake = new InputEvent();
            fake.setStageX(touchpad1.x);
            fake.setStageY(touchpad1.y);
            fake.setType(InputEvent.Type.touchDown);
            fake.setPointer(pointer);
            Controller.getUserInterface().getTouchpadMove().fire(fake);
            Controller.getUserInterface().getTouchpadMove().setVisible(true);
        }else {
            touchpad1.set(Controller.getUserInterface().screenToStageCoordinates(touchpad1.set(screenX, screenY)));
            Controller.getUserInterface().getTouchpadAttack().setPosition(touchpad1.x - Controller.getUserInterface().getTouchpadAttack().getWidth() / 2, touchpad1.y -
                    +Controller.getUserInterface().getTouchpadAttack().getHeight() / 2);
            InputEvent fake = new InputEvent();
            fake.setStageX(touchpad1.x);
            fake.setStageY(touchpad1.y);
            fake.setType(InputEvent.Type.touchDown);
            fake.setPointer(pointer);
            Controller.getUserInterface().getTouchpadAttack().fire(fake);
            Controller.getUserInterface().getTouchpadAttack().setVisible(true);
        }
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


    public void setMapSize(float width, float height){
        mapWidth=width;
        mapHeight=height;
    }

    public void update() {
        if(movingUp){
            player.moveNorth();
        }
        if(movingLeft){
            player.moveWest();
        }
        if(movingRight){
            player.moveEast();
        }
        if(movingDown){
            player.moveSouth();
        }
        if(attackingLeft){
            player.attack(-100,0);
        }
        if(attackingRight){
            player.attack(100,0);
        }
        if(attackingUp){
            player.attack(0,100);
        }
        if(attackingDown){
            player.attack(0,-100);
        }
    }

    public void resetAll(){
        movingRight = false;
        movingLeft = false;
        movingUp = false;
        movingDown = false;
        attackingDown = false;
        attackingUp = false;
        attackingRight = false;
        attackingLeft =false;
    }
}
