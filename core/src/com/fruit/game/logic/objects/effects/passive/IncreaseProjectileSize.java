package com.fruit.game.logic.objects.effects.passive;

import com.fruit.game.logic.objects.effects.PassiveEffect;
import com.fruit.game.logic.objects.entities.*;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class IncreaseProjectileSize extends PassiveEffect {

    private com.fruit.game.logic.objects.entities.Character character;
    private float scalar;

    public IncreaseProjectileSize(Character character, float scalar){
        this.character = character;
        setEffectType(PassiveEffect.INCREASE_PROJECTILE_SIZE);
        this.scalar = scalar;
    }
    @Override
    public void update(float delta) {

    }

    @Override
    public void join(PassiveEffect passiveEffect) {
        IncreaseProjectileSize ip = (IncreaseProjectileSize)passiveEffect;
        onRemove();
        scalar+= ip.scalar;
        apply();
    }

    @Override
    public void apply() {
        Player p  = (Player)character;
        p.setProjectileRadius(p.getProjectileRadius()*scalar);
    }

    @Override
    public void onRemove() {
        Player p  = (Player)character;
        p.setProjectileRadius(p.getProjectileRadius()/scalar);
    }
}
