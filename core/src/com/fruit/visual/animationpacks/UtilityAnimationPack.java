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
    private Vector2 pos;
    private boolean loaded;
    private Sprite boxSprite;


    public void load(){
        if(!loaded) {
            pos = new Vector2();
            Texture boxTexture = (Texture) Assets.getAsset("box2.png", Texture.class);
            boxSprite = new Sprite(boxTexture);
            loaded = true;
        }
    }

    public void render(float stateTime, MovableGameObject object, SpriteBatch batch){
        pos.set((object.getBody().getPosition().x*PIXELS_TO_METERS)-object.getWidth()/2,
                (object.getBody().getPosition().y*PIXELS_TO_METERS)-object.getHeight()/2);
        if(object.getEntityID() == GameObject.BOX){
            batch.draw(boxSprite,pos.x,pos.y,object.getWidth(),object.getHeight());
        }
    }
}
