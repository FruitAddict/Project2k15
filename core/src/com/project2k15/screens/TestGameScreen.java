package com.project2k15.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.project2k15.entities.MindlessBlob;
import com.project2k15.entities.MindlessWalker;
import com.project2k15.entities.Player;
import com.project2k15.utilities.Assets;
import com.project2k15.utilities.ObjectManager;
import com.project2k15.utilities.TestInputProcessor;

import java.util.ArrayList;

/**
 * Various tests are performed here
 */
public class TestGameScreen implements Screen {
    Game game;
    SpriteBatch batch;
    OrthographicCamera cam;
    TestInputProcessor testProc;
    TiledMap map;
    TiledMapRenderer renderer;
    Player player;
    ObjectManager manager;

    Animation playerAnimation;
    TextureRegion[] walkFrames;
    float stateTime;

    Animation mobAnimation;
    TextureRegion[] mobWalk;

    MindlessBlob blob;

    ArrayList<MindlessBlob> blobList;
    ArrayList<MindlessWalker> walkerList;
    MindlessWalker walker;

    BitmapFont bitMapFont;

    float testTime = 0;

    public TestGameScreen(Game game){
        bitMapFont = new BitmapFont();
        this.game = game;
        batch = new SpriteBatch();
        Assets.loadTestMap();
        cam = new OrthographicCamera(30, 30 * (Gdx.graphics.getWidth() / Gdx.graphics.getHeight()));
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = Assets.manager.get("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, batch);
        renderer.setView(cam);
        player = new Player(124, 250);
        blob = new MindlessBlob(200, 250, 25, 25, 50, player);
        testProc = new TestInputProcessor(cam, player);
        cam.zoom = 0.5f;
        Gdx.input.setInputProcessor(testProc);
        manager = new ObjectManager(map.getLayers().get("collisionObjects"));

        Texture testT = Assets.manager.get("test.gif");
        TextureRegion[][] tmp = TextureRegion.split(testT, testT.getWidth() / 2, testT.getHeight());
        walkFrames = new TextureRegion[2];
        walkFrames[0] = tmp[0][0];
        walkFrames[1] = tmp[0][1];

        playerAnimation = new Animation(0.1f, walkFrames);

        Texture testM = Assets.manager.get("pet.png");
        TextureRegion[][] tmpM = TextureRegion.split(testM, testM.getWidth() / 8, testM.getHeight() / 4);
        mobWalk = new TextureRegion[8];
        for (int i = 0; i < 8; i++) {
            mobWalk[i] = tmpM[0][i];
        }

        mobAnimation = new Animation(0.1f, mobWalk);
        blobList = new ArrayList<MindlessBlob>();
        manager.addObject(player);
        manager.addObject(blob);
        blobList.add(blob);
        walkerList = new ArrayList<MindlessWalker>();

        walker = new MindlessWalker(100, 100, 20, 20, 40, player);
        walkerList.add(walker);
        manager.addObject(walker);
        /**
         Random rng = new Random();
         for(int i=0; i <10;i++){
         MindlessBlob blob = new MindlessBlob(100+10*i,100+10*i,10+rng.nextInt(50),10+rng.nextInt(50),10+rng.nextInt(25),player);
         blobList.add(blob);
         manager.objectList.add(blob);

         }
         blobList.add(blob);
         */

    }
    @Override
    public void render(float delta) {
        testProc.translateCamera();
        cam.update();
        manager.update(delta);
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(cam);
        batch.setProjectionMatrix(cam.combined);
        renderer.render();
        batch.begin();

        for (int i = 0; i < blobList.size(); i++) {
            batch.draw(mobAnimation.getKeyFrame(stateTime, true), blobList.get(i).getPosition().x, blobList.get(i).getPosition().y, blobList.get(i).getWidth(), blobList.get(i).getHeight());
        }
        for (int i = 0; i < walkerList.size(); i++) {
            batch.draw(mobAnimation.getKeyFrame(stateTime, true), walkerList.get(i).getPosition().x, walkerList.get(i).getPosition().y, walkerList.get(i).getWidth(), walkerList.get(i).getHeight());
        }

        batch.draw(playerAnimation.getKeyFrame(stateTime, true), player.getPosition().x, player.getPosition().y, player.getWidth(), player.getHeight());
        bitMapFont.draw(batch, Float.toString(Gdx.graphics.getFramesPerSecond()), player.getPosition().x, player.getPosition().y);
        bitMapFont.draw(batch, Float.toString(blobList.size()), player.getPosition().x, player.getPosition().y - 10);
        batch.end();
        stateTime += delta;

        if (Gdx.input.isTouched()) {
            testTime = 0;
            Vector3 coords = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            MindlessBlob blobs = MindlessBlob.getRandomBlob(coords.x, coords.y);
            manager.addObject(blobs);
            blobList.add(blobs);
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

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
