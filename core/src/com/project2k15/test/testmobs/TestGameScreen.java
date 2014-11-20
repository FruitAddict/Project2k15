package com.project2k15.test.testmobs;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.project2k15.logic.ObjectManager;
import com.project2k15.logic.WorldInputProcessor;
import com.project2k15.logic.entities.Player;
import com.project2k15.rendering.Assets;

/**
 * Various tests are performed here
 */
public class TestGameScreen implements Screen {

    //GAME STUFF
    Game game;
    SpriteBatch batch;
    OrthographicCamera cam;
    WorldInputProcessor proc;
    TiledMap map;
    TiledMapRenderer renderer;
    Player player;
    ObjectManager manager;

    float stateTime;

    MindlessWalker walker;

    BitmapFont bitMapFont;

    boolean spawnMode = true;

    float mapWidth, mapHeight;

    TestInputProcessor proc2;

    //GUI STUFF
    Stage stage;
    InputMultiplexer inputMultiplexer;
    Skin skin;
    Label firstLabel;
    Label secondLabel;

    public TestGameScreen(Game game){
        /**
         * DECLARING GAME STUFF
         */
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

        cam.zoom = 0.5f;
        manager = new ObjectManager(map.getLayers().get("collisionObjects"), (int) mapWidth, (int) mapHeight);
        player = new Player(124, 250, batch, manager);
        manager.setPlayer(player);
        manager.addObject(player);
        manager.addObject(new WalkerSpawner(200, 200, manager, batch));
        manager.addObject(new WalkerSpawner(400, 190, manager, batch));

        /**
         * DECLARING GUI STUFF AND INPUT
         */
        stage = new Stage();
        skin = new Skin();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        BitmapFont font = new BitmapFont();
        font.scale(0.5f);
        BitmapFont font2 = new BitmapFont();
        skin.add("default", font);
        skin.add("font2", font2);
        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.RED);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");

        TextButton.TextButtonStyle textButtonStyle1 = new TextButton.TextButtonStyle();
        textButtonStyle1.checked = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle1.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle1.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle1.over = skin.newDrawable("white", Color.RED);
        textButtonStyle1.font = skin.getFont("default");

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("font2");
        skin.add("default", labelStyle);
        skin.add("nofancy", textButtonStyle1);
        skin.add("default", textButtonStyle);
        // Create a table that fills the screen. Everything else will go inside this table.
        VerticalGroup table = new VerticalGroup();
        table.setFillParent(true);
        stage.addActor(table);
        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        final TextButton button = new TextButton("Clear Moving Objects", textButtonStyle1);
        final TextButton button2 = new TextButton("Piercing projectiles", skin);
        final TextButton button3 = new TextButton("Spawn boss", textButtonStyle1);

        firstLabel = new Label("", skin);
        secondLabel = new Label("", skin);
        table.addActor(button);
        table.addActor(button2);
        table.addActor(button3);
        table.addActor(firstLabel);
        table.addActor(secondLabel);
        table.align(Align.topRight);
        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                manager.clear();
            }
        });
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.piercingShotsDebug = !player.piercingShotsDebug;
            }
        });
        button3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MindlessWalker walker = MindlessWalker.getRandomWalker(player.getPosition().x + 50, player.getPosition().y, batch, manager);
                walker.setHealthPoints(1000);
                walker.setSize(100, 100);
                manager.addObject(walker);
            }
        });
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            proc = new WorldInputProcessor(cam, player, mapWidth, mapHeight);
            inputMultiplexer.addProcessor(proc);
        } else {
            proc2 = new TestInputProcessor(cam, player, mapWidth, mapHeight);
            inputMultiplexer.addProcessor(proc2);
        }
        Gdx.input.setInputProcessor(inputMultiplexer);

    }
    @Override
    public void render(float delta) {
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            WorldInputProcessor in = (WorldInputProcessor) (inputMultiplexer.getProcessors().get(1));
            in.update();
        } else {
            TestInputProcessor in = (TestInputProcessor) (inputMultiplexer.getProcessors().get(1));
            in.translateCamera();
        }
        cam.update();
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(cam);
        batch.setProjectionMatrix(cam.combined);
        renderer.render();
        batch.begin();
        manager.update(delta);
        batch.end();
        stateTime += delta;

        firstLabel.setText(Float.toString(Gdx.graphics.getFramesPerSecond()) + " FPS");
        secondLabel.setText(manager.getNumberOfObjects() + " objects");
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
