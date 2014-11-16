package com.project2k15.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.project2k15.entities.abstracted.Character;
import com.project2k15.utilities.Assets;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by FruitAddict on 2014-11-12.
 */
public class MindlessWalker extends Character {
    static Player player;
    static Random rng = new Random();
    float timeSpentDoingShit;
    int random = 0;
    TextureRegion[] mobWalkSouth;
    TextureRegion[] mobWalkNorth;
    TextureRegion[] mobWalkRight;
    TextureRegion[] mobWalkLeft;
    Animation southAnimation;
    Animation northAnimation;
    Animation rightAnimation;
    Animation leftAnimation;
    float stateTime;
    SpriteBatch batch;


    public MindlessWalker(float positionX, float positionY, float width, float height, float speed, Player player, SpriteBatch batch) {
        position.set(positionX, positionY);
        this.width = width;
        this.height = height;
        this.player = player;
        collisionRectangles.add(new Rectangle(position.x, position.y, width, height));
        this.speed = speed;
        maxVelocity = 100;

        this.batch = batch;

        Texture testM = Assets.manager.get("pet.png");
        TextureRegion[][] tmpM = TextureRegion.split(testM, testM.getWidth() / 8, testM.getHeight() / 4);
        mobWalkSouth = new TextureRegion[8];
        mobWalkNorth = new TextureRegion[8];
        mobWalkRight = new TextureRegion[8];
        mobWalkLeft = new TextureRegion[8];
        for (int i = 0; i < 8; i++) {
            mobWalkSouth[i] = tmpM[0][i];
        }
        for (int i = 0; i < 8; i++) {
            mobWalkNorth[i] = tmpM[1][i];
        }
        for (int i = 0; i < 8; i++) {
            mobWalkLeft[i] = tmpM[2][i];
        }
        for (int i = 0; i < 8; i++) {
            mobWalkRight[i] = tmpM[3][i];
        }

        northAnimation = new Animation(0.1f, mobWalkNorth);
        southAnimation = new Animation(0.1f, mobWalkSouth);
        rightAnimation = new Animation(0.1f, mobWalkRight);
        leftAnimation = new Animation(0.1f, mobWalkLeft);

        facingSouth = true;
    }

    @Override
    public void update(float delta, ArrayList<Rectangle> checkRectangles) {
        super.update(delta, checkRectangles);
        if (timeSpentDoingShit == 0) {
            random = rng.nextInt(4);
            stateTime += delta;
            switch (random) {
                case 0: {
                    moveDown();
                    break;
                }
                case 1: {
                    moveRight();
                    break;
                }
                case 2: {
                    moveUp();
                    break;
                }
                case 3: {
                    moveLeft();
                    break;
                }
            }
            timeSpentDoingShit += delta;
        } else if (timeSpentDoingShit > 0 && timeSpentDoingShit < 1) {
            stateTime += delta;
            switch (random) {
                case 0: {
                    moveDown();
                    break;
                }
                case 1: {
                    moveRight();
                    break;
                }
                case 2: {
                    moveUp();
                    break;
                }
                case 3: {
                    moveLeft();
                    break;
                }

            }
            timeSpentDoingShit += delta;
        } else {
            timeSpentDoingShit = 0;
        }

        batch.draw(getCurrentFrame(), getPosition().x, getPosition().y, getWidth(), getHeight());
    }

    public static MindlessWalker getRandomWalker(float x, float y, SpriteBatch batch) {
        Random rng = new Random();
        return new MindlessWalker(x, y, 10 + rng.nextInt(50), 10 + rng.nextInt(50), 5 + rng.nextInt(30), player, batch);
    }


    public TextureRegion getCurrentFrame() {
        if (facingNorth) {
            return northAnimation.getKeyFrame(stateTime, true);
        } else if (facingSouth) {
            return southAnimation.getKeyFrame(stateTime, true);
        } else if (facingRight) {
            return rightAnimation.getKeyFrame(stateTime, true);
        } else if (facingLeft) {
            return leftAnimation.getKeyFrame(stateTime, true);
        }

        return null;
    }

}
