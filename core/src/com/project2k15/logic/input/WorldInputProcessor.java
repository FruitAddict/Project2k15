package com.project2k15.logic.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.project2k15.logic.entities.Player;
import com.project2k15.logic.managers.Controller;
import com.project2k15.logic.managers.MapManager;

/**
 * Game world input processor. Takes care of steering, attacking and other things not releated to GUI
 */
public class WorldInputProcessor implements InputProcessor {
    /**
     * Orthographic camera used in the game screen, used to translate the camera to follow the player
     * Player object, used for making the player move around during input
     * Width/height of the current map, used to bound the camera.
     * Vectors used to save touch down position of the first&second touch events and to store their new positions
     * after drag is detected. Used for movement & attacking
     * Vector to store normalized velocity derived in the attacking alghorithm in the update() method
     * MapManager to obtain current map width/height
     * Controller containing references to everything else in this program
     * TODO Isaac-esque room changing effect (slides)
     */

    private OrthographicCamera camera;
    private Player player;
    private float mapWidth;
    private float mapHeight;
    private Vector3 firstMovementPosition;
    private Vector3 secondMovementPosition;
    private Vector3 firstAttackingPosition;
    private Vector3 secondAttackingPosition;
    private Vector2 velocityNormalized;
    private MapManager manager;
    private Controller controller;
    protected float lerpValue;


    public WorldInputProcessor() {
        //initiators
        firstMovementPosition = new Vector3(-1, -1, 0);
        secondMovementPosition = new Vector3();
        firstAttackingPosition = new Vector3(-1, -1, 0);
        secondAttackingPosition = new Vector3();
        velocityNormalized = new Vector2();
        lerpValue = 0.1f;
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

    public void setController(Controller con){
        controller = con;
    }

    public void update() {
        player = controller.getPlayer();
        camera = controller.getCam();
        manager = controller.getMapManager();
        /**
         * Camera translating algorithm. Initially moves the camera to the player position (centered), then checks whether the current camera
         * position overlaps the map boundaries. If so, sets it to the corner
         */
        //camera.position.set(player.getPosition().x + player.getWidth() / 2, player.getPosition().y + player.getHeight() / 2, camera.zoom);
        camera.position.lerp(new Vector3(player.getPosition().x + player.getWidth() / 2, player.getPosition().y + player.getHeight() / 2, 0), lerpValue);

        float cameraLeft = camera.position.x - camera.viewportWidth * camera.zoom / 2;
        float cameraRight = camera.position.x + camera.viewportWidth * camera.zoom / 2;
        float cameraBottom = camera.position.y - camera.viewportHeight * camera.zoom / 2;
        float cameraTop = camera.position.y + camera.viewportHeight * camera.zoom / 2;

        // Horizontal axis
        if (cameraLeft <= 0) {
            camera.position.x = camera.viewportWidth * camera.zoom / 2;
        } else if (cameraRight >= manager.getMapWidth()) {
            camera.position.x = manager.getMapWidth() - camera.viewportWidth * camera.zoom / 2;
        }

        // Vertical axis
        if (cameraBottom <= 0) {
            camera.position.y = camera.viewportHeight * camera.zoom / 2;
        } else if (cameraTop >= manager.getMapHeight()) {
            camera.position.y = manager.getMapHeight() - camera.viewportHeight * camera.zoom / 2;
        }

        /**
         *  Movement update algorithm. Checks for the angle between first touch position and the dragged touch position and moves the player according to that
         *  angle.
         */
        if (firstMovementPosition.x != -1 && firstMovementPosition.y != -1) {
            float angle = (float) (MathUtils.atan2(secondMovementPosition.y - firstMovementPosition.y, secondMovementPosition.x - firstMovementPosition.x) * 180 / Math.PI);
            float length = (float) Math.sqrt(Math.pow(secondMovementPosition.x - firstMovementPosition.x, 2) + Math.pow(secondMovementPosition.y - firstMovementPosition.y, 2));
            if (length > 25) {

                if (angle < 22.5 && angle >= -22.5) {
                    player.moveEast();
                } else if (angle < -22.5 && angle >= -67.5) {
                    player.moveNorthEast();
                } else if (angle < -67.5 && angle >= -112.5) {
                    player.moveNorth();
                } else if (angle < -112.5 && angle >= -157.5) {
                    player.moveNorthWest();
                } else if (angle < -157.5 || angle >= 157.5) {
                    player.moveWest();
                } else if (angle < 157.5 && angle >= 112.5) {
                    player.moveSouthWest();
                } else if (angle < 112.5 && angle >= 67.5) {
                    player.moveSouth();
                } else if (angle < 67.5 && angle >= 22.5) {
                    player.moveSouthEast();
                }
             }
        }



        /**
         * Attacking alghorithm. Works like the movement, but instead of checking angles it normalizes the vectors and calls
         * the attack() method of player class.
         */
        if (firstAttackingPosition.x != -1 && firstAttackingPosition.y != -1) {
            velocityNormalized.set(secondAttackingPosition.x - firstAttackingPosition.x, secondAttackingPosition.y - firstAttackingPosition.y);
            velocityNormalized.nor();
            player.attack(velocityNormalized.cpy());
        }


    }

    public float getLerpValue() {
        return lerpValue;
    }

    public void setLerpValue(float lerpValue) {
        this.lerpValue = lerpValue;
    }
}
