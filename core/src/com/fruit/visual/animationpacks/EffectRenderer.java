package com.fruit.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fruit.visual.Assets;

/**
 * Class dedicated to rendering effects on stuff. Like, a bleeding particle effect around a mob
 * If it has a poison stats etc.
 */
public class EffectRenderer {
    //effect ids
    public static final int HEALED = 1;
    public static final int POISONED = 2;
    public static final int SHIELDED = 3;

    private Animation healingAnimation;
    private Animation poisonedAnimation;
    private Animation shieldedAnimation;

    public EffectRenderer(){
        //Healing animation effect
        Texture testHealTexture = (Texture) Assets.getAsset("effects//healeffect.png", Texture.class);
        TextureRegion[][] tmp2 = TextureRegion.split(testHealTexture, testHealTexture.getWidth() / 4, testHealTexture.getHeight());
        TextureRegion[] animFramesHealed = new TextureRegion[4];
        animFramesHealed[0] = tmp2[0][0];
        animFramesHealed[1] = tmp2[0][1];
        animFramesHealed[2] = tmp2[0][2];
        animFramesHealed[3] = tmp2[0][3];
        healingAnimation = new Animation(0.1f, animFramesHealed);

        //poisoned animation effect
        Texture testPoisonedTexture = (Texture) Assets.getAsset("effects//poisoneffect.png", Texture.class);
        TextureRegion[][] tmp = TextureRegion.split(testPoisonedTexture, testPoisonedTexture.getWidth() / 4, testHealTexture.getHeight());
        TextureRegion[] animFramesPoisoned = new TextureRegion[4];
        animFramesPoisoned[0] = tmp[0][0];
        animFramesPoisoned[1] = tmp[0][1];
        animFramesPoisoned[2] = tmp[0][2];
        animFramesPoisoned[3] = tmp[0][3];
        poisonedAnimation = new Animation(0.1f, animFramesPoisoned);

        //shielded animation effect
        Texture shieldedTexture = (Texture) Assets.getAsset("effects//shieldeffect.png", Texture.class);
        TextureRegion[][] tmp3 = TextureRegion.split(shieldedTexture, shieldedTexture.getWidth() / 4, testHealTexture.getHeight());
        TextureRegion[] animFramesShielded = new TextureRegion[4];
        animFramesShielded[0] = tmp3[0][0];
        animFramesShielded[1] = tmp3[0][1];
        animFramesShielded[2] = tmp3[0][2];
        animFramesShielded[3] = tmp3[0][3];
        shieldedAnimation = new Animation(0.1f, animFramesShielded);


    }

    public void render(SpriteBatch batch,float stateTime, int effectType, float x, float y, float width, float height){
        switch(effectType) {
            case HEALED: {
                batch.draw(healingAnimation.getKeyFrame(stateTime, true), x, y, width,height);
                break;
            }
            case POISONED: {
                batch.draw(poisonedAnimation.getKeyFrame(stateTime,true),x,y,width,height);
                break;
            }
            case SHIELDED: {
                batch.draw(shieldedAnimation.getKeyFrame(stateTime,true),x,y,width,height);
                break;
            }
        }
    }
}
