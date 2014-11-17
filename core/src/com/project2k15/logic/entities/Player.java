package com.project2k15.logic.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.entities.abstracted.Character;
import com.project2k15.logic.quadtree.PropertyRectangle;
import com.project2k15.rendering.Assets;

/**
 * Test player class
 */
public class Player extends Character {

    private PropertyRectangle colRect;
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
    SpriteBatch batch;


    public Player(float positionX, float positionY, SpriteBatch batch) {
        width = 24;
        height = 32;
        position.set(positionX, positionY);
        colRect = new PropertyRectangle(positionX, positionY, 24, 16, PropertyRectangle.MOVING_OBJECT);
        collisionRectangles.add(colRect);
        speed = 20;
        maxVelocity = 75;
        clamping = 0.88f;
        this.batch = batch;

        facingSouth = true;
        Texture testT = Assets.manager.get("redheady.png");
        TextureRegion[][] tmp = TextureRegion.split(testT, testT.getWidth() / 3, testT.getHeight() / 4);
        southRegion = new TextureRegion[3];
        northRegion = new TextureRegion[3];
        eastRegion = new TextureRegion[3];
        westRegion = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            southRegion[i] = tmp[0][i];
            westRegion[i] = tmp[1][i];
            northRegion[i] = tmp[2][i];
            eastRegion[i] = tmp[3][i];
        }


        animationSouth = new Animation(0.1f, southRegion);
        animationNorth = new Animation(0.1f, northRegion);
        animationWest = new Animation(0.1f, westRegion);
        animationEast = new Animation(0.1f, eastRegion);


    }

    @Override
    public void update(float delta, Array<PropertyRectangle> collisionRecs) {
        super.update(delta, collisionRecs);
        stateTime += delta;
        batch.draw(getCurrentFrame(), getPosition().x, getPosition().y, getWidth(), getHeight());
    }

    public TextureRegion getCurrentFrame() {
        if (idle) {
            return southRegion[1];
        } else if (facingNorth) {
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
