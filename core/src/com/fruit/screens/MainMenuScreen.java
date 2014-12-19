package com.fruit.screens;

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
import com.fruit.MainGame;
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


        //todo json skin again
        skin = new Skin();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        skin.add("default", TextRenderer.redFont);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.BLACK);
        textButtonStyle.down = skin.newDrawable("white", Color.BLACK);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLACK);
        textButtonStyle.over = skin.newDrawable("white", Color.BLACK);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default",textButtonStyle);
        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.background = skin.newDrawable("white",Color.BLACK);
        windowStyle.titleFont = skin.getFont("default");
        windowStyle.titleFontColor = Color.OLIVE;
        skin.add("default",windowStyle);
        VerticalGroup verticalGroup = new VerticalGroup();

        Container<VerticalGroup> container = new Container<>();
        TextButton playButton = new TextButton("Play",skin);
        TextButton optionsButton = new TextButton("Options",skin);
        playButton.setFillParent(true);
        verticalGroup.addActor(playButton);
        verticalGroup.addActor(optionsButton);
        verticalGroup.pack();
        verticalGroup.setSize(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3);

        container.setFillParent(true);
        container.setActor(verticalGroup);
        container.align(Align.center);
        menuStage.addActor(container);

        Gdx.input.setInputProcessor(menuStage);

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                mainGame.setScreen(new GameScreen(mainGame));
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
    }
}
