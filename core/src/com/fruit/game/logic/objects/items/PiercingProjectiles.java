package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class PiercingProjectiles extends Item {
    private ObjectManager objectManager;

    public PiercingProjectiles(ObjectManager objectManager, float spawnCoordX, float spawnCoordY) {
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        width=32;
        height=32;
        description = "You've failed this city. Your projectiles can now pierce through the enemies.";
        setSaveInRooms(true);        setItemType(Item.PIERCING_PROJECTILE);
        setEntityID(GameObject.ITEM);
    }
    public PiercingProjectiles(ObjectManager objectManager) {
        this.objectManager = objectManager;
        width=32;
        height=32;
        description = "You've failed this city. Your projectiles can now pierce through the enemies.";
        setSaveInRooms(true);        setItemType(Item.PIERCING_PROJECTILE);
        setEntityID(GameObject.ITEM);
    }

    @Override
    public void onPickUp(Player player) {
        player.addPassiveEffect(new com.fruit.game.logic.objects.effects.passive.PiercingProjectiles(player));
        //Controller.addOnScreenMessage(new TextMessage("Your shots now pierce!",
        //        getBody().getPosition().x * PIXELS_TO_UNITS, getBody().getPosition().y * PIXELS_TO_UNITS, 3, TextRenderer.greenFont, TextMessage.FIXED_POINT_UPFALL));
        killYourself();
        Controller.getUserInterface().addItemDialogBox(this);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
    }
}
