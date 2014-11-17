package com.project2k15.logic.entities.testmobs;

import com.project2k15.logic.entities.abstracted.MovableObject;

/**
 * Created by FruitAddict on 2014-11-17.
 */
public class Projectile extends MovableObject {

    public Projectile(float originX, float originY, float targetX, float targetY, int width, int height) {
        position.set(originX, originY);
        velocity.set(targetX - position.x, targetY - position.y).nor().scl(Math.min(position.dst(targetX, targetY), maxVelocity));
    }
}
