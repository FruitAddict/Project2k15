package com.fruit.visual.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fruit.logic.WorldUpdater;
import com.fruit.tests.Box;
import com.fruit.tests.MindlessWalker;

public class UserInterface extends Stage {
    private OrthographicCamera camera;
    private WorldUpdater updater;

    //hardcoded skin as placeholder
    Skin skin;
    //debug labels
    Label firstLabel;
    Label secondLabel;
    Label posLabel;

    public UserInterface(OrthographicCamera camera, WorldUpdater worldUpdater){
        this.camera = camera;
        this.updater=  worldUpdater;
        createGui();
    }
    @Override
    public void act(float delta){
        super.act(delta);
        firstLabel.setText(Float.toString(Gdx.graphics.getFramesPerSecond())+" FPS");
        secondLabel.setText("Objects: " + updater.getObjectManager().getNumberOfObjects());
        posLabel.setText(String.format( "X: %.2f Y: %.2f",updater.getObjectManager().getPlayer().getBody().getPosition().x,
                updater.getObjectManager().getPlayer().getBody().getPosition().y));
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
        textButtonStyle.checked = skin.newDrawable("white", Color.DARK_GRAY);
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
        final Slider sliderZoom = new Slider(0.05f,1.5f,0.05f,false,skin);
        final ScrollPane scrollPane = new ScrollPane(table,skin);
        final Label infoAttack = new Label("Attack speed",skin);
        final Label infoZoom = new Label("Zoom",skin);
        final Slider sliderAttack = new Slider(0.01f,2.0f,0.05f,false,skin);
        sliderAttack.setValue(updater.getObjectManager().getPlayer().getTimeBetweenAttacks());
        final TextButton addMob = new TextButton("Add mob",skin);
        final TextButton addBox = new TextButton("Add box",skin);
        final TextButton clearObjects = new TextButton("Remove all objects",skin);
        scrollPane.getStyle().hScrollKnob.setMinHeight(10);
        scrollPane.getStyle().vScrollKnob.setMinWidth(10);
        scrollPane.setSize(200, 300);
        buttonTable.addActor(addMob);
        buttonTable.addActor(addBox);
        buttonTable.addActor(clearObjects);
        addActor(table);
        addActor(buttonTable);
        sliderZoom.getStyle().knob.setMinWidth(10);
        sliderZoom.getStyle().knob.setMinHeight(50);
        sliderZoom.setValue(camera.zoom);

        firstLabel = new Label("", skin);
        secondLabel = new Label("", skin);
        posLabel = new Label("",skin);
        table.addActor(firstLabel);
        table.addActor(secondLabel);
        table.addActor(posLabel);
        table.addActor(infoZoom);
        table.addActor(sliderZoom);
        table.addActor(infoAttack);
        table.addActor(sliderAttack);
        table.align(Align.topRight);
        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        sliderZoom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                camera.zoom = sliderZoom.getValue();
            }
        });
        addMob.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for(int i =0 ;i<5;i++) {
                    updater.getObjectManager().addObject(new MindlessWalker(updater.getObjectManager(), updater.getObjectManager().getPlayer().getBody().getPosition().x,
                            updater.getObjectManager().getPlayer().getBody().getPosition().y));
                }
            }
        });
        addBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updater.getObjectManager().addObject(new Box(updater.getObjectManager(), updater.getObjectManager().getPlayer().getBody().getPosition().x,
                        updater.getObjectManager().getPlayer().getBody().getPosition().y));
            }
        });
        clearObjects.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updater.getObjectManager().removeAllGameObjects(false);
            }
        });
        sliderAttack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updater.getObjectManager().getPlayer().setTimeBetweenAttacks(sliderAttack.getValue());
            }
        });
    }
}
