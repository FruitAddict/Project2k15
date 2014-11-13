package com.project2k15.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.project2k15.entities.abstracted.Character;
import com.project2k15.utilities.Assets;

import java.util.ArrayList;

/**
 * Test player class
 */
public class Player extends Character {

    private Rectangle colRect;
    public boolean holding = false;
    public boolean holdingSomething = false;
    Animation animationNorth;
    Animation animationSouth;
    Animation animationWest;
    Animation animationEast;
    TextureRegion[] southRegion = new TextureRegion[3];
    TextureRegion[] northRegion = new TextureRegion[3];
    TextureRegion[] eastRegion = new TextureRegion[3];
    TextureRegion[] westRegion = new TextureRegion[3];
    float stateTime;
    char lastFacing;


    public Player(float positionX, float positionY) {
        width = 24;
        height = 32;
        position.set(positionX, positionY);
        colRect = new Rectangle(positionX, positionY, 24, 16);
        collisionRectangles.add(colRect);
        speed = 20;
        maxVelocity = 100;
        scalar = 0.88f;

        facingSouth = true;
        lastFacing = 'S';

        Texture testT = Assets.manager.get("redheady.png");
        TextureRegion[][] tmp = TextureRegion.split(testT, testT.getWidth() / 3, testT.getHeight() / 3);
        southRegion = new TextureRegion[3];
        northRegion = new TextureRegion[3];
        eastRegion = new TextureRegion[3];
        westRegion = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            southRegion[i] = tmp[0][i];
            westRegion[i] = tmp[1][i];
            northRegion[i] = tmp[2][i];
            eastRegion[i] = tmp[1][i];
        }


        animationSouth = new Animation(0.1f, southRegion);
        animationNorth = new Animation(0.1f, northRegion);
        animationWest = new Animation(0.1f, westRegion);
        animationEast = new Animation(0.1f, eastRegion);


    }

    @Override
    public void update(float delta, ArrayList<Rectangle> collisionRecs) {
        super.update(delta, collisionRecs);
        stateTime += delta;
        if (facingRight && lastFacing != 'R') {
            lastFacing = 'R';
            for (int i = 0; i < 3; i++) {
                westRegion[i].flip(true, false);
            }
        } else if (facingLeft && lastFacing != 'L') {
            lastFacing = 'L';
            for (int i = 0; i < 3; i++) {
                westRegion[i].flip(true, false);
            }
        }

    }

    public TextureRegion getCurrentFrame() {
        if (facingNorth) {
            return animationNorth.getKeyFrame(stateTime, true);
        } else if (facingSouth) {
            return animationSouth.getKeyFrame(stateTime, true);
        } else if (facingRight) {
            return animationEast.getKeyFrame(stateTime, true);
        } else if (facingLeft) {
            return animationWest.getKeyFrame(stateTime, true);
        }

        return null;
    }


}
