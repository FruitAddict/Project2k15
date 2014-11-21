package com.project2k15.logic.managers;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.collision.QuadRectangle;
import com.project2k15.logic.collision.Quadtree;
import com.project2k15.logic.entities.Player;
import com.project2k15.logic.entities.abstracted.Entity;

import java.util.Iterator;

/**
 * Object Manager
 */
public class ObjectManager {

    private Array<Entity> objectList = new Array<Entity>();
    private Array<PropertyRectangle> passRectangleList;
    private MapObjects collisionObjects;
    private Quadtree quadtree;
    private Player player;

    public ObjectManager(MapLayer t, int mapWidth, int mapHeight) {
        collisionObjects = t.getObjects();
        quadtree = new Quadtree(0, new QuadRectangle(0, 0, mapWidth, mapHeight));
        passRectangleList = new Array<PropertyRectangle>();
    }

    public void setCollisionObjects(MapLayer til) {
        collisionObjects = null;
        collisionObjects = til.getObjects();
    }

    public void setPlayer(Player p) {
        player = p;
    }


    public void update(float delta) {
        float startTime = System.nanoTime();
        passRectangleList.clear();
        quadtree.clear();

        for (int i = 0; i < collisionObjects.getCount(); i++) {
            PropertyRectangle obj = new PropertyRectangle(((RectangleMapObject) collisionObjects.get(i)).getRectangle(), PropertyRectangle.TERRAIN);
            quadtree.insert(obj);
        }

        for (int i = 0; i < objectList.size; i++) {
            quadtree.insert(objectList.get(i).getCollisionRectangle());
        }

        if (objectList.size > 0) {
            for (Entity o : objectList) {
                passRectangleList.clear();
                o.update(delta, quadtree.retrieve(passRectangleList, o.getCollisionRectangle()));
            }
        }
        System.out.println(System.nanoTime()-startTime);
    }

    public void clear() {
        Iterator<Entity> i = objectList.iterator();
        while (i.hasNext()) {
            System.out.println("Clearing...");
            Entity g = i.next();
            if (g.getCollisionRectangle().getType() == PropertyRectangle.CHARACTER || g.getCollisionRectangle().getType() == PropertyRectangle.PROJECTILE) {
                removeObject(g);
            }
        }
    }

    public Quadtree getTree() {
        return quadtree;
    }

    public boolean addObject(Entity obj) {
        if (objectList.size < 200) {
            objectList.add(obj);
            return true;
        }
        return false;
    }

    public void removeObject(Entity obj) {
        objectList.removeValue(obj, true);
    }

    public int getNumberOfObjects() {
        return objectList.size;
    }

}
