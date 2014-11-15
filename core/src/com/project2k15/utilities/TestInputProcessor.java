package com.project2k15.utilities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.project2k15.entities.Player;

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
    Rectangle[] camBorderRectangles;

    public TestInputProcessor(OrthographicCamera cam, Player player, float mapWidth, float mapHeight) {
        camera = cam;
        this.player = player;
        touchDownPosition = new Vector3(-1, -1, 0);
        currentPosition = new Vector3();
        speedSaved = player.getSpeed();
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
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
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println("Touch up");
        if (pointer == 0) {
            touchDownPosition.set(-1, -1, 0);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        currentPosition.set(screenX, screenY, 0);
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
        if (zUp) {
            if (camera.zoom > 0.1)
                camera.zoom -= 0.03;
        }
        if (zDown) {
            camera.zoom += 0.03;
        }

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
            player.moveRight();
        } else if (tLeft) {
            player.idle = false;
            player.moveLeft();
        } else if (tUp) {
            player.idle = false;
            player.moveUp();
        } else if (tDown) {
            player.idle = false;
            player.moveDown();
        } else {
            player.idle = true;
        }
        if (sUp) {
            if (player.getScalar() < 1.0f)
                player.setScalar(player.getScalar() + 0.005f);
        }
        if (sDown) {
            if (player.getScalar() > 0)
                player.setScalar(player.getScalar() - 0.005f);
        }
        if (touchDownPosition.x != -1 && touchDownPosition.y != -1) {
            float angle = (float) (MathUtils.atan2(currentPosition.y - touchDownPosition.y, currentPosition.x - touchDownPosition.x) * 180 / Math.PI);
            //float length = (float) Math.sqrt(Math.pow(currentPosition.x - touchDownPosition.x, 2) + Math.pow(currentPosition.y - touchDownPosition.y, 2));
            /**if(length<50){
             player.setSpeed(speedSaved/4);
             }else {
             player.setSpeed(speedSaved);
             }*/
            if (angle < 22.5 && angle >= -22.5) {
                player.idle = false;
                player.moveRight();
            } else if (angle < -22.5 && angle >= -67.5) {
                player.idle = false;
                player.moveRight();
                player.moveUp();
            } else if (angle < -67.5 && angle >= -112.5) {
                player.idle = false;
                player.moveUp();
            } else if (angle < -112.5 && angle >= -157.5) {
                player.idle = false;
                player.moveLeft();
                player.moveUp();
            } else if (angle < -157.5 || angle >= 157.5) {
                player.idle = false;
                player.moveLeft();
            } else if (angle < 157.5 && angle >= 112.5) {
                player.idle = false;
                player.moveLeft();
                player.moveDown();
            } else if (angle < 112.5 && angle >= 67.5) {
                player.idle = false;
                player.moveDown();
            } else if (angle < 67.5 && angle >= 22.5) {
                player.idle = false;
                player.moveDown();
                player.moveRight();
            } else {
                player.idle = true;
            }
        }
    }
}
