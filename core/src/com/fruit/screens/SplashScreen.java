package com.fruit.screens;

import android.graphics.Color;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
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
public class SplashScreen implements Screen,TweenAccessor<SplashScreen> {

    private MainGame game;
    private Sprite sprite;
    private SpriteBatch batch;
    private float timePassed;
    private float disappearThreshold;
    private BitmapFont font;

    public SplashScreen(MainGame game){
        //loads splash screen texture
        Assets.loadSplashScreen();
        //loads test map TODO change it to load only gui-releated stuff
        this.game = game;
        //register the tween accessor for sprites
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Texture splashImage = (Texture)Assets.getAsset("playerhead.png", Texture.class);
        sprite = new Sprite(splashImage);
        sprite.setPosition(Gdx.graphics.getWidth()/2-sprite.getWidth()/2,500-sprite.getHeight()/2);
        batch = new SpriteBatch();
        //setting up tweens
        timePassed = 0f;
        disappearThreshold = 0.5f;
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
        if (Assets.manager.update() && timePassed > 3) {
            this.dispose();
            game.setScreen(new GameScreen(game));
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
        Tween.to(sprite,SpriteAccessor.ALPHA,3).target(1).start(TweenUtils.tweenManager);
        Tween.to(sprite,SpriteAccessor.POSITION_Y,3).target(400).start(TweenUtils.tweenManager);
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

    @Override
    public int getValues(SplashScreen splashScreen, int i, float[] floats) {
        return 0;
    }

    @Override
    public void setValues(SplashScreen splashScreen, int i, float[] floats) {

    }
}
