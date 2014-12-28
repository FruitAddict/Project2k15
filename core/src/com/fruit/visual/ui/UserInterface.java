package com.fruit.visual.ui;

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
import com.fruit.Controller;
import com.fruit.logic.WorldUpdater;
import com.fruit.logic.objects.entities.player.Player;
import com.fruit.logic.objects.items.Item;
import com.fruit.maps.Room;
import com.fruit.screens.MainMenuScreen;
import com.fruit.visual.Assets;
import com.fruit.visual.GameCamera;

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
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        Pixmap biggerPixMap = new Pixmap(10,10, Pixmap.Format.RGBA8888);
        biggerPixMap.setColor(Color.WHITE);
        biggerPixMap.fill();

        skin.add("touchBackground", Assets.getAsset("touchBackground.png", Texture.class));
        skin.add("touchKnob", Assets.getAsset("touchKnob.png",Texture.class));
        skin.add("characterIcon", Assets.getAsset("icon.png",Texture.class));
        skin.add("white", new Texture(pixmap));
        skin.add("bigPix", new Texture(biggerPixMap));
        skin.add("optimus-font", new BitmapFont(Gdx.files.internal("fonts//souls.fnt")));
        //skin.add("default", TextRenderer.redFont);

        //touchpad style
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = skin.newDrawable("touchBackground",new Color(0,0,0,0f));
        touchpadStyle.knob = skin.newDrawable("touchKnob", new Color(0,0f,0f,0.2f));
        skin.add("default",touchpadStyle);

        skin.getFont("default-font").setScale(0.7f,0.7f);

        //create touchpads
        createControlTouchPads();

        //create hp/exp bars
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
        Button menuButtonPortrait = new Button(skin);
        menuButtonPortrait.getStyle().down = skin.newDrawable("characterIcon",new Color(1,1,1,0.7f));
        menuButtonPortrait.getStyle().up = skin.newDrawable("characterIcon",new Color(1,1,1,1f));

        iconAndBarsGroup.addActor(menuButtonPortrait);
        iconAndBarsGroup.addActor(barGroup);
        iconAndBarsGroup.setFillParent(true);
        iconAndBarsGroup.align(Align.topLeft);
        iconAndBarsGroup.setColor(iconAndBarsGroup.getColor().r,iconAndBarsGroup.getColor().g,iconAndBarsGroup.getColor().b,0.6f);
        addActor(iconAndBarsGroup);

        //minimap cointainer alpha
        miniMapContainer.setColor(new Color(1,1,1,0.5f));

        //menu window
        Container<Window> windowContainer = new Container<>();
        final GameMenuWindow gameMenuWindow = new GameMenuWindow(skin);
        gameMenuWindow.setVisible(false);
        windowContainer.setActor(gameMenuWindow);
        windowContainer.setFillParent(true);
        addActor(windowContainer);

        menuButtonPortrait.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Controller.pauseGame();
                gameMenuWindow.setVisible(true);
            }
        });

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
        //todo string formatting
        healthBar.setValue(hp/maxHP*100);
        experienceBar.setValue(exp/maxEXP*100);
        healthLabelValue.setText((int)hp+"/"+(int)maxHP);
        experienceLabelValue.setText((int)exp+"/"+(int)maxEXP);
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

    public void addItemDialogBox(Item item){
        ItemDialog dialog = new ItemDialog(this,skin, item);
    }

    public void addLevelUpDialog(Player player){
        LevelUpDialog dialog = new LevelUpDialog(this, skin, player);
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

    private class ItemDialog extends Dialog{

        public ItemDialog(Stage stage,Skin skin, Item item) {
            super("You've obtained an item!", skin);
            Label label = new Label(item.getDescription(),skin);
            label.setWrap(true);
            label.setAlignment(Align.left, Align.left);
            label.setFontScale(0.9f);
            getContentTable().add(label).width(400);
            button("Ok", true);
            show(stage);
            setWidth(400);
            Controller.pauseGame();
        }

        @Override
        public void result(Object object){
            if((Boolean) object){
                Controller.unpauseGame();
            }
        }
    }

    private class LevelUpDialog extends Dialog{
        private Player player;

        public LevelUpDialog(Stage stage, Skin skin, Player player) {
            super("Level up!", skin);
            this.player = player;
            Controller.pauseGame();
            Label label = new Label("Choose one stat to improve:",skin);
            label.setWrap(true);
            label.setAlignment(Align.left, Align.left);
            label.setFontScale(0.9f);
            getContentTable().add(label).width(400);
            button("Damage", 1);
            button("Speed", 2);
            button("Attack Speed",3);
            show(stage);
        }

        @Override
        public void result(Object object){
            switch((Integer)object){
                case 1:{
                    player.stats.setBaseDamageModifier(player.stats.getBaseDamageModifier()+1);
                    Controller.unpauseGame();
                    break;
                }
                case 2:{
                    player.stats.setMaxVelocity(player.stats.getMaxVelocity()+1);
                    Controller.unpauseGame();
                    break;
                }
                case 3:{
                    player.stats.setAttackSpeedModifier(player.stats.getAttackSpeedModifier() * 0.8f);
                    Controller.unpauseGame();
                    break;
                }
            }
        }
    }

    private class GameMenuWindow extends Window{

        public GameMenuWindow( Skin skin) {
            super("Menu", skin);
            TextButton continueButton = new TextButton("Continue",skin);
            TextButton mainMenuButton = new TextButton("Exit",skin);
            add(continueButton).width(Gdx.graphics.getWidth() / 3);
            row();
            add(mainMenuButton).width(Gdx.graphics.getWidth() / 3);

            continueButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    setVisible(false);
                    Controller.unpauseGame();
                }
            });

            mainMenuButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Controller.getGameScreen().dispose();
                    Controller.getMainGame().setScreen(new MainMenuScreen(Controller.getMainGame()));
                }
            });
        }

        public GameMenuWindow(String title, Skin skin, String styleName) {
            super(title, skin, styleName);
        }

        public GameMenuWindow(String title, WindowStyle style) {
            super(title, style);
        }
    }

}
