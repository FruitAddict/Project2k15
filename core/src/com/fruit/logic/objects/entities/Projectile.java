package com.fruit.logic.objects.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.fruit.logic.EntityID;
import com.fruit.logic.ObjectManager;

//Basic projectile class.
public class Projectile extends MovableGameObject {
    private ObjectManager objectManager;
    private Vector2 direction;
    private float spawnX;
    private float spawnY;

    public Projectile(ObjectManager objectManager, float spawnX, float spawnY, Vector2 dir){
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.objectManager = objectManager;
        this.direction = dir;
        setEntityID(EntityID.PROJECTILE);
        setMaxVelocity(6);
        setSpeed(0.30f);
        setSaveInRooms(DONT_SAVE);
    }
    @Override
    public void update(float delta) {
        if((body.getLinearVelocity().x <0.5f && body.getLinearVelocity().x >-0.5f)&&
                (body.getLinearVelocity().y <0.5f && body.getLinearVelocity().y > -0.5f)){
            killYourself();
        }
    }

    @Override
    public void addToWorld(World world) {
        //setting width and height
        width = 24;
        height = 24;

        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spawnX,spawnY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.allowSleep = false;
        bodyDef.linearVelocity.set(direction.scl(maxVelocity));

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
        fixtureDef.filter.maskBits = (ENEMY_BIT | TERRAIN_BIT | CLUTTER_BIT | ITEM_BIT | PORTAL_BIT);

        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
    }
}
