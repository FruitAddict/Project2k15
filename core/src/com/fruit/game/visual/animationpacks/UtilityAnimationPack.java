package com.fruit.game.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.misc.Portal;
import com.fruit.game.utilities.Utils;
import com.fruit.game.visual.Assets;

public class UtilityAnimationPack implements Constants {
    private Vector2 pos;
    private boolean loaded;
    private Sprite boxSprite;
    private Sprite tempPortalHorizontal;
    private Sprite tempPortalVertical;
    private Animation torchAnimation;

    public void load(){
        if(!loaded) {
            pos = new Vector2();
            Texture boxTexture = (Texture) Assets.getAsset("box2.png", Texture.class);
            tempPortalHorizontal = new Sprite((Texture)Assets.getAsset("portalhorizontal.png",Texture.class));
            tempPortalVertical = new Sprite((Texture)Assets.getAsset("portalvertical.png",Texture.class));
            boxSprite = new Sprite(boxTexture);
            Texture torchTexture = (Texture)Assets.getAsset("effects//torch.png",Texture.class);
            TextureRegion[] torchRegion = new TextureRegion[5];
            TextureRegion[][] torchTextureSplit = TextureRegion.split(torchTexture,torchTexture.getWidth()/5,torchTexture.getHeight());
            for(int i =0;i<5;i++){
                torchRegion[i] = torchTextureSplit[0][i];
            }
            torchAnimation = new Animation(0.1f,torchRegion);
            loaded = true;
        }
    }

    public void render(float stateTime, GameObject object, SpriteBatch batch){
        if(object.getEntityID() == GameObject.BOX){
            pos.set(Utils.getDrawPositionBasedOnBox2dCircle(object,pos));
            batch.draw(boxSprite,pos.x,pos.y,object.getWidth(),object.getHeight());
        }
        if(object.getEntityID() == GameObject.PORTAL){
            pos.set(Utils.getDrawPositionBasedOnBox2dRectangle(object,pos));
            if(((Portal)object).isHorizontal()){
                batch.draw(tempPortalHorizontal,pos.x,pos.y);
            }else {
                batch.draw(tempPortalVertical,pos.x,pos.y);
            }
        }
        if(object.getEntityID() == GameObject.TORCH){
            pos.set(Utils.getDrawPositionBasedOnBox2dCircle(object,pos));
            batch.draw(torchAnimation.getKeyFrame(stateTime,true),pos.x,pos.y,object.getWidth(),object.getHeight());
        }
    }
}
