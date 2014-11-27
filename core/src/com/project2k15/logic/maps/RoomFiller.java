package com.project2k15.logic.maps;

import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.CollisionResolver;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.collision.RectangleTypes;
import com.project2k15.logic.entities.abstracted.MovableObject;
import com.project2k15.logic.managers.Controller;
import com.project2k15.logic.managers.ObjectManager;
import com.project2k15.test.testmobs.MindlessWalker;

import java.util.Random;

/**
 * Temporary class used to fill rooms with random enemies.
 */
public class RoomFiller implements RectangleTypes {

    public static void fillRoom(Room room, int numberOfMobs, ObjectManager manager, Controller controller){
        Array<MovableObject> list = room.getGameObjectList();
        int numbOfSpawn = numberOfMobs;
        Array<PropertyRectangle> recs = room.getTerrainCollisionRectangles();
        Random random = new Random();
        System.out.println("Filling room..");
        while(numbOfSpawn>0){
            int posX = random.nextInt((int)room.getWidth());
            int posY = random.nextInt((int)room.getHeight());
            int width = 32;
            int height = 32;
            PropertyRectangle testRec = new PropertyRectangle(posX,posY,width,height, CHARACTER_REC);
            if(!CollisionResolver.resolveCollisionSimple(recs,testRec)){
                list.add(MindlessWalker.getRandomWalker(posX,posY,controller.getBatch(),manager,controller));
                numbOfSpawn--;
            }
        }


    }
}
