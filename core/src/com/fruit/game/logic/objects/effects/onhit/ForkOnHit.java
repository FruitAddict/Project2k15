package com.fruit.game.logic.objects.effects.onhit;

import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.effects.OnHitEffect;
import com.fruit.game.logic.objects.entities.*;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.player.Player;
import com.fruit.game.logic.objects.entities.projectiles.PlayerProjectile;

/**
 * @Author FruitAddict
 */
public class ForkOnHit extends OnHitEffect {
    private Player player;

    public ForkOnHit(Player player){
        this.player = player;
        setEffectID(OnHitEffect.FORK_ON_HIT);
    }
    @Override
    public void onHit(Projectile proj, Character enemy, Value damage) {
        if(!proj.isForked()) {
            PlayerProjectile projectile = new PlayerProjectile(player, player.getObjectManager(), enemy.getPosition().x, enemy.getPosition().y, proj.getDirection()
                    .rotate(5f), proj.getVelocity());
            projectile.setForked(true);
            projectile.setWidth(projectile.getWidth() * (2/3f));
            projectile.setHeight(projectile.getHeight() * (2/3f));
            projectile.setRadius(projectile.getRadius() * (2/3f));
            projectile.getDamage().setValue(Math.max(1,projectile.getDamage().getValue() / 2));
            player.getObjectManager().addObject(projectile);
            proj.setForked(true);
            proj.getBody().setLinearVelocity(proj.getBody().getLinearVelocity().rotate(-5f));
            proj.setWidth(proj.getWidth()* (2/3f));
            proj.setHeight(proj.getHeight()* (2/3f));
            proj.setRadius(proj.getRadius()* (2/3f));
            proj.getDamage().setValue(Math.max(1,projectile.getDamage().getValue() / 2));
        }
    }

    @Override
    public void join(OnHitEffect onHitEffect) {

    }
}
