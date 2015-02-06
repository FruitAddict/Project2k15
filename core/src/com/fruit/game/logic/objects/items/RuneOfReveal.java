package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.graphics.Color;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class RuneOfReveal extends Item {

    private ObjectManager objectManager;

    public RuneOfReveal(ObjectManager objectManager){
        this.objectManager = objectManager;
        width = 32;
        height = 32;
        setSaveInRooms(true);
        setItemType(Item.RUNE_OF_REVEAL);
        setEntityID(GameObject.ITEM);
    }

    @Override
    public void onPickUp(Player player) {
        killYourself();
        player.getObjectManager().getWorldUpdater().getMapManager().getCurrentMap().revealMap();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void killYourself() {
        super.killYourself();
        objectManager.removeObject(this);
        Controller.getUserInterface().getMessageHandler().addMessage("See it all!.", Color.GREEN, 3f);
    }
}
