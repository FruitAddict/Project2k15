package com.project2k15.utilities;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.project2k15.entities.abstracted.MovableObject;

import java.util.ArrayList;

/**
 * Created by FruitAddict on 2014-11-10.
 */
public class ObjectManager {

    public ArrayList<MovableObject> objectList = new ArrayList<MovableObject>();
    private Pool<Rectangle> rectanglePool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };
    private ArrayList<Rectangle> stupidPlaceholder = new ArrayList<Rectangle>();

    private TiledMapTileLayer collisionLayer;

    public ObjectManager(MapLayer t) {
        MapObjects collisionObjects = t.getObjects();

        for (int i = 0; i < collisionObjects.getCount(); i++) {
            RectangleMapObject obj = (RectangleMapObject) collisionObjects.get(i);
            Rectangle rect = obj.getRectangle();
            stupidPlaceholder.add(rect);
            System.out.println("adding rectangle to cols" + rect.getWidth() + " " + rect.getHeight());
        }
    }

    public void setCollisionLayer(TiledMapTileLayer til) {
        collisionLayer = til;
    }


    public void update(float delta) {
        if (objectList.size() > 0) {
            for (MovableObject o : objectList) {
                o.update(delta, stupidPlaceholder);
            }
        }
    }

    public void addObject(MovableObject obj) {
        objectList.add(obj);
        stupidPlaceholder.add(obj.getCollisionRectangles().get(0));
    }
}
