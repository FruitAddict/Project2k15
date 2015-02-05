package com.fruit.game.logic.objects.entities.projectiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.fruit.game.Controller;
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
    private Player player;
    private Color tint;

    public PlayerProjectile(Player player, ObjectManager objectManager, float spawnX, float spawnY, Vector2 dir) {
        this.objectManager = objectManager;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.player = player;
        onHitEffects = player.getOnHitEffects();
        damage = new Value(player.stats.getCombinedDamage(),Value.NORMAL_DAMAGE);
        setTypeID(Projectile.PLAYER_PROJECTILE);
        setEntityID(GameObject.PROJECTILE);
        setSaveInRooms(false);
        //setting width, height and radius of the box2d body
        radius = player.getProjectileRadius();
        velocity = player.getProjectileVelocity();
        tint = player.getProjectileTint();
        direction = dir;
        piercing = player.stats.isPiercingProjectiles();
        knockBack = player.stats.getKnockBack();
        width = radius*2;
        height = radius*2;
    }

    public PlayerProjectile setRadiusChain(float radius){
        this.radius = radius;
        return this;
    }

    @Override
    public void onHit(Character character){
        for(OnHitEffect onHitEffect : onHitEffects ){
            onHitEffect.onHit(this,character,damage);
        }
        character.onDamageTaken(player,damage);
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
