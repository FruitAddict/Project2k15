package com.fruit.visual;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.*;
import com.fruit.logic.objects.abstracted.GameObject;
import com.fruit.logic.objects.abstracted.MovableGameObject;
import com.fruit.tests.Dummy;
import com.fruit.tests.MindlessWalker;
import com.fruit.visual.animationpacks.*;

import java.util.Comparator;

/**
 * Class stub. Should take care of all the rendering of objects and entities.
 *
 */
public class ObjectRenderer implements Constants {
    float stateTime = 0;
    PlayerAnimationPack playerAnimationPack;
    MindlessWalkerAnimationPack mindlessWalkerAnimationPack;
    UtilityAnimationPack utilityAnimationPack;
    ProjectileAnimationPack projectileAnimationPack;
    DummyAnimationPack dummyAnimationPack;

    public ObjectRenderer(){
        playerAnimationPack = new PlayerAnimationPack();
        mindlessWalkerAnimationPack = new MindlessWalkerAnimationPack();
        utilityAnimationPack = new UtilityAnimationPack();
        projectileAnimationPack = new ProjectileAnimationPack();
        dummyAnimationPack = new DummyAnimationPack();
    }

    public void render(float delta, Array<GameObject> objects, SpriteBatch batch){
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

        for(GameObject e : objects){
            switch(e.getTypeID()){
                case PLAYER_TYPE:{
                    playerAnimationPack.load();
                    playerAnimationPack.render(stateTime,(Player)e,batch);
                    break;
                }
                case WALKER_TYPE:{
                    mindlessWalkerAnimationPack.load();
                    mindlessWalkerAnimationPack.render(stateTime,(MindlessWalker)e,batch);
                    break;
                }
                case CLUTTER_TYPE:{
                    utilityAnimationPack.load();
                    utilityAnimationPack.render(stateTime,(MovableGameObject)e,batch);
                    break;
                }
                case PROJECTILE_TYPE:{
                    projectileAnimationPack.load();
                    projectileAnimationPack.render(stateTime,(Projectile)e,batch);
                    break;
                }
                case DUMMY_TYPE:{
                    dummyAnimationPack.load();
                    dummyAnimationPack.render(stateTime,(Dummy)e,batch);
                    break;
                }
            }
        }
        stateTime+=delta;
    }
}
