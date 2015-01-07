package com.fruit.game.logic.objects.items;

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
        setSaveInRooms(true);        setItemType(Item.FORKING_PROJECTILES);
        setEntityID(GameObject.ITEM);
    }
    @Override
    public void update(float delta) {

    }

    @Override
    public void addToBox2dWorld(World world) {

        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(lastKnownX,lastKnownY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 1.0f;
        bodyDef.fixedRotation = true;

        //create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        //Shape definiton
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/ PIXELS_TO_UNITS /2,height/ PIXELS_TO_UNITS /2);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 50f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ITEM_BIT;
        fixtureDef.filter.maskBits = PLAYER_BIT | TERRAIN_BIT | PORTAL_BIT | ITEM_BIT |  ENEMY_BIT;
        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
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
    }
}
