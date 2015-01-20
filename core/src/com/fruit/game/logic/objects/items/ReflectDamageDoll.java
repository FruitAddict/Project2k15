package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.effects.OnDamageTakenEffect;
import com.fruit.game.logic.objects.effects.ondamaged.ReflectDamage;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class ReflectDamageDoll extends Item {


    private ObjectManager objectManager;

    public ReflectDamageDoll(ObjectManager objectManager, float spawnCoordX, float spawnCoordY){
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        width=32;
        height=32;
        description = "Hurt them before they hurt you!";
        setSaveInRooms(true);
        setItemType(Item.VOODO_DOLL);
        setEntityID(GameObject.ITEM);
        setItemName("Voodoo doll");
    }
    public ReflectDamageDoll(ObjectManager objectManager){
        this.objectManager = objectManager;
        width=32;
        height=32;
        description = "Hurt them before they hurt you!";
        setSaveInRooms(true);
        setItemType(Item.VOODO_DOLL);
        setEntityID(GameObject.ITEM);
        setItemName("Voodoo doll");
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
    public void onPickUp(Player player){
        player.addOnDamageTakenEffect(new ReflectDamage(player, OnDamageTakenEffect.INFINITE_CHARGES));
        Controller.getUserInterface().addItemDialogBox(this);
        killYourself();
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
        super.killYourself();
    }
}
