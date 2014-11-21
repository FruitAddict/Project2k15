package com.project2k15.logic.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.managers.ObjectManager;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.entities.abstracted.Character;
import com.project2k15.rendering.Assets;
import com.project2k15.test.testmobs.Projectile;

/**
 * Test player class
 */
public class Player extends Character {

    private PropertyRectangle colRect;
    public boolean piercingShotsDebug = false;
    Animation animationNorth;
    Animation animationSouth;
    Animation animationWest;
    Animation animationEast;
    TextureRegion[] southRegion = new TextureRegion[3];
    TextureRegion[] northRegion = new TextureRegion[3];
    TextureRegion[] eastRegion = new TextureRegion[3];
    TextureRegion[] westRegion = new TextureRegion[3];
    float stateTime;
    float lastAttack = 0;
    float timeBetweenAttacks = 0.1f;
    SpriteBatch batch;
    ObjectManager manager;


    public Player(float positionX, float positionY, SpriteBatch batch, ObjectManager manager) {
        width = 48;
        height = 64;
        position.set(positionX, positionY);
        colRect = new PropertyRectangle(positionX, positionY, 48, 32, this, PropertyRectangle.PLAYER);
        collisionRectangle = colRect;
        speed = 20;
        maxVelocity = 150;
        clamping = 0.88f;
        this.batch = batch;
        this.manager = manager;
        facingDown = true;
        Texture testT = Assets.manager.get("redheady.png");
        TextureRegion[][] tmp = TextureRegion.split(testT, testT.getWidth() / 3, testT.getHeight() / 4);
        southRegion = new TextureRegion[3];
        northRegion = new TextureRegion[3];
        eastRegion = new TextureRegion[3];
        westRegion = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            southRegion[i] = tmp[0][i];
            westRegion[i] = tmp[1][i];
            northRegion[i] = tmp[2][i];
            eastRegion[i] = tmp[3][i];
        }


        animationSouth = new Animation(0.1f, southRegion);
        animationNorth = new Animation(0.1f, northRegion);
        animationWest = new Animation(0.1f, westRegion);
        animationEast = new Animation(0.1f, eastRegion);


    }

    @Override
    public void update(float delta, Array<PropertyRectangle> collisionRecs) {
        CollisionResolver.resolveCollisions(delta, collisionRecs, this);
        stateTime += delta;
        batch.draw(getCurrentFrame(), getPosition().x, getPosition().y, getWidth(), getHeight());
    }

    public void attack(Vector2 direction) {
        if (stateTime - lastAttack > timeBetweenAttacks) {
            manager.addObject(new Projectile(position.x, position.y, 12, 12, direction, manager, batch, piercingShotsDebug));
            manager.addObject(new Projectile(position.x, position.y, 12, 12, direction.cpy().add(-0.1f,-0.1f), manager, batch, piercingShotsDebug));
            manager.addObject(new Projectile(position.x, position.y, 12, 12, direction.cpy().add(0.1f,0.1f), manager, batch, piercingShotsDebug));
            lastAttack = stateTime;
        }
    }

    public TextureRegion getCurrentFrame() {
        if (idle) {
            return southRegion[1];
        } else if (facingUp) {
            return animationNorth.getKeyFrame(stateTime, true);
        } else if (facingDown) {
            return animationSouth.getKeyFrame(stateTime, true);
        } else if (facingRight) {
            return animationEast.getKeyFrame(stateTime, true);
        } else if (facingLeft) {
            return animationWest.getKeyFrame(stateTime, true);
        }

        return null;
    }


}
