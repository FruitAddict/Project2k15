package com.fruit.visual;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.fruit.Controller;
import com.fruit.logic.Constants;
import com.fruit.logic.WorldUpdater;
import com.fruit.visual.messagess.TextRenderer;
import com.fruit.visual.tween.GameCameraAccessor;
import com.fruit.visual.tween.TweenUtils;

import java.nio.ByteBuffer;

public class WorldRenderer implements Constants {
    //TiledMap renderer.
    private TiledMapRenderer tiledMapRenderer;
    private SpriteBatch batch;
    private GameCamera camera;
    //world updater reference to fetch list of objects to pass to Object Renderer.
    private WorldUpdater worldUpdater;
    //Object renderer that handles rendering game objects (e.g. player, projectiles, collectibles etc).
    private ObjectRenderer objectRenderer;
    //text renderer to display on-screen messages like damage done to monsters
    private TextRenderer textRenderer;
    //texture region for map transition effects and its position
    private TextureRegion lastMapTexture;
    private float lastMapTextureX, lastMaptextureY;
    private FrameBuffer frameBuffer;


    public WorldRenderer(SpriteBatch batch, GameCamera camera, WorldUpdater worldUpdater){
        tiledMapRenderer = new OrthogonalTiledMapRenderer(worldUpdater.getMapManager().getCurrentMap().getCurrentRoom().getTiledMap(), batch);
        objectRenderer = new ObjectRenderer();
        textRenderer = new TextRenderer();
        this.batch = batch;
        this.camera = camera;
        this.worldUpdater = worldUpdater;
    }

    public void changeRenderedMap(Vector2 spawnPoint, int direction, boolean doTransition){
        System.out.println("map renderer change map function called");
        if(doTransition) {
            camera.setFreeCamera(true);
            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, (int)(camera.viewportWidth*camera.zoom),(int)(camera.viewportHeight*camera.zoom),false);
            frameBuffer.begin();
            render(0);
            frameBuffer.end();
            Texture temporary = frameBuffer.getColorBufferTexture();
            lastMapTexture = new TextureRegion(temporary);
            lastMapTexture.flip(false, true);
            float spawnX = spawnPoint.x;
            float spawnY = spawnPoint.y;
            float mapWidth = Controller.getWorldUpdater().getMapManager().getCurrentMapWidth();
            float mapHeight = Controller.getWorldUpdater().getMapManager().getCurrentMapHeight();
            float viewPortWidth = camera.viewportWidth;
            float viewPortHeight = camera.viewportHeight;
            float playerWidth = worldUpdater.getPlayer().getWidth();
            float playerHeight = worldUpdater.getPlayer().getHeight();
            switch(direction){
                case NORTH_DIR: {
                    lastMapTextureX = spawnX - viewPortWidth/2*camera.zoom;
                    lastMaptextureY = mapHeight;
                    camera.position.set(spawnX,mapHeight+viewPortHeight/2*camera.zoom,0);
                    Tween.to(camera,GameCameraAccessor.positionXY,0.5f).target(spawnX,mapHeight-viewPortHeight/2*camera.zoom)
                            .setCallbackTriggers(TweenCallback.END).setCallback(camera.followObjectCallback)
                            .start(TweenUtils.tweenManager);
                    break;
                }
                case SOUTH_DIR: {
                    lastMapTextureX = spawnX - viewPortWidth/2*camera.zoom;
                    lastMaptextureY = -lastMapTexture.getRegionHeight();
                    camera.position.set(spawnX,-viewPortHeight/2*camera.zoom,0);
                    Tween.to(camera,GameCameraAccessor.positionXY,0.5f).target(spawnX,viewPortHeight/2*camera.zoom)
                            .setCallbackTriggers(TweenCallback.END).setCallback(camera.followObjectCallback)
                            .start(TweenUtils.tweenManager);
                    break;
                }
                case EAST_DIR: {
                    lastMapTextureX = mapWidth;
                    lastMaptextureY = spawnY - viewPortHeight/2*camera.zoom;
                    camera.position.set(mapWidth+viewPortWidth/2*camera.zoom,spawnY,0);
                    Tween.to(camera,GameCameraAccessor.positionXY,0.5f).target(mapWidth-viewPortWidth/2*camera.zoom,spawnY)
                            .setCallbackTriggers(TweenCallback.END).setCallback(camera.followObjectCallback)
                            .start(TweenUtils.tweenManager);
                    break;
                }
                case WEST_DIR: {
                    lastMapTextureX = -lastMapTexture.getRegionWidth();
                    lastMaptextureY = spawnY - viewPortHeight/2*camera.zoom;
                    camera.position.set(-viewPortWidth/2*camera.zoom,spawnY,0);
                    Tween.to(camera,GameCameraAccessor.positionXY,0.5f).target(viewPortWidth/2*camera.zoom,spawnY)
                            .setCallbackTriggers(TweenCallback.END).setCallback(camera.followObjectCallback)
                            .start(TweenUtils.tweenManager);
                    break;
                }
            }
        }
        tiledMapRenderer= null;
        tiledMapRenderer = new OrthogonalTiledMapRenderer(worldUpdater.getMapManager().getCurrentMap().getCurrentRoom().getTiledMap(),batch);
    }
    public void render(float delta){
        //update camera
        camera.update();
        //update tween manager
        TweenUtils.tweenManager.update(delta);
        //set projection matrix of map renderer to camera
        tiledMapRenderer.setView(camera);
        //set batch projection matrix to camera
        batch.setProjectionMatrix(camera.combined);
        //clear screen
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //render map
        tiledMapRenderer.render();
        batch.begin();
        //render all objects
        if(lastMapTexture!=null){
            batch.draw(lastMapTexture,lastMapTextureX,lastMaptextureY);
        }
        objectRenderer.render(delta, worldUpdater.getObjectManager().getGameObjects(), batch);
        textRenderer.render(batch,delta);
        batch.end();
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }
}
