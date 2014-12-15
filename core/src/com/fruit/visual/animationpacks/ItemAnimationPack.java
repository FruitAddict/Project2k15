package com.fruit.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.items.Item;
import com.fruit.visual.Assets;

/**
 * @Author FruitAddict
 */
public class ItemAnimationPack implements Constants {
    private Vector2 pos;
    private Sprite heartSprite;
    private Sprite dmgUpSprite;
    private Sprite healthPotionSprite;
    private Sprite sphereOfProtectionSprite;
    private boolean loaded = false;

    public void load(){
        if(!loaded) {
            pos = new Vector2();
            Texture heartTexture = (Texture) Assets.getAsset("heart.png", Texture.class);
            heartSprite = new Sprite(heartTexture);
            Texture dmgUpTexture = (Texture) Assets.getAsset("dmgup.png",Texture.class);
            dmgUpSprite = new Sprite(dmgUpTexture);
            Texture healthPotiontexture = (Texture)Assets.getAsset("items//hppotion_col.png", Texture.class);
            healthPotionSprite = new Sprite(healthPotiontexture);
            Texture sphereOfProtectionTexture = (Texture)Assets.getAsset("items//sphere.png", Texture.class);
            sphereOfProtectionSprite = new Sprite(sphereOfProtectionTexture);
            loaded = true;
        }
    }


    public void render(float stateTime, Item item, SpriteBatch batch){
        pos.set((item.getBody().getPosition().x*PIXELS_TO_METERS)-item.getWidth()/2,
                (item.getBody().getPosition().y*PIXELS_TO_METERS)-item.getHeight()/2);
        if (item.getItemType() == Item.HEART){
            batch.draw(heartSprite, pos.x, pos.y, item.getWidth(), item.getHeight());
        }
        else if (item.getItemType() == Item.DAMAGE_UP_1){
            batch.draw(dmgUpSprite, pos.x, pos.y, item.getWidth(), item.getHeight());
        }
        else if (item.getItemType() == Item.HEALTH_POTION){
            batch.draw(healthPotionSprite,pos.x,pos.y,item.getWidth(),item.getHeight());
        }
        else if(item.getItemType() == Item.SPHERE_OF_PROTECTION){
            batch.draw(sphereOfProtectionSprite,pos.x,pos.y,item.getWidth(),item.getHeight());
        }
    }
}
