package com.fruit.logic.objects.abstracted;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.logic.Constants;

/**
 * Main abstract game object class from which all game entities will
 * inherit from.
 * EVERY GAME OBJECT MUST TAKE OBJECT MANAGER IN THE CONSTRUCTOR
 * AS THE MEANS TO DESTROY ITSELF.
 */
public abstract class GameObject implements Constants {
    //general boolean for debug purposes, can be used to make some additional functionality of the object available
    public boolean debug = false;
    //Box2d rigid body representing this object.
    protected Body body;
    //bodydef to be used when re-creating
    //body category bit, for use with collsiion filtering
    protected int categoryBit;

    //typeID for this object to be used with rendering ( each object will have its own animation/resource pack that will
    //know how to draw this specific object).
    protected int typeID;

    //group ID, very simmiliar to categoryID of the body, but is not bound to any fixture and is more easily obtained
    protected int groupID;

    //last known x position of this object, useful when loading it to the world after it has been
    //temporally removed, for example when changing maps back and forth.
    protected float lastKnownX;
    protected float lastKnownY;


    //Update method, can contain AI code etc.
    public abstract void update(float delta);

    //Mandatory addToWorld method, so constructors of game objects doesnt have to bind to the box2d
    //world on creation
    public abstract void addToWorld(World world);

    //every gameobject needs to know how to kill itself
    public abstract void killYourself();

    public Body getBody(){
        return body;
    }

    public void setCategoryBit(int bit){
        categoryBit = bit;
    }

    public int getCategoryBit(){
        return categoryBit;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }


    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }


    public float getLastKnownY() {
        return lastKnownY;
    }

    public void setLastKnownY(float lastKnownY) {
        this.lastKnownY = lastKnownY;
    }

    public float getLastKnownX() {
        return lastKnownX;
    }

    public void setLastKnownX(float lastKnownX) {
        this.lastKnownX = lastKnownX;
    }


}
