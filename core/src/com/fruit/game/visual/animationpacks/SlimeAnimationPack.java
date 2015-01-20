package com.fruit.game.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.entities.Character;
import com.fruit.game.logic.objects.entities.Enemy;
import com.fruit.game.utilities.Utils;
import com.fruit.game.visual.Assets;


public class SlimeAnimationPack implements Constants {
    private Vector2 pos;

    private Animation playerAnimationNorth;
    private Animation playerAnimationNorthEast;
    private Animation playerAnimationNorthWest;
    private Animation playerAnimationSouthWest;
    private Animation playerAnimationSouthEast;
    private Animation playerAnimationSouth;
    private Animation playerAnimationWest;
    private Animation playerAnimationEast;

    private TextureRegion[] playerSouthRegion;
    private TextureRegion[] playerNorthRegion;
    private TextureRegion[] playerEastRegion;
    private TextureRegion[] playerWestRegion;
    private TextureRegion[] playerNorthEastRegion;
    private TextureRegion[] playerNorthWestRegion;
    private TextureRegion[] playerSouthEastRegion;
    private TextureRegion[] playerSouthWestRegion;
    private boolean loaded = false;
    private OnCharacterEffectPack onCharacteREffectPack;

    public SlimeAnimationPack(OnCharacterEffectPack onCharacteREffectPack){
        this.onCharacteREffectPack = onCharacteREffectPack;
    }

    public void load(){
        //nullify the old references
        if(!loaded) {
            pos = new Vector2();
            Texture testPlayerTexture = (Texture) Assets.getAsset("v2.png", Texture.class);
            TextureRegion[][] tmp = TextureRegion.split(testPlayerTexture, testPlayerTexture.getWidth() / 4, testPlayerTexture.getHeight() / 4);
            playerSouthRegion = new TextureRegion[4];
            playerNorthRegion = new TextureRegion[4];
            playerEastRegion = new TextureRegion[4];
            playerWestRegion = new TextureRegion[4];
            playerNorthEastRegion = new TextureRegion[8];
            playerNorthWestRegion = new TextureRegion[8];
            playerSouthEastRegion = new TextureRegion[8];
            playerSouthWestRegion = new TextureRegion[8];
            for (int i = 0; i < 4; i++) {
                playerSouthRegion[i] = tmp[0][i];
                playerWestRegion[i] = tmp[1][i];
                playerEastRegion[i] = tmp[2][i];
                playerNorthRegion[i] = tmp[3][i];

            }
            playerAnimationSouth = new Animation(0.1f, playerSouthRegion);
            playerAnimationNorth = new Animation(0.1f, playerNorthRegion);
            playerAnimationWest = new Animation(0.1f, playerWestRegion);
            playerAnimationEast = new Animation(0.1f, playerEastRegion);
            loaded = true;
        }
    }

    public void render(float stateTime, Enemy slime, SpriteBatch batch){
        pos.set(Utils.getDrawPositionBasedOnBox2dCircle(slime,pos));
        if(slime.status.isBurning()){
            onCharacteREffectPack.render(slime,batch,stateTime, OnCharacterEffectPack.BURNING,pos.x,pos.y,slime.getWidth(),slime.getHeight());
        }
        if(slime.status.isJustHit()){
                batch.setColor(1, 0.5f, 0.5f, 0.8f);
        }
        if(slime.facingN){
            batch.draw(playerAnimationNorth.getKeyFrame(stateTime,true),pos.x,pos.y,slime.getWidth(),slime.getHeight());
        }else if(slime.facingS){
            batch.draw(playerAnimationSouth.getKeyFrame(stateTime,true),pos.x,pos.y,slime.getWidth(),slime.getHeight());
        }else if(slime.facingE){
            batch.draw(playerAnimationEast.getKeyFrame(stateTime,true),pos.x,pos.y,slime.getWidth(),slime.getHeight());
        }else if(slime.facingW){
            batch.draw(playerAnimationWest.getKeyFrame(stateTime,true),pos.x,pos.y,slime.getWidth(),slime.getHeight());
        } else {
            batch.draw(playerSouthRegion[1],pos.x,pos.y,slime.getWidth(),slime.getHeight());
        }
        if(slime.status.isJustHit()){
            batch.setColor(1,1f,1f,1f);
        }
        if(slime.status.isHealing()){
            onCharacteREffectPack.render(slime,batch,stateTime, OnCharacterEffectPack.HEALED,pos.x,pos.y,slime.getWidth(),slime.getHeight());
        }
        if(slime.status.isShielded()){
            onCharacteREffectPack.render(slime,batch,stateTime, OnCharacterEffectPack.SHIELDED,pos.x,pos.y,slime.getWidth(),slime.getHeight());
        }
        if(slime.status.isPoisoned()){
            onCharacteREffectPack.render(slime,batch,stateTime, OnCharacterEffectPack.POISONED,pos.x,pos.y,slime.getWidth(),slime.getHeight());
        }
        if(slime.status.isAttackedByPlayer()){
            onCharacteREffectPack.render(slime,batch,stateTime, OnCharacterEffectPack.HP_BAR,pos.x,pos.y,slime.getWidth(),slime.getHeight());
        }
    }

}
