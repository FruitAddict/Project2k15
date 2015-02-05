package com.fruit.game.logic.objects.items;

import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.entities.Enemy;
import com.fruit.game.utilities.Utils;

/**
 * @Author FruitAddict
 * Class containing static methods to add random items to the game world.
 */
public class ItemManager {

    public static final int TYPE_COMMON = 1;
    public static final int TYPE_RARE = 2;

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
            switch(Utils.mapRandomNumberGenerator.nextInt(11)){
                case 0:{
                    enemy.addItemToLoot(new MoreProjectiles(objectManager));
                    break;
                }
                case 1: {
                    enemy.addItemToLoot(new DamageUp(objectManager));
                    break;
                }
                case 2: {
                    enemy.addItemToLoot(new FlamingProjectiles(objectManager));
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
                case 5:{
                    enemy.addItemToLoot(new IncreaseKnockbackHammer(objectManager));
                    break;
                }
                case 6:{
                    enemy.addItemToLoot(new BloodAmulet(objectManager));
                    break;
                }
                case 7:{
                    enemy.addItemToLoot(new ReflectDamageDoll(objectManager));
                    break;
                }
                case 8:{
                    enemy.addItemToLoot(new SlowerProjectilesRing(objectManager));
                    break;
                }
                case 9:{
                    enemy.addItemToLoot(new IncreaseProjectileSizeBall(objectManager));
                    break;
                }
                case 10:{
                    enemy.addItemToLoot(new TomeOfKnowledge(objectManager));
                    break;
                }

            }
        }
    }
}
