package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class DamageUp extends Item {
    private ObjectManager objectManager;
    private float renewValue = 1;

    public DamageUp(ObjectManager objectManager, float spawnCoordX, float spawnCoordY){
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        width = 32;
        height = 32;
        description = "Your attacks are more fierce as you lust for murder.";
        setSaveInRooms(true);
        setItemType(Item.DAMAGE_UP_1);
        setEntityID(GameObject.ITEM);
        setItemName("Blood stained dagger");
    }
    public DamageUp(ObjectManager objectManager){
        this.objectManager = objectManager;
        width = 32;
        height = 32;
        description = "Your attacks are more fierce as you lust for murder.";
        setSaveInRooms(true);
        setItemType(Item.DAMAGE_UP_1);
        setEntityID(GameObject.ITEM);
        setItemName("Blood stained dagger");
    }
    @Override
    public void update(float delta) {

    }

    @Override
    public void addToBox2dWorld(World world){
        super.addToBox2dWorld(world);
        Controller.getWorldRenderer().getLightRenderer().attachPointLightToBody(this, Color.BLACK,0.5f );
    }


    @Override
    public void onPickUp(Player player){
        player.addPassiveEffect(new com.fruit.game.logic.objects.effects.passive.DamageUp(player,1));
        //Controller.addOnScreenMessage(new TextMessage("You feel stronger..!", getBody().getPosition().x * PIXELS_TO_UNITS,
        //        getBody().getPosition().y * PIXELS_TO_UNITS, 3, TextRenderer.greenFont, TextMessage.FIXED_POINT_UPFALL));
        killYourself();
        Controller.getUserInterface().addItemDialogBox(this);
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
        super.killYourself();
    }

    public float getRenewValue() {
        return renewValue;
    }

    public void setRenewValue(float renewValue) {
        this.renewValue = renewValue;
    }
}
