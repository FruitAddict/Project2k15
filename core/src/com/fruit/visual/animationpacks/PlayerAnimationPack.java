package com.fruit.visual.animationpacks;

import android.media.effect.Effect;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.visual.Assets;


public class PlayerAnimationPack implements Constants {

    private Animation playerAnimationNorth;
    private Animation playerAnimationNorthEast;
    private Animation playerAnimationNorthWest;
    private Animation playerAnimationSouthWest;
    private Animation playerAnimationSouthEast;
    private Animation playerAnimationSouth;
    private Animation playerAnimationWest;
    private Animation playerAnimationEast;
    //debug font
    private BitmapFont font = new BitmapFont();
    //debug test
    private Animation healingAnimation;

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

    public void load(){
        //nullify the old references
        if(!loaded) {
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

            //test
            Texture testHealTexture = (Texture) Assets.getAsset("healeffect.png", Texture.class);
            TextureRegion[][] tmp2 = TextureRegion.split(testHealTexture, testHealTexture.getWidth() / 3, testHealTexture.getHeight());
            TextureRegion[] animFrames = new TextureRegion[3];
            animFrames[0] = tmp2[0][0];
            animFrames[1] = tmp2[0][1];
            animFrames[2] = tmp2[0][2];
            healingAnimation = new Animation(0.1f, animFrames);
        }
    }

    public void render(float stateTime, Player character, SpriteBatch batch){
        Vector2 pos = new Vector2((character.getBody().getPosition().x*PIXELS_TO_METERS)-character.getWidth()/2,
                (character.getBody().getPosition().y*PIXELS_TO_METERS)-character.getHeight()/2);
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
        //batch.draw(playerHead,pos.x  ,pos.y+character.getHeight()-5,64,64);

        if(character.status.isHealing()) {
            batch.draw(healingAnimation.getKeyFrame(stateTime, true), pos.x, pos.y, character.getWidth(), character.getHeight());
        }
    }
}
