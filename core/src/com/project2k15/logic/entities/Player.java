package com.project2k15.logic.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.CollisionResolver;
import com.project2k15.logic.collision.RectangleTypes;
import com.project2k15.logic.managers.MapManager;
import com.project2k15.logic.managers.ObjectManager;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.entities.abstracted.Character;
import com.project2k15.logic.maps.Room;
import com.project2k15.rendering.Assets;
import com.project2k15.test.testmobs.Projectile;

/**
 * Test player class
 */
public class  Player extends Character implements RectangleTypes {

    private PropertyRectangle colRect;
    public boolean piercingShotsDebug = false;
    private Animation animationNorth;
    private Animation animationSouth;
    private Animation animationWest;
    private Animation animationEast;
    private TextureRegion[] southRegion = new TextureRegion[3];
    private TextureRegion[] northRegion = new TextureRegion[3];
    private TextureRegion[] eastRegion = new TextureRegion[3];
    private TextureRegion[] westRegion = new TextureRegion[3];
    private float stateTime;
    private float lastAttack = 0;
    private float timeBetweenAttacks = 0.1f;
    private SpriteBatch batch;
    private ObjectManager objectManager;
    private MapManager mapManager;
    private Room currentRoom;


    public Player(float positionX, float positionY, SpriteBatch batch, ObjectManager objectManager) {
        width = 48;
        height = 64;
        position.set(positionX, positionY);
        colRect = new PropertyRectangle(positionX, positionY, 48, 32, this, PLAYER);
        collisionRectangle = colRect;
        speed = 20;
        maxVelocity = 150;
        clamping = 0.88f;
        this.batch = batch;
        this.objectManager = objectManager;
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

    public void setMapManager(MapManager mapManager){
        this.mapManager = mapManager;
    }

    public void setCurrentRoom(Room room){
        currentRoom = room;
    }

    @Override
    public void update(float delta, Array<PropertyRectangle> collisionRecs) {
        /**
         * Check whether you're not colliding with a map transition portal
         * if so, handle it
         */
        PropertyRectangle rec = CollisionResolver.resolveCollisionsByType(collisionRecs, this, PORTAL_EAST, PORTAL_NORTH, PORTAL_SOUTH, PORTAL_WEST);
        if(rec != null){
            mapManager.getCurrentMap().changeRoom(rec.getLinkID());
            objectManager.onRoomChanged();
            position.set(mapManager.getCurrentMap().getCurrentRoom().getSpawnPositionByType(rec.getType()));
            objectManager.addObject(this);
            System.out.println("portal");
        }
        /**
         * Resolve terrain collisions
         */
        CollisionResolver.resolveCollisionsTerrain(delta, collisionRecs, this);
        stateTime += delta;
        batch.draw(getCurrentFrame(), getPosition().x, getPosition().y, getWidth(), getHeight());
    }

    public void attack(Vector2 direction) {
        if (stateTime - lastAttack > timeBetweenAttacks) {
            objectManager.addObject(new Projectile(position.x, position.y, 12, 12, direction, objectManager, batch, piercingShotsDebug,1,TERRAIN,CHARACTER));
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
