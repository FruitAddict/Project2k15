package com.fruit.game.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.fruit.game.logic.objects.entities.Enemy;
import com.fruit.game.utilities.Utils;
import com.fruit.game.visual.Assets;
import com.fruit.game.logic.objects.entities.Character;


/**
 * @Author FruitAddict
 */
public class TheEyeAnimationPack {

    private Vector2 pos;

    private Animation playerAnimationNorth;
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

    public TheEyeAnimationPack(OnCharacterEffectPack onCharacteREffectPack){
        this.onCharacteREffectPack = onCharacteREffectPack;
    }

    public void load(){
        //nullify the old references
        if(!loaded) {
            pos = new Vector2();
            Texture testPlayerTexture = (Texture) Assets.getAsset("borrowed//eyeball.png", Texture.class);
            TextureRegion[][] tmp = TextureRegion.split(testPlayerTexture, testPlayerTexture.getWidth() / 4, testPlayerTexture.getHeight() / 4);
            playerSouthRegion = new TextureRegion[3];
            playerNorthRegion = new TextureRegion[3];
            playerEastRegion = new TextureRegion[3];
            playerWestRegion = new TextureRegion[3];
            for (int i = 0; i < 3; i++) {
                playerSouthRegion[i] = tmp[0][i];
                playerWestRegion[i] = tmp[1][i];
                playerEastRegion[i] = tmp[2][i];
                playerNorthRegion[i] = tmp[3][i];

            }
            playerAnimationSouth = new Animation(0.15f, playerSouthRegion);
            playerAnimationNorth = new Animation(0.15f, playerNorthRegion);
            playerAnimationWest = new Animation(0.15f, playerWestRegion);
            playerAnimationEast = new Animation(0.15f, playerEastRegion);
            loaded = true;
        }
    }

    public void render(float stateTime,Enemy theEye, SpriteBatch batch){
        pos.set(Utils.getDrawPositionBasedOnBox2dCircle(theEye,pos));
        if(theEye.status.isJustHit()){
            batch.setColor(1,0.1f,0.1f,1f);
        }
        if(theEye.facingN ){
            batch.draw(playerAnimationNorth.getKeyFrame(stateTime,true),pos.x,pos.y,theEye.getWidth(),theEye.getHeight());
        }else if(theEye.facingS){
            batch.draw(playerAnimationSouth.getKeyFrame(stateTime,true),pos.x,pos.y,theEye.getWidth(),theEye.getHeight());
        }else if(theEye.facingE){
            batch.draw(playerAnimationEast.getKeyFrame(stateTime,true),pos.x,pos.y,theEye.getWidth(),theEye.getHeight());
        }else if(theEye.facingW){
            batch.draw(playerAnimationWest.getKeyFrame(stateTime,true),pos.x,pos.y,theEye.getWidth(),theEye.getHeight());
        } else {
            batch.draw(playerSouthRegion[1],pos.x,pos.y,theEye.getWidth(),theEye.getHeight());
        }
        if(theEye.status.isJustHit()){
            batch.setColor(1,1f,1f,1f);
        }
        if(theEye.status.isHealing()){
            onCharacteREffectPack.render(theEye,batch,stateTime, OnCharacterEffectPack.HEALED,pos.x,pos.y,theEye.getWidth(),theEye.getHeight());
        }
        if(theEye.status.isShielded()){
            onCharacteREffectPack.render(theEye,batch,stateTime, OnCharacterEffectPack.SHIELDED,pos.x,pos.y,theEye.getWidth(),theEye.getHeight());
        }
        if(theEye.status.isPoisoned()){
            onCharacteREffectPack.render(theEye,batch,stateTime, OnCharacterEffectPack.POISONED,pos.x,pos.y,theEye.getWidth(),theEye.getHeight());
        }
        if(theEye.status.isAttackedByPlayer()){
            onCharacteREffectPack.render(theEye,batch,stateTime, OnCharacterEffectPack.HP_BAR,pos.x,pos.y,theEye.getWidth(),theEye.getHeight());
        }
        if(theEye.status.isBurning()){
            onCharacteREffectPack.render(theEye,batch,stateTime, OnCharacterEffectPack.BURNING,pos.x,pos.y,theEye.getWidth(),theEye.getHeight());
        }
    }

}
