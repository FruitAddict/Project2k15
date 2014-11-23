package com.project2k15.rendering.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.project2k15.logic.managers.MapManager;
import com.project2k15.logic.managers.ObjectManager;
import com.project2k15.logic.entities.Player;
import com.project2k15.rendering.WorldRenderer;
import com.project2k15.test.testmobs.WalkerSpawner;

/**
 * Gui stage, concentrates everything gui releated to free up space in the main game class
 */
public class GuiStage extends Stage {
    /**
     * Holds objectmanager, player, batch and camera and mapmanager references to operate on
     */
    ObjectManager objectManager;
    OrthographicCamera gameCamera;
    Player player;
    MapManager mapManager;
    SpriteBatch batch;

    /**
     * Hardcoded skin as a placeholder
     */
    Skin skin;

    /**
     * Debug labels
     */
    Label firstLabel;
    Label secondLabel;
    Label posLabel;

    public GuiStage(ObjectManager man, OrthographicCamera cam, Player play, MapManager map, SpriteBatch batch){
        objectManager = man;
        gameCamera = cam;
        player = play;
        mapManager = map;
        this.batch = batch;
        createGui();
    }

    @Override
    public void act(float delta){
        super.act(delta);
        firstLabel.setText(Float.toString(Gdx.graphics.getFramesPerSecond())+" FPS");
        secondLabel.setText("Objects: "+objectManager.getNumberOfObjects());
        posLabel.setText(String.format( "X: %.2f Y: %.2f",player.getPosition().x,player.getPosition().y));
    }

    private void createGui(){
        /**
         * Creates a new texture, simply a white pixel that can be colored.
         * TODO: JSON skin handling
         */
        skin = new Skin();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        /**
         * Debuggin&GUI tests
         */
        test();
    }

    private void test(){
        /**
         * Placeholder fonts
         */
        BitmapFont font = new BitmapFont();
        font.scale(0.5f);
        BitmapFont font2 = new BitmapFont();
        skin.add("default", font);
        skin.add("font2", font2);

        /**
         * Default style for buttons
         */
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.RED);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        /**
         * Default label style
         */
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("font2");
        skin.add("default", labelStyle);
        skin.add("default",textButtonStyle);

        /**
         * Default slider style
         */
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = skin.newDrawable("white",Color.WHITE);
        sliderStyle.knob = skin.newDrawable("white",Color.RED);
        skin.add("default-horizontal",sliderStyle);

        // Create a table that fills the screen. Everything else will go inside this table.
        VerticalGroup table = new VerticalGroup();
        table.setFillParent(true);
        addActor(table);
        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        final TextButton button = new TextButton("Clear Moving Objects", skin);
        final TextButton button2 = new TextButton("Piercing projectiles", skin);
        final TextButton spawnerButton = new TextButton("Mob Spawner", skin);
        final TextButton enableQuadView = new TextButton("Quadtree view", skin);
        final Slider slider = new Slider(0,1,0.05f,false,skin);
        final Slider sliderZoom = new Slider(0.05f,1.5f,0.05f,false,skin);
        slider.getStyle().knob.setMinHeight(50);
        slider.getStyle().knob.setMinWidth(10);
        slider.setValue(player.getClamping());

        sliderZoom.getStyle().knob.setMinWidth(10);
        sliderZoom.getStyle().knob.setMinHeight(50);
        sliderZoom.setValue(gameCamera.zoom);

        firstLabel = new Label("", skin);
        secondLabel = new Label("", skin);
        posLabel = new Label("",skin);
        table.addActor(button);
        table.addActor(button2);
        table.addActor(spawnerButton);
        table.addActor(enableQuadView);
        table.addActor(firstLabel);
        table.addActor(secondLabel);
        table.addActor(posLabel);
        table.addActor(slider);
        table.addActor(sliderZoom);
        table.align(Align.topRight);
        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                objectManager.clear();
            }
        });
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.piercingShotsDebug = !player.piercingShotsDebug;
            }
        });
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.setClamping(slider.getValue());
            }
        });
        spawnerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                objectManager.addObject(new WalkerSpawner(player.getPosition().x,player.getPosition().y,objectManager,batch));
            }
        });
        sliderZoom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameCamera.zoom = sliderZoom.getValue();
            }
        });
        enableQuadView.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                WorldRenderer.debugEnabled = !WorldRenderer.debugEnabled;
            }
        });
    }
}
