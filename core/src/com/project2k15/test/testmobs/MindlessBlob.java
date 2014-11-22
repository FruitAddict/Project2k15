package com.project2k15.test.testmobs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.collision.RectangleTypes;
import com.project2k15.logic.entities.Player;
import com.project2k15.logic.entities.abstracted.Character;
import com.project2k15.rendering.Assets;

import java.util.Random;

/**
 * Created by FruitAddict on 2014-11-11.
 */
public class MindlessBlob extends Character implements RectangleTypes {
    static Player player;
    TextureRegion[] mobWalkSouth;
    TextureRegion[] mobWalkNorth;
    TextureRegion[] mobWalkRight;
    TextureRegion[] mobWalkLeft;
    Animation southAnimation;
    Animation northAnimation;
    Animation rightAnimation;
    Animation leftAnimation;
    float stateTime;

    public MindlessBlob(float positionX, float positionY, float width, float height, float speed, Player player) {
        position.set(positionX, positionY);
        this.width = width;
        this.height = height;
        this.player = player;
        collisionRectangle = new PropertyRectangle(position.x, position.y, width, height, MOVING_OBJECT);
        this.speed = speed;
        maxVelocity = 100;
        setClamping(0.82f);
        stateTime = 0;

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

        facingDown = true;

    }

    @Override
    public void update(float delta, Array<PropertyRectangle> checkRectangles) {
        stateTime += delta;
        float distance = (float) (Math.sqrt(Math.pow(position.x - player.getPosition().x, 2) + Math.pow(position.y - player.getPosition().y, 2)));
        if (distance > 100) {
            if (player.getPosition().x > position.x) {
                moveRight();
            } else if (player.getPosition().x < position.x) {
                moveLeft();
            }
            if (player.getPosition().y > position.y) {
                moveUp();
            } else if (player.getPosition().y < position.y) {
                moveDown();
            }
        }
    }

    public static MindlessBlob getRandomBlob(float x, float y) {
        Random rng = new Random();
        return new MindlessBlob(x, y, 10 + rng.nextInt(50), 10 + rng.nextInt(50), 5 + rng.nextInt(50), player);
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
