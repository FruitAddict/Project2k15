package com.fruit.logic.objects.items;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.Controller;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.effects.HealOverTime;
import com.fruit.logic.objects.entities.GameObject;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.visual.Assets;
import com.fruit.visual.messages.TextMessage;

/**
 * @Author FruitAddict
 */
public class HealthPotion extends Item {

    private ObjectManager objectManager;
    private float healDuration;
    private float healDelay;
    private float healAmount;

    public HealthPotion(ObjectManager objectManager, float spawnCoordX, float spawnCoordY,float width, float height,float healDuration, float delay, float healAmount){
        this.objectManager = objectManager;
        lastKnownX = spawnCoordX;
        lastKnownY = spawnCoordY;
        this.width = width;
        this.height = height;
        this.healDuration = healDuration;
        this.healDelay= delay;
        this.healAmount = healAmount;
        setSaveInRooms(DO_SAVE);
        setItemType(Item.HEALTH_POTION);
        setEntityID(GameObject.ITEM);
        setMaxVelocity(0.5f);
        setSpeed(0.1f);
    }
    @Override
    public void onPickUp(Player player) {
        killYourself();
        player.addPassiveEffect(new HealOverTime(player,healDuration,healDelay,new Value(healAmount)));
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
        shape.setAsBox(width/PIXELS_TO_METERS/2,height/PIXELS_TO_METERS/2);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 50f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ITEM_BIT;
        fixtureDef.filter.maskBits = PLAYER_BIT | TERRAIN_BIT | PORTAL_BIT;
        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }

    @Override
    public void killYourself() {
        System.out.println("hp potion picked");
        objectManager.removeObject(this);
        Controller.addOnScreenMessage(new TextMessage("A health potion!",getBody().getPosition().x*PIXELS_TO_METERS,getBody().getPosition().y*PIXELS_TO_METERS,3, Assets.greenFont));
    }
}