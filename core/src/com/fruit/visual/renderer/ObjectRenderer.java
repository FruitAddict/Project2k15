package com.fruit.visual.renderer;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.entities.GameObject;
import com.fruit.logic.objects.entities.MovableGameObject;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.logic.objects.entities.misc.Projectile;
import com.fruit.logic.objects.entities.enemies.Dummy;
import com.fruit.logic.objects.entities.enemies.MindlessWalker;
import com.fruit.visual.animationpacks.*;

import java.util.Comparator;

/**
 * Object renderer that works with world renderer. While world renderer takes care of
 * drawing the map, this class renders the player, projectiles and everything
 * related.
 */
public class ObjectRenderer implements Constants {
    float stateTime = 0;

    //animation packs that will handle drawing specific entities
    PlayerAnimationPack playerAnimationPack;
    MindlessWalkerAnimationPack mindlessWalkerAnimationPack;
    UtilityAnimationPack utilityAnimationPack;
    ProjectileAnimationPack projectileAnimationPack;
    DummyAnimationPack dummyAnimationPack;

    //Effect Renderer that will take care of rendering effects for each entity based on their stats.
    EffectRenderer effectRenderer;

    public ObjectRenderer(){
        playerAnimationPack = new PlayerAnimationPack();
        mindlessWalkerAnimationPack = new MindlessWalkerAnimationPack();
        utilityAnimationPack = new UtilityAnimationPack();
        projectileAnimationPack = new ProjectileAnimationPack();
        dummyAnimationPack = new DummyAnimationPack();
        effectRenderer = new EffectRenderer();
    }

    public void render(float delta, Array<GameObject> objects, SpriteBatch batch){
        //Sort the objects based on their vertical position in the world
        //to achieve correct rendering order
        objects.sort(new Comparator<GameObject>() {
            @Override
            public int compare(GameObject o1, GameObject o2) {
                if(o1.getBody().getPosition().y > o2.getBody().getPosition().y){
                    return -1;
                }
                else if(o1.getBody().getPosition().y == o2.getBody().getPosition().y){
                    return 0;
                }
                else {
                    return 1;
                }
            }
        });

        //render the objects based on their entity ID
        for(GameObject e : objects){
            switch(e.getEntityID()){
                case GameObject.PLAYER:{
                    playerAnimationPack.load();
                    playerAnimationPack.render(stateTime,(Player)e,batch);
                    break;
                }
                case GameObject.MINDLESS_WALKER:{
                    mindlessWalkerAnimationPack.load();
                    mindlessWalkerAnimationPack.render(stateTime,(MindlessWalker)e,batch);
                    break;
                }
                case GameObject.BOX:{
                    utilityAnimationPack.load();
                    utilityAnimationPack.render(stateTime,(MovableGameObject)e,batch);
                    break;
                }
                case GameObject.HEART:{
                    utilityAnimationPack.load();
                    utilityAnimationPack.render(stateTime,(MovableGameObject)e,batch);
                    break;
                }
                case GameObject.DMGUP:{
                    utilityAnimationPack.load();
                    utilityAnimationPack.render(stateTime,(MovableGameObject)e,batch);
                    break;
                }
                case GameObject.PROJECTILE:{
                    projectileAnimationPack.load();
                    projectileAnimationPack.render(stateTime,(Projectile)e,batch);
                    break;
                }
                case GameObject.DUMMY:{
                    dummyAnimationPack.load();
                    dummyAnimationPack.render(stateTime,(Dummy)e,batch);
                    break;
                }
            }
        }
        //increase state time by delta.
        stateTime+=delta;
    }
}
