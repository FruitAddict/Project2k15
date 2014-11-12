package com.project2k15.entities;

import com.badlogic.gdx.math.Rectangle;
import com.project2k15.entities.abstracted.MovableObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by FruitAddict on 2014-11-12.
 */
public class MindlessWalker extends MovableObject {
    static Player player;
    static Random rng = new Random();
    float timeSpentDoingShit;
    int random = 0;


    public MindlessWalker(float positionX, float positionY, float width, float height, float speed, Player player) {
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
        if (timeSpentDoingShit == 0) {
            random = rng.nextInt(4);
            System.out.println(random);
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
        } else if (timeSpentDoingShit > 0 && timeSpentDoingShit < 3) {
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
    }

    public static MindlessWalker getRandomWalker(float x, float y) {
        Random rng = new Random();
        return new MindlessWalker(x, y, 10 + rng.nextInt(50), 10 + rng.nextInt(50), 5 + rng.nextInt(30), player);
    }

}
