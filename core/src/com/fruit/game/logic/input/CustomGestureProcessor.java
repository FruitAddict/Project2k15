package com.fruit.game.logic.input;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class CustomGestureProcessor implements GestureDetector.GestureListener {
    private Player player;
    private Vector2 movementDirection;
    private boolean stillMoving = false;
    private GestureDetector gestureDetector;

    public CustomGestureProcessor(Player player){
        this.player = player;
        movementDirection = new Vector2();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        System.out.println(velocityX+" "+velocityY+" "+button);
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        System.out.println(x+" "+y+" | "+deltaX+" "+deltaY);
        gestureDetector.invalidateTapSquare();
        //stillMoving = true;
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        System.out.println(pointer+" pointer up");
        if(pointer==0){
            stillMoving=false;
            gestureDetector.reset();
        }
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    public void update(){
        if(stillMoving)
        player.addLinearVelocity(movementDirection.x*player.stats.getMaxVelocity(),movementDirection.y*player.stats.getMaxVelocity());
    }

    public void setParent(GestureDetector parent) {
        this.gestureDetector = parent;
    }

    public GestureDetector getParent() {
        return gestureDetector;
    }
}
