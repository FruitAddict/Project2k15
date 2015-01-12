package com.fruit.game.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.utilities.Utils;
import com.fruit.game.visual.Assets;

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

    public void render(float stateTime, GameObject object, SpriteBatch batch){
        pos.set(Utils.getDrawPositionBasedOnBox2dCircle(object));
        if(object.getEntityID() == GameObject.BOX){
            batch.draw(boxSprite,pos.x,pos.y,object.getWidth(),object.getHeight());
        }
    }
}