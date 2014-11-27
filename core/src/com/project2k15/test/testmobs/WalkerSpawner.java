package com.project2k15.test.testmobs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.RectangleTypes;
import com.project2k15.logic.managers.Controller;
import com.project2k15.logic.managers.ObjectManager;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.entities.abstracted.MovableObject;

import java.util.Random;

/**
 * Created by FruitAddict on 2014-11-18.
 */
@Deprecated
public class WalkerSpawner extends MovableObject implements RectangleTypes {

    private float stateTime;
    private ObjectManager manager;
    private SpriteBatch batch;
    private Random random;
    private Controller controller;

    public WalkerSpawner(float positionX, float positionY, ObjectManager manager, SpriteBatch batch, Controller controller) {
        width = 1;
        height = 1;
        position.set(positionX, positionY);
        collisionRectangle = new PropertyRectangle(position.x, position.y, width, height, this, MOVING_OBJECT_REC);
        this.manager = manager;
        this.batch = batch;
        random = new Random();
        this.controller= controller;
    }

    @Override
    public void update(float delta, Array<PropertyRectangle> checkRectangles) {
        stateTime += delta;
        if (stateTime > 5) {
            manager.addObject(MindlessWalker.getRandomWalker(position.x + random.nextInt(50), position.y + random.nextInt(50), batch, manager, controller));
            stateTime = 0;
        }
    }
}
