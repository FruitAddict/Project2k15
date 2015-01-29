package com.fruit.game.logic.objects.effects.passive;

import com.fruit.game.Controller;
import com.fruit.game.logic.objects.effects.PassiveEffect;
import com.fruit.game.logic.objects.entities.player.Player;

/**
 * @Author FruitAddict
 */
public class IncreaseHealthPoints extends PassiveEffect {

    private Player player;
    private int amount;

    public IncreaseHealthPoints(Player player, int amount){
        setEffectType(PassiveEffect.INCREASE_HEALTH_POINTS);
        this.player = player;
        this.amount = amount;
    }

    @Override
    public void update(float delta) {
        //no
    }

    @Override
    public void join(PassiveEffect passiveEffect) {
        IncreaseHealthPoints increaseHealthPoints = (IncreaseHealthPoints)passiveEffect;
        onRemove();
        amount+=increaseHealthPoints.amount;
        apply();
    }

    @Override
    public void apply() {
        player.stats.setBaseMaximumHealthPoints(player.stats.getBaseMaximumHealthPoints()+amount);
        Controller.getUserInterface().updateStatusBars(
                player.stats.getHealthPoints(),
                player.stats.getBaseMaximumHealthPoints(),
                player.getExperiencePoints(),
                player.getNextLevelExpRequirement(),
                player.getStatPoints());

    }

    @Override
    public void onRemove() {
        player.stats.setBaseMaximumHealthPoints(player.stats.getBaseMaximumHealthPoints()-amount);
    }
}
