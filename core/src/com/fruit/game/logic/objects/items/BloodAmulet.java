package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.effects.onhit.HealthDrain;
import com.fruit.game.logic.objects.effects.passive.IncreaseNumberOfProjectiles;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class BloodAmulet extends Item {
    private ObjectManager objectManager;

    public BloodAmulet(ObjectManager objectManager, float spawnCoordX, float spawnCoordY) {
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        width=32;
        height=32;
        description = "This smells of blood.";
        setSaveInRooms(true);
        setItemType(Item.BLOOD_AMULET);
        setEntityID(GameObject.ITEM);
    }
    public BloodAmulet(ObjectManager objectManager) {
        this.objectManager = objectManager;
        width=32;
        height=32;
        description = "This smells of blood.";
        setSaveInRooms(true);
        setItemType(Item.BLOOD_AMULET);
        setEntityID(GameObject.ITEM);
    }


    @Override
    public void onPickUp(Player player) {
        player.addOnHitEffect(new HealthDrain(player));
        //Controller.addOnScreenMessage(new TextMessage("Your shots now pierce!",
        //        getBody().getPosition().x * PIXELS_TO_UNITS, getBody().getPosition().y * PIXELS_TO_UNITS, 3, TextRenderer.greenFont, TextMessage.FIXED_POINT_UPFALL));
        killYourself();
        Controller.getUserInterface().addItemDialogBox(this);
    }

    @Override
    public void addToBox2dWorld(World world){
        super.addToBox2dWorld(world);
        Controller.getWorldRenderer().getLightRenderer().attachPointLightToBody(this, Color.RED,0.5f);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
        super.killYourself();
    }
}
