package com.fruit.logic.objects.entities.misc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fruit.Controller;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.effects.passive.DamageOverTime;
import com.fruit.logic.objects.entities.*;
import com.fruit.logic.objects.entities.Character;
import com.fruit.visual.messages.TextMessage;
import com.fruit.visual.messages.TextRenderer;

/**
 * @Author FruitAddict
 */
public class Explosion extends Enemy {
    private World world;
    private ObjectManager objectManager;
    public float stateTime;
    private float lifeTime;
    private int damage;
    private float range;

    public Explosion(ObjectManager objectManager,float spawnX, float spawnY,float range, float lifeTime, int damage) {
        lastKnownX = spawnX;
        lastKnownY= spawnY;
        this.objectManager = objectManager;
        setEntityID(GameObject.EXPLOSION);
        setSaveInRooms(DONT_SAVE);
        this.lifeTime = lifeTime;
        this.damage = damage;
        this.range = range;
        width = range*2*PIXELS_TO_UNITS;
        height = range*2*PIXELS_TO_UNITS;
    }

    @Override
    public void onDirectContact(Character character) {
        character.onDamageTaken(new Value(damage,Value.BURNING_DAMAGE));
        character.addPassiveEffect(new DamageOverTime(character, 5f, 0.5f, new Value(Math.max(1,damage/2), Value.POISON_DAMAGE), DamageOverTime.BURNING));

        Controller.addOnScreenMessage(new TextMessage("Burning!!", character.getBody().getPosition().x * PIXELS_TO_UNITS,
                character.getBody().getPosition().y * PIXELS_TO_UNITS, 1.5f, TextRenderer.redFont, TextMessage.UP_AND_FALL));

        character.getBody().applyLinearImpulse(new Vector2(character.getBody().getPosition().x - getBody().getPosition().x
                ,character.getBody().getPosition().y - getBody().getPosition().y).nor().scl(500), character.getBody().getPosition(),true);

    }

    @Override
    public void onContactWithTerrain(int direction) {

    }

    @Override
    public void update(float delta) {
        stateTime+=delta;
        if(stateTime>lifeTime){
            killYourself();
        }
    }

    @Override
    public void addToBox2dWorld(World world) {
        this.world = world;


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
        CircleShape shape = new CircleShape();
        shape.setRadius(range);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 99999f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = AREA_OF_EFFECT_BIT;
        fixtureDef.filter.maskBits = PLAYER_BIT | ENEMY_BIT ;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
    }

    public float getLifeTime() {
        return lifeTime;
    }
}
