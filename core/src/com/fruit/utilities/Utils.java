package com.fruit.utilities;

import java.util.Random;

/**
 * @author FruitAddict
 */
public class Utils {
    public static final Random randomGenerator = new Random();

    public static int getRandomFromRange(int begin, int max){
        return begin + randomGenerator.nextInt(max-begin);
    }
}
