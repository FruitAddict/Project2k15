package com.fruit.logic.objects.items;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.logic.Constants;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.entities.GameObject;
import com.fruit.logic.objects.entities.player.Player;

/**
 * Heart. Renews hp based on its load. If player's hp is full, it acts as a physics object.
 */
public class Heart extends Item implements Constants {

    private ObjectManager objectManager;
    private float renewValue = 1;

    public Heart(ObjectManager objectManager, float spawnCoordX, float spawnCoordY,float width, float height){
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        this.width = width;
        this.height = height;
        setSaveInRooms(DO_SAVE);
        setItemType(Item.HEART);
        setEntityID(GameObject.ITEM);
    }
    @Override
    public void update(float delta) {

    }

    @Override
    public void addToBox2dWorld(World world) {

        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(lastKnownX,lastKnownY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 1.0f;
        bodyDef.fixedRotation = true;

        //create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        //Shape definiton
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/PIXELS_TO_METERS/2,height/PIXELS_TO_METERS/2);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 50f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ITEM_BIT;
        fixtureDef.filter.maskBits = PLAYER_BIT | TERRAIN_BIT | PORTAL_BIT;
        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }

    @Override
    public void onPickUp(Player player){
        if(player.stats.getHealthPoints() < player.stats.getBaseMaximumHealthPoints()){
            if(player.stats.getHealthPoints()+renewValue > player.stats.getBaseMaximumHealthPoints()){
                player.stats.changeHealthPoints(player.stats.getBaseMaximumHealthPoints() - player.stats.getHealthPoints());
                killYourself();
            } else {
                player.stats.changeHealthPoints(renewValue);
                killYourself();
            }
        }
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
    }

    public float getRenewValue() {
        return renewValue;
    }

    public void setRenewValue(float renewValue) {
        this.renewValue = renewValue;
    }
}
