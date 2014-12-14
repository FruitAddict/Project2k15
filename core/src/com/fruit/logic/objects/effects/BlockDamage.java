package com.fruit.logic.objects.effects;

import com.fruit.Controller;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.visual.Assets;
import com.fruit.visual.messages.TextMessage;

/**
 * @Author FruitAddict
 */
public class BlockDamage extends OnDamageTakenEffect implements Constants {
    private float blockCount;
    private Player player;
    public BlockDamage(Player player, float blockCount){
        this.blockCount = blockCount;
        this.player = player;
    }
    @Override
    public void onDamageTaken(Value value) {
        if(blockCount>0){
            value.setValue(0);
            Controller.addOnScreenMessage(new TextMessage("Blocked!", player.getBody().getPosition().x * PIXELS_TO_METERS,
                    player.getBody().getPosition().y * PIXELS_TO_METERS, 1.5f, Assets.greenFont));
            blockCount--;
        }else {
            player.removeOnDamageTakenEffect(this);
        }
    }
}
