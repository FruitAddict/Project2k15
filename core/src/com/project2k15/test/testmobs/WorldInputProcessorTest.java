package com.project2k15.test.testmobs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.project2k15.logic.entities.Player;
import com.project2k15.logic.input.WorldInputProcessor;
import com.project2k15.logic.managers.Controller;
import com.project2k15.logic.managers.MapManager;
import com.project2k15.logic.managers.ObjectManager;
import com.project2k15.rendering.WorldRenderer;

/**
 * Game world input processor. Takes care of steering, attacking and other things not releated to GUI
 */
public class WorldInputProcessorTest extends WorldInputProcessor {
    private boolean leftMove,rightMove,upMove,downMove,rightAttack,leftAttack,upAttack,downAttack = false;
    private Player player;
    private Controller controller;
    private OrthographicCamera camera;
    private MapManager mapManager;
    private float lerpValue;


    public WorldInputProcessorTest() {
        super();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode){
            case Input.Keys.W: {
                upMove = true;
                break;
            }
            case Input.Keys.S: {
                downMove = true;
                break;
            }
            case Input.Keys.A: {
                rightMove = true;
                break;
            }
            case Input.Keys.D: {
                leftMove = true;
                break;
            }
            case Input.Keys.UP: {
                upAttack= true;
                break;
            }
            case Input.Keys.DOWN: {
                downAttack = true;
                break;
            }
            case Input.Keys.LEFT: {
                leftAttack = true;
                break;
            }
            case Input.Keys.RIGHT: {
                rightAttack = true;
                break;
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode){
        switch(keycode){
        case Input.Keys.W: {
            upMove = false;
            break;
        }
        case Input.Keys.S: {
            downMove = false;
            break;
        }
        case Input.Keys.A: {
            rightMove = false;
            break;
        }
        case Input.Keys.D: {
            leftMove = false;
            break;
        }
        case Input.Keys.UP: {
            upAttack= false;
            break;
        }
        case Input.Keys.DOWN: {
            downAttack = false;
            break;
        }
        case Input.Keys.LEFT: {
            leftAttack = false;
            break;
        }
        case Input.Keys.RIGHT: {
            rightAttack = false;
            break;
        }
    }
        return false;
    }
    @Override
    public void setController(Controller c){
        this.controller=c;
    }
    @Override
    public float getLerpValue() {
        return lerpValue;
    }
    @Override
    public void setLerpValue(float lerpValue) {
        this.lerpValue = lerpValue;
    }


    public void update() {
        player = controller.getPlayer();
        camera = controller.getOrthographicCamera();
        mapManager = controller.getMapManager();
        /**
         * Camera translating algorithm. Initially moves the camera to the player position (centered), then checks whether the current camera
         * position overlaps the map boundaries. If so, sets it to the corner
         */
        //camera.position.set(player.getPosition().x + player.getWidth() / 2, player.getPosition().y + player.getHeight() / 2, camera.zoom);
        camera.position.lerp(new Vector3(player.getPosition().x+player.getWidth()/2,player.getPosition().y+player.getHeight()/2,0),lerpValue);

        float cameraLeft = camera.position.x - camera.viewportWidth * camera.zoom / 2;
        float cameraRight = camera.position.x + camera.viewportWidth * camera.zoom / 2;
        float cameraBottom = camera.position.y - camera.viewportHeight * camera.zoom / 2;
        float cameraTop = camera.position.y + camera.viewportHeight * camera.zoom / 2;

        // Horizontal axis
        if (cameraLeft <= 0) {
            camera.position.x = camera.viewportWidth * camera.zoom / 2;
        } else if (cameraRight >= mapManager.getMapWidth()) {
            camera.position.x = mapManager.getMapWidth() - camera.viewportWidth * camera.zoom / 2;
        }

        // Vertical axis
        if (cameraBottom <= 0) {
            camera.position.y = camera.viewportHeight * camera.zoom / 2;
        } else if (cameraTop >= mapManager.getMapHeight()) {
            camera.position.y = mapManager.getMapHeight() - camera.viewportHeight * camera.zoom / 2;
        }

        /**
         * TEST
         */
        if(upMove){
            player.moveUp();
        }
        if(downMove){
            player.moveDown();
        }
        if(rightMove){
            player.moveLeft();
        }
        if(leftMove){
            player.moveRight();
        }
        if(upAttack){
            player.attack(new Vector2(0,-1));
        }
        if(downAttack){
            player.attack(new Vector2(0,1));
        }
        if(leftAttack){
            player.attack(new Vector2(-1,0));
        }
        if(rightAttack){
            player.attack(new Vector2(1,0));
        }

        /**
         * TEST
         */

    }
}
