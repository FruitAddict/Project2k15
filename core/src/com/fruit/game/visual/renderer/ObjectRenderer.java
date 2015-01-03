package com.fruit.game.visual.renderer;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.Projectile;
import com.fruit.game.logic.objects.entities.enemies.Dummy;
import com.fruit.game.logic.objects.entities.enemies.MindlessWalker;
import com.fruit.game.logic.objects.entities.enemies.TheEye;
import com.fruit.game.logic.objects.entities.misc.Explosion;
import com.fruit.game.logic.objects.entities.player.Player;
import com.fruit.game.logic.objects.items.Item;
import com.fruit.game.visual.animationpacks.*;

import java.util.Comparator;


/**
 * Object renderer that works with world renderer. While world renderer takes care of
 * drawing the map, this class renders the player, projectiles and everything
 * related.
 */
public class ObjectRenderer implements Constants {
    private float stateTime;

    //Effect Renderer that will take care of rendering effects for each entity based on their stats.
    public OnCharacterEffectPack onCharacteREffectPack;
    //animation packs that will handle drawing specific entities
    public PlayerAnimationPack playerAnimationPack;
    public MindlessWalkerAnimationPack mindlessWalkerAnimationPack;
    public UtilityAnimationPack utilityAnimationPack;
    public ProjectileAnimationPack projectileAnimationPack;
    public DummyAnimationPack dummyAnimationPack;
    public ItemAnimationPack itemAnimationPack;
    public TheEyeAnimationPack theEyeAnimationPack;
    public EffectAnimationPack effectAnimationPack;

    public ObjectRenderer(){
        //some animation packs (for rendering characters) will need instance of effect renderer.
        onCharacteREffectPack = new OnCharacterEffectPack();
        playerAnimationPack = new PlayerAnimationPack(onCharacteREffectPack);
        mindlessWalkerAnimationPack = new MindlessWalkerAnimationPack(onCharacteREffectPack);
        utilityAnimationPack = new UtilityAnimationPack();
        projectileAnimationPack = new ProjectileAnimationPack();
        dummyAnimationPack = new DummyAnimationPack(onCharacteREffectPack);
        itemAnimationPack = new ItemAnimationPack();
        theEyeAnimationPack = new TheEyeAnimationPack(onCharacteREffectPack);
        effectAnimationPack = new EffectAnimationPack();
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
                    playerAnimationPack.render(((Player)e).stateTime,(Player)e,batch);
                    break;
                }
                case GameObject.MINDLESS_WALKER:{
                    mindlessWalkerAnimationPack.load();
                    mindlessWalkerAnimationPack.render(((MindlessWalker)e).stateTime,(MindlessWalker)e,batch);
                    break;
                }
                case GameObject.BOX:{
                    utilityAnimationPack.load();
                    utilityAnimationPack.render(stateTime,e,batch);
                    break;
                }
                case GameObject.ITEM: {
                    itemAnimationPack.load();
                    itemAnimationPack.render(stateTime, (Item)e, batch);
                    break;
                }
                case GameObject.PROJECTILE:{
                    projectileAnimationPack.load();
                    projectileAnimationPack.render(((Projectile)e).stateTime,(Projectile)e,batch);
                    break;
                }
                case GameObject.DUMMY:{
                    dummyAnimationPack.load();
                    dummyAnimationPack.render(stateTime,(Dummy)e,batch);
                    break;
                }
                case GameObject.THE_EYE:{
                    theEyeAnimationPack.load();
                    theEyeAnimationPack.render(((TheEye)e).stateTime,(TheEye)e,batch);
                    break;
                }
                case GameObject.EXPLOSION:{
                    effectAnimationPack.load();
                    effectAnimationPack.render(((Explosion)e).stateTime,e,batch);
                    break;
                }
            }
        }
        //increase state time by delta.
        stateTime+=delta;
    }
}
