package com.fruit.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.fruit.logic.objects.items.Item;
import com.fruit.utilities.Utils;
import com.fruit.visual.Assets;

/**
 * @Author FruitAddict
 * Rendering game objects like explosions and other cool stuff will be
 * handled by this class.
 */
public class EffectAnimationPack {
    private Vector2 pos;
    private Animation explosionAnimation;

    public void load(){
        pos = new Vector2();
        Texture explosionTexture = (Texture) Assets.getAsset("borrowed//explosion.png", Texture.class);
        TextureRegion[][] animFrames = TextureRegion.split(explosionTexture,explosionTexture.getWidth()/5,explosionTexture.getHeight()/5);
        TextureRegion[] frames = new TextureRegion[25];
        for(int i =0 ;i <animFrames.length;i++){
            for (int j = 0; j < animFrames.length; j++) {
                frames[i * 5 + j] = animFrames[i][j];
            }
        }
        explosionAnimation = new Animation(1/25f,frames);
    }

    public void render(float stateTime, Item item, SpriteBatch batch) {
        pos.set(Utils.getDrawPositionBasedOnBox2dCircle(item));
    }
}
