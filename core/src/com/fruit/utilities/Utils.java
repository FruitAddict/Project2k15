package com.fruit.utilities;

import com.badlogic.gdx.math.Vector2;
import com.fruit.logic.Constants;
import com.fruit.logic.objects.entities.GameObject;

import java.util.Random;

/**
 * @author FruitAddict
 */
public class Utils implements Constants {
    public static Random randomGenerator = new Random();

    public static Random mapRandomNumberGenerator;

    public static int getRandomFromRange(int begin, int max){
        return begin + randomGenerator.nextInt(max-begin);
    }

    public static void setRandomGeneratorSeed(long seed){
        randomGenerator = new Random(seed);
    }

    public static void initializeMapRandomGen(long seed){
        mapRandomNumberGenerator = new Random(seed);
    }

    public static Vector2 getDrawPositionBasedOnBox2d(GameObject gameObject){
        return new Vector2((gameObject.getBody().getPosition().x*PIXELS_TO_METERS)-Math.min(gameObject.getWidth(), gameObject.getHeight())/2
                -(gameObject.getWidth()>gameObject.getHeight()?((gameObject.getWidth()-gameObject.getHeight())/2):0),
                (gameObject.getBody().getPosition().y*PIXELS_TO_METERS)-Math.min(gameObject.getWidth(), gameObject.getHeight())/2);
    }
}
