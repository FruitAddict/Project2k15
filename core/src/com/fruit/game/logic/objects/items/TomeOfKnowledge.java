package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.effects.onhit.HealthDrain;
import com.fruit.game.logic.objects.effects.passive.IncreaseHealthPoints;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class TomeOfKnowledge extends Item{
    private ObjectManager objectManager;

    public TomeOfKnowledge(ObjectManager objectManager, float spawnCoordX, float spawnCoordY) {
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        width=32;
        height=32;
        description = "You can't understand anything but you feel smarter anyway.";
        setSaveInRooms(true);
        setItemType(Item.TOME_OF_KNOWLEDGE);
        setEntityID(GameObject.ITEM);
        setItemName("Tome of Knowledge");
    }
    public TomeOfKnowledge(ObjectManager objectManager) {
        this.objectManager = objectManager;
        width=32;
        height=32;
        description = "You can't understand anything but you feel smarter anyway.";
        setSaveInRooms(true);
        setItemType(Item.TOME_OF_KNOWLEDGE);
        setEntityID(GameObject.ITEM);
        setItemName("Tome of Knowledge");
    }


    @Override
    public void onPickUp(Player player) {
        player.setStatPoints(player.getStatPoints()+3);
        player.addExperiencePoints(100);
        //Controller.addOnScreenMessage(new TextMessage("Your shots now pierce!",
        //        getBody().getPosition().x * PIXELS_TO_UNITS, getBody().getPosition().y * PIXELS_TO_UNITS, 3, TextRenderer.greenFont, TextMessage.FIXED_POINT_UPFALL));
        killYourself();
        Controller.getUserInterface().addItemDialogBox(this);
    }

    @Override
    public void addToBox2dWorld(World world){
        super.addToBox2dWorld(world);
        Controller.getWorldRenderer().getLightRenderer().attachPointLightToBody(this, Color.WHITE,0.5f);
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