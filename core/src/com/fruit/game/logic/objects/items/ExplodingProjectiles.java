package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.effects.onhit.ExplodeOnHit;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class ExplodingProjectiles extends Item {

    private ObjectManager objectManager;
    private int renewValue = 1;

    public ExplodingProjectiles(ObjectManager objectManager, float spawnCoordX, float spawnCoordY){
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        width=32;
        height=32;
        description = "Your projectiles now explode on hit, it will come to an end though.";
        setSaveInRooms(true);        setItemType(Item.MICHAEL_BAY);
        setEntityID(GameObject.ITEM);
    }
    public ExplodingProjectiles(ObjectManager objectManager){
        this.objectManager = objectManager;
        width=32;
        height=32;
        description = "Your projectiles now explode on hit, it will come to an end though.";
        setSaveInRooms(true);        setItemType(Item.MICHAEL_BAY);
        setEntityID(GameObject.ITEM);
    }
    @Override
    public void update(float delta) {

    }

    @Override
    public void onPickUp(Player player){
        player.addOnHitEffect(new ExplodeOnHit(player,20));
        Controller.getUserInterface().addItemDialogBox(this);
        killYourself();
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
    }

}
