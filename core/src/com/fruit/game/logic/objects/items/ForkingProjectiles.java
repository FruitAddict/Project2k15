package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.effects.onhit.ExplodeOnHit;
import com.fruit.game.logic.objects.effects.onhit.ForkOnHit;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class ForkingProjectiles extends Item {

    private ObjectManager objectManager;
    private int renewValue = 1;

    public ForkingProjectiles(ObjectManager objectManager, float spawnCoordX, float spawnCoordY){
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        width=32;
        height=32;
        description = "Your projectiles now fork on hit! Woooo! Just imagine the synergies.";
        setSaveInRooms(true);
        setItemType(Item.FORKING_PROJECTILES);
        setEntityID(GameObject.ITEM);
    }
    public ForkingProjectiles(ObjectManager objectManager){
        this.objectManager = objectManager;
        width=32;
        height=32;
        description = "Your projectiles now fork on hit! Woooo! Just imagine the synergies.";
        setSaveInRooms(true);
        setItemType(Item.FORKING_PROJECTILES);
        setEntityID(GameObject.ITEM);
    }
    @Override
    public void update(float delta) {

    }

    @Override
    public void addToBox2dWorld(World world){
        super.addToBox2dWorld(world);
        Controller.getWorldRenderer().getLightRenderer().attachPointLightToBody(this, Color.GREEN,0.5f);
    }

    @Override
    public void onPickUp(Player player){
        player.addOnHitEffect(new ForkOnHit(player));
        Controller.getUserInterface().addItemDialogBox(this);
        killYourself();
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
        super.killYourself();
    }
}
