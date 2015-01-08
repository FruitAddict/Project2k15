package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * Heart. Renews hp based on its load. If player's hp is full, it acts as a physics object.
 */
public class Heart extends Item implements Constants {

    private ObjectManager objectManager;
    private int renewValue = 1;

    public Heart(ObjectManager objectManager, float spawnCoordX, float spawnCoordY,float width, float height){
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        this.width = width;
        this.height = height;
        setSaveInRooms(true);
        setItemType(Item.HEART);
        setEntityID(GameObject.ITEM);
    }
    @Override
    public void update(float delta) {

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

    public void setRenewValue(int renewValue) {
        this.renewValue = renewValue;
    }
}
