package com.fruit.game.visual.animationpacks;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;
import com.fruit.game.Controller;
import com.fruit.game.visual.Assets;
import com.fruit.game.visual.tween.SpriteAccessor;
import com.fruit.game.visual.tween.TweenUtils;
import com.fruit.game.logic.objects.entities.Character;

/**
 * Class dedicated to rendering effects on stuff. Like, a bleeding particle effect around a mob
 * If it has a poison stats etc.
 */
public class OnCharacterEffectPack {
    //effect ids
    public static final int HEALED = 1;
    public static final int POISONED = 2;
    public static final int SHIELDED = 3;
    public static final int LEVEL_UP_TRIGGER = 4;
    public static final int HP_BAR = 5;
    public static final int BURNING = 6;

    private Animation healingAnimation;
    private Animation poisonedAnimation;
    private Animation shieldedAnimation;
    private Animation burnedAnimation;
    private Animation levelUpAnimation;

    private Texture blackTexture;
    private Texture redTexture;
    TextureRegion[] animFramesLevelUp;

    private float levelUpStateTime = 0;


    public OnCharacterEffectPack(){
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
        Texture testPoisonedTexture = (Texture) Assets.getAsset("effects//poison.png", Texture.class);
        TextureRegion[][] tmp = TextureRegion.split(testPoisonedTexture, testPoisonedTexture.getWidth() / 4, testPoisonedTexture.getHeight());
        TextureRegion[] animFramesPoisoned = new TextureRegion[4];
        animFramesPoisoned[0] = tmp[0][0];
        animFramesPoisoned[1] = tmp[0][1];
        animFramesPoisoned[2] = tmp[0][2];
        animFramesPoisoned[3] = tmp[0][3];
        poisonedAnimation = new Animation(0.1f, animFramesPoisoned);

        //shielded animation effect
        Texture shieldedTexture = (Texture) Assets.getAsset("effects//shieldeffect.png", Texture.class);
        TextureRegion[][] tmp3 = TextureRegion.split(shieldedTexture, shieldedTexture.getWidth() / 4, shieldedTexture.getHeight());
        TextureRegion[] animFramesShielded = new TextureRegion[4];
        animFramesShielded[0] = tmp3[0][0];
        animFramesShielded[1] = tmp3[0][1];
        animFramesShielded[2] = tmp3[0][2];
        animFramesShielded[3] = tmp3[0][3];
        shieldedAnimation = new Animation(0.1f, animFramesShielded);

        Texture levelUpTexture = (Texture) Assets.getAsset("lvl_spritesheet.png",Texture.class);
        TextureRegion[][] tmp4 = TextureRegion.split(levelUpTexture,levelUpTexture.getWidth()/7,levelUpTexture.getHeight());
        animFramesLevelUp = new TextureRegion[7];
        for(int i =0 ;i < animFramesLevelUp.length;i++){
            animFramesLevelUp[i] = tmp4[0][6-i];
        }
        levelUpAnimation = new Animation(0.15f, animFramesLevelUp);

        //burned animation effect
        /**Texture burnedTexture = (Texture) Assets.getAsset("effects//fireeffect.png", Texture.class);
        TextureRegion[][] tmp4 = TextureRegion.split(burnedTexture, burnedTexture.getWidth() / 8, burnedTexture.getHeight());
        TextureRegion[] burnedFrames = new TextureRegion[8];
        burnedFrames[0] = tmp4[0][0];
        burnedFrames[1] = tmp4[0][1];
        burnedFrames[2] = tmp4[0][2];
        burnedFrames[3] = tmp4[0][3];
        burnedFrames[4] = tmp4[0][4];
        burnedFrames[5] = tmp4[0][5];
        burnedFrames[6] = tmp4[0][6];
        burnedFrames[7] = tmp4[0][7];
        burnedAnimation = new Animation(0.1f, burnedFrames);
         */
        TextureAtlas burnedAtlas = (TextureAtlas)Assets.getAsset("effects//fire.atlas",TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> atlasRegions = new Array<>();
        atlasRegions = burnedAtlas.findRegions("onfire");
        burnedAnimation = new Animation(0.1f, atlasRegions);

        //texture for hp bars under mobs
        Pixmap redPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        redPixmap.setColor(Color.RED);
        redPixmap.fill();
        Pixmap blackPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        blackPixmap.setColor(Color.BLACK);
        blackPixmap.fill();

        redTexture = new Texture(redPixmap);
        blackTexture = new Texture(blackPixmap);

    }

    public  <T extends Character> void render(T character, SpriteBatch batch,float stateTime, int effectType, float x, float y, float width, float height){
        // renders continuous effects on mobs etc.
        switch(effectType) {
            case HEALED: {
                batch.draw(healingAnimation.getKeyFrame(stateTime, true), x, y, width,height);
                break;
            }
            case POISONED: {
                batch.draw(poisonedAnimation.getKeyFrame(stateTime,true),x,y+height,width,height/4);
                break;
            }
            case BURNING:{
                batch.draw(burnedAnimation.getKeyFrame(stateTime,true),x,y,width,height);
                break;
            }
            case SHIELDED: {
                batch.draw(shieldedAnimation.getKeyFrame(stateTime,true),x,y,width,height);
                break;
            }
            case LEVEL_UP_TRIGGER: {
                if(!levelUpAnimation.isAnimationFinished(levelUpStateTime)) {
                    levelUpStateTime+= Gdx.graphics.getDeltaTime();
                    batch.draw(levelUpAnimation.getKeyFrame(levelUpStateTime, true), x - (181 / 2) + (width / 2), y, 181, 176);
                }else {
                    Controller.getWorldUpdater().getPlayer().status.setLeveledUp(false);
                }

                break;
            }
            case HP_BAR:{
                if(character.stats.getHealthPoints()>0) {
                    batch.draw(blackTexture, x, y - 10, width, 10);
                    batch.draw(redTexture, x, y - 9, width * character.stats.getHealthPointPercentOfMax(), 8);
                }
                break;
            }

        }
    }
}
