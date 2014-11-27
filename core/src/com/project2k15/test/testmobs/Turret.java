package com.project2k15.test.testmobs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.collision.RectangleTypes;
import com.project2k15.logic.entities.Player;
import com.project2k15.logic.entities.abstracted.MovableObject;
import com.project2k15.logic.managers.ObjectManager;

import java.util.Random;

/**
 * Created by FruitAddict on 2014-11-24.
 */
@Deprecated
public class Turret extends MovableObject implements RectangleTypes {
    private ObjectManager objectManager;
    private SpriteBatch batch;
    private float stateTime=0;
    private float timeBetweenAttacks = 0.25f;
    private float lastAttack;
    private float rotationStart = 1;
    private float firstProj=0;
    private Player player;
    private Random random;

    public Turret(float positionX, float positionY, ObjectManager manager, SpriteBatch batch, Player player){
        random = new Random();
        this.player =player;
        width = 12;
        height = 12;
        position.set(positionX, positionY);
        collisionRectangle = new PropertyRectangle(position.x, position.y, width, height, this, MOVING_OBJECT_REC);
        objectManager = manager;
        this.batch = batch;
    }
    @Override
    public void update(float delta, Array<PropertyRectangle> checkRectangles) {
        stateTime+=delta;
        Vector2 velocityNormalized = new Vector2().set(-1*(position.x - player.getPosition().x), position.y - player.getPosition().y);
        velocityNormalized.nor();
        if (stateTime - lastAttack > timeBetweenAttacks) {
            objectManager.addObject(new Projectile(position.x, position.y, 12, 12, velocityNormalized, false,2,objectManager,TERRAIN_REC, PLAYER_REC, CHARACTER_REC));
            lastAttack = stateTime;
        }
        rotationStart+=0.01f;
        if(rotationStart>1){
            rotationStart=-1;
        }
        firstProj+=0.01f;
        if(firstProj>1){
            firstProj=-1;
        }
    }
}
