package com.fruit.game.utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.fruit.game.Configuration;
import com.fruit.game.logic.Constants;
import com.fruit.game.logic.objects.entities.GameObject;

import java.util.Random;

/**
 * @author FruitAddict
 */
public abstract class Utils implements Constants {
    public static Random randomGenerator = new Random();

    public static Random mapRandomNumberGenerator;

    public static int getRandomFromRange(int begin, int max){
        return begin + randomGenerator.nextInt(max-begin);
    }

    public static void setRandomGeneratorSeed(long seed){
        randomGenerator = new Random(seed);
    }

    //color interpolation temps
    private static final Vector3 tmp1 = new Vector3(), tmp2 = new Vector3();
    private static final Color colorTmp = new Color();

    public static void initializeMapRandomGen(long seed){
        //if the seed is set in config, use it, if not unspecified
        if(seed!=UNSPECIFIED) {
            mapRandomNumberGenerator = new Random(seed);
        }else {
            Configuration.seed = System.currentTimeMillis();
            mapRandomNumberGenerator = new Random(Configuration.seed);
        }
    }

    public static Vector2 getDrawPositionBasedOnBox2dCircle(GameObject gameObject){
        //returns chinese stuff i literally forgot what it does 5 minutes after writing it
        return new Vector2((gameObject.getBody().getPosition().x* PIXELS_TO_UNITS)-Math.min(gameObject.getWidth(), gameObject.getHeight())/2
                -(gameObject.getWidth()>gameObject.getHeight()?((gameObject.getWidth()-gameObject.getHeight())/2):0),
                (gameObject.getBody().getPosition().y* PIXELS_TO_UNITS)-Math.min(gameObject.getWidth(), gameObject.getHeight())/2);
    }

    public static <T> void fill2dArray(T[][] array,T value){
        //fills 2d array with given value
        for(int i = 0;i <array.length; i++){
            for(int j =0; j<array.length ; j++){
                array[i][j] = value;
            }
        }
    }

    public static <T> void print2dArray(T[][] array){
        //fills 2d array with given value
        for (T[] anArray : array) {
            for (int j = 0; j < array.length; j++) {
                System.out.print(anArray[j]+" ");
            }
            System.out.println();
        }
    }

    public static Color interpolateColours(Color color1, Color color2, float interpolation) {
        tmp1.set(color1.r, color1.g, color1.b).scl(1 - interpolation);
        tmp2.set(color2.r, color2.g, color2.b).scl(interpolation);
        tmp1.add(tmp2);

        return colorTmp.set(tmp1.x, tmp1.y, tmp1.z, color1.a * (1 - interpolation) + color2.a * interpolation);
    }
}
