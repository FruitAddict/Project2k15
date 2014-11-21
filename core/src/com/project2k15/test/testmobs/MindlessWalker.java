package com.project2k15.test.testmobs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.managers.ObjectManager;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.entities.CollisionResolver;
import com.project2k15.logic.entities.Player;
import com.project2k15.logic.entities.abstracted.Character;
import com.project2k15.rendering.Assets;

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
    ObjectManager manager;


    public MindlessWalker(float positionX, float positionY, float width, float height, float speed, Player player, SpriteBatch batch, ObjectManager manager) {
        position.set(positionX, positionY);
        this.width = width;
        this.height = height;
        this.player = player;
        collisionRectangle = new PropertyRectangle(position.x, position.y, width, height / 2, this, PropertyRectangle.CHARACTER);
        this.speed = speed;
        maxVelocity = 100;
        healthPoints = 5;
        this.batch = batch;
        this.manager = manager;

        Texture testM = Assets.manager.get("pet.png");
        Texture test2m = Assets.manager.get("frontwalk.png");
        TextureRegion[][] tmpM = TextureRegion.split(testM, testM.getWidth() / 8, testM.getHeight() / 4);

        TextureRegion[][] tmp2 = TextureRegion.split(test2m, test2m.getWidth()/8, test2m.getHeight());
        mobWalkSouth = new TextureRegion[8];
        mobWalkNorth = new TextureRegion[8];
        mobWalkRight = new TextureRegion[8];
        mobWalkLeft = new TextureRegion[8];

        TextureRegion[] walkTest = new TextureRegion[8];

        for(int i=0;i<8;i++){
            walkTest[i] = tmp2[0][i];
        }

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

        northAnimation = new Animation(0.1f, walkTest);
        southAnimation = new Animation(0.1f, walkTest);
        rightAnimation = new Animation(0.1f, walkTest);
        leftAnimation = new Animation(0.1f,walkTest);

        facingDown = true;
    }

    @Override
    public void update(float delta, Array<PropertyRectangle> checkRectangles) {
        if (healthPoints < 1) {
            manager.removeObject(this);
        }
        CollisionResolver.resolveCollisions(delta, checkRectangles, this);
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

    public static MindlessWalker getRandomWalker(float x, float y, SpriteBatch batch, ObjectManager manager) {
        return new MindlessWalker(x, y, 25, 25, 25, player, batch, manager);
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        getCollisionRectangle().setSize(width, height / 2);

    }


    public TextureRegion getCurrentFrame() {
        if (facingUp) {
            return northAnimation.getKeyFrame(stateTime, true);
        } else if (facingDown) {
            return southAnimation.getKeyFrame(stateTime, true);
        } else if (facingRight) {
            return rightAnimation.getKeyFrame(stateTime, true);
        } else if (facingLeft) {
            return leftAnimation.getKeyFrame(stateTime, true);
        }

        return null;
    }

}
