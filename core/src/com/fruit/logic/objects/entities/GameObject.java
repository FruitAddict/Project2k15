package com.fruit.logic.objects.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.logic.Constants;

/**
 * Main abstract game object class from which all game entities will
 * inherit from.
 * EVERY GAME OBJECT MUST TAKE OBJECT MANAGER IN THE CONSTRUCTOR
 * AS THE MEANS TO DESTROY ITSELF.
 */
public abstract class GameObject implements Constants{
    //Game Object EntityID's
    public static final int PLAYER = 1;
    public static final int MINDLESS_WALKER =2;
    public static final int BOX = 3;
    public static final int ITEM = 4;
    public static final int DUMMY = 5;
    public static final int PROJECTILE = 6;
    public static final int THE_EYE = 7;
    public static final int EXPLOSION = 8;

    //general boolean for debug purposes, can be used to make some additional functionality of the object available
    public boolean debug = false;
    //Box2d rigid body representing this object.
    protected Body body;
    //bodydef to be used when re-creating
    //body category bit, for use with collsiion filtering
    protected int categoryBit;
    //entityID for this object to be used with rendering ( each object will have its own animation/resource pack that will
    //know how to draw this specific object).
    protected int entityID;
    //saveInRooms boolean, used to mark a game object so rooms can determine whether they should save them or not
    protected int saveInRooms;
    //last known x position of this object, useful when loading it to the world after it has been
    //temporally removed, for example when changing maps back and forth.
    protected float lastKnownX;
    protected float lastKnownY;
    //Width and height of this game object. Only exists to be used with translating from
    //logic coordinates to screen coordinates (Bodies origin point is at their center, textures origin
    //points are at their bottom left corner.
    protected float width, height;

    //Update method, can contain AI code etc.
    public abstract void update(float delta);
    //Mandatory addToBox2dWorld method, so constructors of game objects doesnt have to bind to the box2d
    //world on creation
    public abstract void addToBox2dWorld(World world);
    //every gameobject needs to know how to kill itself
    public abstract void killYourself();

    public Body getBody(){
        return body;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public int getSaveInRooms() {
        return saveInRooms;
    }

    public void setSaveInRooms(int saveInRooms) {
        this.saveInRooms = saveInRooms;
    }

    public void setLastKnownY(float lastKnownY) {
        this.lastKnownY = lastKnownY;
    }

    public void setLastKnownX(float lastKnownX) {
        this.lastKnownX = lastKnownX;
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }

}
