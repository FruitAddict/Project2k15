package com.fruit.game.visual.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.entities.GameObject;

/**
 * @Author FruitAddict
 */
public class ParticleRenderer {

    //particle emitter types
    public static final int BLOOD = 1;
    public static final int TORCH = 2;

    private Array<ParticleEffectPool.PooledEffect> activeEffects;
    private ParticleEffectPool effectPoolBlood;
    private ParticleEffectPool effectPoolTorch;
    private SpriteBatch batch;

    public ParticleRenderer(SpriteBatch batch){
        this.batch = batch;
        activeEffects = new Array<>();
        ParticleEffect testEffect = new ParticleEffect();
        testEffect.load(Gdx.files.internal("effects/boom.p"),Gdx.files.internal("effects"));
        effectPoolBlood = new ParticleEffectPool(testEffect,1,10);

        ParticleEffect torchEffect = new ParticleEffect();
        torchEffect.load(Gdx.files.internal("effects/smoke.p"),Gdx.files.internal("effects"));
        effectPoolTorch = new ParticleEffectPool(torchEffect,1,10);
    }

    public void addParticleEffect(GameObject gameObject, int type){
        switch(type) {
            case BLOOD: {
                ParticleEffectPool.PooledEffect bloodEffect = effectPoolBlood.obtain();
                activeEffects.add(bloodEffect);
                bloodEffect.setPosition(gameObject.getBodyPositionX() * Constants.PIXELS_TO_UNITS, gameObject.getBodyPositionY() * Constants.PIXELS_TO_UNITS);
                break;
            }
            case TORCH: {
                ParticleEffectPool.PooledEffect torchEffect = effectPoolTorch.obtain();
                activeEffects.add(torchEffect);
                torchEffect.setPosition(gameObject.getBodyPositionX() * Constants.PIXELS_TO_UNITS, gameObject.getBodyPositionY() * Constants.PIXELS_TO_UNITS + gameObject.getHeight() / 2);
                break;
            }

        }
    }

    public void render(float delta){
        for( int i =0; i < activeEffects.size; i++){
            ParticleEffectPool.PooledEffect effect = activeEffects.get(i);
            effect.draw(batch, delta);
            if(effect.isComplete()){
                effect.free();
                activeEffects.removeIndex(i);
            }
        }
    }

    public void removeAllParticleEffects(){
        for(int i =0 ; i < activeEffects.size; i++){
            activeEffects.get(i).free();
        }

        activeEffects.clear();
    }
}
