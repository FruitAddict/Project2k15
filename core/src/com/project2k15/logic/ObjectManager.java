package com.project2k15.logic;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.collision.QuadRectangle;
import com.project2k15.logic.collision.Quadtree;
import com.project2k15.logic.entities.abstracted.MovableObject;

/**
 * Object Manager
 */
public class ObjectManager {

    private Array<MovableObject> objectList = new Array<MovableObject>();
    private Array<PropertyRectangle> passRectangleList;
    private MapObjects collisionObjects;
    private Quadtree quadtree;

    public ObjectManager(MapLayer t, int mapWidth, int mapHeight) {
        collisionObjects = t.getObjects();
        quadtree = new Quadtree(0, new QuadRectangle(0, 0, mapWidth, mapHeight));
        passRectangleList = new Array<PropertyRectangle>();
    }

    public void setCollisionObjects(MapLayer til) {
        collisionObjects = null;
        collisionObjects = til.getObjects();
    }


    public void update(float delta) {
        passRectangleList.clear();
        quadtree.clear();

        for (int i = 0; i < collisionObjects.getCount(); i++) {
            PropertyRectangle obj = new PropertyRectangle(((RectangleMapObject) collisionObjects.get(i)).getRectangle(), PropertyRectangle.TERRAIN);
            quadtree.insert(obj);
        }

        for (int i = 0; i < objectList.size; i++) {
            quadtree.insert(objectList.get(i).getCollisionRectangles().get(0));
        }

        if (objectList.size > 0) {
            for (MovableObject o : objectList) {
                passRectangleList.clear();
                o.update(delta, quadtree.retrieve(passRectangleList, o.getCollisionRectangles().get(0)));
            }
        }
    }

    public Quadtree getTree() {
        return quadtree;
    }

    public void addObject(MovableObject obj) {
        objectList.add(obj);
    }

    public void removeObject(MovableObject obj) {
        objectList.removeValue(obj, true);
    }

    public int getNumberOfObjects() {
        return objectList.size;
    }

}
