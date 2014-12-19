package com.fruit.utilities;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.fruit.logic.Constants;
import com.fruit.logic.WorldUpdater;
import com.fruit.maps.Room;

/**
 * Map object parser. Takes objects from the tiled map and parses them into rigid box2d
 * bodies.
 */
public class MapObjectParser implements Constants {

    public static void addMapObjectsToWorld(WorldUpdater worldUpdater, Room room){

        World world = worldUpdater.getWorld();
        //get collision layer and obtain the objects
        MapObjects collisionMapObjects = room.getTiledMap().getLayers().get("collisionObjects").getObjects();
        //create array of rectangles based on collision objects
        Array<Rectangle> terrainCollisionRecs = new Array<Rectangle>();
        for(int i=0;i<collisionMapObjects.getCount();i++){
            //cast mapobjects into rectangle map object and add their rectangle to the list.
            RectangleMapObject object = (RectangleMapObject)collisionMapObjects.get(i);
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

        //parse portal objects
        MapObjects portalMapObjects = room.getTiledMap().getLayers().get("portalObjects").getObjects();
        //create array of rectangles based on portal objects
        Array<Rectangle> portalRectangles = new Array<>();
        //create array of corresponding type strings
        Array<Integer> directions = new Array<>();
        for(int i=0;i<portalMapObjects.getCount();i++){
            //cast mapobjects into rectangle map object and add their rectangle to the list.
            RectangleMapObject object = (RectangleMapObject)portalMapObjects.get(i);
            String direction = object.getProperties().get("type",String.class);
            portalRectangles.add(object.getRectangle());
            switch(direction){
                case "NORTH":{
                    directions.add(NORTH_DIR);
                    break;
                }
                case "SOUTH":{
                    directions.add(SOUTH_DIR);
                    break;
                }
                case "WEST": {
                    directions.add(WEST_DIR);
                    break;
                }
                case "EAST": {
                    directions.add(EAST_DIR);
                    break;
                }
            }
        }
        for(int i =0 ;i <portalRectangles.size;i++){
            Rectangle rec = portalRectangles.get(i);
            //copypasted from above
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
            //add userdata
            body.setUserData(directions.get(i));


            //Shape definiton
            PolygonShape shape = new PolygonShape();
            //setAsBox take's half of width and height
            shape.setAsBox(halfWidth,halfHeight);

            //fixture
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 100f;
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = PORTAL_BIT;
            body.createFixture(fixtureDef);

            //dispose of shape
            shape.dispose();
        }

        //finally add all the objects stored in this room to the object manager
        worldUpdater.getObjectManager().addObjects(room.getGameObjectsStored());
    }

    public static void addInfoFromTiledMapToRoom(Room room){
        room.setTileWidth(room.getTiledMap().getProperties().get("tilewidth", Integer.class));
        room.setTileHeight(room.getTiledMap().getProperties().get("tileheight", Integer.class));

        TiledMap map = room.getTiledMap();

        if(map.getLayers().get("spawnPoints")!=null) {
            MapObjects spawnPoints = map.getLayers().get("spawnPoints").getObjects();
            Array<Rectangle> spawnPointRecs = new Array<>();
            for (int i = 0; i < spawnPoints.getCount(); i++) {
                //cast mapobjects into rectangle map object and add their rectangle to the list.
                RectangleMapObject object = (RectangleMapObject) spawnPoints.get(i);
                spawnPointRecs.add(object.getRectangle());
            }
            for (int i = 0; i < spawnPointRecs.size; i++) {
                String request = spawnPoints.get(i).getProperties().get("type", String.class);
                if (request != null) {
                    switch (request) {
                        case "NORTH": {
                            room.setSpawnPointNorth(new Vector2((spawnPointRecs.get(i).getX() + spawnPointRecs.get(i).getWidth() / 2) / PIXELS_TO_METERS,
                                    (spawnPointRecs.get(i).getY() + spawnPointRecs.get(i).getHeight() / 2) / PIXELS_TO_METERS));
                            break;
                        }
                        case "SOUTH": {
                            room.setSpawnPointSouth(new Vector2((spawnPointRecs.get(i).getX() + spawnPointRecs.get(i).getWidth() / 2) / PIXELS_TO_METERS,
                                    (spawnPointRecs.get(i).getY() + spawnPointRecs.get(i).getHeight() / 2) / PIXELS_TO_METERS));
                            break;
                        }
                        case "EAST": {
                            room.setSpawnPointEast(new Vector2((spawnPointRecs.get(i).getX() + spawnPointRecs.get(i).getWidth() / 2) / PIXELS_TO_METERS,
                                    (spawnPointRecs.get(i).getY() + spawnPointRecs.get(i).getHeight() / 2) / PIXELS_TO_METERS));
                            break;
                        }
                        case "WEST": {
                            room.setSpawnPointWest(new Vector2((spawnPointRecs.get(i).getX() + spawnPointRecs.get(i).getWidth() / 2) / PIXELS_TO_METERS,
                                    (spawnPointRecs.get(i).getY() + spawnPointRecs.get(i).getHeight() / 2) / PIXELS_TO_METERS));
                            break;
                        }
                        case "CENTER": {
                            room.setSpawnPointCenter(new Vector2((spawnPointRecs.get(i).getX() + spawnPointRecs.get(i).getWidth() / 2) / PIXELS_TO_METERS,
                                    (spawnPointRecs.get(i).getY() + spawnPointRecs.get(i).getHeight() / 2) / PIXELS_TO_METERS));
                            break;
                        }
                        default: {
                            room.addMobSpawnPoint(new Vector2((spawnPointRecs.get(i).getX() + spawnPointRecs.get(i).getWidth() / 2) / PIXELS_TO_METERS,
                                    (spawnPointRecs.get(i).getY() + spawnPointRecs.get(i).getHeight() / 2) / PIXELS_TO_METERS));
                            break;
                        }
                    }
                }
            }
        }else {
            System.out.println("Warning! No spawn point layer found in " + room);
        }

        //parse portal objects
        if(room.getTiledMap().getLayers().get("portalObjects") != null) {
            MapObjects portalMapObjects = room.getTiledMap().getLayers().get("portalObjects").getObjects();
            //create array of rectangles based on portal objects
            Array<Rectangle> portalRectangles = new Array<>();

            for (int i = 0; i < portalMapObjects.getCount(); i++) {
                //cast mapobjects into rectangle map object and add their rectangle to the list.
                RectangleMapObject object = (RectangleMapObject) portalMapObjects.get(i);
                String direction = object.getProperties().get("type", String.class);
                portalRectangles.add(object.getRectangle());
                switch (direction) {
                    case "NORTH": {
                        //portalMapObjects and portalRectangles are the same size ( second is created on the base of first), so we know
                        //this object was created and we can use it to set the portal center position(for use with map transition animation
                        //alignment)
                        room.setPortalPointNorth(new Vector2((portalRectangles.get(i).getX() + portalRectangles.get(i).getWidth() / 2) / PIXELS_TO_METERS,
                                (portalRectangles.get(i).getY() + portalRectangles.get(i).getHeight() / 2) / PIXELS_TO_METERS));
                        break;
                    }
                    case "SOUTH": {
                        room.setPortalPointSouth(new Vector2((portalRectangles.get(i).getX() + portalRectangles.get(i).getWidth() / 2) / PIXELS_TO_METERS,
                                (portalRectangles.get(i).getY() + portalRectangles.get(i).getHeight() / 2) / PIXELS_TO_METERS));
                        break;
                    }
                    case "WEST": {
                        room.setPortalPointWest(new Vector2((portalRectangles.get(i).getX() + portalRectangles.get(i).getWidth() / 2) / PIXELS_TO_METERS,
                                (portalRectangles.get(i).getY() + portalRectangles.get(i).getHeight() / 2) / PIXELS_TO_METERS));
                        break;
                    }
                    case "EAST": {
                        room.setPortalPointEast(new Vector2((portalRectangles.get(i).getX() + portalRectangles.get(i).getWidth() / 2) / PIXELS_TO_METERS,
                                (portalRectangles.get(i).getY() + portalRectangles.get(i).getHeight() / 2) / PIXELS_TO_METERS));
                        break;
                    }
                }
            }
        }else {
            System.out.println("WARNING! No portal object layer found in " + room);
        }

         // Static lights section. Obtains info about light color, positioning and length if it exists.
        //parse portal objects
        //TODO Make it handle more shit
        if(room.getTiledMap().getLayers().get("staticLights") != null) {
            MapObjects staticLightObjects = room.getTiledMap().getLayers().get("staticLights").getObjects();

            for(MapObject mapObject : staticLightObjects){
                RectangleMapObject object = (RectangleMapObject)mapObject;
                room.addStaticLightPosition(new Vector2(object.getRectangle().getX()/PIXELS_TO_METERS,object.getRectangle().getY()/PIXELS_TO_METERS));
            }
        }else {
            System.out.println("WARNING! No static light layer found in " + room);
        }
    }
}
