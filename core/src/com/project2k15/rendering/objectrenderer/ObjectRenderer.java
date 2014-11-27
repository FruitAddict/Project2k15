package com.project2k15.rendering.objectrenderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.entities.EntityTypes;
import com.project2k15.logic.entities.Player;
import com.project2k15.logic.entities.abstracted.Entity;
import com.project2k15.test.testmobs.MindlessWalker;
import com.project2k15.test.testmobs.Projectile;

import java.util.Comparator;

/**
 * Class stub. Should take care of all the rendering of objects and entities.
 *
 */
public class ObjectRenderer implements EntityTypes {
    float stateTime = 0;
    PlayerAnimationPack playerAnimationPack;
    ProjectileAnimationPack projectileAnimationPack;
    MindlessWalkerAnimationPack mindlessWalkerAnimationPack;

    public ObjectRenderer(){
        playerAnimationPack = new PlayerAnimationPack();
        projectileAnimationPack = new ProjectileAnimationPack();
        mindlessWalkerAnimationPack = new MindlessWalkerAnimationPack();
    }

    public void render(float delta, Array<Entity> objects, SpriteBatch batch){
        objects.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                if(o1.getPosition().y > o2.getPosition().y){
                    return -1;
                }
                else if(o1.getPosition().y == o2.getPosition().y){
                    return 0;
                }
                else {
                    return 1;
                }
            }
        });

        for(Entity e : objects){
            switch(e.getTypeID()){
                case PLAYER:{
                    playerAnimationPack.load();
                    playerAnimationPack.render(stateTime,(Player)e,batch);
                    break;
                }
                case MINDLESS_WALKER:{
                    mindlessWalkerAnimationPack.load();
                    mindlessWalkerAnimationPack.render(stateTime,(MindlessWalker)e,batch);
                    break;
                }
                case PROJECTILE:{
                    projectileAnimationPack.load();
                    projectileAnimationPack.render(stateTime,(Projectile)e,batch);
                    break;
                }
            }
        }
        stateTime+=delta;
    }
}
