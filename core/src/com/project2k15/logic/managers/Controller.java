package com.project2k15.logic.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.project2k15.logic.entities.Player;
import com.project2k15.logic.input.WorldInputProcessor;
import com.project2k15.rendering.WorldUpdater;
import com.project2k15.rendering.ui.GuiStage;

/**
 * Controller class contains references to all of the important managers/objects of the game.
 * This way each class only needs reference to controller class in order to operate on different stuff.
 * For example, player changing maps would need direct reference to object manager, map manager and
 * camera controls etc. This class will call them instead.
 */
public class Controller {
    private ObjectManager objectManager;
    private MapManager mapManager;
    private WorldInputProcessor worldInputProcessor;
    private OrthographicCamera orthographicCamera;
    private GuiStage guiStage;
    private WorldUpdater worldUpdater;
    private SpriteBatch batch;
    private Player player;

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public void setObjectManager(ObjectManager objectManager) {
        this.objectManager = objectManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public void setMapManager(MapManager mapManager) {
        this.mapManager = mapManager;
    }

    public WorldInputProcessor getWorldInputProcessor() {
        return worldInputProcessor;
    }

    public void setWorldInputProcessor(WorldInputProcessor worldInputProcessor) {
        this.worldInputProcessor = worldInputProcessor;
    }

    public OrthographicCamera getCam() {
        return orthographicCamera;
    }

    public void setCam(OrthographicCamera orthographicCamera) {
        this.orthographicCamera = orthographicCamera;
    }

    public GuiStage getGuiStage() {
        return guiStage;
    }

    public void setGuiStage(GuiStage guiStage) {
        this.guiStage = guiStage;
    }

    public WorldUpdater getWorldUpdater() {
        return worldUpdater;
    }

    public void setWorldUpdater(WorldUpdater worldUpdater) {
        this.worldUpdater = worldUpdater;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
