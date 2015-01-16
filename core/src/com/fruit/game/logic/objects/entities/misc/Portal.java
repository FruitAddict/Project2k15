package com.fruit.game.logic.objects.entities.misc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fruit.game.Controller;
import com.fruit.game.logic.objects.entities.GameObject;

/**
 * @Author FruitAddict
 */
public class Portal extends GameObject {

    public static final int PORTAL_NORTH = 1;
    public static final int PORTAL_SOUTH = 2;
    public static final int PORTAL_EAST = 3;
    public static final int PORTAL_WEST = 4;

    private int portalType;
    private boolean active = true;
    //if the portal is horizontal set to true, else set to false
    private boolean horizontal;

    public Portal(Vector2 position, int type, float width, float height, boolean isHorizontal){
        portalType = type;
        lastKnownX = position.x;
        lastKnownY = position.y;
        horizontal = isHorizontal;
        this.width = width;
        this.height = height;
        setEntityID(GameObject.PORTAL);
        setSaveInRooms(true);
    }
    @Override
    public void update(float delta) {

    }

    @Override
    public void addToBox2dWorld(World world) {
        //create a new bodyDef for static terrain object
        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(lastKnownX,lastKnownY);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        //create the body
        body = world.createBody(bodyDef);
        //add userdata
        body.setUserData(this);


        //Shape definiton
        PolygonShape shape = new PolygonShape();
        //setAsBox take's half of width and height
        shape.setAsBox(width/PIXELS_TO_UNITS/2,height/PIXELS_TO_UNITS/2);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 100f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = PORTAL_BIT;
        body.createFixture(fixtureDef);

        //dispose of shape
        shape.dispose();
    }

    @Override
    public void killYourself() {

    }

    public void onContactWithPlayer(){
        if(active){
            switch(portalType) {
                case PORTAL_NORTH: {
                    Controller.getWorldUpdater().getMapManager().requestChange(NORTH_DIR);
                    break;
                }
                case PORTAL_SOUTH: {
                    Controller.getWorldUpdater().getMapManager().requestChange(SOUTH_DIR);
                    break;
                }
                case PORTAL_WEST: {
                    Controller.getWorldUpdater().getMapManager().requestChange(WEST_DIR);
                    break;
                }
                case PORTAL_EAST: {
                    Controller.getWorldUpdater().getMapManager().requestChange(EAST_DIR);
                    break;
                }
            }
        }
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    @Override
    public float getBodyPositionY(){
        return 1000;
    }
}
