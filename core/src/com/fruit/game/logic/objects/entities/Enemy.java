package com.fruit.game.logic.objects.entities;

import com.badlogic.gdx.utils.Array;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.items.Item;

//todo remove or rework this
public abstract class Enemy extends Character {
    public static final int WEAK_ENEMY = 1;
    public static final int CHAMPION_ENEMY = 2;
    public static final int BOSS_ENEMY = 3;
    /**
     * Every enemy Should implement onDirectContact method that will resolve direct collision between the enemy and the
     * player/other characters
     * @param character
     * Character passed to this method to resolve the contact, for example, the enemy can invoke its throwYourselfAtPlayer method
     */
    public abstract void onDirectContact(Character character);

    //every enemy should have its abstract level to be used in mapgen alghorithms. Level 1 should be for weak stuff,
    //Level 2 for champions, level 3 for bosses
    protected int enemyLevel;

    //every enemy should hold items that it will drop on death
    protected Array<Item> lootItems = new Array<>();

    public void clearLoot(){
        lootItems.clear();
    }

    public void addItemToLoot(Item item){
        lootItems.add(item);
    }

    public void addItemsToLoot(Item... items){
        for(Item item : items){
            lootItems.add(item);
        }
    }

    public void dropAllLoot(ObjectManager objectManager){
        System.out.println(lootItems.size);
        for(Item item: lootItems){
            item.setLastKnownX(body.getPosition().x);
            item.setLastKnownY(body.getPosition().y);
            objectManager.addObject(item);
        }
    }

    public void setEnemyThreatLevel(int type){
        this.enemyLevel = type;
    }

}
