package com.fruit.game.visual.renderer;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.Projectile;
import com.fruit.game.logic.objects.entities.bosses.EnormousGlutton;
import com.fruit.game.logic.objects.entities.enemies.Dummy;
import com.fruit.game.logic.objects.entities.enemies.Slime;
import com.fruit.game.logic.objects.entities.enemies.TheEye;
import com.fruit.game.logic.objects.entities.misc.Explosion;
import com.fruit.game.logic.objects.entities.misc.Torch;
import com.fruit.game.logic.objects.entities.player.Player;
import com.fruit.game.logic.objects.items.Item;
import com.fruit.game.visual.animationpacks.*;


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
    public SlimeAnimationPack slimeAnimationPack;
    public UtilityAnimationPack utilityAnimationPack;
    public ProjectileAnimationPack projectileAnimationPack;
    public DummyAnimationPack dummyAnimationPack;
    public ItemAnimationPack itemAnimationPack;
    public TheEyeAnimationPack theEyeAnimationPack;
    public EffectAnimationPack effectAnimationPack;
    public EnormousGluttonPack enormousGluttonPack;

    public ObjectRenderer(){
        //some animation packs (for rendering characters) will need instance of effect renderer.
        onCharacteREffectPack = new OnCharacterEffectPack();
        playerAnimationPack = new PlayerAnimationPack(onCharacteREffectPack);
        slimeAnimationPack = new SlimeAnimationPack(onCharacteREffectPack);
        utilityAnimationPack = new UtilityAnimationPack();
        projectileAnimationPack = new ProjectileAnimationPack();
        dummyAnimationPack = new DummyAnimationPack(onCharacteREffectPack);
        itemAnimationPack = new ItemAnimationPack();
        theEyeAnimationPack = new TheEyeAnimationPack(onCharacteREffectPack);
        effectAnimationPack = new EffectAnimationPack();
        enormousGluttonPack = new EnormousGluttonPack(onCharacteREffectPack);
    }

    public void render(float delta, Array<GameObject> objects, SpriteBatch batch){
        //Sort the objects based on their vertical position in the world
        //to achieve correct rendering order
        objects.sort();
        //render the objects based on their entity ID
        for(GameObject e : objects){
            switch(e.getEntityID()){
                case GameObject.PLAYER:{
                    playerAnimationPack.load();
                    playerAnimationPack.render(((Player)e).stateTime,(Player)e,batch);
                    break;
                }
                case GameObject.MINDLESS_WALKER:{
                    slimeAnimationPack.load();
                    slimeAnimationPack.render(((Slime)e).stateTime,(Slime)e,batch);
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
                case GameObject.ENORMOUS_GLUTTON:{
                    enormousGluttonPack.load();
                    enormousGluttonPack.render(((EnormousGlutton)e).stateTime,(EnormousGlutton)e,batch);
                    break;
                }
                case GameObject.PORTAL:{
                    utilityAnimationPack.load();
                    utilityAnimationPack.render(stateTime,e,batch);
                    break;
                }
                case GameObject.TORCH:{
                    utilityAnimationPack.load();
                    utilityAnimationPack.render(((Torch)e).stateTime,e,batch);
                    break;
                }
                case GameObject.ROCK:{
                    utilityAnimationPack.load();
                    utilityAnimationPack.render(stateTime,e,batch);
                    break;
                }
            }
        }
        //increase state time by delta.
        stateTime+=delta;
    }
}
