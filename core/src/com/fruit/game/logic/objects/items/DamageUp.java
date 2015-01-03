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
public class DamageUp extends Item {
    private ObjectManager objectManager;
    private float renewValue = 1;

    public DamageUp(ObjectManager objectManager, float spawnCoordX, float spawnCoordY){
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        width = 32;
        height = 32;
        description = "Power of the ancient surges through your veins as you feel stronger and sexier.";
        setSaveInRooms(DO_SAVE);
        setItemType(Item.DAMAGE_UP_1);
        setEntityID(GameObject.ITEM);
    }
    public DamageUp(ObjectManager objectManager){
        this.objectManager = objectManager;
        width = 32;
        height = 32;
        description = "Power of the ancient surges through your veins as you feel stronger and sexier.";
        setSaveInRooms(DO_SAVE);
        setItemType(Item.DAMAGE_UP_1);
        setEntityID(GameObject.ITEM);
    }
    @Override
    public void update(float delta) {

    }

    @Override
    public void addToBox2dWorld(World world) {
        //setting width and height
        width = 32;
        height = 32;

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
        fixtureDef.filter.maskBits = PLAYER_BIT | TERRAIN_BIT | PORTAL_BIT | ENEMY_BIT| ITEM_BIT | ENEMY_BIT;
        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }

    @Override
    public void onPickUp(Player player){
        player.addPassiveEffect(new com.fruit.game.logic.objects.effects.passive.DamageUp(player,1));
        //Controller.addOnScreenMessage(new TextMessage("You feel stronger..!", getBody().getPosition().x * PIXELS_TO_UNITS,
        //        getBody().getPosition().y * PIXELS_TO_UNITS, 3, TextRenderer.greenFont, TextMessage.UP_AND_FALL));
        killYourself();
        Controller.getUserInterface().addItemDialogBox(this);
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
    }

    public float getRenewValue() {
        return renewValue;
    }

    public void setRenewValue(float renewValue) {
        this.renewValue = renewValue;
    }
}
