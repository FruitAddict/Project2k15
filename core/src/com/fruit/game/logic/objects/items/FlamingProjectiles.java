package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.effects.onhit.ExplodeOnHit;
import com.fruit.game.logic.objects.effects.onhit.SetOnFire;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict todo update rest
 */
public class FlamingProjectiles extends Item {

    private ObjectManager objectManager;

    public FlamingProjectiles(ObjectManager objectManager, float spawnCoordX, float spawnCoordY){
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        width=32;
        height=32;
        description = "Your projectiles will now set enemies on fire, baby!";
        setSaveInRooms(true);
        setItemType(Item.MICHAEL_BAY);
        setEntityID(GameObject.ITEM);
        setItemName("Flaming Sword");
    }
    public FlamingProjectiles(ObjectManager objectManager){
        this.objectManager = objectManager;
        width=32;
        height=32;
        description = "Your projectiles will now set enemies on fire, baby!";
        setSaveInRooms(true);
        setItemType(Item.MICHAEL_BAY);
        setEntityID(GameObject.ITEM);
        setItemName("Flaming Sword");
    }
    @Override
    public void update(float delta) {

    }

    @Override
    public void addToBox2dWorld(World world){
        super.addToBox2dWorld(world);
        Controller.getWorldRenderer().getLightRenderer().attachPointLightToBody(this, com.badlogic.gdx.graphics.Color.RED,0.5f);
    }

    @Override
    public void onPickUp(Player player){
        player.addOnHitEffect(new SetOnFire(player));
        Controller.getUserInterface().addItemDialogBox(this);
        killYourself();
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
        super.killYourself();
    }

}
