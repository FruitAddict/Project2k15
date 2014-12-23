package com.fruit.logic.objects.items;

import com.fruit.logic.ObjectManager;
import com.fruit.utilities.Utils;

/**
 * @Author FruitAddict
 * Class containing static methods to add random items to the game world.
 * TODO you know. add items to rooms
 */
public class ItemManager {

    public static void addRandomItem(ObjectManager objectManager,float posX, float posY, int circleNumber){
        switch(circleNumber){
            default:{
                switch(Utils.randomGenerator.nextInt(4)){
                    case 0:{
                        objectManager.addObject(new HealthPotion(objectManager,posX,posY,32,32,5f,0.5f,2f));
                        break;
                    }
                    case 1:{
                        objectManager.addObject(new DamageUp(objectManager,posX,posY,32,32));
                        break;
                    }
                    case 2:{
                        objectManager.addObject(new SphereOfProtection(objectManager,posX,posY,32,32,5));
                        break;
                    }
                    case 3:{
                        objectManager.addObject(new PoisonProjectiles(objectManager,posX,posY,32,32));
                    }
                    default:{
                        break;
                    }
                }
                break;
            }
        }
    }
}
