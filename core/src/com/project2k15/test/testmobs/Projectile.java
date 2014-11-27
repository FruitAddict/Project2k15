package com.project2k15.test.testmobs;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.RectangleTypes;
import com.project2k15.logic.entities.EntityTypes;
import com.project2k15.logic.managers.ObjectManager;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.collision.CollisionResolver;
import com.project2k15.logic.entities.abstracted.Character;
import com.project2k15.logic.entities.abstracted.MovableObject;

/**
 * Test projectile
 */
@Deprecated
public class Projectile extends MovableObject implements RectangleTypes, EntityTypes {

    float stateTime;
    ObjectManager objectManager;
    Vector2 direction;
    public boolean dead = false;
    float timeDead = 0;
    boolean pierce = false;
    int[] targetTypes;
    float knockBackMultiplier;

    public Projectile(float originX, float originY, int width, int height, Vector2 direction, boolean piercing,float knockbackmultiplier,ObjectManager om, int... targetTypes) {
        this.knockBackMultiplier = knockbackmultiplier;
        pierce = piercing;
        this.width = width;
        this.direction = direction;
        this.height = height;
        position.set(originX, originY);
        maxVelocity = 200;
        clamping = 1;
        this.targetTypes = targetTypes;
        this.objectManager=om;
        setTypeID(PROJECTILE);
        /**
        Texture testM = Assets.manager.get("projectile.png");
        TextureRegion[][] tmpM = TextureRegion.split(testM, testM.getWidth() / 4, testM.getHeight());
        TextureRegion[] animRegion = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            animRegion[i] = tmpM[0][i];
        }
        animation = new Animation(0.1f, animRegion);
        Texture testM2 = Assets.manager.get("deadfireball.png");
        TextureRegion[][] tmpM2 = TextureRegion.split(testM2, testM2.getWidth() / 4, testM2.getHeight());
        TextureRegion[] animRegion2 = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            animRegion2[i] = tmpM2[0][i];
        }
        animationDead = new Animation(0.1f, animRegion2);
         */
        collisionRectangle = new PropertyRectangle(position.x, position.y, width, height, PROJECTILE_REC);
    }

    @Override
    public void update(float delta, Array<PropertyRectangle> checkRectangles) {
        if (dead) {
            timeDead += delta;
            if (timeDead > 1) {
                objectManager.removeObject(this);
            }
        } else {
            velocity.set(direction.x, direction.y * -1).scl(maxVelocity);
            position.add(velocity.scl(delta));
            getCollisionRectangle().setPosition(position.x, position.y);
            PropertyRectangle rec = CollisionResolver.resolveCollisionsByType(checkRectangles, this,targetTypes);
            if (rec != null) {
                if (rec.getType() == TERRAIN_REC) {
                    dead = true;
                    width *= 1.5;
                    height *= 1.5;
                }
                else{
                    if(rec.getOwner() instanceof Character) {
                        Character ch = (Character) rec.getOwner();
                        ch.setHealthPoints(ch.getHealthPoints() - 1);
                        ch.hitByPlayer=true;
                    }
                    if(rec.getOwner() instanceof MovableObject) {
                        ((MovableObject) (rec.getOwner())).getVelocity().add(this.velocity.x * 50 *knockBackMultiplier, this.velocity.y * 50*knockBackMultiplier);
                    }
                    if (!pierce) {
                        dead = true;
                    }
                    width *= 1.05;
                    height *= 1.05;

                }
            } else {
            }
            if ((velocity.x < 1 && velocity.x > -1) && (velocity.y < 1 && velocity.y > -1)) {
                objectManager.removeObject(this);
            }
        }
        stateTime += delta;
    }
}
