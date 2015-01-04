package com.fruit.game.screens;

;import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.fruit.game.Configuration;
import com.fruit.game.MainGame;
import com.fruit.game.logic.Constants;
import com.fruit.game.visual.Assets;
import com.fruit.game.visual.messages.TextRenderer;
import pl.kotcrab.vis.ui.VisTable;
import pl.kotcrab.vis.ui.VisUI;
import pl.kotcrab.vis.ui.widget.*;

/**
 * @author FruitAddict
 */
public class MainMenuScreen implements Screen {
    private Stage menuStage;
    private MainGame mainGame;
    private Skin skin;

    public MainMenuScreen(final MainGame mainGame){
        menuStage = new Stage();
        Gdx.input.setInputProcessor(menuStage);
        this.mainGame = mainGame;
        VisUI.load(Gdx.files.internal("mod.json"));
        //VisUI.load();
        final VisTable visTable = new VisTable(true);
        VisTextButton newRunButton = new VisTextButton("New Run");
        visTable.add(newRunButton).width(200).height(50);
        visTable.row();
        VisTextButton optionsButton = new VisTextButton("Options");
        visTable.add(optionsButton).width(200).height(50);
        visTable.row();
        VisTextButton aboutButton = new VisTextButton("About");
        visTable.add(aboutButton).width(200).height(50);
        visTable.row();
        VisTextButton exitButton = new VisTextButton("Exit");
        visTable.add(exitButton).width(200).height(50);
        visTable.row();


        visTable.setFillParent(true);
        menuStage.addActor(visTable);

        Container<VisWindow> windowContainer = new Container<>();
        final VisWindow optionsWindow = new VisWindow("Options", true);
        final VisTextField seedField = new VisTextField();
        VisLabel infoSeedLabel = new VisLabel("Seed: ");
        VisTextButton confirmButton = new VisTextButton("Confirm");

        optionsWindow.add(infoSeedLabel);
        optionsWindow.add(seedField);
        optionsWindow.row();
        optionsWindow.add(confirmButton).colspan(3);
        optionsWindow.setTitleAlignment(Align.center);
        optionsWindow.setVisible(false);
        optionsWindow.setKeepWithinStage(true);

        windowContainer.setActor(optionsWindow);
        windowContainer.setFillParent(true);
        windowContainer.top();
        windowContainer.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        menuStage.addActor(windowContainer);

        Container<VisWindow> aboutContainer = new Container<>();
        final VisWindow aboutWindow = new VisWindow("About");
        aboutWindow.setTitleAlignment(Align.center);
        VisLabel aboutArea = new VisLabel("Programming done by FruitAddict, graphics are done by tenszi and some are stolen.");
        aboutArea.setWrap(true);
        VisTextButton okButton = new VisTextButton("Ok");
        aboutWindow.add(aboutArea).width(400).height(200);
        aboutWindow.row();
        aboutWindow.add(okButton).colspan(3);
        aboutWindow.setVisible(false);

        aboutContainer.setActor(aboutWindow);
        aboutContainer.center();
        aboutContainer.setFillParent(true);
        menuStage.addActor(aboutContainer);

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                optionsWindow.setVisible(true);
                visTable.setVisible(false);
            }
        });
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                optionsWindow.setVisible(false);
                visTable.setVisible(true);
            }
        });
        newRunButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                try {
                    int seedParsed = Integer.parseInt(seedField.getText());
                    Configuration.seed = seedParsed;
                } catch (Exception ex) {
                    Configuration.seed = Constants.UNSPECIFIED;
                }
                mainGame.setScreen(new GameScreen(mainGame));
            }
        });
        aboutButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                aboutWindow.setVisible(true);
                visTable.setVisible(false);
            }
        });
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                aboutWindow.setVisible(false);
                visTable.setVisible(true);
            }
        });
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Assets.disposeAll();
                TextRenderer.disposeAllFonts();
                Gdx.app.exit();
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
        menuStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),true);
    }

    @Override
    public void show() {
        menuStage.setViewport(new StretchViewport(840,480));
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
