package com.fruit.game.logic.objects.items;

import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.entities.Enemy;
import com.fruit.game.utilities.Utils;

/**
 * @Author FruitAddict
 * Class containing static methods to add random items to the game world.
 * TODO you know. add items to rooms
 */
public class ItemManager {

    public static final int TYPE_COMMON = 1;
    public static final int TYPE_RARE = 2;

    public static void addRandomItemToWorld(ObjectManager objectManager, float posX, float posY, int circleNumber){
        switch(circleNumber){
            default:{
                switch(Utils.randomGenerator.nextInt(7)){
                    case 0:{
                        objectManager.addObject(new HealthPotion(objectManager,posX,posY,5f,0.5f,2));
                        break;
                    }
                    case 1:{
                        objectManager.addObject(new DamageUp(objectManager,posX,posY));
                        break;
                    }
                    case 2:{
                        objectManager.addObject(new SphereOfProtection(objectManager,posX,posY,5));
                        break;
                    }
                    case 3:{
                        objectManager.addObject(new PoisonProjectiles(objectManager,posX,posY));
                        break;
                    }
                    case 4:{
                        objectManager.addObject(new PiercingProjectiles(objectManager,posX,posY));
                        break;
                    }
                    case 5:{
                        objectManager.addObject(new MoreProjectiles(objectManager,posX,posY));
                        break;
                    }
                    case 6:{
                        objectManager.addObject(new ExplodingProjectiles(objectManager,posX,posY));
                    }
                    default:{
                        break;
                    }
                }
                break;
            }
        }
    }

    public static void addItemToEnemy(ObjectManager objectManager, Enemy enemy, int type){
        if(type == TYPE_COMMON){
            switch(Utils.mapRandomNumberGenerator.nextInt(3)){
                case 0: {
                    enemy.addItemToLoot(new HealthPotion(objectManager,5f,0.5f,2));
                    break;
                }
                case 1: {
                    enemy.addItemToLoot(new SphereOfProtection(objectManager,5));
                    break;
                }
                case 2: {
                    enemy.addItemToLoot(new PoisonProjectiles(objectManager));
                    break;
                }
            }
        }
        if(type == TYPE_RARE){
            switch(Utils.mapRandomNumberGenerator.nextInt(5)){
                case 0:{
                    enemy.addItemToLoot(new MoreProjectiles(objectManager));
                    break;
                }
                case 1: {
                    enemy.addItemToLoot(new DamageUp(objectManager));
                    break;
                }
                case 2: {
                    enemy.addItemToLoot(new ExplodingProjectiles(objectManager));
                    break;
                }
                case 3: {
                    enemy.addItemToLoot(new PiercingProjectiles(objectManager));
                    break;
                }
                case 4: {
                    enemy.addItemToLoot(new ForkingProjectiles(objectManager));
                    break;
                }

            }
        }
    }
}
