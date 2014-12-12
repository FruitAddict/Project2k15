package com.fruit.visual;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    public static BitmapFont redFont;
    public static BitmapFont greenFont;
    private static Array<String> requests = new Array<String>();

    static {

        //todo change
        manager.setLoader(TiledMap.class , new TmxMapLoader( new InternalFileHandleResolver()));
        manager.load("notfound.png", Texture.class);
        manager.finishLoading();
        redFont = new BitmapFont();
        redFont.setScale(1.5f, 1f);
        redFont.setColor(1.0f, 0.1f, 0.1f, 0.9f);

        greenFont = new BitmapFont();
        greenFont.setScale(1.5f,1f);
        greenFont.setColor(0.1f,1f,0.1f,0.9f);
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
                if(type == Texture.class) {
                    return manager.get("notfound.png");
                }
                else if(type == TiledMap.class) {
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

    public static void loadIntroLevel() {
        manager.load("64map.tmx", TiledMap.class);
        manager.load("64map2.tmx",TiledMap.class);
        manager.finishLoading();
    }
}
