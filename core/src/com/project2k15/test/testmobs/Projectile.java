package com.project2k15.test.testmobs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.ObjectManager;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.entities.CollisionResolver;
import com.project2k15.logic.entities.abstracted.MovableObject;
import com.project2k15.rendering.Assets;

/**
 * Created by FruitAddict on 2014-11-17.
 */
public class Projectile extends MovableObject {

    Animation animation;
    float stateTime;
    ObjectManager objectManager;
    SpriteBatch batch;
    Vector2 direction;

    public Projectile(float originX, float originY, int width, int height, Vector2 direction, ObjectManager objectManager, SpriteBatch batch) {
        this.width = width;
        this.direction = direction;
        this.height = height;
        position.set(originX, originY);
        this.objectManager = objectManager;
        this.batch = batch;
        maxVelocity = 200;
        clamping = 1;
        Texture testM = Assets.manager.get("projectile.png");
        TextureRegion[][] tmpM = TextureRegion.split(testM, testM.getWidth() / 4, testM.getHeight());
        TextureRegion[] animRegion = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            animRegion[i] = tmpM[0][i];
        }
        animation = new Animation(0.05f, animRegion);
        collisionRectangles.add(new PropertyRectangle(position.x, position.y, width, height, PropertyRectangle.MOVING_OBJECT));
    }

    @Override
    public void update(float delta, Array<PropertyRectangle> checkRectangles) {
        velocity.set(direction.x, direction.y * -1).scl(maxVelocity);
        batch.draw(getCurrentFrame(), position.x, position.y, width, height);
        position.add(velocity.scl(delta));
        getCollisionRectangles().get(0).setPosition(position.x, position.y);
        if (CollisionResolver.resolveCollisionsWithTerrainSimple(checkRectangles, this) || (velocity.x < 1 && velocity.x > -1) && (velocity.y < 1 && velocity.y > -1)) {
            objectManager.removeObject(this);
        }
        stateTime += delta;
    }

    public TextureRegion getCurrentFrame() {
        return animation.getKeyFrame(stateTime, true);
    }
}
