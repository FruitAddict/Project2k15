package com.fruit.visual;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.fruit.logic.Constants;
import com.fruit.logic.WorldUpdater;
import com.fruit.visual.messagess.TextRenderer;
import com.fruit.visual.tween.TweenUtils;

public class WorldRenderer implements Constants {
    //TiledMap renderer.
    private TiledMapRenderer tiledMapRenderer;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    //world updater reference to fetch list of objects to pass to Object Renderer.
    private WorldUpdater worldUpdater;
    //Object renderer that handles rendering game objects (e.g. player, projectiles, collectibles etc).
    private ObjectRenderer objectRenderer;

    //text renderer to display on-screen messages like damage done to monsters
    private TextRenderer textRenderer;

    public WorldRenderer(SpriteBatch batch, OrthographicCamera camera, WorldUpdater worldUpdater){
        tiledMapRenderer = new OrthogonalTiledMapRenderer(worldUpdater.getMapManager().getCurrentMap().getCurrentRoom().getTiledMap(), batch);
        objectRenderer = new ObjectRenderer();
        textRenderer = new TextRenderer();
        this.batch = batch;
        this.camera = camera;
        this.worldUpdater = worldUpdater;
    }

    public void changeRenderedMap(){
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
        objectRenderer.render(delta, worldUpdater.getObjectManager().getGameObjects(), batch);
        textRenderer.render(batch,delta);
        batch.end();
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }
}
