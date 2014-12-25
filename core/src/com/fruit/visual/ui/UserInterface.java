package com.fruit.visual.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.fruit.Controller;
import com.fruit.logic.WorldUpdater;
import com.fruit.maps.Room;
import com.fruit.visual.Assets;
import com.fruit.visual.GameCamera;
import com.fruit.visual.messages.TextRenderer;

/**
 * @Author FruitAddict
 */
public class UserInterface extends Stage {
    private GameCamera gameCamera;
    private WorldUpdater worldUpdater;
    private Touchpad touchpadMove;
    private Touchpad touchpadAttack;
    private Skin skin;
    private Container<Table> miniMapContainer;
    private ProgressBar healthBar;
    private ProgressBar experienceBar;
    private Label healthLabelInfo, healthLabelValue, experienceLabelInfo,experienceLabelValue;

    public UserInterface(GameCamera camera, WorldUpdater worldUpdater){
        this.gameCamera = camera;
        this.worldUpdater=  worldUpdater;
        miniMapContainer = new Container<>();
        Controller.registerUserInterface(this);
        //init gui
        initializeGUI();
    }

    public void initializeGUI(){
        //create default skin
        skin = new Skin();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        Pixmap biggerPixMap = new Pixmap(20,20, Pixmap.Format.RGBA8888);
        biggerPixMap.setColor(Color.WHITE);
        biggerPixMap.fill();

        skin.add("touchBackground", Assets.getAsset("touchBackground.png", Texture.class));
        skin.add("touchKnob", Assets.getAsset("touchKnob.png",Texture.class));
        skin.add("white", new Texture(pixmap));
        skin.add("bigPix", new Texture(biggerPixMap));
        skin.add("default", TextRenderer.redFont);

        //touchpad style
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = skin.newDrawable("touchBackground",new Color(0,0,0,0f));
        touchpadStyle.knob = skin.newDrawable("touchKnob", new Color(0,0f,0f,0.2f));
        skin.add("default",touchpadStyle);

        //label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        skin.add("default", labelStyle);

        //progress bar style
        ProgressBar.ProgressBarStyle progressBarStyleHP = new ProgressBar.ProgressBarStyle();
        progressBarStyleHP.background = skin.newDrawable("white", new Color(0,0,0,0));
        progressBarStyleHP.knob = skin.newDrawable("bigPix",new Color(1f,0,0,0));
        progressBarStyleHP.knobBefore = skin.newDrawable("bigPix", new Color(1f,0,0,0.9f));
        skin.add("health-bar",progressBarStyleHP);
        ProgressBar.ProgressBarStyle progressBarStyleEXP = new ProgressBar.ProgressBarStyle();
        progressBarStyleEXP.background = skin.newDrawable("white", new Color(0,0,0,0));
        progressBarStyleEXP.knob = skin.newDrawable("bigPix",new Color(0,0,0,0));
        progressBarStyleEXP.knobBefore = skin.newDrawable("bigPix", new Color(1f,255/215f,0,0.9f));
        skin.add("exp-bar",progressBarStyleEXP);

        //create touchpads
        createControlTouchPads();

        //create hp/exp bars
        healthBar = new ProgressBar(0,100,1,false,skin.get("health-bar", ProgressBar.ProgressBarStyle.class));
        experienceBar = new ProgressBar(0,100,1,false,skin.get("exp-bar", ProgressBar.ProgressBarStyle.class));
        healthBar.getStyle().knob.setMinHeight(20);
        experienceBar.getStyle().knob.setMinHeight(20);
        healthBar.setValue(Controller.getWorldUpdater().getPlayer().stats.getHealthPoints()/Controller.getWorldUpdater().getPlayer().stats.getBaseMaximumHealthPoints()*100);
        experienceBar.setValue(Controller.getWorldUpdater().getPlayer().getExperiencePoints()/10*100);
        if(experienceBar.getValue()<1){
            experienceBar.getStyle().knobBefore = skin.newDrawable("bigPix", new Color(1f,255/215f,0,0f));
        }
        //create labels
        experienceLabelInfo = new Label("Exp: ",skin);
        healthLabelInfo = new Label("HP: ",skin);
        healthLabelValue = new Label("",skin);
        experienceLabelValue = new Label("",skin);

        VerticalGroup barGroup = new VerticalGroup();
        //health stack
        Stack stackHealth = new Stack();
        stackHealth.addActor(healthBar);
        stackHealth.addActor(healthLabelInfo);
        stackHealth.addActor(healthLabelValue);
        healthLabelValue.setAlignment(Align.right);
        Stack stackExp = new Stack();
        stackExp.addActor(experienceBar);
        stackExp.addActor(experienceLabelInfo);
        stackExp.addActor(experienceLabelValue);
        experienceLabelValue.setAlignment(Align.right);
        barGroup.addActor(stackHealth);
        barGroup.addActor(stackExp);
        barGroup.align(Align.topLeft);
        barGroup.setFillParent(true);
        addActor(barGroup);

        updateMinimap();
        updateStatusBars(Controller.getWorldUpdater().getPlayer().stats.getHealthPoints(),Controller.getWorldUpdater().getPlayer().stats.getBaseMaximumHealthPoints(),
                Controller.getWorldUpdater().getPlayer().getExperiencePoints(),Controller.getWorldUpdater().getPlayer().getNextLevelExpRequirement());
    }

    public void updateMinimap(){
        miniMapContainer.clear();
        miniMapContainer.setActor(getMiniMap());
        miniMapContainer.right().top();
        miniMapContainer.setFillParent(true);
        addActor(miniMapContainer);
    }

    public void updateStatusBars(float hp, float maxHP, float exp, float maxEXP){
        if(experienceBar.getValue() < 1){
            if(experienceBar.getValue() + exp/maxEXP*100 > 1){
                experienceBar.getStyle().knobBefore = skin.newDrawable("bigPix", new Color(1f,255/215f,0,9f));
            }
        }
        //todo string formatting
        healthBar.setValue(hp/maxHP*100);
        experienceBar.setValue(exp/maxEXP*100);
        healthLabelValue.setText((int)hp+"/"+(int)maxHP);
        experienceLabelValue.setText((int)exp+"/"+(int)maxEXP);
        if(experienceBar.getValue()<1){
            experienceBar.getStyle().knobBefore = skin.newDrawable("bigPix", new Color(1f,255/215f,0,0f));
        }
    }


    public void createControlTouchPads(){
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
    }

    public Table getMiniMap(){
        Table minimap = new Table(skin);
        Room[][] matrix = worldUpdater.getMapManager().getCurrentMap().getRoomMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if(matrix[i][j]!=null){
                    if(matrix[i][j].isContainsPlayer()){
                        Image img = new Image((Texture)Assets.getAsset("mini1.png",Texture.class));
                        img.setScale(1f);
                        minimap.add(img).fill().expand();
                    }else {
                        Image img = new Image((Texture)Assets.getAsset("mini2.png",Texture.class));
                        img.setScale(1f);
                        minimap.add(img).fill().expand();
                    }
                } else {
                    Image img = new Image((Texture)Assets.getAsset("mini3.png",Texture.class));
                    img.setScale(1f);
                    minimap.add(img).fill().expand();
                }
            }
            minimap.row();
        }
        return minimap;
    }

    @Override
    public void act(float delta){
        super.act(delta);
        if(touchpadMove.getKnobPercentX()!=0 && touchpadMove.getKnobPercentY()!=0) {
            worldUpdater.getObjectManager().getPlayer().addLinearVelocity(touchpadMove.getKnobPercentX() * worldUpdater
                    .getObjectManager().getPlayer().stats.getSpeed(), touchpadMove.getKnobPercentY() * worldUpdater.getObjectManager().getPlayer().stats.getSpeed());
        }
        //update attacking
        if(touchpadAttack.getKnobPercentY()!=0 && touchpadAttack.getKnobPercentX()!=0) {
            worldUpdater.getObjectManager().getPlayer().attack(touchpadAttack.getKnobPercentX(),touchpadAttack.getKnobPercentY());
        }
    }



}
