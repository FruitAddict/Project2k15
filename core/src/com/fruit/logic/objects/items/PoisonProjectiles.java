package com.fruit.logic.objects.items;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.Controller;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.effects.onhit.PoisonOnHit;
import com.fruit.logic.objects.entities.GameObject;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.visual.messages.TextMessage;
import com.fruit.visual.messages.TextRenderer;

/**
 * @Author FruitAddict
 */
public class PoisonProjectiles extends Item {

    private ObjectManager objectManager;

    public PoisonProjectiles(ObjectManager objectManager, float spawnCoordX, float spawnCoordY,float width, float height) {
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        this.width = width;
        this.height = height;

        setSaveInRooms(DO_SAVE);
        setItemType(Item.POISON_TOUCH);
        setEntityID(GameObject.ITEM);
    }

    @Override
    public void onPickUp(Player player) {
        player.addOnHitEffect(new PoisonOnHit(player,20));
        Controller.addOnScreenMessage(new TextMessage("You poison on hit...",
                getBody().getPosition().x * PIXELS_TO_UNITS, getBody().getPosition().y * PIXELS_TO_UNITS, 3, TextRenderer.greenFont,TextMessage.UP_AND_FALL));
        killYourself();
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
        fixtureDef.filter.maskBits = PLAYER_BIT | TERRAIN_BIT | PORTAL_BIT | ITEM_BIT;
        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
    }
}
