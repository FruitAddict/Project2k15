package com.fruit.screens;

import android.graphics.Color;
import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fruit.MainGame;
import com.fruit.visual.Assets;
import com.fruit.visual.tween.SpriteAccessor;
import com.fruit.visual.tween.TweenUtils;

/**
 * Splash screen, appears for 5 seconds when the program is started.
 */
public class SplashScreen implements Screen {

    private MainGame game;
    private Sprite sprite;
    private SpriteBatch batch;
    private float timePassed;
    private BitmapFont font;

    public SplashScreen(MainGame game){
        this.game = game;
        //register the tween accessor for sprites
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Texture splashImage = (Texture)Assets.getAsset("playerhead.png", Texture.class);
        sprite = new Sprite(splashImage);
        sprite.setPosition(0,Gdx.graphics.getHeight()/2-sprite.getHeight()/2);
        batch = new SpriteBatch();
        timePassed = 0f;
        font = new BitmapFont();
        font.scale(2);
        font.setColor(Color.RED);
    }

    @Override
    public void render(float delta) {
        //clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timePassed+=delta;
        //update tween manager
        TweenUtils.tweenManager.update(delta);
        if (Assets.manager.update() && timePassed > 1) {
            this.dispose();
            game.setScreen(new MainMenuScreen(game));
        } else {
            batch.begin();
            sprite.draw(batch);
            batch.end();
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        Tween.set(sprite,SpriteAccessor.ALPHA).target(0).start(TweenUtils.tweenManager);
        Tween.to(sprite,SpriteAccessor.ALPHA,1).target(1).start(TweenUtils.tweenManager);
        Tween.to(sprite,SpriteAccessor.POSITION_X,1).target(Gdx.graphics.getWidth()).start(TweenUtils.tweenManager);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

}
