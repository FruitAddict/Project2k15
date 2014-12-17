package com.fruit.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.entities.Projectile;
import com.fruit.logic.objects.entities.projectiles.MobProjectile;
import com.fruit.visual.Assets;

public class ProjectileAnimationPack implements Constants{
    private Vector2 pos;
    private boolean loaded;
    private Animation animation;


    public void load(){
        if(!loaded) {
            pos = new Vector2();
            Texture testM = (Texture) Assets.getAsset("projectileball.png", Texture.class);
            TextureRegion[][] tmpM = TextureRegion.split(testM, testM.getWidth() / 4, testM.getHeight());
            TextureRegion[] animRegion = new TextureRegion[4];
            for (int i = 0; i < 4; i++) {
                animRegion[i] = tmpM[0][i];
            }
            animation = new Animation(0.05f, animRegion);
            loaded = true;
        }
    }

    public void render(float stateTime, Projectile projectile, SpriteBatch batch){
        pos.set((projectile.getBody().getPosition().x*PIXELS_TO_METERS)-projectile.getWidth()/2,
                (projectile.getBody().getPosition().y*PIXELS_TO_METERS)-projectile.getHeight()/2);
        batch.draw(animation.getKeyFrame(stateTime,true),pos.x,pos.y,projectile.getWidth(),projectile.getHeight());
    }
}
