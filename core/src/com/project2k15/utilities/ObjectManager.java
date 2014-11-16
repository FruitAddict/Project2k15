package com.project2k15.utilities;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.project2k15.entities.abstracted.MovableObject;
import com.project2k15.utilities.quadtree.QuadRectangle;
import com.project2k15.utilities.quadtree.Quadtree;

import java.util.ArrayList;

/**
 * Created by FruitAddict on 2014-11-10.
 */
public class ObjectManager {

    private ArrayList<MovableObject> objectList = new ArrayList<MovableObject>();
    private ArrayList<Rectangle> passRectangleList;
    private MapObjects collisionObjects;
    private Quadtree quadtree;

    public ObjectManager(MapLayer t, int mapWidth, int mapHeight) {
        collisionObjects = t.getObjects();
        quadtree = new Quadtree(0, new QuadRectangle(0, 0, mapWidth, mapHeight));
        passRectangleList = new ArrayList<Rectangle>();

    }

    public void setCollisionObjects(MapLayer til) {
        collisionObjects = null;
        collisionObjects = til.getObjects();
    }


    public void update(float delta) {
        passRectangleList.clear();
        quadtree.clear();
        for (int i = 0; i < collisionObjects.getCount(); i++) {
            RectangleMapObject obj = (RectangleMapObject) collisionObjects.get(i);
            quadtree.insert(obj.getRectangle());
        }
        for (int i = 0; i < objectList.size(); i++) {
            quadtree.insert(objectList.get(i).getCollisionRectangles().get(0));
        }
        if (objectList.size() > 0) {
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

}
