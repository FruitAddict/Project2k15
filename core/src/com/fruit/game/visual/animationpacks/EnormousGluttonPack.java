package com.fruit.game.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.fruit.game.logic.objects.entities.*;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.utilities.Utils;
import com.fruit.game.visual.Assets;

/**
 * @Author FruitAddict
 */
public class EnormousGluttonPack {

    private Vector2 pos;
    private boolean loaded = false;
    private OnCharacterEffectPack onCharacterEffectPack;

    private Animation bossWalk;
    private TextureRegion[] frames;

    public EnormousGluttonPack(OnCharacterEffectPack onCharacterEffectPack){
        this.onCharacterEffectPack = onCharacterEffectPack;
    }

    public void load(){
        //nullify the old references
        if(!loaded) {
            pos = new Vector2();
            Texture testPlayerTexture = (Texture) Assets.getAsset("mobs//glutton_walk.png", Texture.class);
            TextureRegion[][] tmp = TextureRegion.split(testPlayerTexture, testPlayerTexture.getWidth() / 3, testPlayerTexture.getHeight() / 2);
            frames = new TextureRegion[6];
            for(int i =0 ;i <2;i++){
                for (int j = 0; j < 3; j++) {
                    frames[i * 3 + j] = tmp[i][j];
                }
            }
            bossWalk = new Animation(0.1f,frames);
            loaded = true;
        }
    }

    public void render(float stateTime, Character character, SpriteBatch batch){
        pos.set(Utils.getDrawPositionBasedOnBox2dCircle(character));

        if(character.idle){
            batch.draw(frames[0], pos.x, pos.y, character.getWidth(), character.getHeight());
        }else{
            batch.draw(bossWalk.getKeyFrame(stateTime,true),pos.x,pos.y,character.getWidth(),character.getHeight());
        }
        if(character.status.isHealing()){
            onCharacterEffectPack.render(character, batch, stateTime, OnCharacterEffectPack.HEALED, pos.x, pos.y, character.getWidth(), character.getHeight());
        }
        if(character.status.isShielded()){
            onCharacterEffectPack.render(character, batch, stateTime, OnCharacterEffectPack.SHIELDED, pos.x, pos.y, character.getWidth(), character.getHeight());
        }
        if(character.status.isPoisoned()){
            onCharacterEffectPack.render(character, batch, stateTime, OnCharacterEffectPack.POISONED, pos.x, pos.y, character.getWidth(), character.getHeight());
        }
        if(character.status.isAttackedByPlayer()){
            onCharacterEffectPack.render(character, batch, stateTime, OnCharacterEffectPack.HP_BAR, pos.x, pos.y, character.getWidth(), character.getHeight());
        }
        if(character.status.isBurning()){
            onCharacterEffectPack.render(character, batch, stateTime, OnCharacterEffectPack.BURNING, pos.x, pos.y, character.getWidth(), character.getHeight());
        }
    }
}