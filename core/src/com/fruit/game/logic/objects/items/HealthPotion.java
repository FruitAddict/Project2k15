package com.fruit.game.logic.objects.items;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.Controller;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.effects.passive.HealOverTime;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;
import com.fruit.game.visual.messages.TextMessage;
import com.fruit.game.visual.messages.TextRenderer;

/**
 * @Author FruitAddict
 */
public class HealthPotion extends Item {

    private ObjectManager objectManager;
    private float healDuration;
    private float healDelay;
    private int healAmount;

    public HealthPotion(ObjectManager objectManager, float spawnCoordX, float spawnCoordY,float healDuration, float delay, int healAmount){
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        width = 32;
        height = 32;
        this.healDuration = healDuration;
        this.healDelay= delay;
        this.healAmount = healAmount;
        setSaveInRooms(true);        setItemType(Item.HEALTH_POTION);
        setEntityID(GameObject.ITEM);
    }
    public HealthPotion(ObjectManager objectManager,float healDuration, float delay, int healAmount){
        this.objectManager = objectManager;
        width = 32;
        height = 32;
        this.healDuration = healDuration;
        this.healDelay= delay;
        this.healAmount = healAmount;
        setSaveInRooms(true);        setItemType(Item.HEALTH_POTION);
        setEntityID(GameObject.ITEM);
    }
    @Override
    public void onPickUp(Player player) {
        killYourself();
        player.addPassiveEffect(new HealOverTime(player,healDuration,healDelay,new Value(healAmount,Value.HEALING)));
        if(!player.status.isHealing()) {
            Controller.addOnScreenMessage(new TextMessage("A health potion!", getBody().getPosition().x * PIXELS_TO_UNITS,
                    getBody().getPosition().y * PIXELS_TO_UNITS, 3, TextRenderer.greenFont, TextMessage.UP_AND_FALL));
        }
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
        fixtureDef.filter.maskBits = PLAYER_BIT | TERRAIN_BIT | PORTAL_BIT | ITEM_BIT | ENEMY_BIT;
        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
    }
}
