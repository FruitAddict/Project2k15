package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.effects.ondamaged.BlockDamage;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;
import com.fruit.game.visual.messages.TextMessage;
import com.fruit.game.visual.messages.TextRenderer;

/**
 * @Author FruitAddict
 */
public class SphereOfProtection extends Item {

    private ObjectManager objectManager;
    private float blockCount;

    public SphereOfProtection(ObjectManager objectManager, float spawnCoordX, float spawnCoordY, int blockCount){
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        width = 32;
        height = 32;
        this.blockCount = blockCount;
        setSaveInRooms(true);        setItemType(Item.SPHERE_OF_PROTECTION);
        setEntityID(GameObject.ITEM);
    }
    public SphereOfProtection(ObjectManager objectManager, int blockCount){
        this.objectManager = objectManager;
        width = 32;
        height = 32;
        this.blockCount = blockCount;
        setSaveInRooms(true);        setItemType(Item.SPHERE_OF_PROTECTION);
        setEntityID(GameObject.ITEM);
    }
    @Override
    public void onPickUp(Player player) {
        killYourself();
        player.addOnDamageTakenEffect(new BlockDamage(player,5));
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
        Controller.getUserInterface().getMessageHandler().addMessage("You're protected.", Color.GREEN, 3f);
    }
}
