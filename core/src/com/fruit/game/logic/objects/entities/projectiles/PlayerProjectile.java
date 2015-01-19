package com.fruit.game.logic.objects.entities.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.effects.OnHitEffect;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.Enemy;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.Projectile;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class PlayerProjectile extends Projectile {

    //Player-created projectiles should have a reference to player's on hit effect list on their creation
    private Array<OnHitEffect> onHitEffects;
    private boolean piercing;
    private float knockBack;

    public PlayerProjectile(Player player, ObjectManager objectManager, float spawnX, float spawnY, Vector2 dir, float velocity) {
        this.objectManager = objectManager;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        onHitEffects = player.getOnHitEffects();
        damage = new Value(player.stats.getCombinedDamage(),Value.NORMAL_DAMAGE);
        setTypeID(Projectile.PLAYER_PROJECTILE);
        setEntityID(GameObject.PROJECTILE);
        setSaveInRooms(false);
        //setting width, height and radius of the box2d body
        width =16;
        height=16;
        radius = 8;
        if(damage.getValue()>=2){
            width*=1.1f;
            height*=1.1f;
            radius*=1.1f;
        }
        direction = dir;
        this.velocity = velocity;
        piercing = player.stats.isPiercingProjectiles();
        knockBack = player.stats.getKnockBack();
    }

    @Override
    public void onHit(Character character){
        for(OnHitEffect onHitEffect : onHitEffects ){
            onHitEffect.onHit(this,character,damage);
        }
        character.onDamageTaken(damage);
        character.addLinearVelocity(direction.x*knockBack,direction.y*knockBack);
        if(!piercing) {
            killYourself();
        }
    }

    @Override
    public void addToBox2dWorld(World world) {

        //Player body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spawnX,spawnY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.allowSleep = false;
        bodyDef.linearVelocity.set(direction.cpy().scl(velocity));

        //create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        //Shape definiton
        CircleShape shape = new CircleShape();
        shape.setRadius(radius/ PIXELS_TO_UNITS);

        //fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 50f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = PLAYER_PROJECTILE_BIT;
        fixtureDef.filter.maskBits = (ENEMY_BIT | TERRAIN_BIT| ITEM_BIT | PORTAL_BIT | TREASURE_BIT | CLUTTER_BIT);
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);

        //dispose shape
        shape.dispose();
    }
    @Override
    public void update(float delta) {
        stateTime+=delta;
    }

    @Override
    public void killYourself() {
        objectManager.removeObject(this);
        //SoundManager.sound.play();
    }

}
