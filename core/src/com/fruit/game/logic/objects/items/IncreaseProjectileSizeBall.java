package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.effects.passive.IncreaseProjectileSize;
import com.fruit.game.logic.objects.effects.passive.SlowerProjectiles;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class IncreaseProjectileSizeBall extends Item {
    private ObjectManager objectManager;

    public IncreaseProjectileSizeBall(ObjectManager objectManager, float spawnCoordX, float spawnCoordY) {
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        width=32;
        height=32;
        description = "Your balls grow!";
        setSaveInRooms(true);
        setItemType(Item.INCREASE_PROJECTILE_SIZE_BALL);
        setEntityID(GameObject.ITEM);
        setItemName("Huge metal sphere");
    }
    public IncreaseProjectileSizeBall(ObjectManager objectManager) {
        this.objectManager = objectManager;
        width=32;
        height=32;
        description = "Your balls grow!";
        setSaveInRooms(true);
        setItemType(Item.INCREASE_PROJECTILE_SIZE_BALL);
        setEntityID(GameObject.ITEM);
        setItemName("Huge metal sphere");
    }
    @Override
    public void onPickUp(Player player) {
        player.addPassiveEffect(new IncreaseProjectileSize(player,2));
        killYourself();
        Controller.getUserInterface().addItemDialogBox(this);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void addToBox2dWorld(World world){
        super.addToBox2dWorld(world);
        Controller.getWorldRenderer().getLightRenderer().attachPointLightToBody(this, Color.MAROON,0.5f);
    }

    @Override
    public void killYourself(){
        super.killYourself();
        objectManager.removeObject(this);
    }
}
