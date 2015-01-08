package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.effects.onhit.PoisonOnHit;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;
import com.fruit.game.visual.messages.TextMessage;
import com.fruit.game.visual.messages.TextRenderer;

/**
 * @Author FruitAddict
 */
public class PoisonProjectiles extends Item {

    private ObjectManager objectManager;

    public PoisonProjectiles(ObjectManager objectManager, float spawnCoordX, float spawnCoordY) {
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        width=32;
        height=32;
        setSaveInRooms(true);        setItemType(Item.POISON_TOUCH);
        setEntityID(GameObject.ITEM);
    }
    public PoisonProjectiles(ObjectManager objectManager) {
        this.objectManager = objectManager;
        width=32;
        height=32;
        setSaveInRooms(true);        setItemType(Item.POISON_TOUCH);
        setEntityID(GameObject.ITEM);
    }

    @Override
    public void onPickUp(Player player) {
        player.addOnHitEffect(new PoisonOnHit(player,20));
        Controller.getUserInterface().getMessageHandler().addMessage("You poison on hit.", Color.GREEN, 3f);
        killYourself();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
    }
}
