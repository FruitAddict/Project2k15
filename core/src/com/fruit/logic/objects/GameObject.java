package com.fruit.logic.objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.logic.Constants;

/**
 * Main abstract game object class from which all game entities will
 * inherit from.
 */
public abstract class GameObject implements Constants {
    //Box2d rigid body representing this object.
    protected Body body;
    //body category bit, for use with collsiion filtering
    protected int categoryBit;

    //typeID for this object to be used with rendering ( each object will have its own animation/resource pack that will
    //know how to draw this specific object).
    protected int typeID;

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

}
