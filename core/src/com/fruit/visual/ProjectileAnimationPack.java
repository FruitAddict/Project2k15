package com.fruit.visual;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.Projectile;

public class ProjectileAnimationPack implements Constants{
    private boolean loaded;
    private Animation animation;
    private Animation animationDead;

    public void load(){
        if(!loaded) {
            Texture testM = (Texture) Assets.getAsset("projectile.png", Texture.class);
            TextureRegion[][] tmpM = TextureRegion.split(testM, testM.getWidth() / 4, testM.getHeight());
            TextureRegion[] animRegion = new TextureRegion[4];
            for (int i = 0; i < 4; i++) {
                animRegion[i] = tmpM[0][i];
            }
            animation = new Animation(0.1f, animRegion);

            Texture testM2 = (Texture) Assets.getAsset("deadfireball.png", Texture.class);
            TextureRegion[][] tmpM2 = TextureRegion.split(testM2, testM2.getWidth() / 4, testM2.getHeight());
            TextureRegion[] animRegion2 = new TextureRegion[4];
            for (int i = 0; i < 4; i++) {
                animRegion2[i] = tmpM2[0][i];
            }
            animationDead = new Animation(0.1f, animRegion2);
        }
    }

    public void render(float stateTime, Projectile projectile, SpriteBatch batch){
        Vector2 pos = new Vector2((projectile.getBody().getPosition().x*PIXELS_TO_METERS)-projectile.getWidth()/2,
                (projectile.getBody().getPosition().y*PIXELS_TO_METERS)-projectile.getHeight()/2);
        batch.draw(animationDead.getKeyFrame(stateTime,true),pos.x,pos.y,projectile.getWidth(),projectile.getHeight());
    }
}
