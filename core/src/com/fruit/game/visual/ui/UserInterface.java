package com.fruit.game.visual.ui;


import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Quad;
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
import com.fruit.game.Configuration;
import com.fruit.game.Controller;
import com.fruit.game.logic.WorldUpdater;
import com.fruit.game.logic.objects.entities.Enemy;
import com.fruit.game.logic.objects.entities.player.Player;
import com.fruit.game.logic.objects.items.Item;
import com.fruit.game.maps.Room;
import com.fruit.game.screens.MainMenuScreen;
import com.fruit.game.visual.Assets;
import com.fruit.game.visual.GameCamera;
import com.fruit.game.visual.tween.OnScreenMessageAccessor;
import com.fruit.game.visual.tween.TweenUtils;
import pl.kotcrab.vis.ui.VisTable;
import pl.kotcrab.vis.ui.VisUI;
import pl.kotcrab.vis.ui.widget.*;

import java.util.PriorityQueue;

/**
 * @Author FruitAddict
 */
public class UserInterface extends Stage {
    private GameCamera gameCamera;
    private WorldUpdater worldUpdater;
    private Touchpad touchpadMove;
    private Touchpad touchpadAttack;
    private Container<Table> miniMapContainer;
    private ProgressBar healthBar;
    private ProgressBar experienceBar;
    private ProgressBar bossBar;
    private Container<VerticalGroup> bossBarContainer;
    private Label healthLabelInfo, healthLabelValue, experienceLabelInfo,experienceLabelValue, bossLabelInfo, bossLabelValue;
    private Skin skin;
    private TextButton menuButtonPortrait;
    private VisWindow optionsWindow;
    private MessageHandler messageHandler;

    public UserInterface(GameCamera camera, WorldUpdater worldUpdater){
        this.gameCamera = camera;
        this.worldUpdater=  worldUpdater;
        miniMapContainer = new Container<>();
        Controller.registerUserInterface(this);
        messageHandler = new MessageHandler(this);
        //init gui
        initializeGUI();
    }

    public void initializeGUI(){
        //create default skin
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        Pixmap biggerPixMap = new Pixmap(10,10, Pixmap.Format.RGBA8888);
        biggerPixMap.setColor(Color.WHITE);
        biggerPixMap.fill();

        skin.add("touchBackground", Assets.getAsset("touchBackground.png", Texture.class));
        skin.add("touchKnob", Assets.getAsset("touchKnob.png",Texture.class));
        skin.add("characterIcon", Assets.getAsset("pentagram.jpg",Texture.class));
        skin.add("white", new Texture(pixmap));
        skin.add("bigPix", new Texture(biggerPixMap));
        skin.add("optimus-font", new BitmapFont(Gdx.files.internal("fonts//souls.fnt")));
        //skin.add("default", TextRenderer.redFont);

        //boss hp bar
        createBossBar();
        //touchpad style
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = skin.newDrawable("touchBackground",new Color(0,0,0,0f));
        touchpadStyle.knob = skin.newDrawable("touchKnob", new Color(0,0f,0f,0.2f));
        skin.add("default",touchpadStyle);

        skin.getFont("default-font").setScale(0.7f,0.7f);

        //create touchpads
        createControlTouchPads();

        //create hp/exp bars
        createStatusBars();

        //minimap cointainer alpha
        miniMapContainer.setColor(new Color(1,1,1,0.5f));



        //menu window

        //todo separate class
        Container<VisWindow> windowContainer = new Container<>();
        optionsWindow = new VisWindow("Menu", true);
        VisTextButton continueButton = new VisTextButton("Continue");
        VisTextButton statsButton = new VisTextButton("Character");
        VisTextButton exitButton = new VisTextButton("Main Menu");

        optionsWindow.add(continueButton).width(200).height(50);
        optionsWindow.row();
        optionsWindow.add(statsButton).width(200).height(50);
        optionsWindow.row();
        optionsWindow.add(exitButton).width(200).height(50);
        optionsWindow.setTitleAlignment(Align.center);
        optionsWindow.setVisible(false);

        windowContainer.setActor(optionsWindow);
        windowContainer.setFillParent(true);
        windowContainer.center();
        windowContainer.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        this.addActor(windowContainer);

        //Character window
        Container<VisWindow> characterWindowContainer = new Container<>();
        final CharacterWindow characterWindow = new CharacterWindow(Controller.getWorldUpdater().getPlayer(),"Character");
        characterWindow.setTitleAlignment(Align.center);
        characterWindow.setVisible(false);
        characterWindowContainer.setActor(characterWindow);
        characterWindowContainer.setFillParent(true);
        characterWindowContainer.center();
        this.addActor(characterWindowContainer);

        menuButtonPortrait.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Controller.pauseGame();
                optionsWindow.setVisible(true);
            }
        });

        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Controller.unpauseGame();
                optionsWindow.setVisible(false);
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Controller.getGameScreen().dispose();
                Controller.getMainGame().setScreen(new MainMenuScreen(Controller.getMainGame()));
            }
        });
        statsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                characterWindow.updateText();
                optionsWindow.setVisible(false);
                characterWindow.setVisible(true);
            }
        });




        updateMinimap();
        updateStatusBars(Controller.getWorldUpdater().getPlayer().stats.getHealthPoints(),Controller.getWorldUpdater().getPlayer().stats.getBaseMaximumHealthPoints(),
                Controller.getWorldUpdater().getPlayer().getExperiencePoints(),Controller.getWorldUpdater().getPlayer().getNextLevelExpRequirement(),Controller.getWorldUpdater().getPlayer().getStatPoints());
    }

    private void createBossBar() {
        bossBar = new ProgressBar(0,100,1,false,skin.get("boss-bar", ProgressBar.ProgressBarStyle.class));
        bossBar.getStyle().knob.setMinHeight(25);
        bossBar.getStyle().knobBefore = skin.newDrawable("bigPix",new Color(1f,0.1f,0,0.9f));
        bossLabelInfo = new Label("",skin);
        bossLabelValue = new Label("",skin);
        Stack stackBoss = new Stack();
        stackBoss.addActor(bossBar);
        stackBoss.addActor(bossLabelValue);

        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.addActor(bossLabelInfo);
        verticalGroup.addActor(stackBoss);

        bossLabelValue.setAlignment(Align.center);
        bossBarContainer = new Container<>();
        bossBarContainer.setActor(verticalGroup);
        bossBarContainer.top();
        bossBarContainer.setFillParent(true);
        bossBarContainer.setVisible(false);
        addActor(bossBarContainer);
    }

    public void addBossHealthBar(String bossName){
        bossBarContainer.setVisible(true);
        bossLabelInfo.setText(bossName);
    }

    public void updateBossBar(int hp, int maxhp, float perc){
        bossBar.setValue((float)hp/maxhp*100);
        bossLabelValue.setText(hp+"/"+maxhp);
    }

    public void removeBossBar(){
        bossBarContainer.setVisible(false);
    }

    public void createStatusBars(){
        healthBar = new ProgressBar(0,100,1,false,skin.get("health-bar", ProgressBar.ProgressBarStyle.class));
        experienceBar = new ProgressBar(0,100,1,false,skin.get("exp-bar", ProgressBar.ProgressBarStyle.class));
        healthBar.getStyle().knob.setMinHeight(20);
        experienceBar.getStyle().knob.setMinHeight(20);
        healthBar.getStyle().knobBefore = skin.newDrawable("bigPix",new Color(1f,0,0,0.9f));
        experienceBar.getStyle().knobBefore = skin.newDrawable("bigPix", new Color(1f,255/215f,0,0.9f));
        healthBar.setValue(Controller.getWorldUpdater().getPlayer().stats.getHealthPoints()/Controller.getWorldUpdater().getPlayer().stats.getBaseMaximumHealthPoints()*100);
        experienceBar.setValue(Controller.getWorldUpdater().getPlayer().getExperiencePoints()/10*100);
        //create labels
        experienceLabelInfo = new Label("Exp: ",skin);
        healthLabelInfo = new Label("Hp: ",skin);
        healthLabelValue = new Label("",skin);
        experienceLabelValue = new Label("",skin);
        VerticalGroup barGroup = new VerticalGroup();
        //health stack
        Stack stackHealth = new Stack();
        stackHealth.setScaleX(1.5f);
        stackHealth.addActor(healthBar);
        stackHealth.addActor(healthLabelInfo);
        stackHealth.addActor(healthLabelValue);
        healthLabelValue.setAlignment(Align.right);
        //exp stack
        Stack stackExp = new Stack();
        stackExp.setScaleX(1.5f);
        stackExp.addActor(experienceBar);
        stackExp.addActor(experienceLabelInfo);
        stackExp.addActor(experienceLabelValue);
        experienceLabelValue.setAlignment(Align.right);

        //add everything to bar vertical group
        barGroup.addActor(stackHealth);
        barGroup.addActor(stackExp);
        barGroup.setScaleX(1.2f);

        //horizontal group to hold both bars and the character portrait for accessing menus
        HorizontalGroup iconAndBarsGroup = new HorizontalGroup();
        menuButtonPortrait = new TextButton("Lv."+worldUpdater.getPlayer().getLevel(),skin);
        menuButtonPortrait.getStyle().down = skin.newDrawable("characterIcon",new Color(1,1,1,0.7f));
        menuButtonPortrait.getStyle().up = skin.newDrawable("characterIcon",new Color(1,1,1,1f));

        iconAndBarsGroup.addActor(menuButtonPortrait);
        iconAndBarsGroup.addActor(barGroup);
        iconAndBarsGroup.setFillParent(true);
        iconAndBarsGroup.align(Align.topLeft);
        iconAndBarsGroup.setColor(iconAndBarsGroup.getColor().r,iconAndBarsGroup.getColor().g,iconAndBarsGroup.getColor().b,0.6f);
        iconAndBarsGroup.setColor(new Color(1,1,1,0.5f));
        addActor(iconAndBarsGroup);
    }

    public void updateMinimap(){
        miniMapContainer.clear();
        miniMapContainer.setActor(getMiniMap());
        miniMapContainer.right().top();
        miniMapContainer.setFillParent(true);
        addActor(miniMapContainer);
    }

    public void updateStatusBars(int hp, int maxHP, int exp, int maxEXP, int statPoints){
        //todo string formatting
        healthBar.setValue((float)hp/maxHP*100);
        experienceBar.setValue((float)exp/maxEXP*100);
        healthLabelValue.setText(hp+"/"+maxHP);
        experienceLabelValue.setText(exp+"/"+maxEXP);
        menuButtonPortrait.setText(statPoints>0?"+ "+statPoints:"");
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
        VisTable minimap = new VisTable();
        Room[][] matrix = worldUpdater.getMapManager().getCurrentMap().getRoomMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if(matrix[i][j]!=null){
                    if(matrix[i][j].isContainsPlayer() && !matrix[i][j].isBossRoom()){
                        Image img = new Image((Texture)Assets.getAsset("mini1.png",Texture.class));
                        img.setScale(1f);
                        minimap.add(img).fill().expand();
                    }else if(matrix[i][j].isBossRoom())  {
                        Image img = new Image((Texture)Assets.getAsset("mini4.png",Texture.class));
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

    public void addItemDialogBox(Item item){
        Controller.pauseGame();
        final VisDialog itemDialog = new VisDialog("You've got an item!"){
          @Override
          public void result(Object o){
              int result =(Integer)o;
              switch(result){
                  case 1 : {
                      Controller.unpauseGame();
                      break;
                  }
              }
          }
        };
        Image itemSprite = new Image(Controller.getWorldRenderer().getObjectRenderer().itemAnimationPack.getItemSpriteByType(item.getItemType()));
        VisLabel descriptionLabel = new VisLabel(item.getDescription());
        descriptionLabel.setFontScale(0.8f);
        descriptionLabel.setWrap(true);
        itemDialog.getContentTable().add(itemSprite).colspan(3);
        itemDialog.row();
        itemDialog.getContentTable().add(descriptionLabel).width(400).height(100);
        itemDialog.button("Okay", 1);
        itemDialog.show(this);
    }

    public void addOnDeathDialog(Player player){
        Controller.pauseGame();
        final VisDialog deathDialog = new VisDialog("You're dead."){
          @Override
        public void result(Object o){
              Controller.getGameScreen().dispose();
              Controller.getMainGame().setScreen(new MainMenuScreen(Controller.getMainGame()));
          }
        };
        int minutes = (int)Controller.getGameScreen().gameLogicStateTime/60;
        int seconds = (int)Controller.getGameScreen().gameLogicStateTime%60;
        VisLabel survivalTime = new VisLabel(String.format("You've survived for %02d:%02d",minutes,seconds));
        survivalTime.setWrap(true);
        survivalTime.setFontScale(0.8f);
        VisLabel slainEnemies = new VisLabel("You've slain "+player.getSlainEnemies()+" enemies");
        slainEnemies.setWrap(true);
        slainEnemies.setFontScale(0.8f);
        VisLabel seedLabel = new VisLabel("Map seed: "+ Configuration.seed);
        seedLabel.setWrap(true);
        seedLabel.setFontScale(0.8f);
        deathDialog.getContentTable().add(survivalTime).width(400).height(50);
        deathDialog.getContentTable().row();
        deathDialog.getContentTable().add(slainEnemies).width(400).height(50);
        deathDialog.getContentTable().row();
        deathDialog.getContentTable().add(seedLabel).width(400).height(50);
        deathDialog.row();
        deathDialog.button("Confirm");
        deathDialog.show(this);
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

        messageHandler.update(delta);
    }

    @Override
    public void dispose(){
        VisUI.dispose();
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public class MessageHandler {
        private VisLabel messageLabel;
        private PriorityQueue<OnScreenMessage> messageQueue;
        private Stage stage;
        private float messageTimer;
        private OnScreenMessage currentMsg;

        public MessageHandler(Stage stage){
            Tween.registerAccessor(OnScreenMessage.class, new OnScreenMessageAccessor());
            messageLabel = new VisLabel();
            messageQueue = new PriorityQueue<>();
            messageLabel.setAlignment(Align.bottom);
            messageLabel.setFillParent(true);
            stage.addActor(messageLabel);
            this.stage = stage;
        }

        public void update(float delta){
            if(currentMsg != null && messageTimer > currentMsg.time){
                if(!messageQueue.isEmpty()){
                    System.out.println("setting new message");
                    currentMsg = messageQueue.poll();
                    messageLabel.setText(currentMsg.message);
                    messageTimer = 0;
                    startTween();
                }else {
                    currentMsg = null;
                    messageLabel.setText("");
                    messageTimer = 0;
                }
            }else if(currentMsg == null && !messageQueue.isEmpty()){
                System.out.println("setting new message");
                currentMsg = messageQueue.poll();
                messageLabel.setText(currentMsg.message);
                messageTimer = 0;
                startTween();
            }
            if(currentMsg!=null){
                messageTimer+=delta;
                messageLabel.setColor(currentMsg.getColor().r,currentMsg.getColor().g,currentMsg.getColor().b,currentMsg.getAlpha());
                messageLabel.setPosition(messageLabel.getX(),currentMsg.posY);
                messageLabel.setFontScale(currentMsg.getScale());
            }
        }

        public void startTween() {
                //starts the tweening sequence
            Timeline.createSequence()
                    .push(Tween.set(currentMsg, OnScreenMessageAccessor.ALPHA).target(0f))
                    .push(Tween.set(currentMsg, OnScreenMessageAccessor.POSITION_Y).target(currentMsg.getPosY()))
                    .push(Tween.set(currentMsg,OnScreenMessageAccessor.SCALE).target(0.9f))
                    .beginParallel()
                    .push(Tween.to(currentMsg, OnScreenMessageAccessor.POSITION_Y, currentMsg.time * 2 / 3).target(currentMsg.getPosY() + 35).ease(Quad.INOUT))
                    .push(Tween.to(currentMsg, OnScreenMessageAccessor.ALPHA, currentMsg.time * 2 / 3).target(1f))
                    .push(Tween.to(currentMsg, OnScreenMessageAccessor.SCALE, currentMsg.time * 2 / 3).target(1f))
                    .end()
                    .beginParallel()
                    .push(Tween.to(currentMsg, OnScreenMessageAccessor.POSITION_Y, currentMsg.time * 1 / 3).target(currentMsg.getPosY()).ease(Quad.INOUT))
                    .push(Tween.to(currentMsg, OnScreenMessageAccessor.ALPHA, currentMsg.time * 1 / 3).target(0f))
                    .push(Tween.to(currentMsg, OnScreenMessageAccessor.SCALE, currentMsg.time * 1 / 3).target(0.9f))
                    .end()
                    .start(TweenUtils.tweenManager);
        }


        public void addMessage(String message, Color color, float lifespan){
            messageQueue.offer(new OnScreenMessage(message,color,lifespan));
        }

        public class OnScreenMessage implements Comparable<OnScreenMessage>{
            private String message;
            private Color color;
            private float time;
            private float alpha;
            private float posX, posY;
            private int priority;
            private float scale;

            public OnScreenMessage(String msg, Color color, float time, int priority){
                this.message = msg;
                this.color = color;
                this.time = time;
                this.priority = priority;
                posY = Controller.getUserInterface().getViewport().getScreenY()/3f;
                scale = 1;
            }

            public OnScreenMessage(String msg, Color color, float time){
                this.message = msg;
                this.color = color;
                this.time = time;
                this.priority = 1;
                scale = 1;
                posY = Controller.getUserInterface().getViewport().getScreenY()/3f;
                System.out.println(Controller.getUserInterface().getHeight() / 3f);
                System.out.println(Controller.getUserInterface().getHeight() / 2f);
                System.out.println(Controller.getUserInterface().getHeight());
            }

            public float getAlpha() {
                return alpha;
            }

            public void setAlpha(float alpha) {
                this.alpha = alpha;
            }

            public float getPosX() {
                return posX;
            }

            public void setPosX(float posX) {
                this.posX = posX;
            }

            public float getPosY() {
                return posY;
            }

            public void setPosY(float posY) {
                this.posY = posY;
            }

            public float getScale() {
                return scale;
            }

            public void setScale(float scale) {
                this.scale = scale;
            }

            @Override
            public int compareTo(OnScreenMessage another) {
                if(priority > another.priority){
                    return 1;
                }
                else if( priority == another.priority){
                    return 0;
                } else {
                    return -1;
                }
            }

            public Color getColor() {
                return color;
            }
        }

    }

    private class CharacterWindow extends VisWindow{
        VisLabel levelInfoLabel;
        VisLabel statPointLabel;
        VisLabel hpLabel;
        VisTextButton hpButton;
        VisLabel aspdLabel;
        VisTextButton aspdButton;
        VisLabel speedLabel;
        VisTextButton speedButton;
        VisLabel damageLabel;
        VisTextButton damageButton;
        VisLabel aimLabel;
        VisTextButton aimButton;
        private Player player;

        public CharacterWindow(final Player player, String title) {
            super(title);
            this.player = player;
            levelInfoLabel = new VisLabel();
            levelInfoLabel.setFontScale(0.8f);
            levelInfoLabel.setColor(Color.RED);
            add(levelInfoLabel).width(300).height(50).colspan(2);
            row();
            statPointLabel = new VisLabel();
            statPointLabel.setFontScale(0.8f);
            statPointLabel.setColor(Color.RED);
            add(statPointLabel).width(300).height(50).colspan(2);
            row();
            hpLabel = new VisLabel();
            hpLabel.setFontScale(0.8f);
            hpButton = new VisTextButton("+");
            add(hpLabel).width(300).height(50);
            add(hpButton);
            row();
            aspdLabel = new VisLabel();
            aspdLabel.setFontScale(0.8f);
            aspdButton = new VisTextButton("+");
            add(aspdLabel).width(300).height(50);
            add(aspdButton);
            row();
            speedLabel = new VisLabel();
            speedLabel.setFontScale(0.8f);
            speedButton = new VisTextButton("+");
            add(speedLabel).width(300).height(50);
            add(speedButton);
            row();
            damageLabel = new VisLabel();
            damageLabel.setFontScale(0.8f);
            damageButton = new VisTextButton("+");
            add(damageLabel).width(300).height(50);
            add(damageButton);
            row();
            aimLabel = new VisLabel();
            aimLabel.setFontScale(0.8f);
            aimButton = new VisTextButton("+");
            add(aimLabel).width(300).height(50);
            add(aimButton);
            row();
            VisTextButton confirmButton = new VisTextButton("Confirm");
            add(confirmButton).width(200).height(50).colspan(2);
            confirmButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    optionsWindow.setVisible(true);
                    setVisible(false);
                }
            });
            hpButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(player.getStatPoints()>0){
                        player.stats.setBaseMaximumHealthPoints(player.stats.getBaseMaximumHealthPoints()+25);
                        player.setStatPoints(player.getStatPoints()-1);
                        updateText();
                        updateStatusBars(player.stats.getHealthPoints(),player.stats.getBaseMaximumHealthPoints(),
                                player.getExperiencePoints(),player.getNextLevelExpRequirement(),player.getStatPoints());
                    }
                }
            });
            aspdButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(player.getStatPoints()>0){
                        player.stats.setAttackSpeedModifier(player.stats.getAttackSpeedModifier()*0.8f);
                        player.setStatPoints(player.getStatPoints()-1);
                        updateText();
                        updateStatusBars(player.stats.getHealthPoints(),player.stats.getBaseMaximumHealthPoints(),
                                player.getExperiencePoints(),player.getNextLevelExpRequirement(),player.getStatPoints());
                    }
                }
            });
            speedButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(player.getStatPoints()>0){
                        player.stats.setMaxVelocity(player.stats.getMaxVelocity()+0.5f);
                        player.setStatPoints(player.getStatPoints()-1);
                        updateText();
                        updateStatusBars(player.stats.getHealthPoints(),player.stats.getBaseMaximumHealthPoints(),
                                player.getExperiencePoints(),player.getNextLevelExpRequirement(),player.getStatPoints());
                    }
                }
            });
            damageButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(player.getStatPoints()>0){
                        player.stats.setBaseDamage(player.stats.getBaseDamage()+1);
                        player.setStatPoints(player.getStatPoints() - 1);
                        updateText();
                        updateStatusBars(player.stats.getHealthPoints(),player.stats.getBaseMaximumHealthPoints(),
                                player.getExperiencePoints(),player.getNextLevelExpRequirement(),player.getStatPoints());
                    }
                }
            });
            aimButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(player.getStatPoints()>0){
                        player.stats.setAimSway(player.stats.getAimSway()+2);
                        player.setStatPoints(player.getStatPoints()-1);
                        updateText();
                        updateStatusBars(player.stats.getHealthPoints(),player.stats.getBaseMaximumHealthPoints(),
                                player.getExperiencePoints(),player.getNextLevelExpRequirement(),player.getStatPoints());
                    }
                }
            });
        }

        public void updateText(){
            levelInfoLabel.setText("Level: "+player.getLevel());
            statPointLabel.setText("Stat points: "+player.getStatPoints());
            hpLabel.setText("Max. health points: "+player.stats.getBaseMaximumHealthPoints());
            aspdLabel.setText(String.format("Attack speed: %.2f",player.stats.getCombinedAttackSpeed()));
            speedLabel.setText("Movement speed: " + player.stats.getMaxVelocity());
            damageLabel.setText("Damage: "+player.stats.getCombinedDamage());
            aimLabel.setText("Aim sway: "+player.stats.getAimSway());
        }
    }

}
