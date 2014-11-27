package com.project2k15.rendering.objectrenderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.project2k15.rendering.Assets;
import com.project2k15.test.testmobs.MindlessWalker;

/**
 * Created by FruitAddict on 2014-11-27.
 */
public class MindlessWalkerAnimationPack {
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

    public void load(){
        //nullify the old references
        if(!loaded) {
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

    public void render(float stateTime, MindlessWalker player, SpriteBatch batch){
        if(player.facingN){
            batch.draw(playerAnimationNorth.getKeyFrame(stateTime,true),player.getPosition().x,player.getPosition().y
                    ,player.getWidth(),player.getHeight());
        }else if(player.facingS){
            batch.draw(playerAnimationSouth.getKeyFrame(stateTime,true),player.getPosition().x,player.getPosition().y
                    ,player.getWidth(),player.getHeight());
        }else if(player.facingE){
            batch.draw(playerAnimationEast.getKeyFrame(stateTime,true),player.getPosition().x,player.getPosition().y
                    ,player.getWidth(),player.getHeight());
        }else if(player.facingW){
            batch.draw(playerAnimationWest.getKeyFrame(stateTime,true),player.getPosition().x,player.getPosition().y
                    ,player.getWidth(),player.getHeight());
        } else {
            batch.draw(playerSouthRegion[1],player.getPosition().x,player.getPosition().y
                    ,player.getWidth(),player.getHeight());
        }
    }
}
