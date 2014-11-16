package com.project2k15.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.project2k15.entities.MindlessBlob;
import com.project2k15.entities.MindlessWalker;
import com.project2k15.entities.MovableBox;
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

    float stateTime;

    Texture boxTexture;

    MindlessBlob blob;
    MovableBox box;

    ArrayList<MindlessBlob> blobList;
    ArrayList<MovableBox> boxList;
    MindlessWalker walker;

    BitmapFont bitMapFont;

    boolean spawnMode = true;

    float mapWidth, mapHeight;

    public TestGameScreen(Game game){
        bitMapFont = new BitmapFont();
        this.game = game;
        batch = new SpriteBatch();
        Assets.loadTestMap();
        cam = new OrthographicCamera(30, 30 * (Gdx.graphics.getWidth() / Gdx.graphics.getHeight()));
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = Assets.manager.get("map.tmx");
        mapWidth = Float.parseFloat(map.getProperties().get("width").toString()) * 32;
        mapHeight = Float.parseFloat(map.getProperties().get("height").toString()) * 32;


        System.out.println(mapWidth + " " + mapHeight);
        renderer = new OrthogonalTiledMapRenderer(map, batch);
        renderer.setView(cam);
        player = new Player(124, 250, batch);
        blob = new MindlessBlob(200, 250, 25, 25, 20, player);


        Rectangle[] borderRecs = new Rectangle[4];
        borderRecs[0] = new Rectangle(-10, 0, 10, mapHeight);
        borderRecs[1] = new Rectangle(0, mapHeight, mapWidth, 10);
        borderRecs[2] = new Rectangle(mapWidth, 0, 10, mapHeight);
        borderRecs[3] = new Rectangle(0, -10, mapWidth, 10);

        testProc = new TestInputProcessor(cam, player, mapWidth, mapHeight);
        cam.zoom = 0.5f;
        Gdx.input.setInputProcessor(testProc);
        manager = new ObjectManager(map.getLayers().get("collisionObjects"), (int) mapWidth, (int) mapHeight);

        blobList = new ArrayList<MindlessBlob>();
        manager.addObject(player);

        boxTexture = Assets.manager.get("testBox.png");

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
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(cam);
        batch.setProjectionMatrix(cam.combined);
        renderer.render();
        batch.begin();

        for (int i = 0; i < blobList.size(); i++) {
            batch.draw(blobList.get(i).getCurrentFrame(), blobList.get(i).getPosition().x, blobList.get(i).getPosition().y, blobList.get(i).getWidth(), blobList.get(i).getHeight());
        }
        manager.update(delta);
        manager.getTree().debugDraw(batch);
        bitMapFont.draw(batch, Float.toString(Gdx.graphics.getFramesPerSecond()), player.getPosition().x, player.getPosition().y - 20);
        batch.end();
        stateTime += delta;

        if (Gdx.input.isTouched(0) && stateTime > 0.1) {
            Vector3 unprojected2 = cam.unproject(new Vector3(Gdx.input.getX(0), Gdx.input.getY(0), 0));
            manager.addObject(MindlessWalker.getRandomWalker(unprojected2.x, unprojected2.y, batch));
            stateTime = 0;
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
