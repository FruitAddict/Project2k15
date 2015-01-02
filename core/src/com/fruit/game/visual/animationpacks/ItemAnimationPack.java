package com.fruit.game.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.items.Item;
import com.fruit.game.utilities.Utils;
import com.fruit.game.visual.Assets;

/**
 * @Author FruitAddict
 */
public class ItemAnimationPack implements Constants {
    private Vector2 pos;
    private Sprite heartSprite;
    private Sprite dmgUpSprite;
    private Sprite healthPotionSprite;
    private Sprite sphereOfProtectionSprite;
    private Sprite poisonTouchSprite;
    private Sprite piercingProjectilesSprite;
    private Sprite moreProjectilesSprite;
    private Sprite michaelBaySprite;
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
            Texture poisonTouchTexture = (Texture)Assets.getAsset("items//poisontouch.png", Texture.class);
            poisonTouchSprite = new Sprite(poisonTouchTexture);
            Texture piercingProjectileTexture = (Texture)Assets.getAsset("circle.png",Texture.class);
            piercingProjectilesSprite = new Sprite(piercingProjectileTexture);
            Texture morePRojectilesTexture = (Texture)Assets.getAsset("items//triforce.png",Texture.class);
            moreProjectilesSprite = new Sprite(morePRojectilesTexture);
            Texture michaelBayTexture = (Texture)Assets.getAsset("items//bay.png",Texture.class);
            michaelBaySprite = new Sprite(michaelBayTexture);
            loaded = true;
        }
    }


    public void render(float stateTime, Item item, SpriteBatch batch){
        pos.set(Utils.getDrawPositionBasedOnBox2dCircle(item));
       switch(item.getItemType()){
           case Item.HEART: {
               batch.draw(heartSprite, pos.x, pos.y, item.getWidth(), item.getHeight());
               break;
           }
           case Item.DAMAGE_UP_1: {
               batch.draw(dmgUpSprite, pos.x, pos.y, item.getWidth(), item.getHeight());
               break;
           }
           case Item.HEALTH_POTION: {
               batch.draw(healthPotionSprite,pos.x,pos.y,item.getWidth(),item.getHeight());
               break;
           }
           case Item.SPHERE_OF_PROTECTION: {
               batch.draw(sphereOfProtectionSprite,pos.x,pos.y,item.getWidth(),item.getHeight());
               break;
           }
           case Item.POISON_TOUCH: {
               batch.draw(poisonTouchSprite,pos.x,pos.y,item.getWidth(),item.getHeight());
               break;
           }
           case Item.PIERCING_PROJECTILE: {
               batch.draw(piercingProjectilesSprite,pos.x,pos.y,item.getWidth(),item.getHeight());
               break;
           }
           case Item.MORE_PROJECTILES: {
               batch.draw(moreProjectilesSprite,pos.x,pos.y,item.getWidth(),item.getHeight());
               break;
           }
           case Item.MICHAEL_BAY: {
               batch.draw(michaelBaySprite,pos.x,pos.y,item.getWidth(),item.getHeight());
           }
       }
    }
}
