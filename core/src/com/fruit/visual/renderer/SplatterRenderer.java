package com.fruit.visual.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fruit.Controller;
import com.fruit.logic.Constants;
import com.fruit.utilities.Utils;

/**
 * @Author FruitAddict
 * Responsible for rendering blood on the ground, gore and other
 * disgusting shit.
 */
public class SplatterRenderer implements Constants {
    //splatter types
    public static final int BLOOD_1 = 1;
    public static final int BLOOD_2 = 2;
    public static final int BLOOD_3 = 3;
    public static final int BLOOD_4 = 4;
    public static final int BLOOD_5 = 5;

    //Array holding current splatters
    private Array<Splatter> splatterArray;

    private Sprite blood1Sprite;
    private Sprite blood2Sprite;
    private Sprite blood3Sprite;
    private Sprite blood4Sprite;
    private Sprite blood5Sprite;

    private TextureRegion combinedTextureRegion;

    //fbo rendering technique to combine all the splatters into one texture when something is added.
    private FrameBuffer frameBuffer;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    //float holding the time between splatter updates
    private float lastSplatterUpdate;
    //interval between splatter updates (so they are batched in bulk, not one by one)
    private float splatterInverval = 0.5f;

    public SplatterRenderer(SpriteBatch batch){
        splatterArray = new Array<Splatter>();
        camera = new OrthographicCamera();
        combinedTextureRegion = new TextureRegion();
        this.batch = batch;
        //init sprites
        TextureAtlas splatterAtlas = new TextureAtlas(Gdx.files.internal("splatters//Splatters.pack"));

        blood1Sprite = splatterAtlas.createSprite("blood1");
        blood2Sprite = splatterAtlas.createSprite("blood2");
        blood3Sprite = splatterAtlas.createSprite("blood3");
        blood4Sprite = splatterAtlas.createSprite("blood4");
        blood5Sprite = splatterAtlas.createSprite("blood5");

        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888,(int)Controller.getWorldUpdater().getMapManager().getCurrentMapWidth(),
                (int)Controller.getWorldUpdater().getMapManager().getCurrentMapHeight(),false);
        renderToFBO();
    }

    public void render(){
        if (combinedTextureRegion != null) {
            batch.draw(combinedTextureRegion, 0, 0);
        }
    }


    public void update(float delta){
        lastSplatterUpdate+=delta;
        if(lastSplatterUpdate>splatterInverval){
            renderToFBO();
            lastSplatterUpdate = 0;
        }
    }

    public void renderToFBO(){
        if(splatterArray.size >0) {
            System.out.println("rendering to fbo..");
            camera.setToOrtho(false, Controller.getWorldUpdater().getMapManager().getCurrentMapWidth(),
                    Controller.getWorldUpdater().getMapManager().getCurrentMapHeight());
            frameBuffer.begin();
            camera.update();
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            for (Splatter splatter : splatterArray) {
                switch (splatter.type) {
                    case SplatterRenderer.BLOOD_1: {
                        blood1Sprite.setPosition(splatter.position.x * PIXELS_TO_UNITS, splatter.position.y * PIXELS_TO_UNITS);
                        blood1Sprite.setRotation(splatter.rotation);
                        blood1Sprite.draw(batch);
                        break;
                    }
                    case SplatterRenderer.BLOOD_2: {
                        blood2Sprite.setPosition(splatter.position.x * PIXELS_TO_UNITS, splatter.position.y * PIXELS_TO_UNITS);
                        blood2Sprite.setRotation(splatter.rotation);
                        blood2Sprite.draw(batch);
                        break;
                    }
                    case SplatterRenderer.BLOOD_3: {
                        blood3Sprite.setPosition(splatter.position.x * PIXELS_TO_UNITS, splatter.position.y * PIXELS_TO_UNITS);
                        blood3Sprite.setRotation(splatter.rotation);
                        blood3Sprite.draw(batch);
                        break;
                    }
                    case SplatterRenderer.BLOOD_4: {
                        blood4Sprite.setPosition(splatter.position.x * PIXELS_TO_UNITS, splatter.position.y * PIXELS_TO_UNITS);
                        blood4Sprite.setRotation(splatter.rotation);
                        blood4Sprite.draw(batch);
                        break;
                    }
                    case SplatterRenderer.BLOOD_5: {
                        blood5Sprite.setPosition(splatter.position.x * PIXELS_TO_UNITS, splatter.position.y * PIXELS_TO_UNITS);
                        blood5Sprite.setRotation(splatter.rotation);
                        blood5Sprite.draw(batch);
                        break;
                    }
                }
            }
            batch.end();
            frameBuffer.end();
            Texture combinedTextureTemporary = frameBuffer.getColorBufferTexture();
            combinedTextureRegion.setRegion(combinedTextureTemporary);
            combinedTextureRegion.flip(false, true);
            splatterArray.clear();
        }else {
            frameBuffer.begin();
            frameBuffer.end();
            Texture combinedTextureTemporary = frameBuffer.getColorBufferTexture();
            combinedTextureRegion.setRegion(combinedTextureTemporary);
            combinedTextureRegion.flip(false, true);
        }
    }

    public void addSplatter(Vector2 position, int type){
        //2 overloaded methods to add new splatters
        splatterArray.add(new Splatter(position,type,0));
    }

    public void addSplatter(Vector2 position, int type, float rotationDegrees){
        splatterArray.add(new Splatter(position,type,rotationDegrees));
    }

    public void addMultiSplatter(Vector2 position, int numberOfSplatters, int range) {
        for (int i = 0; i < numberOfSplatters; i++) {
            if (range > 0) {
                float randomSign = Math.signum(Utils.randomGenerator.nextInt());
                splatterArray.add(new Splatter(position.cpy().add(Utils.randomGenerator.nextFloat()*randomSign*range, Utils.randomGenerator.nextFloat()*randomSign*range),
                        1 + Utils.randomGenerator.nextInt(5), Utils.randomGenerator.nextInt(360)));
            } else {
                splatterArray.add(new Splatter(position,1 + Utils.randomGenerator.nextInt(5), Utils.randomGenerator.nextInt(360)));
            }
        }
    }

    public void updateFrameBufferAndCamera(){
        frameBuffer.dispose();
        combinedTextureRegion = null;
        combinedTextureRegion = new TextureRegion();
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888,(int)Controller.getWorldUpdater().getMapManager().getCurrentMapWidth(),
                (int)Controller.getWorldUpdater().getMapManager().getCurrentMapHeight(),false);
        renderToFBO();
        camera.setToOrtho(false,Controller.getWorldUpdater().getMapManager().getCurrentMapWidth() ,
                Controller.getWorldUpdater().getMapManager().getCurrentMapHeight());
    }

    private class Splatter{
        private Vector2 position;
        private int type;
        private float rotation;

        public Splatter(Vector2 position, int type, float rotation){
            this.position = position.cpy();
            this.type = type;
            this.rotation = rotation;
        }
    }
}
