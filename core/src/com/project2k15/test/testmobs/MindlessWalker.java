package com.project2k15.test.testmobs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.RectangleTypes;
import com.project2k15.logic.managers.Controller;
import com.project2k15.logic.managers.ObjectManager;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.collision.CollisionResolver;
import com.project2k15.logic.entities.Player;
import com.project2k15.logic.entities.abstracted.Character;
import com.project2k15.rendering.Assets;

import java.util.Random;

/**
 * Created by FruitAddict on 2014-11-12.
 */
public class MindlessWalker extends Character implements RectangleTypes {
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
    static SpriteBatch batch;
    ObjectManager manager;
    private Controller controller;


    public MindlessWalker(float positionX, float positionY, float width, float height, float speed, SpriteBatch batch, ObjectManager manager, Controller controller) {
        position.set(positionX, positionY);
        this.width = width;
        this.height = height;
        collisionRectangle = new PropertyRectangle(position.x, position.y, width, height / 2, this, CHARACTER);
        this.speed = speed;
        maxVelocity = 100;
        healthPoints = 5;
        this.batch = batch;
        this.manager = manager;
        this.controller = controller;

        Texture testM = Assets.manager.get("pet.png");
        Texture test2m = Assets.manager.get("front.png");
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

        facingS = true;
    }

    @Override
    public void update(float delta, Array<PropertyRectangle> checkRectangles) {
        if (healthPoints < 1) {
            manager.removeObject(this);
            controller.getMapManager().getCurrentMap().getCurrentRoom().getGameObjectList().removeValue(this,true);
        }
        CollisionResolver.resolveCollisionsTerrain(delta, checkRectangles, this);
        if(!hitByPlayer) {
            if (timeSpentDoingShit == 0) {
                random = rng.nextInt(4);
                stateTime += delta;
                switch (random) {
                    case 0: {
                        moveSouth();
                        break;
                    }
                    case 1: {
                        moveEast();
                        break;
                    }
                    case 2: {
                        moveNorth();
                        break;
                    }
                    case 3: {
                        moveWest();
                        break;
                    }
                }
                timeSpentDoingShit += delta;
            } else if (timeSpentDoingShit > 0 && timeSpentDoingShit < 1) {
                stateTime += delta;
                switch (random) {
                    case 0: {
                        moveSouth();
                        break;
                    }
                    case 1: {
                        moveEast();
                        break;
                    }
                    case 2: {
                        moveNorth();
                        break;
                    }
                    case 3: {
                        moveWest();
                        break;
                    }

                }
                timeSpentDoingShit += delta;
            } else {
                timeSpentDoingShit = 0;
            }
            batch.draw(getCurrentFrame(), getPosition().x, getPosition().y, getWidth(), getHeight());
        } else {
            setWidth(64);
            setHeight(64);
            maxVelocity = 150;
            speed = 75;
            stateTime+=delta;
            float distance = (float) (Math.sqrt(Math.pow(position.x - controller.getPlayer().getPosition().x, 2) + Math.pow(position.y - controller.getPlayer().getPosition().y, 2)));
            if (distance > 10) {
                if (controller.getPlayer().getPosition().x > position.x) {
                    moveEast();
                } else if (controller.getPlayer().getPosition().x < position.x) {
                    moveWest();
                }
                if (controller.getPlayer().getPosition().y > position.y) {
                    moveNorth();
                } else if (controller.getPlayer().getPosition().y < position.y) {
                    moveSouth();
                }
            }
            batch.setColor(1.0f,0.3f,0.3f,1);
            batch.draw(getCurrentFrame(), getPosition().x, getPosition().y, getWidth(), getHeight());
            batch.setColor(1.0f,1.0f,1.0f,1);
        }

    }

    public static MindlessWalker getRandomWalker(float x, float y, SpriteBatch batch, ObjectManager manager, Controller con) {
        return new MindlessWalker(x, y, 32, 32, 25, batch, manager, con);
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        getCollisionRectangle().setSize(width, height / 2);

    }


    public TextureRegion getCurrentFrame() {
        if (facingN) {
            return northAnimation.getKeyFrame(stateTime, true);
        } else if (facingS) {
            return southAnimation.getKeyFrame(stateTime, true);
        } else if (facingE) {
            return rightAnimation.getKeyFrame(stateTime, true);
        } else if (facingW) {
            return leftAnimation.getKeyFrame(stateTime, true);
        }

        return null;
    }

}
