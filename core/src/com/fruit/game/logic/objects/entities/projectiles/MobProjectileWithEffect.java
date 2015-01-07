package com.fruit.game.logic.objects.entities.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.effects.OnHitEffect;
import com.fruit.game.logic.objects.entities.Character;

/**
 * @Author FruitAddict
 */
public class MobProjectileWithEffect extends MobProjectile {

    private OnHitEffect onHitEffect;

    public MobProjectileWithEffect(ObjectManager objectManager, float spawnX, float spawnY, Vector2 dir, float velocity, int damage, OnHitEffect onHitEffect) {
        super(objectManager, spawnX, spawnY, dir, velocity, damage);
        this.onHitEffect = onHitEffect;
    }
    @Override
    public void onHit(Character character){
        super.onHit(character);
        onHitEffect.onHit(this,character, damage);
    }

}
