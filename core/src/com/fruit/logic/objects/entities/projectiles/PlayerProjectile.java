package com.fruit.logic.objects.entities.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.fruit.SoundManager;
import com.fruit.logic.ObjectManager;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.effects.OnHitEffect;
import com.fruit.logic.objects.entities.Character;
import com.fruit.logic.objects.entities.Enemy;
import com.fruit.logic.objects.entities.GameObject;
import com.fruit.logic.objects.entities.Projectile;
import com.fruit.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class PlayerProjectile extends Projectile{

    //Player-created projectiles should have a reference to player's on hit effect list on their creation
    private Array<OnHitEffect> onHitEffects;

    public PlayerProjectile(Player player, ObjectManager objectManager, float spawnX, float spawnY, Vector2 dir, float velocity) {
        this.objectManager = objectManager;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        onHitEffects = player.getOnHitEffects();
        damage = new Value(player.stats.getCombinedDamage());
        setTypeID(Projectile.PLAYER_PROJECTILE);
        setEntityID(GameObject.PROJECTILE);
        setSaveInRooms(DONT_SAVE);
        //setting width, height and radius of the box2d body
        width =24;
        height=24;
        radius = 12;
        if(damage.getValue()>2){
            width*=1.5f;
            height*=1.5f;
        }
        direction = dir;
        this.velocity = new Value(velocity);
    }

    @Override
    public void onHit(Character character){
        for(OnHitEffect onHitEffect : onHitEffects ){
            onHitEffect.onHit((Enemy)character,damage);
        }
        character.onDamageTaken(damage);
        killYourself();
    }

    @Override
    public void addToBox2dWorld(World world) {

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
        CircleShape shape = new CircleShape();
        shape.setRadius(radius/PIXELS_TO_METERS);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 50f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = PLAYER_PROJECTILE_BIT;
        fixtureDef.filter.maskBits = (ENEMY_BIT | TERRAIN_BIT | CLUTTER_BIT | ITEM_BIT | PORTAL_BIT | TREASURE_BIT);

        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }
    @Override
    public void update(float delta) {
        //
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
        SoundManager.sound.play();
    }

}
