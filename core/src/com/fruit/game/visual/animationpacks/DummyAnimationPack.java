package com.fruit.game.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.entities.enemies.Dummy;
import com.fruit.game.utilities.Utils;
import com.fruit.game.visual.Assets;

public class DummyAnimationPack implements Constants {
    private boolean loaded;
    private Animation animation;
    private Vector2 pos;
    private OnCharacterEffectPack onCharacteREffectPack;

    public DummyAnimationPack(OnCharacterEffectPack onCharacteREffectPack){
        this.onCharacteREffectPack = onCharacteREffectPack;
    }

    public void load(){
        if(!loaded) {
            pos = new Vector2();

            Texture testM = (Texture) Assets.getAsset("dummysheet.png", Texture.class);
            TextureRegion[][] tmpM = TextureRegion.split(testM, testM.getWidth() / 6, testM.getHeight() / 2);
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
        pos.set(Utils.getDrawPositionBasedOnBox2dCircle(dummy,pos));
                batch.draw(animation.getKeyFrame(stateTime, true), pos.x, pos.y, dummy.getWidth(), dummy.getHeight());
        if(dummy.status.isHealing()){
            onCharacteREffectPack.render(dummy,batch, stateTime, OnCharacterEffectPack.HEALED, pos.x, pos.y, dummy.getWidth(), dummy.getHeight());
        }
        if(dummy.status.isShielded()){
            onCharacteREffectPack.render(dummy,batch,stateTime, OnCharacterEffectPack.SHIELDED,pos.x,pos.y,dummy.getWidth(),dummy.getHeight());
        }
        if(dummy.status.isPoisoned()){
            onCharacteREffectPack.render(dummy,batch,stateTime, OnCharacterEffectPack.POISONED,pos.x,pos.y,dummy.getWidth(),dummy.getHeight());
        }
    }
}
