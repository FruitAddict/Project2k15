package com.fruit.logic.input;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.fruit.logic.objects.entities.Player;

/**
 * Game world input processor. Takes care of steering, attacking and other things not releated to GUI
 */
public class WorldInputProcessorTest extends WorldInputProcessor {
    private boolean leftMove,rightMove,upMove,downMove,rightAttack,leftAttack,upAttack,downAttack = false;
    private float lerpValue = 0.05f;

    public WorldInputProcessorTest(Player player, OrthographicCamera camera) {
        super(player,camera);
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

        /**
         * TEST
         */
        if(upMove){
            player.moveNorth();
        }
        if(downMove){
            player.moveSouth();
        }
        if(rightMove){
            player.moveWest();
        }
        if(leftMove){
            player.moveEast();
        }

        if(upAttack){
            player.attack(new Vector2(0,1));
        }
        if(downAttack){
            player.attack(new Vector2(0,-1));
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
