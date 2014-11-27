package com.project2k15.rendering.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.project2k15.logic.managers.Controller;
import com.project2k15.rendering.WorldUpdater;
import com.project2k15.test.testmobs.Turret;
import com.project2k15.test.testmobs.WalkerSpawner;

/**
 * Gui stage, concentrates everything gui releated to free up space in the main game class
 */
public class GuiStage extends Stage {
    //hardcoded skin as placeholder
    Skin skin;
    //debug labels
    Label firstLabel;
    Label secondLabel;
    Label posLabel;
    private Controller controller;

    public void setController(Controller con){
        controller = con;
    }

    @Override
    public void act(float delta){
        super.act(delta);
        firstLabel.setText(Float.toString(Gdx.graphics.getFramesPerSecond())+" FPS");
        secondLabel.setText("Objects: " + controller.getObjectManager().getNumberOfObjects());
        posLabel.setText(String.format( "X: %.2f Y: %.2f",controller.getPlayer().getPosition().x,controller.getPlayer().getPosition().y));
    }

    public void createGui(){
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
         * test method creates debug buttons/info labels and adds them to the stage.
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

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.hScrollKnob = skin.newDrawable("white",Color.RED);
        scrollPaneStyle.vScrollKnob = skin.newDrawable("white",Color.BLUE);
        skin.add("default",scrollPaneStyle);

        // Create a table that fills the screen. Everything else will go inside this table.
        VerticalGroup table = new VerticalGroup();
        VerticalGroup buttonTable = new VerticalGroup();
        buttonTable.setFillParent(true);
        buttonTable.align(Align.topRight);
        table.setFillParent(true);
        table.right();
        buttonTable.left();
        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        final TextButton button = new TextButton("Clear Moving Objects", skin);
        final TextButton button2 = new TextButton("Piercing projectiles", skin);
        final TextButton spawnerButton = new TextButton("Mob Spawner", skin);
        final TextButton turretButton = new TextButton("Place turret", skin);
        final TextButton enableQuadView = new TextButton("Quadtree view", skin);
        final Slider slider = new Slider(0,1,0.05f,false,skin);
        final Slider sliderZoom = new Slider(0.05f,1.5f,0.05f,false,skin);
        final Slider sliderCam = new Slider(0.0f,1f,0.05f,false,skin);
        final ScrollPane scrollPane = new ScrollPane(table,skin);
        final Label infoGround = new Label("Sliding around level",skin);
        final Label infoZoom = new Label("Zoom",skin);
        final Label infoCam = new Label("Camera interpolation",skin);
        scrollPane.getStyle().hScrollKnob.setMinHeight(10);
        scrollPane.getStyle().vScrollKnob.setMinWidth(10);
        scrollPane.setSize(200, 300);

        sliderCam.setValue(controller.getWorldInputProcessor().getLerpValue());

        addActor(table);
        addActor(buttonTable);

        slider.getStyle().knob.setMinHeight(50);
        slider.getStyle().knob.setMinWidth(10);
        slider.setValue(controller.getPlayer().getClamping());

        sliderZoom.getStyle().knob.setMinWidth(10);
        sliderZoom.getStyle().knob.setMinHeight(50);
        sliderZoom.setValue(controller.getCam().zoom);

        firstLabel = new Label("", skin);
        secondLabel = new Label("", skin);
        posLabel = new Label("",skin);
        buttonTable.addActor(button);
        buttonTable.addActor(button2);
        buttonTable.addActor(spawnerButton);
        buttonTable.addActor(enableQuadView);
        buttonTable.addActor(turretButton);
        table.addActor(firstLabel);
        table.addActor(secondLabel);
        table.addActor(posLabel);
        table.addActor(infoGround);
        table.addActor(slider);
        table.addActor(infoZoom);
        table.addActor(sliderZoom);
        table.addActor(infoCam);
        table.addActor(sliderCam);
        table.align(Align.topRight);
        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                controller.getObjectManager().clear();
            }
        });
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.getPlayer().piercingShotsDebug = !controller.getPlayer().piercingShotsDebug;
            }
        });
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.getPlayer().setClamping(slider.getValue());
            }
        });
        spawnerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.getObjectManager().addObject(new WalkerSpawner(controller.getPlayer().getPosition().x, controller.getPlayer().getPosition().y, controller.getObjectManager(), controller.getBatch(),controller));
            }
        });
        sliderZoom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.getCam().zoom = sliderZoom.getValue();
            }
        });
        enableQuadView.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                WorldUpdater.debugEnabled = !WorldUpdater.debugEnabled;
            }
        });
        turretButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.getObjectManager().addObject(new Turret(controller.getPlayer().getPosition().x, controller.getPlayer().getPosition().y, controller.getObjectManager(),controller.getBatch(), controller.getPlayer()));
            }
        });
        sliderCam.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.getWorldInputProcessor().setLerpValue(sliderCam.getValue());
            }
        });
    }
}
