package com.fruit.utilities;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.fruit.logic.Constants;

/**
 * Map object parser. Takes objects from the tiled map and parses them into rigid box2d
 * bodies. TODO: More than terrain
 */
public class MapObjectParser implements Constants {

    public static void addMapObjects(World world, TiledMap map){
        //get tile width and height from the map
        int tileWidth = map.getProperties().get("tilewidth", Integer.class);
        int tileHeight = map.getProperties().get("tileheight", Integer.class);
        //get collision layer and obtain the objects
        MapObjects mapObjects = map.getLayers().get("collisionObjects").getObjects();
        //create array of rectangles based on collision objects
        Array<Rectangle> terrainCollisionRecs = new Array<Rectangle>();
        for(int i=0;i<mapObjects.getCount();i++){
            //cast mapobjects into rectangle map object and add their rectangle to the list.
            RectangleMapObject object = (RectangleMapObject)mapObjects.get(i);
            terrainCollisionRecs.add(object.getRectangle());
        }
        for(Rectangle rec: terrainCollisionRecs){
            //box2d bodies origin point is at their center, therefore we get the
            //rectangles x and y and add half width/height to them
            float originX = rec.getX() + rec.getWidth()/2;
            float originY = rec.getY() + rec.getHeight()/2;
            //divide them both by our pixels to box2d unit scaling
            originX /= PIXELS_TO_METERS;
            originY /= PIXELS_TO_METERS;
            //get half width and half height of the rectangle and divide them by unit scaling
            float halfWidth = rec.getWidth()/PIXELS_TO_METERS/2;
            float halfHeight = rec.getHeight()/PIXELS_TO_METERS/2;

            //create a new bodyDef for static terrain object
            //Player body definition
            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(originX,originY);
            bodyDef.type = BodyDef.BodyType.StaticBody;

            //create the body
            Body body = world.createBody(bodyDef);


            //Shape definiton
            PolygonShape shape = new PolygonShape();
            //setAsBox take's half of width and height
            shape.setAsBox(halfWidth,halfHeight);

            //fixture
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 100f;
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = TERRAIN_BIT;
            body.createFixture(fixtureDef);

            //dispose of shape
            shape.dispose();
        }

    }
}
