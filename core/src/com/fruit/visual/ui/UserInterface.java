package com.fruit.visual.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fruit.logic.WorldUpdater;
import com.fruit.tests.Box;
import com.fruit.tests.Dummy;
import com.fruit.tests.MindlessWalker;
import com.fruit.visual.Assets;

public class UserInterface extends Stage {
    private OrthographicCamera camera;
    private WorldUpdater updater;
    //TODO KILL THIS CLASS WITH HOLY FIRE
    //hardcoded skin as placeholder
    Skin skin;
    //debug labels
    Label firstLabel;
    Label secondLabel;
    Label posLabel;
    //transparent colors
    private Color transRed;
    private Color transWhite;
    private Touchpad touchpadMove;
    private Touchpad touchpadAttack;

    private Vector2 attackDirectionNormalized;
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
        posLabel.setText(String.format("X: %.2f Y: %.2f", updater.getObjectManager().getPlayer().getBody().getPosition().x,
                updater.getObjectManager().getPlayer().getBody().getPosition().y));

        //update movement
        updater.getObjectManager().getPlayer().addLinearVelocity(touchpadMove.getKnobPercentX() * updater
                .getObjectManager().getPlayer().getSpeed(), touchpadMove.getKnobPercentY() * updater.getObjectManager().getPlayer().getSpeed());
        //update attacking
        if(touchpadAttack.getKnobPercentY()!=0 && touchpadAttack.getKnobPercentX()!=0) {
            attackDirectionNormalized.set(0 - touchpadAttack.getKnobPercentX(), 0 - touchpadAttack.getKnobPercentY());
            attackDirectionNormalized.nor();
            attackDirectionNormalized.x*=-1;
            attackDirectionNormalized.y*=-1;
            updater.getObjectManager().getPlayer().attack(attackDirectionNormalized);
        }
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
        skin.add("touchBackground", Assets.getAsset("touchBackground.png", Texture.class));
        skin.add("touchKnob", Assets.getAsset("touchKnob.png",Texture.class));
        skin.add("white", new Texture(pixmap));

        transRed = new Color(1,0,0,0f);
        transWhite = new Color(1,1,1,0.5f);

        attackDirectionNormalized = new Vector2();

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

        BitmapFont font2 = new BitmapFont();
        font.setColor(Color.WHITE);
        font2.setColor(Color.WHITE);
        skin.add("default", font);
        skin.add("font2", font2);

        /**
         * Default style for buttons
         */
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.BLACK);
        textButtonStyle.down = skin.newDrawable("white", Color.BLACK);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLACK);
        textButtonStyle.over = skin.newDrawable("white", Color.BLACK);
        textButtonStyle.font = skin.getFont("default");
        /**
         * Default label style
         */
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("font2");
        skin.add("default", labelStyle);
        skin.add("default",textButtonStyle);

        /**
         * touchpad style
         */
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = skin.newDrawable("touchBackground",transRed);
        touchpadStyle.knob = skin.newDrawable("touchKnob", transWhite);
        skin.add("default",touchpadStyle);

        /**
         * slider style
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
        HorizontalGroup buttonTable = new HorizontalGroup();
        buttonTable.setFillParent(true);
        buttonTable.align(Align.topLeft);
        table.setFillParent(true);
        table.align(Align.topRight);


        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        final Slider sliderZoom = new Slider(0.05f,1.5f,0.05f,false,skin);
        final ScrollPane scrollPane = new ScrollPane(table,skin);
        final Label infoAttack = new Label("Attack speed",skin);
        final Label infoZoom = new Label("Zoom",skin);
        final Slider sliderAttack = new Slider(0.15f,2.0f,0.05f,false,skin);
        sliderAttack.setValue(updater.getObjectManager().getPlayer().getTimeBetweenAttacks());
        sliderAttack.setValue(updater.getObjectManager().getPlayer().getTimeBetweenAttacks());
        final TextButton addMob = new TextButton("Add mobs ",skin);
        final TextButton addBox = new TextButton("Add dummy ",skin);
        final TextButton clearObjects = new TextButton("Remove all objects",skin);
        addMob.setColor(addMob.getColor().r,addMob.getColor().g,addMob.getColor().b,0.5f);
        addBox.setColor(addBox.getColor().r, addBox.getColor().g, addBox.getColor().b, 0.5f);
        clearObjects.setColor(clearObjects.getColor().r, clearObjects.getColor().g, clearObjects.getColor().b, 0.5f);
        scrollPane.getStyle().hScrollKnob.setMinHeight(10);
        scrollPane.getStyle().vScrollKnob.setMinWidth(10);
        scrollPane.setSize(200, 300);
        buttonTable.addActor(addMob);
        buttonTable.addActor(addBox);
        buttonTable.addActor(clearObjects);
        addActor(table);
        addActor(buttonTable);

        Container<Touchpad> containerMove = new Container<>();
        touchpadMove = new Touchpad(20,skin);
        containerMove.setActor(touchpadMove);
        containerMove.align(Align.bottomLeft);
        containerMove.setFillParent(true);

        Container<Touchpad> containerAttack = new Container<>();
        touchpadAttack = new Touchpad(20,skin);
        containerAttack.setActor(touchpadAttack);
        containerAttack.align(Align.bottomRight);
        containerAttack.setFillParent(true);

        addActor(containerMove);
        addActor(containerAttack);

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
                updater.getObjectManager().addObject(new Dummy(updater.getObjectManager(), updater.getObjectManager().getPlayer().getBody().getPosition().x,
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
