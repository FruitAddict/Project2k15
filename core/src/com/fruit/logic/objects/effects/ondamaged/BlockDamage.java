package com.fruit.logic.objects.effects.ondamaged;

import com.fruit.Controller;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.Value;
import com.fruit.logic.objects.effects.OnDamageTakenEffect;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.visual.messages.TextMessage;
import com.fruit.visual.messages.TextRenderer;

/**
 * @Author FruitAddict
 */
public class BlockDamage extends OnDamageTakenEffect implements Constants {
    private int blockCount;
    private Player player;

    public BlockDamage(Player player, int blockCount){
        this.blockCount = blockCount;
        this.player = player;
        player.status.setShielded(true);
    }
    @Override
    public void onDamageTaken(Value value) {
        if(blockCount>0){
            value.setValue(0);
            Controller.addOnScreenMessage(new TextMessage("Blocked!", player.getBody().getPosition().x * PIXELS_TO_METERS,
                    player.getBody().getPosition().y * PIXELS_TO_METERS, 1.5f, TextRenderer.greenFont,TextMessage.UP_AND_FALL));
            blockCount--;
        }else {
            player.removeOnDamageTakenEffect(this);
            player.status.setShielded(false);
        }
    }
    @Override
    public void join(OnDamageTakenEffect onDamageTakenEffect){
        BlockDamage blockEffectReceived = (BlockDamage)onDamageTakenEffect;
        blockCount+=blockEffectReceived.blockCount/2;
    }
}
