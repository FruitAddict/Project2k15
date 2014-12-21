package com.fruit.visual.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fruit.Controller;
import com.fruit.logic.Constants;
import com.fruit.utilities.Utils;
import com.fruit.visual.Assets;

/**
 * @Author FruitAddict
 * Responsible for rendering blood on the ground, gore and other
 * disgusting shit.
 */
public class SplatterRenderer implements Constants {
    //splatter types
    public static final int BLOOD_1 = 1;
    public static final int BLOOD_2 = 2;

    //Array holding current splatters
    private Array<Splatter> splatterArray;

    private Sprite blood1Sprite;
    private Sprite blood2Sprite;

    private TextureRegion combinedTextureRegion;

    //fbo rendering technique to combine all the splatters into one texture when something is added.
    private FrameBuffer frameBuffer;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    public SplatterRenderer(SpriteBatch batch){
        splatterArray = new Array<Splatter>();
        camera = new OrthographicCamera();
        combinedTextureRegion = new TextureRegion();
        this.batch = batch;
        //init sprites
        Texture blood1Texture = (Texture)Assets.getAsset("bloodsplatter1.png",Texture.class);
        Texture blood2Texture = (Texture)Assets.getAsset("bloodsplatter2.png",Texture.class);

        blood1Sprite = new Sprite(blood1Texture);
        blood2Sprite = new Sprite(blood2Texture);

        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888,(int)Controller.getWorldUpdater().getMapManager().getCurrentMapWidth(),
                (int)Controller.getWorldUpdater().getMapManager().getCurrentMapHeight(),false);
        renderToFBO();
    }

    public void render(){
        if(combinedTextureRegion !=null){
            batch.draw(combinedTextureRegion,0,0);
        }
    }

    public void renderToFBO(){
        System.out.println("rendering to fbo..");
        camera.setToOrtho(false,Controller.getWorldUpdater().getMapManager().getCurrentMapWidth() ,
                Controller.getWorldUpdater().getMapManager().getCurrentMapHeight());
        frameBuffer.begin();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Splatter splatter : splatterArray){
            switch(splatter.type){
                case SplatterRenderer.BLOOD_1:{
                    blood1Sprite.setPosition(splatter.position.x*PIXELS_TO_METERS,splatter.position.y*PIXELS_TO_METERS);
                    blood1Sprite.setRotation(splatter.rotation);
                    blood1Sprite.draw(batch);
                    break;
                }
                case SplatterRenderer.BLOOD_2:{
                    blood2Sprite.setPosition(splatter.position.x*PIXELS_TO_METERS,splatter.position.y*PIXELS_TO_METERS);
                    blood2Sprite.setRotation(splatter.rotation);
                    blood2Sprite.draw(batch);
                    break;
                }
            }
        }
        batch.end();
        frameBuffer.end();
        Texture combinedTextureTemporary = frameBuffer.getColorBufferTexture();
        combinedTextureRegion.setRegion(combinedTextureTemporary);
        combinedTextureRegion.flip(false,true);
        splatterArray.clear();
    }

    public void addSplatter(Vector2 position, int type){
        //2 overloaded methods to add new splatters
        splatterArray.add(new Splatter(position,type,0));
        renderToFBO();
    }

    public void addSplatter(Vector2 position, int type, float rotationDegrees){
        splatterArray.add(new Splatter(position,type,rotationDegrees));
        renderToFBO();
    }

    public void addMultiSplatter(Vector2 position, int numberOfSplatters, int range) {
        //TODO make it work properly
        for(int i =0;i<numberOfSplatters;i++) {
            if(range>0) {
                splatterArray.add(new Splatter(position.add(Utils.getRandomFromRange(-range, range), Utils.getRandomFromRange(-range, range)),
                        1 + Utils.randomGenerator.nextInt(2), Utils.randomGenerator.nextInt(360)));
            } else {
                splatterArray.add(new Splatter(position,1 + Utils.randomGenerator.nextInt(2), Utils.randomGenerator.nextInt(360)));
            }
        }
        renderToFBO();
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
