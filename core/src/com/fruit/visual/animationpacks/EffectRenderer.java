package com.fruit.visual.animationpacks;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.fruit.Controller;
import com.fruit.logic.objects.entities.*;
import com.fruit.logic.objects.entities.Character;
import com.fruit.utilities.Utils;
import com.fruit.visual.Assets;
import com.fruit.visual.tween.SpriteAccessor;
import com.fruit.visual.tween.TweenUtils;

/**
 * Class dedicated to rendering effects on stuff. Like, a bleeding particle effect around a mob
 * If it has a poison stats etc.
 */
public class EffectRenderer {
    //effect ids
    public static final int HEALED = 1;
    public static final int POISONED = 2;
    public static final int SHIELDED = 3;
    public static final int LEVEL_UP_TRIGGER = 4;
    public static final int HP_BAR = 5;

    private Animation healingAnimation;
    private Animation poisonedAnimation;
    private Animation shieldedAnimation;

    //level up effect sprites
    private Sprite levelUpText;
    private Sprite wingLeft;
    private Sprite wingRight;

    private Texture blackTexture;
    private Texture redTexture;

    //level up effect stuff
    private boolean levelUpTweenStarted = false;
    public TweenCallback levelUpCallBack;
    private float savedPosX, savedPosY;

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

        //level up sprites
        levelUpText = new Sprite((Texture)Assets.getAsset("effects//leveluptext.png", Texture.class));
        wingLeft = new Sprite((Texture)Assets.getAsset("effects//wing1.png", Texture.class));
        wingRight = new Sprite((Texture)Assets.getAsset("effects//wing2.png", Texture.class));

        //texture for hp bars under mobs
        Pixmap redPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        redPixmap.setColor(Color.RED);
        redPixmap.fill();
        Pixmap blackPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        blackPixmap.setColor(Color.BLACK);
        blackPixmap.fill();

        redTexture = new Texture(redPixmap);
        blackTexture = new Texture(blackPixmap);

        //level up tween callback
        levelUpCallBack = new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                levelUpTweenStarted = false;
                Controller.getWorldUpdater().getPlayer().status.setLeveledUp(false);
                levelUpText.setAlpha(0);
                wingLeft.setAlpha(0);
                wingRight.setAlpha(0);
                System.out.println("levle up callback reached");
            }
        };
    }

    public  <T extends Character> void render(T character, SpriteBatch batch,float stateTime, int effectType, float x, float y, float width, float height){
        // renders continuous effects on mobs etc.
        switch(effectType) {
            case HEALED: {
                batch.draw(healingAnimation.getKeyFrame(stateTime, true), x, y, width,height);
                break;
            }
            case POISONED: {
                batch.draw(poisonedAnimation.getKeyFrame(stateTime,true),x,y+48,width,20);
                break;
            }
            case SHIELDED: {
                batch.draw(shieldedAnimation.getKeyFrame(stateTime,true),x,y,width,height);
                break;
            }
            case LEVEL_UP_TRIGGER: {
                if (!levelUpTweenStarted) {
                    startLevelUpTween(x, y);
                    levelUpTweenStarted = true;
                } else {
                    levelUpText.draw(batch);
                    wingLeft.draw(batch);
                    wingRight.draw(batch);
                }
                break;
            }
            case HP_BAR:{
                batch.draw(blackTexture,x,y-10,width,10);
                batch.draw(redTexture,x,y-9,width*character.stats.getHealthPointPercentOfMax(),8);
                break;
            }
        }
    }

    public void startLevelUpTween(float posx, float posy){
        System.out.println("Starting the tween...");
        Timeline.createSequence()
                .push(Tween.set(levelUpText, SpriteAccessor.ALPHA).target(0.4f))
                .push(Tween.set(wingLeft, SpriteAccessor.ALPHA).target(0.4f))
                .push(Tween.set(wingLeft,SpriteAccessor.ROTATION).target(-90f))
                .push(Tween.set(wingRight,SpriteAccessor.ROTATION).target(90f))
                .push(Tween.set(wingRight, SpriteAccessor.ALPHA).target(0.4f))
                .push(Tween.set(levelUpText, SpriteAccessor.POSITION_X).target(posx + 18 - levelUpText.getWidth() / 2))
                .push((Tween.set(levelUpText, SpriteAccessor.POSITION_Y).target(posy)))
                .push(Tween.set(wingLeft, SpriteAccessor.POSITION_X).target(posx+ 18 - levelUpText.getWidth() / 2 - wingLeft.getWidth()))
                .push((Tween.set(wingLeft, SpriteAccessor.POSITION_Y).target(posy)))
                .push(Tween.set(wingRight, SpriteAccessor.POSITION_X).target(posx+ 18 + levelUpText.getWidth() / 2))
                .push((Tween.set(wingRight, SpriteAccessor.POSITION_Y).target(posy)))
                .beginParallel()
                .push(Tween.to(levelUpText, SpriteAccessor.ALPHA, 3f).target(1f))
                .push(Tween.to(wingLeft, SpriteAccessor.ALPHA, 3f).target(1f))
                .push(Tween.to(wingRight, SpriteAccessor.ALPHA, 3f).target(1f))
                .push(Tween.to(wingLeft, SpriteAccessor.ROTATION, 3f).target(0f))
                .push(Tween.to(wingRight, SpriteAccessor.ROTATION, 3f).target(0f))
                .push(Tween.to(levelUpText, SpriteAccessor.POSITION_Y, 3f).target(posy + 100))
                .push(Tween.to(wingLeft, SpriteAccessor.POSITION_Y, 3f).target(posy + 100+levelUpText.getHeight()))
                .push(Tween.to(wingRight, SpriteAccessor.POSITION_Y, 3f).target(posy+100+levelUpText.getHeight()))
                        .end()
                .setCallbackTriggers(TweenCallback.END).setCallback(levelUpCallBack)
                        .start(TweenUtils.tweenManager);
    }
}
