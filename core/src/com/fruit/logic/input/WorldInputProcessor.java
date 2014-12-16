package com.fruit.logic.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.entities.player.Player;

/**
 * Game world input processor. Takes care of steering, attacking and other things not releated to GUI
 */
public class WorldInputProcessor implements InputProcessor, Constants {
    /**
     * Orthographic camera used in the game screen, used to translate the camera to follow the player
     * Player object, used for making the player move around during input
     * Width/height of the current map, used to bound the camera.
     * Vectors used to save touch down position of the first&second touch events and to store their new positions
     * after drag is detected. Used for movement & attacking
     * Vector to store normalized velocity derived in the attacking alghorithm in the update() method
     * MapManager to obtain current map width/height
     * Controller containing references to everything else in this program
     */

    protected Player player;
    protected float mapWidth;
    protected float mapHeight;
    private Vector3 firstMovementPosition;
    private Vector3 secondMovementPosition;
    private Vector3 firstAttackingPosition;
    private Vector3 secondAttackingPosition;
    private Vector2 velocityNormalized;



    public WorldInputProcessor(Player player, OrthographicCamera camera) {
        //initiators
        firstMovementPosition = new Vector3(-1, -1, 0);
        secondMovementPosition = new Vector3();
        firstAttackingPosition = new Vector3(-1, -1, 0);
        secondAttackingPosition = new Vector3();
        velocityNormalized = new Vector2();

        this.player = player;

        camera.zoom = 0.8f;

        setMapSize(2048,2048);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        /**
         * If the pointer is the first one or the second one ( id 0 or 1 ), sets the corresponding vector to those coordinates.
         */
        if (pointer == 0) {
            firstMovementPosition.set(screenX, screenY, 0);
            return true;
        } else if (pointer == 1) {
            firstAttackingPosition.set(screenX, screenY, 0);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        /**
         * After touch event with id 0 or 1 is over, sets the touchdownposition to -1,-1 (offscreen), player movement or
         * attacking will cease in the .update() method.
         */
        if (pointer == 0) {
            firstMovementPosition.set(-1, -1, 0);
            return true;
        } else if (pointer == 1) {
            firstAttackingPosition.set(-1, -1, 0);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        /**
         * If touch event with id 0 or 1 is dragged, constantly saves the new position
         * to corresponding vector. For use with player movement & attacking
         */
        if (pointer == 0) {
            secondMovementPosition.set(screenX, screenY, 0);
            return true;
        } else if (pointer == 1) {
            secondAttackingPosition.set(screenX, screenY, 0);
            return true;
        }
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

    }
}
