package com.project2k15.rendering;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Class for managing assets in the game. Currently naively loads
 * all resources and maps
 * TODO rewrite of this class.
 */
public class Assets {

    public static AssetManager manager = new AssetManager();
    private static Array<String> requests = new Array<String>();

    static {
        manager.setLoader(TiledMap.class , new TmxMapLoader( new InternalFileHandleResolver()));
        manager.load("notfound.png",Texture.class);
    }

    public static void loadSplashScreen(){
        manager.load("splashtoday.jpg", Texture.class);
        manager.finishLoading();
    }

    public static void loadTestMap(){
        manager.load("64map.tmx", TiledMap.class);
        manager.load("testportal.png", Texture.class);
        manager.load("map.tmx", TiledMap.class);
        manager.load("pet.png",Texture.class);
        manager.load("playersheet.png", Texture.class);
        manager.load("front.png",Texture.class);
        manager.finishLoading();
    }

    public static synchronized <T> Object getAsset(String name, Class<T> type){
        try{
            return manager.get(name);
        }catch(GdxRuntimeException ex){
            manager.load(name, type);
            manager.finishLoading();
            return manager.get(name);
        }
    }

    public static void disposeAll(){
        System.out.println("Disposing all assets");
        manager.clear();
    }
}
