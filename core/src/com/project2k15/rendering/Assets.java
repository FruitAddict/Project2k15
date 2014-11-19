package com.project2k15.rendering;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Class for managing assets in the game.
 */
public class Assets {
    public static AssetManager manager = new AssetManager();

    static {
        manager.setLoader(TiledMap.class , new TmxMapLoader( new InternalFileHandleResolver()));
    }

    public static void loadSplashScreen(){
        manager.load("splashtoday.jpg", Texture.class);
        manager.finishLoading();
    }

    public static void loadTestMap(){
        manager.load("map.tmx", TiledMap.class);
        manager.load("playersheet.png", Texture.class);
        manager.load("test.gif", Texture.class);
        manager.load("pet.png", Texture.class);
        manager.load("testBox.png", Texture.class);
        manager.load("redheady.png", Texture.class);
        manager.load("playersheet.png", Texture.class);
        manager.load("projectile.png", Texture.class);
        manager.load("deadfireball.png", Texture.class);
        manager.finishLoading();
    }

    public static void disposeAll(){
        manager.clear();
    }
}