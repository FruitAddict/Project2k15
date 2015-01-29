package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.effects.passive.SlowerProjectiles;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class SlowerProjectilesRing extends Item{
    private ObjectManager objectManager;

    public SlowerProjectilesRing(ObjectManager objectManager, float spawnCoordX, float spawnCoordY) {
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        width=32;
        height=32;
        description = "Your projectiles are slowed down to a snail's pace, perhaps it can be of any use?";
        setSaveInRooms(true);        setItemType(Item.SLOWER_PROJECTILE_RING);
        setEntityID(GameObject.ITEM);
        setItemName("Time warping ring");
    }
    public SlowerProjectilesRing(ObjectManager objectManager) {
        this.objectManager = objectManager;
        width=32;
        height=32;
        description = "Your projectiles are slowed down to a snail's pace, perhaps it can be of any use?";
        setSaveInRooms(true);        setItemType(Item.SLOWER_PROJECTILE_RING);
        setEntityID(GameObject.ITEM);
        setItemName("Time warping ring");
    }
    @Override
    public void onPickUp(Player player) {
        player.addPassiveEffect(new SlowerProjectiles(player));
        killYourself();
        Controller.getUserInterface().addItemDialogBox(this);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void addToBox2dWorld(World world){
        super.addToBox2dWorld(world);
        Controller.getWorldRenderer().getLightRenderer().attachPointLightToBody(this, Color.BLUE,0.5f);
    }

    @Override
    public void killYourself(){
        super.killYourself();
        objectManager.removeObject(this);
    }
}
