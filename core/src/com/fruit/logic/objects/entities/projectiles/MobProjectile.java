package com.fruit.logic.objects.entities.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.entities.Character;
import com.fruit.logic.objects.entities.GameObject;
import com.fruit.logic.objects.entities.Projectile;

/**
 * Projectile that will be shot by mobs.
 */
public class MobProjectile extends Projectile {
    protected ObjectManager objectManager;
    protected Vector2 direction;
    protected float spawnX;
    protected float spawnY;


    public MobProjectile(ObjectManager objectManager, float spawnX, float spawnY, Vector2 dir, float velocity, int damage){
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.objectManager = objectManager;
        this.direction = dir;
        setEntityID(GameObject.PROJECTILE);
        setSaveInRooms(DONT_SAVE);
        setTypeID(Projectile.MOB_PROJECTILE);
        this.damage = new Value(damage,Value.NORMAL_DAMAGE);
        this.velocity = velocity;
        //setting width, height and radius of the box2d body
        width =24;
        height=24;
        radius = 12;
    }
    @Override
    public void onHit(Character character){
        character.onDamageTaken(damage);
        killYourself();
    }
    @Override
    public void update(float delta) {
        stateTime+=delta;
    }

    @Override
    public void addToBox2dWorld(World world) {

        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spawnX,spawnY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.allowSleep = false;
        bodyDef.linearVelocity.set(direction.scl(velocity));

        //create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        //Shape definiton
        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PIXELS_TO_UNITS);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 50f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = PROJECTILE_BIT;
        fixtureDef.filter.maskBits = (PLAYER_BIT| TERRAIN_BIT | CLUTTER_BIT | PORTAL_BIT | TREASURE_BIT);
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
        //SoundManager.sound.play();
    }
}
