package com.fruit.game.logic.objects.items;

import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.effects.passive.IncreaseKnockback;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class IncreaseKnockbackHammer extends Item {
    private ObjectManager objectManager;

    public IncreaseKnockbackHammer(ObjectManager objectManager, float spawnCoordX, float spawnCoordY) {
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        width=32;
        height=32;
        description = "A mighty hammer, your shots will push the foes away.";
        setSaveInRooms(true);        setItemType(Item.INCREASED_KNOCKBACK_HAMMER);
        setEntityID(GameObject.ITEM);
    }
    public IncreaseKnockbackHammer(ObjectManager objectManager) {
        this.objectManager = objectManager;
        width=32;
        height=32;
        description = "A mighty hammer, your shots will push the foes away.";
        setSaveInRooms(true);        setItemType(Item.INCREASED_KNOCKBACK_HAMMER);
        setEntityID(GameObject.ITEM);
    }
    @Override
    public void onPickUp(Player player) {
        player.addPassiveEffect(new IncreaseKnockback(player,25));
        killYourself();
        Controller.getUserInterface().addItemDialogBox(this);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void killYourself(){
        super.killYourself();
        objectManager.removeObject(this);
    }
}
