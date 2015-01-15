package com.fruit.game.logic.objects.entities.enemies;

import com.badlogic.gdx.physics.box2d.World;
import com.fruit.game.logic.ObjectManager;
import com.fruit.game.logic.objects.entities.*;

/**
 * @Author FruitAddict
 */
public class Zombie extends Enemy {

    private ObjectManager objectManager;

    public Zombie(ObjectManager objectManager, float spawnX, float spawnY){
        this.objectManager = objectManager;
        lastKnownX = spawnX;
        lastKnownY = spawnY;
    }

    @Override
    public void onDirectContact(com.fruit.game.logic.objects.entities.Character character) {

    }

    @Override
    public void onContactWithTerrain(int direction) {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void addToBox2dWorld(World world) {

    }
}
