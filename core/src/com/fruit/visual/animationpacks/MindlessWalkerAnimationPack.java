package com.fruit.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.entities.Character;
import com.fruit.visual.Assets;


public class MindlessWalkerAnimationPack implements Constants {
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
    private EffectRenderer effectRenderer;

    public MindlessWalkerAnimationPack(EffectRenderer effectRenderer){
        this.effectRenderer = effectRenderer;
    }

    public void load(){
        //nullify the old references
        if(!loaded) {
            pos = new Vector2();
            Texture testPlayerTexture = (Texture) Assets.getAsset("redheady.png", Texture.class);
            TextureRegion[][] tmp = TextureRegion.split(testPlayerTexture, testPlayerTexture.getWidth() / 3, testPlayerTexture.getHeight() / 4);
            playerSouthRegion = new TextureRegion[3];
            playerNorthRegion = new TextureRegion[3];
            playerEastRegion = new TextureRegion[3];
            playerWestRegion = new TextureRegion[3];
            playerNorthEastRegion = new TextureRegion[8];
            playerNorthWestRegion = new TextureRegion[8];
            playerSouthEastRegion = new TextureRegion[8];
            playerSouthWestRegion = new TextureRegion[8];
            for (int i = 0; i < 3; i++) {
                playerSouthRegion[i] = tmp[0][i];
                playerWestRegion[i] = tmp[1][i];
                playerNorthRegion[i] = tmp[2][i];
                playerEastRegion[i] = tmp[3][i];
            }
            playerAnimationSouth = new Animation(0.1f, playerSouthRegion);
            playerAnimationNorth = new Animation(0.1f, playerNorthRegion);
            playerAnimationWest = new Animation(0.1f, playerWestRegion);
            playerAnimationEast = new Animation(0.1f, playerEastRegion);
            loaded = true;
        }
    }

    public void render(float stateTime, Character character, SpriteBatch batch){
        pos.set((character.getBody().getPosition().x*PIXELS_TO_METERS)-character.getWidth()/2,
                (character.getBody().getPosition().y*PIXELS_TO_METERS)-character.getHeight()/2);
        if(character.facingN || character.facingNE || character.facingNW){
            batch.draw(playerAnimationNorth.getKeyFrame(stateTime,true),pos.x,pos.y,character.getWidth(),character.getHeight());
        }else if(character.facingS || character.facingSE || character.facingSW){
            batch.draw(playerAnimationSouth.getKeyFrame(stateTime,true),pos.x,pos.y,character.getWidth(),character.getHeight());
        }else if(character.facingE){
            batch.draw(playerAnimationEast.getKeyFrame(stateTime,true),pos.x,pos.y,character.getWidth(),character.getHeight());
        }else if(character.facingW){
            batch.draw(playerAnimationWest.getKeyFrame(stateTime,true),pos.x,pos.y,character.getWidth(),character.getHeight());
        } else {
            batch.draw(playerSouthRegion[1],pos.x,pos.y,character.getWidth(),character.getHeight());
        }
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
