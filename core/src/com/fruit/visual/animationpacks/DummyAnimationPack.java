package com.fruit.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.entities.enemies.Dummy;
import com.fruit.visual.Assets;

public class DummyAnimationPack implements Constants {
    private boolean loaded;
    private Animation animation;
    private Vector2 pos;

    public void load(){
        if(!loaded) {
            pos = new Vector2();

            Texture testM = (Texture) Assets.getAsset("dummysheet.png", Texture.class);
            TextureRegion[][] tmpM = TextureRegion.split(testM, testM.getWidth() / 6, testM.getHeight()/2);
            TextureRegion[] animRegion = new TextureRegion[12];

            //controll variable
            int currentFrameBeingAdded = 0;
            for (int i = 0; i < 2; i++) {
                for(int j =0 ;j<6;j++) {
                    animRegion[currentFrameBeingAdded++] = tmpM[i][j];
                }
            }
            animation = new Animation(0.3f, animRegion);
            animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
            loaded = true;
        }
    }

    public void render(float stateTime, Dummy dummy, SpriteBatch batch){
        pos.set((dummy.getBody().getPosition().x*PIXELS_TO_METERS)-dummy.getWidth()/2,
                (dummy.getBody().getPosition().y*PIXELS_TO_METERS)-dummy.getHeight()/2);
        batch.draw(animation.getKeyFrame(stateTime,true),pos.x,pos.y,dummy.getWidth(),dummy.getHeight());
    }
}
