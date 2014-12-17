package com.fruit.logic.objects.entities.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.SoundManager;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.entities.Character;
import com.fruit.logic.objects.entities.GameObject;
import com.fruit.logic.objects.entities.Projectile;

/**
 * Projectile class created by player.
 */
public class MobProjectile extends Projectile {
    protected ObjectManager objectManager;
    protected Vector2 direction;
    protected float spawnX;
    protected float spawnY;

    //damage carried by this projectile. Defaulted to 0 unless set.
    protected Value damage;
    //speed of this velocity in logic units/s
    protected Value velocity;

    public MobProjectile(ObjectManager objectManager, float spawnX, float spawnY, Vector2 dir, float velocity){
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.objectManager = objectManager;
        this.direction = dir;
        setEntityID(GameObject.PROJECTILE);
        setSaveInRooms(DONT_SAVE);
        setTypeID(Projectile.MOB_PROJECTILE);
        damage = new Value(0.5f);
        this.velocity = new Value(velocity);
    }
    @Override
    public void onHit(Character character){
        character.onDamageTaken(damage);
        killYourself();
    }
    @Override
    public void update(float delta) {
        if((body.getLinearVelocity().x <0.5f && body.getLinearVelocity().x >-0.5f)&&
                (body.getLinearVelocity().y <0.5f && body.getLinearVelocity().y > -0.5f)){
            killYourself();
        }
    }

    @Override
    public void addToBox2dWorld(World world) {
        //setting width and height
        width = 24;
        height = 24;

        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spawnX,spawnY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.allowSleep = false;
        bodyDef.linearVelocity.set(direction.scl(velocity.getValue()));

        //create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        //Shape definiton
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/PIXELS_TO_METERS/2,height/PIXELS_TO_METERS/2);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1000f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = PROJECTILE_BIT;
        fixtureDef.filter.maskBits = (PLAYER_BIT| TERRAIN_BIT | CLUTTER_BIT | PORTAL_BIT | TREASURE_BIT);

        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
        SoundManager.sound.play();
    }
}
