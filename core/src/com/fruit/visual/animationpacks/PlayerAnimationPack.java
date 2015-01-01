package com.fruit.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.utilities.Utils;
import com.fruit.visual.Assets;


public class PlayerAnimationPack implements Constants {

    private Vector2 pos;

    private Animation playerAnimationNorth;
    private Animation playerAnimationNorthEast;
    private Animation playerAnimationNorthWest;
    private Animation playerAnimationSouthWest;
    private Animation playerAnimationSouthEast;
    private Animation playerAnimationSouth;
    private Animation playerAnimationWest;
    private Animation playerAnimationEast;

    //debug test


    private TextureRegion[] playerSouthRegion;
    private TextureRegion[] playerNorthRegion;
    private TextureRegion[] playerEastRegion;
    private TextureRegion[] playerWestRegion;
    private TextureRegion[] playerNorthEastRegion;
    private TextureRegion[] playerNorthWestRegion;
    private TextureRegion[] playerSouthEastRegion;
    private TextureRegion[] playerSouthWestRegion;

    private TextureRegion[] playerHeadNorth;
    private TextureRegion[] playerHeadSouth;
    private TextureRegion[] playerHeadWest;
    private TextureRegion[] playerHeadEast;

    private Animation playerHeadAnimationNorth;
    private Animation playerHeadAnimationSouth;
    private Animation playerHeadAnimationWest;
    private Animation playerheadAnimationEast;

    private boolean loaded = false;
    private OnCharacterEffectPack onCharacteREffectPack;

    public PlayerAnimationPack(OnCharacterEffectPack onCharacteREffectPack){
        this.onCharacteREffectPack = onCharacteREffectPack;
    }

    public void load(){
        //nullify the old references
        if(!loaded) {
            pos = new Vector2();
            Texture testPlayerTexture = (Texture) Assets.getAsset("spritesheet.png", Texture.class);
            //body
            TextureRegion[][] tmp = TextureRegion.split(testPlayerTexture, testPlayerTexture.getWidth() / 8, testPlayerTexture.getHeight() / 4);
            playerSouthRegion = new TextureRegion[8];
            playerNorthRegion = new TextureRegion[8];
            playerEastRegion = new TextureRegion[8];
            playerWestRegion = new TextureRegion[8];
            playerNorthEastRegion = new TextureRegion[8];
            playerNorthWestRegion = new TextureRegion[8];
            playerSouthEastRegion = new TextureRegion[8];
            playerSouthWestRegion = new TextureRegion[8];
            for (int i = 0; i < 8; i++) {
                playerSouthRegion[i] = tmp[0][i];
                playerNorthRegion[i] = tmp[1][i];
                playerWestRegion[i] = tmp[2][i];
                playerEastRegion[i] = tmp[3][i];
            }
            playerAnimationSouth = new Animation(0.1f, playerSouthRegion);
            playerAnimationNorth = new Animation(0.1f, playerNorthRegion);
            playerAnimationWest = new Animation(0.1f, playerWestRegion);
            playerAnimationEast = new Animation(0.1f, playerEastRegion);
            //head
            Texture testPlayerHead = (Texture)Assets.getAsset("head.png",Texture.class);
            TextureRegion[][] playerHeadSplit = TextureRegion.split(testPlayerHead, testPlayerHead.getWidth() / 4, testPlayerHead.getHeight());
            playerHeadSouth = new TextureRegion[1];
            playerHeadWest = new TextureRegion[1];
            playerHeadNorth = new TextureRegion[1];
            playerHeadEast = new TextureRegion[1];
            playerHeadSouth[0] = playerHeadSplit[0][0];
            playerHeadWest[0] = playerHeadSplit[0][1];
            playerHeadNorth[0] = playerHeadSplit[0][2];
            playerHeadEast[0] = playerHeadSplit[0][3];

            playerHeadAnimationSouth = new Animation(0.1f,playerHeadSouth);
            playerHeadAnimationNorth = new Animation(0.1f,playerHeadNorth);
            playerheadAnimationEast = new Animation(0.1f,playerHeadEast);
            playerHeadAnimationWest = new Animation(0.1f, playerHeadWest);

            loaded = true;

        }
    }

    public void render(float stateTime, Player player, SpriteBatch batch){
        //position for rendering, this alghorithm is the same for every renderable object
        pos.set(Utils.getDrawPositionBasedOnBox2dCircle(player));
        if(player.facingN ){
            batch.draw(playerAnimationNorth.getKeyFrame(stateTime,true),pos.x,pos.y,player.getWidth(),player.getHeight());
            batch.draw(playerHeadAnimationNorth.getKeyFrame(stateTime,true),pos.x-15f ,pos.y+player.getHeight()-26
                    ,64,88);
        }else if(player.facingS ){
            batch.draw(playerAnimationSouth.getKeyFrame(stateTime,true),pos.x,pos.y,player.getWidth(),player.getHeight());
            batch.draw(playerHeadAnimationSouth.getKeyFrame(stateTime,true),pos.x-15f ,pos.y+player.getHeight()-26
                    ,64,88);
        }else if(player.facingE ){
            batch.draw(playerAnimationEast.getKeyFrame(stateTime,true),pos.x,pos.y,player.getWidth(),player.getHeight());
            batch.draw(playerheadAnimationEast.getKeyFrame(stateTime,true),pos.x-15f ,pos.y+player.getHeight()-26
                    ,64,88);
        }else if(player.facingW){
            batch.draw(playerAnimationWest.getKeyFrame(stateTime,true),pos.x,pos.y,player.getWidth(),player.getHeight());
            batch.draw(playerHeadAnimationWest.getKeyFrame(stateTime,true),pos.x-15f  ,pos.y+player.getHeight()-26
                    ,64,88);
        } else {
            batch.draw(playerSouthRegion[0],pos.x,pos.y,player.getWidth(),player.getHeight());
            batch.draw(playerHeadAnimationSouth.getKeyFrame(stateTime,true),pos.x-15f ,pos.y+player.getHeight()-25
                    ,64,88);
        }
        //batch.draw(playerHead,pos.x-3.2f  ,pos.y+character.getHeight()-6,64,64);
        if(player.status.isHealing()){
            onCharacteREffectPack.render(player,batch,stateTime, OnCharacterEffectPack.HEALED,pos.x,pos.y,player.getWidth(),player.getHeight());
        }
        if(player.status.isShielded()){
            onCharacteREffectPack.render(player,batch,stateTime, OnCharacterEffectPack.SHIELDED,pos.x,pos.y,player.getWidth(),player.getHeight());
        }
        if(player.status.isPoisoned()){
            onCharacteREffectPack.render(player,batch,stateTime, OnCharacterEffectPack.POISONED,pos.x,pos.y,player.getWidth(),player.getHeight());
        }
        if(player.status.isLeveledUp()){
            //effectRenderer.render(player,batch,stateTime,EffectRenderer.LEVEL_UP_TRIGGER,pos.x,pos.y,player.getWidth(),player.getHeight());
        }
        if(player.status.isBurning()){
            onCharacteREffectPack.render(player,batch,stateTime, OnCharacterEffectPack.BURNING,pos.x,pos.y,player.getWidth(),player.getHeight());

        }
    }
}
