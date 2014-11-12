package com.project2k15.entities;

import com.badlogic.gdx.math.Rectangle;
import com.project2k15.entities.abstracted.MovableObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by FruitAddict on 2014-11-11.
 */
public class MindlessBlob extends MovableObject {
    static Player player;

    public MindlessBlob(float positionX, float positionY, float width, float height, float speed, Player player) {
        position.set(positionX, positionY);
        this.width = width;
        this.height = height;
        this.player = player;
        collisionRectangles.add(new Rectangle(position.x, position.y, width, height));
        this.speed = speed;
        maxVelocity = 100;
    }

    @Override
    public void update(float delta, ArrayList<Rectangle> checkRectangles) {
        super.update(delta, checkRectangles);

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
}
