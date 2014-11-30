package com.fruit.logic.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.Player;

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
     * TODO Isaac-esque room changing effect (slides)
     */

    protected OrthographicCamera camera;
    protected Player player;
    protected float mapWidth;
    protected float mapHeight;
    private Vector3 firstMovementPosition;
    private Vector3 secondMovementPosition;
    private Vector3 firstAttackingPosition;
    private Vector3 secondAttackingPosition;
    private Vector2 velocityNormalized;
    protected float lerpValue;



    public WorldInputProcessor(Player player, OrthographicCamera camera) {
        //initiators
        firstMovementPosition = new Vector3(-1, -1, 0);
        secondMovementPosition = new Vector3();
        firstAttackingPosition = new Vector3(-1, -1, 0);
        secondAttackingPosition = new Vector3();
        velocityNormalized = new Vector2();
        lerpValue = 0.1f;

        this.player = player;
        this.camera = camera;

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

    public float getLerpValue() {
        return lerpValue;
    }

    public void setLerpValue(float lerpValue) {
        this.lerpValue = lerpValue;
    }


    public void setMapSize(float width, float height){
        mapWidth=width;
        mapHeight=height;
    }

    public void update() {
        /**
         * Camera translating algorithm. Initially moves the camera to the player position (centered), then checks whether the current camera
         * position overlaps the map boundaries. If so, sets it to the corner
         */

        Vector3 lerpVector = new Vector3((player.getBody().getPosition().x*PIXELS_TO_METERS),
                (player.getBody().getPosition().y*PIXELS_TO_METERS), 0);

        camera.position.lerp(lerpVector, lerpValue);

        float cameraLeft = camera.position.x - camera.viewportWidth * camera.zoom / 2;
        float cameraRight = camera.position.x + camera.viewportWidth * camera.zoom / 2;
        float cameraBottom = camera.position.y - camera.viewportHeight * camera.zoom / 2;
        float cameraTop = camera.position.y + camera.viewportHeight * camera.zoom / 2;

        // Horizontal axis
        if (cameraLeft <= 0) {
            camera.position.x = camera.viewportWidth * camera.zoom / 2;
        } else if (cameraRight >= mapWidth) {
            camera.position.x = mapHeight - camera.viewportWidth * camera.zoom / 2;
        }

        // Vertical axis
        if (cameraBottom <= 0) {
            camera.position.y = camera.viewportHeight * camera.zoom / 2;
        } else if (cameraTop >= mapHeight) {
            camera.position.y = mapHeight - camera.viewportHeight * camera.zoom / 2;
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
            velocityNormalized.y*=-1;
            player.attack(velocityNormalized);
        }


    }
}
