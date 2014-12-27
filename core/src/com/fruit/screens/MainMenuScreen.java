package com.fruit.screens;

import android.nfc.FormatException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fruit.Configuration;
import com.fruit.Controller;
import com.fruit.MainGame;
import com.fruit.logic.Constants;
import com.fruit.visual.messages.TextRenderer;

/**
 * @author FruitAddict
 */
public class MainMenuScreen implements Screen {
    private Stage menuStage;
    private MainGame mainGame;
    private Skin skin;

    public MainMenuScreen(final MainGame mainGame){
        menuStage = new Stage();
        this.mainGame = mainGame;


        skin = new Skin(Gdx.files.internal("uiskin.json"));
        VerticalGroup verticalGroup = new VerticalGroup();

        final Container<VerticalGroup> container = new Container<>();
        TextButton playButton = new TextButton("New Run",skin);
        TextButton optionsButton = new TextButton("Options",skin);
        verticalGroup.addActor(playButton);
        verticalGroup.addActor(optionsButton);


        container.setFillParent(true);
        container.setActor(verticalGroup);
        container.align(Align.center);
        menuStage.addActor(container);

        Gdx.input.setInputProcessor(menuStage);
        Container<Window> windowContainer = new Container<>();
        final Window optionsWindow = new Window("Options",skin);
        optionsWindow.setVisible(false);

        final TextButton confirmButton = new TextButton("Done",skin);
        confirmButton.center();

        Container<TextButton> containerButton = new Container<>();
        containerButton.setActor(confirmButton);

        final CheckBox shadowsEnabled = new CheckBox("Shadows",skin);
        shadowsEnabled.setChecked(true);
        shadowsEnabled.getStyle().checkboxOn.setMinWidth(25);
        shadowsEnabled.getStyle().checkboxOff.setMinHeight(25);
        shadowsEnabled.getStyle().font.setScale(0.9f);

        final TextField seedField = new TextField("",skin);
        Label seedInfo = new Label("Seed: ",skin); //todo handle this shit better

        optionsWindow.add(seedInfo);
        optionsWindow.add(seedField);
        optionsWindow.row();
        optionsWindow.add(confirmButton);
        optionsWindow.getCell(confirmButton).colspan(3);

        windowContainer.setActor(optionsWindow);
        windowContainer.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        windowContainer.align(Align.center);
        windowContainer.setFillParent(true);
        windowContainer.setSize(400,400);
        menuStage.addActor(windowContainer);

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                try{
                    int seedParsed = Integer.parseInt(seedField.getText());
                    Configuration.seed = seedParsed;
                }catch(Exception ex){
                    Configuration.seed = Constants.UNSPECIFIED;
                }
                Configuration.shadowsEnabled = shadowsEnabled.isChecked();
                mainGame.setScreen(new GameScreen(mainGame));
            }
        });

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                optionsWindow.setVisible(true);
                container.setVisible(false);
            }
        });

        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                optionsWindow.setVisible(false);
                container.setVisible(true);
            }
        });

    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        menuStage.act(1/30f);
        menuStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        menuStage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
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
        menuStage.dispose();
        skin.dispose();
    }
}
