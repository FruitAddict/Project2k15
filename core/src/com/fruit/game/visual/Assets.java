package com.fruit.game.visual;

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
 */
public class Assets {

    public static AssetManager manager = new AssetManager();
    private static Array<String> requests = new Array<String>();

    static {

        //todo change
        manager.setLoader(TiledMap.class , new TmxMapLoader( new InternalFileHandleResolver()));
    }

    public static void loadSplashScreen(){
        manager.load("splashtoday.jpg", Texture.class);
        manager.finishLoading();
    }

    public static synchronized <T> Object getAsset(String name, Class<T> type){
        //returns requested asset, if its not loaded forces it to load
        //and then returns it. if not found, returns either debug texture
        //or null
        try{
            return manager.get(name);
        }catch(GdxRuntimeException ex){
            try {
                manager.load(name, type);
                manager.finishLoading();
                return manager.get(name);
            }catch(GdxRuntimeException ex2){
                ex2.printStackTrace();
                if(type == Texture.class) {
                    return manager.get("notfound.png");
                }
                else if(type == TiledMap.class) {
                    System.out.println("couldnt load map "+name);
                    System.exit(1);
                    return null;
                } else {
                    return null;
                }
            }
        }
    }

    public static void disposeAll(){
        System.out.println("Disposing all assets");
        manager.clear();
    }
}
