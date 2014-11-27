package com.project2k15.logic.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.CollisionResolver;
import com.project2k15.logic.collision.RectangleTypes;
import com.project2k15.logic.managers.Controller;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.entities.abstracted.Character;
import com.project2k15.test.testmobs.Projectile;

/**
 * Test player class
 */
public class  Player extends Character implements RectangleTypes, EntityTypes {

    private PropertyRectangle colRect;
    public boolean piercingShotsDebug = false;
    private float stateTime;
    private float lastAttack = 0;
    private float timeBetweenAttacks = 0.25f;
    private Controller controller;


    public Player() {
        width = 48;
        height = 64;
        colRect = new PropertyRectangle(0, 0, 48, 32, this, PLAYER_REC);
        collisionRectangle = colRect;
        speed = 20;
        maxVelocity = 150;
        clamping = 0.88f;
        setTypeID(PLAYER);
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    @Override
    public void update(float delta, Array<PropertyRectangle> collisionRecs) {
        //update facing method from the super class
        updateFacing();
        /**
         * Check whether you're not colliding with a map transition portal
         * if so, handle it
         */
        PropertyRectangle rec = CollisionResolver.resolveCollisionsByType(collisionRecs, this, PORTAL_EAST_REC, PORTAL_NORTH_REC, PORTAL_SOUTH_REC, PORTAL_WEST_REC);
        if(rec != null){
            controller.getMapManager().getCurrentMap().changeRoom(rec.getLinkID());
            controller.getObjectManager().onRoomChanged();
            position.set(controller.getMapManager().getCurrentMap().getCurrentRoom().getSpawnPositionByType(rec.getType()));
            controller.getObjectManager().clear();
            controller.getCam().position.set(position.x,position.y,0);
        }
        /**
         * Resolve terrain collisions
         */
        CollisionResolver.resolveCollisionsTerrain(delta, collisionRecs, this);
        stateTime += delta;
    }

    public void attack(Vector2 direction) {
        if (stateTime - lastAttack > timeBetweenAttacks) {
            lastAttack = stateTime;
            controller.getObjectManager().addObject(new Projectile(position.x,position.y,12,12,direction,piercingShotsDebug,2,controller.getObjectManager(),TERRAIN_REC,CHARACTER_REC));
        }
    }

}
