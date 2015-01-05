 package com.fruit.game.visual.renderer;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fruit.game.Controller;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.WorldUpdater;
import com.fruit.game.logic.objects.entities.GameObject;
import com.fruit.game.logic.objects.entities.player.Player;
import com.fruit.game.maps.Room;
import com.fruit.game.visual.GameCamera;
import com.fruit.game.visual.messages.TextRenderer;
import com.fruit.game.visual.tween.GameCameraAccessor;
import com.fruit.game.visual.tween.TweenUtils;

 /**
  * World Renderer class. Works together with world updater. While world updater takes care of updating
  * the logic of the game, this class takes the data from the updater and draws the game based on it.
  * Contains a longass method to translate the camera smoothly when rooms are changed (changeRenderedMap),
  * and two render method. First one (render) renders the game normally, using object renderer class to render all
  * the game objects, tiled map renderer to render maps and light renderer to render lights.
  * Second one (transitionRender) renders the map and all the objects except for player (used
  * with changeRenderedMap method)
  */
 public class WorldRenderer implements Constants {
     private OrthogonalTiledMapRenderer tiledMapRenderer;
     private SpriteBatch batch;
     private GameCamera camera;
     //world updater reference to fetch list of objects to pass to Object Renderer.
     private WorldUpdater worldUpdater;
     //Object renderer that handles rendering game objects (e.g. player, projectiles, collectibles etc).
     private ObjectRenderer objectRenderer;
     //text renderer to display on-screen messages like damage done to monsters
     private TextRenderer textRenderer;
     //Light renderer to handle all the lighting effects. Can be easily disabled for performance
     private LightRenderer lightRenderer;
     //Splatter renderer that will render all the blood & gore & other delicious stuff on the ground
     private SplatterRenderer splatterRenderer;
     //texture region for map transition effects and its position, frame buffer for screen capturing and
     //additional game object array to filter out player during transition rendering phase
     private TextureRegion lastMapTexture;
     private float lastMapTextureX, lastMaptextureY;
     private FrameBuffer frameBuffer;
     private Array<GameObject> temporaryObjectArray;
     //when this boolean is true, some of the rendering functionality will halt.
     private boolean paused = false;


     public WorldRenderer(SpriteBatch batch, GameCamera camera, WorldUpdater worldUpdater) {
         Controller.registerWorldRenderer(this);
         this.batch = batch;
         this.camera = camera;
         this.worldUpdater = worldUpdater;
         tiledMapRenderer = new OrthogonalTiledMapRenderer(worldUpdater.getMapManager().getCurrentMap().getCurrentRoom().getTiledMap(), batch);
         objectRenderer = new ObjectRenderer();
         textRenderer = new TextRenderer();
         lightRenderer = new LightRenderer(this, worldUpdater.getWorld());
         lightRenderer.setPlayerLight(worldUpdater.getPlayer());
         splatterRenderer = new SplatterRenderer(batch);
         //initially add all the lights from the room if those exist
         for (Room.StaticLightContainer container : worldUpdater.getMapManager().getCurrentMap().getCurrentRoom().getStaticLightPositions()) {
             lightRenderer.addPointLight(container.color, container.length, container.position.x, container.position.y, true);
         }
         temporaryObjectArray = new Array<GameObject>();
     }

     public void render(float delta) {
         //update camera
         camera.update();
         //update tween manager
         TweenUtils.tweenManager.update(delta);
         //set projection matrix of map renderer to camera
         tiledMapRenderer.setView(camera);
         //set batch projection matrix to camera
         batch.setProjectionMatrix(camera.combined);
         //begin rendering to batch
         batch.begin();
         tiledMapRenderer.renderTileLayer((TiledMapTileLayer) tiledMapRenderer.getMap().getLayers().get(0));
         //render splatter effects
         splatterRenderer.render();
         //render walls
         tiledMapRenderer.renderTileLayer((TiledMapTileLayer) tiledMapRenderer.getMap().getLayers().get(1));
         //render all objects
         if (lastMapTexture != null) {
             batch.draw(lastMapTexture, lastMapTextureX, lastMaptextureY);
         }
         objectRenderer.render(delta, worldUpdater.getObjectManager().getGameObjects(), batch);
         batch.end();
         lightRenderer.render();
         if (!paused) {
             textRenderer.render(batch, delta);
         }
         camera.updateCameraMovement(delta);
         //update splatter effects ( must've been done after finishing the batch as this uses fbo and cannot be nested in
         //another batch)
         splatterRenderer.update(delta);
     }

     /**
      * Transition render method used when map is changed.
      * There is no need to render lights or text here compared to the render method.
      * All battle text is removed anyway and the main render method will take care of
      * setting up correct ambient light as soon as this method is over. (if lightmap was rendered here,
      * the created texture would be darkened by the normal renderer)
      */
     public void transitionRender() {
         camera.update();
         tiledMapRenderer.setView(camera);
         batch.setProjectionMatrix(camera.combined);
         Gdx.gl.glClearColor(0, 0, 0, 1);
         Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
         //render map
         tiledMapRenderer.render();
         //render objects except for player using temporary object array
         batch.begin();
         temporaryObjectArray.clear();
         for (GameObject o : worldUpdater.getObjectManager().getGameObjects()) {
             if (o.getEntityID() != GameObject
                     .PLAYER) {
                 temporaryObjectArray.add(o);
             }
         }
         temporaryObjectArray.removeValue(worldUpdater.getPlayer(), true);
         objectRenderer.render(0, temporaryObjectArray, batch);
         temporaryObjectArray.clear();
         batch.end();
     }

     /**
      *
      * @param prevPortalPos
      * Position of the portal (Center, vector2 ) that player came from.
      * @param nextPortalPos
      * Position of the portal the player will come out from
      * @param direction
      * Direction specified in Constants interface, helps with determining
      * how camera should be moved for the smooth transition effect.
      * @param doTransition
      * Specifies whether smooth transition should be done, should be false when player
      * changes maps not rooms.
      */
     public void changeRenderedMap(Vector2 prevPortalPos, Vector2 nextPortalPos, int direction, boolean doTransition) {
         //First, kill all tweening (dmg scrolling text etc)
         TweenUtils.tweenManager.killAll();
         //remove all the text from text renderer
         textRenderer.removeAll();
         if (doTransition) {
             //drawing
             //helper variables
             float spawnX = nextPortalPos.x;
             float spawnY = nextPortalPos.y;
             float mapWidth = Controller.getWorldUpdater().getMapManager().getCurrentMapWidth();
             float mapHeight = Controller.getWorldUpdater().getMapManager().getCurrentMapHeight();
             float viewPortWidth = camera.viewportWidth;
             float viewPortHeight = camera.viewportHeight;
             //set the camera free, so it doesn't follow the player ( or other objects ).
             camera.setFreeCamera(true);
             //Renders the map into the frame buffer, centered on the doors in the previous map
             frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, (int) (camera.viewportWidth * camera.zoom), (int) (camera.viewportHeight * camera.zoom), false);
             frameBuffer.begin();
             switch (direction) {
                 case NORTH_DIR: {
                     //if player is spawning on the north side of the map, it means that he came from the south
                     //therefore we set the camera position to the south on the portal position on x axis
                     //and half the viewport length on the y axis.
                     camera.position.set(prevPortalPos.x, viewPortHeight / 2 * camera.zoom, 0);
                     break;
                 }
                 case SOUTH_DIR: {
                     camera.position.set(prevPortalPos.x, mapHeight - viewPortHeight / 2 * camera.zoom, 0);
                     break;
                 }
                 case WEST_DIR: {
                     camera.position.set(mapWidth - viewPortWidth / 2 * camera.zoom, prevPortalPos.y, 0);
                     break;
                 }
                 case EAST_DIR: {
                     camera.position.set(viewPortWidth / 2 * camera.zoom, prevPortalPos.y, 0);
                     break;
                 }
             }
             //render method is called, this one is designed specifically for map transition as it renders only terrain
             //and nothing else
             transitionRender();
             frameBuffer.end();

             //create a texture from this frame buffer
             Texture temporary = frameBuffer.getColorBufferTexture();

             //set the lastMapTexture Region to this new texture, so the game can render it
             lastMapTexture = new TextureRegion(temporary);
             //flip it on the y axis as it comes out inverted from the frame buffer.
             lastMapTexture.flip(false, true);

             //perform the tween camera transition based on the direction the player will spawn in
             switch (direction) {
                 case NORTH_DIR: {
                     //for example, if player will spawn in the north of the new map,
                     //we set the position at which the texture from the frame buffer will be rendered
                     //to spawn point's position  - half of the viewport on the x axis(so it will cover the whole viewport)
                     //and map height on the y axis.
                     lastMapTextureX = spawnX - viewPortWidth / 2 * camera.zoom;
                     lastMaptextureY = mapHeight;
                     camera.position.set(spawnX, mapHeight + viewPortHeight / 2 * camera.zoom, 0);
                     Tween.to(camera, GameCameraAccessor.positionXY, 0.5f).target(spawnX, mapHeight - viewPortHeight / 2 * camera.zoom)
                             .setCallbackTriggers(TweenCallback.END).setCallback(camera.followObjectCallback)
                             .start(TweenUtils.tweenManager);
                     break;
                 }
                 case SOUTH_DIR: {
                     lastMapTextureX = spawnX - viewPortWidth / 2 * camera.zoom;
                     lastMaptextureY = -lastMapTexture.getRegionHeight();
                     camera.position.set(spawnX, -viewPortHeight / 2 * camera.zoom, 0);
                     Tween.to(camera, GameCameraAccessor.positionXY, 0.5f).target(spawnX, viewPortHeight / 2 * camera.zoom)
                             .setCallbackTriggers(TweenCallback.END).setCallback(camera.followObjectCallback)
                             .start(TweenUtils.tweenManager);
                     break;
                 }
                 case EAST_DIR: {
                     lastMapTextureX = mapWidth;
                     lastMaptextureY = spawnY - viewPortHeight / 2 * camera.zoom;
                     camera.position.set(mapWidth + viewPortWidth / 2 * camera.zoom, spawnY, 0);
                     Tween.to(camera, GameCameraAccessor.positionXY, 0.5f).target(mapWidth - viewPortWidth / 2 * camera.zoom, spawnY)
                             .setCallbackTriggers(TweenCallback.END).setCallback(camera.followObjectCallback)
                             .start(TweenUtils.tweenManager);
                     break;
                 }
                 case WEST_DIR: {
                     lastMapTextureX = -lastMapTexture.getRegionWidth();
                     lastMaptextureY = spawnY - viewPortHeight / 2 * camera.zoom;
                     camera.position.set(-viewPortWidth / 2 * camera.zoom, spawnY, 0);
                     Tween.to(camera, GameCameraAccessor.positionXY, 0.5f).target(viewPortWidth / 2 * camera.zoom, spawnY)
                             .setCallbackTriggers(TweenCallback.END).setCallback(camera.followObjectCallback)
                             .start(TweenUtils.tweenManager);
                     break;
                 }
             }
         }
         //no matter if transition is requested or not, create a new tiled map renderer based on the new map
         tiledMapRenderer = null;
         tiledMapRenderer = new OrthogonalTiledMapRenderer(worldUpdater.getMapManager().getCurrentMap().getCurrentRoom().getTiledMap(), batch);
         //free all the lights
         lightRenderer.freeAllLights();
         for (Room.StaticLightContainer container : worldUpdater.getMapManager().getCurrentMap().getCurrentRoom().getStaticLightPositions()) {
             lightRenderer.addPointLight(container.color, container.length, container.position.x, container.position.y, true);
         }
         //remove all splatters from the splatter renderer
         //update splatter renderer fbo and camera.
         splatterRenderer.updateFrameBufferAndCamera();
         //update minimap
         Controller.getUserInterface().updateMinimap();
         Controller.getUserInterface().removeBossBar();
     }

     /**
      * Called when the rendering is paused, sets the paused boolean to true ( so some
      * of the rendering functionality is disabled ).
      * Batch alpha change, can be used make the screen darker when the game is paused and some
      * menus are displayed for example.
      */
     public void pauseRendering(){
         paused = true;
         camera.pauseTransition();
         batch.setColor(Color.DARK_GRAY);
     }

     public void unpauseRendering(){
         //analogically to the pauseRendering method, set batch colors to its normal values.
         paused = false;
         camera.unpauseTransition();
         batch.setColor(1,1,1,1);
     }

     public TextRenderer getTextRenderer() {
         return textRenderer;
     }

     public WorldUpdater getWorldUpdater() {
         return worldUpdater;
     }

     public LightRenderer getLightRenderer() {
         return lightRenderer;
     }

     public SplatterRenderer getSplatterRenderer() {
         return splatterRenderer;
     }

     public GameCamera getCamera() {
         return camera;
     }

     public ObjectRenderer getObjectRenderer(){
         return objectRenderer;
     }
 }
