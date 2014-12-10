package com.fruit.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.entities.GameObject;
import com.fruit.logic.objects.entities.MovableGameObject;
import com.fruit.visual.Assets;

public class UtilityAnimationPack implements Constants {
    private boolean loaded;
    private Sprite boxSprite;
    private Sprite heartSprite;

    public void load(){
        if(!loaded) {
            Texture boxTexture = (Texture) Assets.getAsset("box2.png", Texture.class);
            boxSprite = new Sprite(boxTexture);
            Texture heartTexture = (Texture) Assets.getAsset("heart.png", Texture.class);
            heartSprite = new Sprite(heartTexture);
            loaded = true;
        }
    }

    public void render(float stateTime, MovableGameObject object, SpriteBatch batch){
        Vector2 pos = new Vector2((object.getBody().getPosition().x*PIXELS_TO_METERS)-object.getWidth()/2,
                (object.getBody().getPosition().y*PIXELS_TO_METERS)-object.getHeight()/2);
        if(object.getEntityID() == GameObject.BOX){;
            batch.draw(boxSprite,pos.x,pos.y,object.getWidth(),object.getHeight());
        }
        else if (object.getEntityID() == GameObject.HEART){
            batch.draw(heartSprite,pos.x,pos.y,object.getWidth(),object.getHeight());
        }
    }
}
