package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.effects.passive.IncreaseNumberOfProjectiles;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;


/**
 * @Author FruitAddict
 */
public class MoreProjectiles extends Item {
    private ObjectManager objectManager;

    public MoreProjectiles(ObjectManager objectManager, float spawnCoordX, float spawnCoordY) {
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        width=32;
        height=32;
        description = "One additional projectile for you, but not without a cost.";
        setSaveInRooms(true);        setItemType(Item.MORE_PROJECTILES);
        setEntityID(GameObject.ITEM);
    }
    public MoreProjectiles(ObjectManager objectManager) {
        this.objectManager = objectManager;
        width=32;
        height=32;
        description = "One additional projectile for you, but not without a cost.";
        setSaveInRooms(true);        setItemType(Item.MORE_PROJECTILES);
        setEntityID(GameObject.ITEM);
    }


    @Override
    public void onPickUp(Player player) {
        player.addPassiveEffect(new IncreaseNumberOfProjectiles(player,1));
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
