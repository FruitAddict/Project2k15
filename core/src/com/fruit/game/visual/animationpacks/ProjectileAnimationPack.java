package com.fruit.game.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.entities.Projectile;
import com.fruit.game.utilities.Utils;
import com.fruit.game.visual.Assets;

public class ProjectileAnimationPack implements Constants {
    private Vector2 pos;
    private boolean loaded;
    private Animation animation;
    private Sprite mobProjectile1;
    private Sprite mobProjectile2;
    private Sprite mindlessWalkerProjectile;

    public void load(){
        if(!loaded) {
            pos = new Vector2();
            //player stuff
            Texture testM = (Texture) Assets.getAsset("projectileball.png", Texture.class);
            TextureRegion[][] tmpM = TextureRegion.split(testM, testM.getWidth() / 4, testM.getHeight());
            TextureRegion[] animRegion = new TextureRegion[4];
            for (int i = 0; i < 4; i++) {
                animRegion[i] = tmpM[0][i];
            }
            animation = new Animation(0.05f, animRegion);
            //mob stuff
            Texture mobProjectile1Texture = (Texture) Assets.getAsset("metal_red.png",Texture.class);
            mobProjectile1 = new Sprite(mobProjectile1Texture);

            Texture mobProjectile2Texture = (Texture) Assets.getAsset("proj.png",Texture.class);
            mobProjectile2 = new Sprite(mobProjectile2Texture);

            Texture mindlessWalkerTexture = (Texture) Assets.getAsset("Rock3.png",Texture.class);
            mindlessWalkerProjectile= new Sprite(mindlessWalkerTexture);

            mobProjectile1.setColor(new com.badlogic.gdx.graphics.Color(1f,0,0,1));

            loaded = true;
        }
    }

    public void render(float stateTime, Projectile projectile, SpriteBatch batch){
        pos.set(Utils.getDrawPositionBasedOnBox2dCircle(projectile));
        switch(projectile.getTypeID()) {
            case Projectile.PLAYER_PROJECTILE:{
                mobProjectile2.setPosition(pos.x,pos.y);
                mobProjectile2.setSize(projectile.getWidth(),projectile.getHeight());
                mobProjectile2.draw(batch);
                break;
            }
            case Projectile.MOB_PROJECTILE: {
                mobProjectile1.setPosition(pos.x,pos.y);
                mobProjectile1.setSize(projectile.getWidth(),projectile.getHeight());
                mobProjectile1.draw(batch);
                break;
            }
            case Projectile.MINDLESS_PROJECTILE:{
                mindlessWalkerProjectile.setPosition(pos.x,pos.y);
                mindlessWalkerProjectile.setSize(projectile.getWidth(),projectile.getHeight());
                mindlessWalkerProjectile.draw(batch);
                break;
            }
        }
    }
}
