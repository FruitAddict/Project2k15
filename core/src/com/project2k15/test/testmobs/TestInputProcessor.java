package com.project2k15.test.testmobs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.project2k15.logic.managers.ObjectManager;
import com.project2k15.logic.entities.Player;

;

/**
 * Created by FruitAddict on 2014-11-04.
 */
public class TestInputProcessor implements InputProcessor {
    OrthographicCamera camera;
    Player player;
    boolean tLeft, tRight, tUp, tDown, zUp, zDown, sUp, sDown = false;
    Vector3 touchDownPosition;
    Vector3 currentPosition;
    float speedSaved;
    float mapHeight;
    float mapWidth;
    Vector2 point1;
    Vector2 point2;
    float lengthBetweenPointers;
    boolean pointer1, pointer2 = false;
    ObjectManager objectManager;

    public TestInputProcessor(OrthographicCamera cam, Player player, float mapWidth, float mapHeight) {
        camera = cam;
        this.player = player;
        touchDownPosition = new Vector3(-1, -1, 0);
        currentPosition = new Vector3();
        speedSaved = player.getSpeed();
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        point1 = new Vector2();
        point2 = new Vector2();
    }


    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.D: {
                System.out.println("Moving right");
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
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("Touch down");
        if (pointer == 0) {
            touchDownPosition.set(screenX, screenY, 0);
            point1.set(screenX, screenY);
            pointer1 = true;
        }
        if (pointer == 1) {
            pointer2 = true;
            point2.set(screenX, screenY);
            lengthBetweenPointers = (float) Math.sqrt(Math.pow(point2.x - point1.x, 2) + Math.pow(point2.y - point1.y, 2));
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println("Touch up");
        if (pointer == 0) {
            touchDownPosition.set(-1, -1, 0);

        }
        if (pointer == 0) {
            pointer1 = false;
            lengthBetweenPointers = 0;
        }
        if (pointer == 1) {
            pointer2 = false;
            lengthBetweenPointers = 0;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        if (pointer == 0) {
            point1.set(screenX, screenY);
            currentPosition.set(screenX, screenY, 0);
        }
        if (pointer == 1) {
            point2.set(screenX, screenY);
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

    public void translateCamera(){
        float old = camera.zoom;
        if (zUp && camera.zoom > 0.1) {
            camera.zoom -= 0.03;
        }
        if (zDown && camera.zoom < 1.2) {
            camera.zoom += 0.03;
        }
        /**
         if (pointer1 && pointer2 ) {
            float newScale = camera.zoom * (float) Math.sqrt(Math.pow(point2.x - point1.x, 2) + Math.pow(point2.y - point1.y, 2)) / lengthBetweenPointers;
            if (newScale > 0.1 && newScale < 1.2 && lengthBetweenPointers != 0) {
                camera.zoom = newScale;
                lengthBetweenPointers = (float) Math.sqrt(Math.pow(point2.x - point1.x, 2) + Math.pow(point2.y - point1.y, 2));

            }
         }*/

        camera.position.set(player.getPosition().x + player.getWidth() / 2, player.getPosition().y + player.getHeight() / 2, camera.zoom);

        float cameraLeft = camera.position.x - camera.viewportWidth * camera.zoom / 2;
        float cameraRight = camera.position.x + camera.viewportWidth * camera.zoom / 2;
        float cameraBottom = camera.position.y - camera.viewportHeight * camera.zoom / 2;
        float cameraTop = camera.position.y + camera.viewportHeight * camera.zoom / 2;

        // Horizontal axis
        if (cameraLeft <= 0) {
            camera.position.x = camera.viewportWidth * camera.zoom / 2;
        } else if (cameraRight >= mapWidth) {
            camera.position.x = mapWidth - camera.viewportWidth * camera.zoom / 2;
        }

        // Vertical axis
        if (cameraBottom <= 0) {
            camera.position.y = camera.viewportHeight * camera.zoom / 2;
        } else if (cameraTop >= mapHeight) {
            camera.position.y = mapHeight - camera.viewportHeight * camera.zoom / 2;
        }



        if(tRight){
            player.idle = false;
            player.moveEast();
        } else if (tLeft) {
            player.idle = false;
            player.moveWest();
        } else if (tUp) {
            player.idle = false;
            player.moveNorth();
        } else if (tDown) {
            player.idle = false;
            player.moveSouth();
        } else {
            player.idle = true;
        }
        if (sUp) {
            if (player.getClamping() < 1.0f)
                player.setClamping(player.getClamping() + 0.005f);
        }
        if (sDown) {
            if (player.getClamping() > 0)
                player.setClamping(player.getClamping() - 0.005f);
        }

        if (touchDownPosition.x != -1 && touchDownPosition.y != -1) {
            Vector2 velocityNormalized = new Vector2();
            velocityNormalized.set(currentPosition.x - touchDownPosition.x, currentPosition.y - touchDownPosition.y);
            velocityNormalized.nor();
            player.attack(velocityNormalized.cpy());
        }
    }
}
