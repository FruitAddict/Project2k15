package com.fruit.game.logic.objects.effects.ondamaged;


import com.fruit.game.Controller;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.Value;
import com.fruit.game.logic.objects.effects.OnDamageTakenEffect;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.player.Player;
import com.fruit.game.visual.messages.TextMessage;
import com.fruit.game.visual.messages.TextRenderer;

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
        setEffectID(OnDamageTakenEffect.BLOCK_ATTACKS);
    }
    @Override
    public void onDamageTaken(Character source, Value mod, Value original) {
        if(blockCount>0 || blockCount == OnDamageTakenEffect.INFINITE_CHARGES){
            mod.setValue(0);
            Controller.addOnScreenMessage("Blocked!", player.getBody().getPosition().x * PIXELS_TO_UNITS,
                    player.getBody().getPosition().y * PIXELS_TO_UNITS, 1.5f, TextRenderer.greenFont, TextMessage.FIXED_POINT_UPFALL);
            if(blockCount!=OnDamageTakenEffect.INFINITE_CHARGES) {
                blockCount--;
            }
        }else {
            player.removeOnDamageTakenEffect(this);
            player.status.setShielded(false);
        }
    }
    @Override
    public void join(OnDamageTakenEffect onDamageTakenEffect){
        BlockDamage blockEffectReceived = (BlockDamage)onDamageTakenEffect;
        if(((BlockDamage) onDamageTakenEffect).blockCount != OnDamageTakenEffect.INFINITE_CHARGES) {
            blockCount += blockEffectReceived.blockCount / 2;
        }else {
            blockCount = onDamageTakenEffect.INFINITE_CHARGES;
        }

    }
}
