package com.fruit.game.visual.animationpacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.fruit.game.logic.objects.entities.Enemy;
import com.fruit.game.logic.objects.entities.enemies.Zombie;
import com.fruit.game.utilities.Utils;
import com.fruit.game.visual.Assets;

/**
 * @Author FruitAddict
 */
public class ZombieAnimationPack {

    private final OnCharacterEffectPack onCharEffectPack;
    private boolean loaded = false;
    private Vector2 pos;

    private Animation zombie1R, zombie1L, zombie1U, zombie1D,
            zombie2R, zombie2L, zombie2U, zombie2D,
            zombie3R, zombie3L, zombie3U, zombie3D,
            zombie4R, zombie4L, zombie4U, zombie4D,
            zombie5R, zombie5L, zombie5U, zombie5D,
            zombie6R, zombie6L, zombie6U, zombie6D,
            zombie7R, zombie7L, zombie7U, zombie7D,
            zombie8R, zombie8L, zombie8U, zombie8D;

    private TextureRegion[] zombie1DTexReg, zombie2DTexReg,
            zombie3DTexReg, zombie4DTexReg, zombie5DTexReg,
            zombie6DTexReg, zombie7DTexReg, zombie8DTexReg;

    public ZombieAnimationPack(OnCharacterEffectPack onCharacterEffectPack){
        this.onCharEffectPack = onCharacterEffectPack;
        pos = new Vector2();
    }

    public void load(){
        if(!loaded){
            loaded = true;
            Texture zombies = (Texture)Assets.getAsset("mobs//ZombieSheet.png",Texture.class);
            TextureRegion[][] zombieFrames = TextureRegion.split(zombies,zombies.getWidth()/12,zombies.getHeight()/8);
            TextureRegion[] zombie1RTexReg = new TextureRegion[3];
            TextureRegion[] zombie1LTexReg = new TextureRegion[3];
            TextureRegion[] zombie1UTexReg = new TextureRegion[3];
            zombie1DTexReg = new TextureRegion[3];
            //2
            TextureRegion[] zombie2RTexReg = new TextureRegion[3];
            TextureRegion[] zombie2LTexReg = new TextureRegion[3];
            TextureRegion[] zombie2UTexReg = new TextureRegion[3];
            zombie2DTexReg = new TextureRegion[3];
            //3
            TextureRegion[] zombie3RTexReg = new TextureRegion[3];
            TextureRegion[] zombie3LTexReg = new TextureRegion[3];
            TextureRegion[] zombie3UTexReg = new TextureRegion[3];
            zombie3DTexReg = new TextureRegion[3];
            //4
            TextureRegion[] zombie4RTexReg = new TextureRegion[3];
            TextureRegion[] zombie4LTexReg = new TextureRegion[3];
            TextureRegion[] zombie4UTexReg = new TextureRegion[3];
            zombie4DTexReg = new TextureRegion[3];
            //5
            TextureRegion[] zombie5RTexReg = new TextureRegion[3];
            TextureRegion[] zombie5LTexReg = new TextureRegion[3];
            TextureRegion[] zombie5UTexReg = new TextureRegion[3];
            zombie5DTexReg = new TextureRegion[3];
            //6
            TextureRegion[] zombie6RTexReg = new TextureRegion[3];
            TextureRegion[] zombie6LTexReg = new TextureRegion[3];
            TextureRegion[] zombie6UTexReg = new TextureRegion[3];
            zombie6DTexReg = new TextureRegion[3];
            //7
            TextureRegion[] zombie7RTexReg = new TextureRegion[3];
            TextureRegion[] zombie7LTexReg = new TextureRegion[3];
            TextureRegion[] zombie7UTexReg = new TextureRegion[3];
            zombie7DTexReg = new TextureRegion[3];
            //8
            TextureRegion[] zombie8RTexReg = new TextureRegion[3];
            TextureRegion[] zombie8LTexReg = new TextureRegion[3];
            TextureRegion[] zombie8UTexReg = new TextureRegion[3];
            zombie8DTexReg = new TextureRegion[3];
            for(int i =0 ; i< 3; i++){
                zombie1DTexReg[i] = zombieFrames[0][i];
                zombie1LTexReg[i] = zombieFrames[1][i];
                zombie1RTexReg[i] = zombieFrames[2][i];
                zombie1UTexReg[i] = zombieFrames[3][i];

                zombie2DTexReg[i] = zombieFrames[4][i];
                zombie2LTexReg[i] = zombieFrames[5][i];
                zombie2RTexReg[i] = zombieFrames[6][i];
                zombie2UTexReg[i] = zombieFrames[7][i];

                zombie3DTexReg[i] = zombieFrames[0][3+i];
                zombie3LTexReg[i] = zombieFrames[1][3+i];
                zombie3RTexReg[i] = zombieFrames[2][3+i];
                zombie3UTexReg[i] = zombieFrames[3][3+i];

                zombie4DTexReg[i] = zombieFrames[4][3+i];
                zombie4LTexReg[i] = zombieFrames[5][3+i];
                zombie4RTexReg[i] = zombieFrames[6][3+i];
                zombie4UTexReg[i] = zombieFrames[7][3+i];

                zombie5DTexReg[i] = zombieFrames[0][6+i];
                zombie5LTexReg[i] = zombieFrames[1][6+i];
                zombie5RTexReg[i] = zombieFrames[2][6+i];
                zombie5UTexReg[i] = zombieFrames[3][6+i];

                zombie6DTexReg[i] = zombieFrames[4][6+i];
                zombie6LTexReg[i] = zombieFrames[5][6+i];
                zombie6RTexReg[i] = zombieFrames[6][6+i];
                zombie6UTexReg[i] = zombieFrames[7][6+i];

                zombie7DTexReg[i] = zombieFrames[0][9+i];
                zombie7LTexReg[i] = zombieFrames[1][9+i];
                zombie7RTexReg[i] = zombieFrames[2][9+i];
                zombie7UTexReg[i] = zombieFrames[3][9+i];

                zombie8DTexReg[i] = zombieFrames[0][9+i];
                zombie8LTexReg[i] = zombieFrames[1][9+i];
                zombie8RTexReg[i] = zombieFrames[2][9+i];
                zombie8UTexReg[i] = zombieFrames[3][9+i];
            }

            zombie1D = new Animation(0.2f, zombie1DTexReg);
            zombie1U = new Animation(0.2f, zombie1UTexReg);
            zombie1R = new Animation(0.2f, zombie1RTexReg);
            zombie1L = new Animation(0.2f, zombie1LTexReg);

            zombie2D = new Animation(0.2f, zombie2DTexReg);
            zombie2U = new Animation(0.2f, zombie2UTexReg);
            zombie2R = new Animation(0.2f, zombie2RTexReg);
            zombie2L = new Animation(0.2f, zombie2LTexReg);

            zombie3D = new Animation(0.2f, zombie3DTexReg);
            zombie3U = new Animation(0.2f, zombie3UTexReg);
            zombie3R = new Animation(0.2f, zombie3RTexReg);
            zombie3L = new Animation(0.2f, zombie3LTexReg);

            zombie4D = new Animation(0.2f, zombie4DTexReg);
            zombie4U = new Animation(0.2f, zombie4UTexReg);
            zombie4R = new Animation(0.2f, zombie4RTexReg);
            zombie4L = new Animation(0.2f, zombie4LTexReg);

            zombie5D = new Animation(0.2f, zombie5DTexReg);
            zombie5U = new Animation(0.2f, zombie5UTexReg);
            zombie5R = new Animation(0.2f, zombie5RTexReg);
            zombie5L = new Animation(0.2f, zombie5LTexReg);

            zombie6D = new Animation(0.2f, zombie6DTexReg);
            zombie6U = new Animation(0.2f, zombie6UTexReg);
            zombie6R = new Animation(0.2f, zombie6RTexReg);
            zombie6L = new Animation(0.2f, zombie6LTexReg);

            zombie7D = new Animation(0.2f, zombie7DTexReg);
            zombie7U = new Animation(0.2f, zombie7UTexReg);
            zombie7R = new Animation(0.2f, zombie7RTexReg);
            zombie7L = new Animation(0.2f, zombie7LTexReg);

            zombie8D = new Animation(0.2f, zombie8DTexReg);
            zombie8U = new Animation(0.2f, zombie8UTexReg);
            zombie8R = new Animation(0.2f, zombie8RTexReg);
            zombie8L = new Animation(0.2f, zombie8LTexReg);
        }
    }

    public void render(float stateTime,Enemy zombie, SpriteBatch batch){
        pos.set(Utils.getDrawPositionBasedOnBox2dCircle(zombie, pos));
        if(zombie.status.isJustHit()){
            batch.setColor(1,0.5f,0.5f,0.8f);
        }
        switch(((Zombie)zombie).getZombieType()){
            case Zombie.ZOMBIE_1:{
                if(zombie.facingN ){
                    batch.draw(zombie1U.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingS){
                    batch.draw(zombie1D.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingE){
                    batch.draw(zombie1R.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingW){
                    batch.draw(zombie1L.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                } else {
                    batch.draw(zombie1DTexReg[1],pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }
                break;
            }
            case Zombie.ZOMBIE_2:{
                if(zombie.facingN ){
                    batch.draw(zombie2U.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingS){
                    batch.draw(zombie2D.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingE){
                    batch.draw(zombie2R.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingW){
                    batch.draw(zombie2L.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                } else {
                    batch.draw(zombie2DTexReg[1],pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }
                break;
            }
            case Zombie.ZOMBIE_3:{
                if(zombie.facingN ){
                    batch.draw(zombie3U.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingS){
                    batch.draw(zombie3D.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingE){
                    batch.draw(zombie3R.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingW){
                    batch.draw(zombie3L.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                } else {
                    batch.draw(zombie3DTexReg[1],pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }
                break;
            }
            case Zombie.ZOMBIE_4:{
                if(zombie.facingN ){
                    batch.draw(zombie4U.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingS){
                    batch.draw(zombie4D.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingE){
                    batch.draw(zombie4R.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingW){
                    batch.draw(zombie4L.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                } else {
                    batch.draw(zombie4DTexReg[1],pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }
                break;
            }
            case Zombie.ZOMBIE_5:{
                if(zombie.facingN ){
                    batch.draw(zombie5U.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingS){
                    batch.draw(zombie5D.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingE){
                    batch.draw(zombie5R.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingW){
                    batch.draw(zombie5L.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                } else {
                    batch.draw(zombie5DTexReg[1],pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }
                break;
            }
            case Zombie.ZOMBIE_6:{
                if(zombie.facingN ){
                    batch.draw(zombie6U.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingS){
                    batch.draw(zombie6D.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingE){
                    batch.draw(zombie6R.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingW){
                    batch.draw(zombie6L.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                } else {
                    batch.draw(zombie6DTexReg[1],pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }
                break;
            }
            case Zombie.ZOMBIE_7:{
                if(zombie.facingN ){
                    batch.draw(zombie7U.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingS){
                    batch.draw(zombie7D.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingE){
                    batch.draw(zombie7R.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingW){
                    batch.draw(zombie7L.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                } else {
                    batch.draw(zombie7DTexReg[1],pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }
                break;
            }
            case Zombie.ZOMBIE_8:{
                if(zombie.facingN ){
                    batch.draw(zombie8U.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingS){
                    batch.draw(zombie8D.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingE){
                    batch.draw(zombie8R.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }else if(zombie.facingW){
                    batch.draw(zombie8L.getKeyFrame(stateTime,true),pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                } else {
                    batch.draw(zombie8DTexReg[1],pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
                }
                break;
            }
        }
        if(zombie.status.isJustHit()){
            batch.setColor(1,1f,1f,1f);
        }
        if(zombie.status.isHealing()){
            onCharEffectPack.render(zombie,batch,stateTime, OnCharacterEffectPack.HEALED,pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
        }
        if(zombie.status.isShielded()){
            onCharEffectPack.render(zombie,batch,stateTime, OnCharacterEffectPack.SHIELDED,pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
        }
        if(zombie.status.isPoisoned()){
            onCharEffectPack.render(zombie,batch,stateTime, OnCharacterEffectPack.POISONED,pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
        }
        if(zombie.status.isAttackedByPlayer()){
            onCharEffectPack.render(zombie,batch,stateTime, OnCharacterEffectPack.HP_BAR,pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
        }
        if(zombie.status.isBurning()){
            onCharEffectPack.render(zombie,batch,stateTime, OnCharacterEffectPack.BURNING,pos.x,pos.y,zombie.getWidth(),zombie.getHeight());
        }
    }
}
