package com.fruit.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

    private Sprite playerHead;
    private boolean loaded = false;
    private EffectRenderer effectRenderer;

    public PlayerAnimationPack(EffectRenderer effectRenderer){
        this.effectRenderer = effectRenderer;
    }

    public void load(){
        //nullify the old references
        if(!loaded) {
            pos = new Vector2();
            Texture testPlayerTexture = (Texture) Assets.getAsset("spritesheet.png", Texture.class);
            Texture testPlayerHead = (Texture)Assets.getAsset("playerhead.png",Texture.class);
            playerHead = new Sprite(testPlayerHead);
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
            loaded = true;

        }
    }

    public void render(float stateTime, Player character, SpriteBatch batch){
        //position for rendering, this alghorithm is the same for every renderable object
        pos.set(Utils.getDrawPositionBasedOnBox2d(character));
        if(character.facingN || character.facingNE || character.facingNW){
            batch.draw(playerAnimationNorth.getKeyFrame(stateTime,true),pos.x,pos.y,character.getWidth(),character.getHeight());
        }else if(character.facingS ||character.facingSE || character.facingSW){
            batch.draw(playerAnimationSouth.getKeyFrame(stateTime,true),pos.x,pos.y,character.getWidth(),character.getHeight());
        }else if(character.facingE){
            batch.draw(playerAnimationEast.getKeyFrame(stateTime,true),pos.x,pos.y,character.getWidth(),character.getHeight());
        }else if(character.facingW){
            batch.draw(playerAnimationWest.getKeyFrame(stateTime,true),pos.x,pos.y,character.getWidth(),character.getHeight());
        } else {
            batch.draw(playerSouthRegion[0],pos.x,pos.y,character.getWidth(),character.getHeight());
        }
        //batch.draw(playerHead,pos.x-3.2f  ,pos.y+character.getHeight()-6,64,64);
        if(character.status.isHealing()){
            effectRenderer.render(batch,stateTime,EffectRenderer.HEALED,pos.x,pos.y,character.getWidth(),character.getHeight());
        }
        if(character.status.isShielded()){
            effectRenderer.render(batch,stateTime,EffectRenderer.SHIELDED,pos.x,pos.y,character.getWidth(),character.getHeight());
        }
        if(character.status.isPoisoned()){
            effectRenderer.render(batch,stateTime,EffectRenderer.POISONED,pos.x,pos.y,character.getWidth(),character.getHeight());
        }
    }
}
