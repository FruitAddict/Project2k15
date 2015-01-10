package com.fruit.game.visual.animationpacks;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.utilities.Utils;
import com.fruit.game.visual.Assets;

/**
 * @Author FruitAddict
 * Rendering game objects like explosions and other cool stuff will be
 * handled by this class.
 */
public class EffectAnimationPack {
    private Vector2 pos;
    private Animation explosionAnimation;

    public void load(){
        pos = new Vector2();
        TextureAtlas burnedAtlas = (com.badlogic.gdx.graphics.g2d.TextureAtlas)Assets.getAsset("effects//explosion.atlas",TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> atlasRegions = new Array<>();
        atlasRegions = burnedAtlas.findRegions("ffireball_hit");
        explosionAnimation = new Animation(0.5f/9f,atlasRegions);
    }

    public void render(float stateTime, GameObject gameObject, SpriteBatch batch) {
        pos.set(Utils.getDrawPositionBasedOnBox2dCircle(gameObject));
        batch.draw(explosionAnimation.getKeyFrame(stateTime,false),pos.x,pos.y,gameObject.getWidth(),gameObject.getHeight());
    }
}
