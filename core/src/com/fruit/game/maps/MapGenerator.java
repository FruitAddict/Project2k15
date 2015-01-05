package com.fruit.game.maps;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fruit.game.logic.objects.entities.bosses.EnormousGlutton;
import com.fruit.game.logic.objects.items.DamageUp;
import com.fruit.game.logic.objects.items.ItemManager;
import com.fruit.game.logic.objects.entities.enemies.MindlessWalker;
import com.fruit.game.logic.objects.entities.enemies.TheEye;
import com.fruit.game.logic.objects.entities.misc.Box;
import com.fruit.game.logic.objects.items.PiercingProjectiles;
import com.fruit.game.utilities.MapObjectParser;
import com.fruit.game.utilities.Utils;
import com.fruit.game.visual.Assets;

/**
 * @Author FruitAddict
 */
public class MapGenerator {

    public static Map generateMap(MapManager mapManager,int circleLevel){
        Map map = new Map(mapManager,9);
        Boolean[][] layout = obtainLayout(9,9);

        //first run, creating rooms based on the layout.
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout.length; j++) {
                if(layout[i][j]){
                   String roomType = "";
                    if(i-1 > 0 && layout[i-1][j]){
                        roomType+="N";
                    }
                    if(j+1 < layout.length && layout[i][j+1]){
                        roomType+="E";
                    }
                    if(i+1 < layout.length && layout[i+1][j]){
                        roomType+="S";
                    }
                    if(j-1 > 0 && layout[i][j-1]){
                        roomType+="W";
                    }


                    System.out.println(roomType);
                    map.getRoomMatrix()[i][j] = new Room((TiledMap) Assets.getAsset("maps//" + roomType + ".tmx", TiledMap.class));
                }
            }
        }

        //second run, linking them together.
        Room[][] roomMatrix = map.getRoomMatrix();
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout.length; j++) {
                if(layout[i][j]){
                    if(j-1 > 0 && layout[i][j-1]){
                        Room thisRoom = map.getRoomMatrix()[i][j];
                        thisRoom.setLinkedRoomWest(roomMatrix[i][j-1]);
                    }
                    if(i+1 < layout.length && layout[i+1][j]){
                        Room thisRoom = map.getRoomMatrix()[i][j];
                        thisRoom.setLinkedRoomSouth(roomMatrix[i + 1][j]);
                    }
                    if(j+1 < layout.length && layout[i][j+1]){
                        Room thisRoom = map.getRoomMatrix()[i][j];
                        thisRoom.setLinkedRoomEast(roomMatrix[i][j + 1]);
                    }
                    if(i-1 > 0 && layout[i-1][j]){
                        Room thisRoom = map.getRoomMatrix()[i][j];
                        thisRoom.setLinkedRoomNorth(roomMatrix[i - 1][j]);
                    }
                }
            }
        }
        //third room, find candidate for the boss room
        Array<Room> candidates = new Array<>();
        for(int i = 0; i<map.getRoomMatrix().length;i++){
            for (int j = 0; j < map.getRoomMatrix().length; j++) {
                if(layout[i][j]) {
                    System.out.println(getNumberOfNeighbours(layout, true, i, j));
                    if (getNumberOfNeighbours(layout, true, i, j) == 1) {
                        candidates.add(map.getRoomMatrix()[i][j]);
                    }
                }
            }
        }
        if(candidates.size>0){
            System.out.println(candidates.size);
            int randomCandidate = Utils.mapRandomNumberGenerator.nextInt(candidates.size);
            candidates.get(randomCandidate).setBossRoom(true);
            candidates.get(randomCandidate).addGameObject(new EnormousGlutton(mapManager.getWorldUpdater().getObjectManager(),5,5));
        }
        //fill the rooms with random shit
        for (int i = 0; i < map.getRoomMatrix().length; i++) {
            for (int j = 0; j < map.getRoomMatrix().length; j++) {
                if(map.getRoomMatrix()[i][j]!=null && !map.getRoomMatrix()[i][j].isBossRoom()){
                    int numofmobs = Utils.mapRandomNumberGenerator.nextInt(10);
                    for (int k = 0; k < numofmobs; k++) {
                        int roll  = Utils.mapRandomNumberGenerator.nextInt(100);
                        Vector2 position = map.getRoomMatrix()[i][j].getMobSpawnPoints().get(Utils.mapRandomNumberGenerator.nextInt(map.getRoomMatrix()[i][j].getMobSpawnPoints().size));

                        if(roll>90) {
                            Box box = new Box(mapManager.getWorldUpdater().getObjectManager(), position.x, position.y);
                            ItemManager.addItemToEnemy(mapManager.getWorldUpdater().getObjectManager(),box,ItemManager.TYPE_RARE);
                            map.getRoomMatrix()[i][j].addGameObject(box);
                        }else {
                            roll = Utils.mapRandomNumberGenerator.nextInt(100);
                            if(roll>86) {
                                TheEye theEye = new TheEye(mapManager.getWorldUpdater().getObjectManager(), position.x, position.y);
                                if(Utils.mapRandomNumberGenerator.nextInt(100)>50){
                                    ItemManager.addItemToEnemy(mapManager.getWorldUpdater().getObjectManager(),theEye,ItemManager.TYPE_COMMON);
                                }
                                map.getRoomMatrix()[i][j].addGameObject(theEye);
                            }else {
                                MindlessWalker mindlessWalker = new MindlessWalker(mapManager.getWorldUpdater().getObjectManager(), position.x, position.y,1);
                                if(Utils.mapRandomNumberGenerator.nextInt(100)>50){
                                    ItemManager.addItemToEnemy(mapManager.getWorldUpdater().getObjectManager(),mindlessWalker,ItemManager.TYPE_COMMON);
                                }
                                map.getRoomMatrix()[i][j].addGameObject(mindlessWalker);
                            }
                        }
                    }
                }
            }
        }
        //set current room to the one in the center
        map.setCurrentRoom(map.getRoomMatrix()[4][4]);
        map.getCurrentRoom().addGameObject(new DamageUp(mapManager.getWorldUpdater().getObjectManager(), 4, 4));
        map.getCurrentRoom().addGameObject(new PiercingProjectiles(mapManager.getWorldUpdater().getObjectManager(),6,4));

        MapObjectParser.addMapObjectsToWorld(mapManager.getWorldUpdater(), map.getCurrentRoom());
        return map;
    }

    public static Boolean[][] obtainLayout(int dimensions, int maxRooms){
        int targetNumOfRooms = maxRooms;
        int currentNumOfRooms = 1;
        if(dimensions*dimensions<maxRooms){
            System.out.println("Dimensions of the room to small, setting them up to 9x9");
            dimensions = 9;
        }
        Boolean[][] roomLayout = new Boolean[dimensions][dimensions];
        //fill the 2d array with falses
        Utils.fill2dArray(roomLayout,false);
        //dead center of the room should be the starting room
        roomLayout[4][4] = true;

        while(currentNumOfRooms < targetNumOfRooms) {
            Array<Vector2> candidates = new Array<>();
            //get all candidate rooms
            for (int i = 0; i < roomLayout.length; i++) {
                for (int j = 0; j < roomLayout.length; j++) {
                    if (roomLayout[i][j] == true) {
                        candidates.add(new Vector2(i, j));
                    }
                }
            }
            //filter it to remove those containing more than 3 neighbours
            for(int i =0 ;i <candidates.size;i++){
                if (getNumberOfNeighbours(roomLayout,'R',(int)candidates.get(i).x,(int)candidates.get(i).y) > 1){
                    candidates.removeIndex(i);
                }
            }
            //get random candidate from the list
            int random = Utils.mapRandomNumberGenerator.nextInt(candidates.size);

            //make a room on a random free direction
            Vector2 chosenRoom = candidates.get(random).cpy();

            //clear the candidates array

            candidates.clear();
            //fill it again with candidates for room spawning
            if(chosenRoom.y-1 > 0 && !roomLayout[(int) chosenRoom.x][(int) chosenRoom.y - 1]){
                candidates.add(new Vector2(chosenRoom.x, chosenRoom.y - 1));

            }
            if(chosenRoom.y+1<roomLayout.length && !roomLayout[(int) chosenRoom.x][(int) chosenRoom.y + 1]){
                candidates.add(new Vector2(chosenRoom.x, chosenRoom.y + 1));

            }
            if(chosenRoom.x-1 > 0 && !roomLayout[(int) chosenRoom.x - 1][(int) chosenRoom.y]){
                candidates.add(new Vector2(chosenRoom.x - 1, chosenRoom.y));

            }
            if(chosenRoom.x+1<roomLayout.length && !roomLayout[(int) chosenRoom.x + 1][(int) chosenRoom.y]){
                candidates.add(new Vector2(chosenRoom.x + 1, chosenRoom.y));

            }
            //if no candidates found, repeat the whole process
            if(candidates.size==0){
                continue;
            }
            //filter the candidates and favor those with least number of neighborus (actually remove everything else )todo
            int smallest = 10;
            for(Vector2 vec : candidates){
                int num = getNumberOfNeighbours(roomLayout,true,(int)vec.x,(int)vec.y);
                if(num<smallest){
                    smallest = num;
                }
            }
            for (int i = 0; i < candidates.size; i++) {
                if(getNumberOfNeighbours(roomLayout,true,(int)candidates.get(i).x,(int)candidates.get(i).y) > smallest){
                    candidates.removeIndex(i);
                }
            }
            //pick a random candidate
            int randomCand = Utils.mapRandomNumberGenerator.nextInt(candidates.size);

            //turn it into a room
            roomLayout[(int)candidates.get(randomCand).x][(int)candidates.get(randomCand).y] = true;
            currentNumOfRooms++;

        }

        for (int i = 0; i < roomLayout.length; i++) {
            for (int j = 0; j < roomLayout.length; j++) {
                if(roomLayout[i][j]){
                    System.out.print("R ");
                }else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
        return roomLayout;
    }

    @Deprecated
    public static Map generateTestLevel(MapManager mapManager){
        Map map = new Map(mapManager,9);
        Room firstRoom = new Room((TiledMap) Assets.getAsset("maps//32maps//NESW.tmx", TiledMap.class));
        Room secondRoom = new Room((TiledMap) Assets.getAsset("maps//32maps//NESW.tmx", TiledMap.class));
        Room thirdRoom = new Room((TiledMap) Assets.getAsset("maps//32maps//NESW.tmx", TiledMap.class));
        Room fourthRoom = new Room((TiledMap) Assets.getAsset("maps//32maps//NESW.tmx", TiledMap.class));

        firstRoom.setLinkedRoomEast(secondRoom);
        secondRoom.setLinkedRoomWest(firstRoom);
        secondRoom.setLinkedRoomEast(thirdRoom);
        thirdRoom.setLinkedRoomWest(secondRoom);
        firstRoom.setLinkedRoomNorth(fourthRoom);
        fourthRoom.setLinkedRoomSouth(firstRoom);

        firstRoom.addGameObject(new MindlessWalker(mapManager.getWorldUpdater().getObjectManager(),4,4,1));

        map.setCurrentRoom(firstRoom);
        //initially add all the game objects manually as no references to managers exist yet.
        MapObjectParser.addMapObjectsToWorld(mapManager.getWorldUpdater(), firstRoom);
        return map;
    }

    /**
     *  GENERATOR TEST
     * @param args placeholder
     */
    public static void main(String[] args){
        Character[][] roomLayout = new Character[9][9];
        //fill the 2d array with falses
        Utils.fill2dArray(roomLayout,'-');
        //dead center of the room should be the starting room
        roomLayout[4][4] = 'R';
        int targetNumOfRooms = 15;
        int currentNumOfRooms = 1;

        while(currentNumOfRooms < targetNumOfRooms) {
            Array<Vector2> candidates = new Array<>();
            //get all candidate rooms
            for (int i = 0; i < roomLayout.length; i++) {
                for (int j = 0; j < roomLayout.length; j++) {
                    if (roomLayout[i][j] == 'R') {
                        candidates.add(new Vector2(i, j));
                    }
                }
            }
            //filter it to remove those containing more than 3 neighbours
            for(int i =0 ;i <candidates.size;i++){
                if (getNumberOfNeighbours(roomLayout,'R',(int)candidates.get(i).x,(int)candidates.get(i).y) > 3){
                    candidates.removeIndex(i);
                }
            }
            //get random candidate from the list
            int random = Utils.randomGenerator.nextInt(candidates.size);

            //make a room on a random free direction
            Vector2 chosenRoom = candidates.get(random).cpy();

            //clear the candidates array

            candidates.clear();
            //fill it again with candidates for room spawning
            if(chosenRoom.y-1 > 0 && roomLayout[(int)chosenRoom.x][(int)chosenRoom.y-1]=='-'){
                candidates.add(new Vector2(chosenRoom.x, chosenRoom.y-1));
            }
            if(chosenRoom.y+1<roomLayout.length && roomLayout[(int)chosenRoom.x][(int)chosenRoom.y+1]=='-'){
                candidates.add(new Vector2(chosenRoom.x, chosenRoom.y+1));
            }
            if(chosenRoom.x-1 > 0 && roomLayout[(int)chosenRoom.x-1][(int)chosenRoom.y]=='-'){
                candidates.add(new Vector2(chosenRoom.x-1, chosenRoom.y));
            }
            if(chosenRoom.x+1<roomLayout.length && roomLayout[(int)chosenRoom.x+1][(int)chosenRoom.y]=='-'){
                candidates.add(new Vector2(chosenRoom.x+1, chosenRoom.y));
            }
            //if no candidates found, repeat the whole process
            if(candidates.size==0){
                continue;
            }
            //pick a random candidate
            int randomCand = Utils.randomGenerator.nextInt(candidates.size);

            //turn it into a room
            roomLayout[(int)candidates.get(randomCand).x][(int)candidates.get(randomCand).y] = 'R';
            currentNumOfRooms++;

        }

        Utils.print2dArray(roomLayout);

    }

    public static <T> int getNumberOfNeighbours(T[][] matrix, T neighbourMark, int x, int y){
        int returnValue = 0;
        if(y-1 > 0 && matrix[x][y-1]==neighbourMark){
            returnValue++;
        }
        if(y+1<matrix.length && matrix[x][y+1]==neighbourMark){
            returnValue++;
        }
        if(x-1 > 0 && matrix[x-1][y]==neighbourMark){
            returnValue++;
        }
        if(x+1<matrix.length && matrix[x+1][y]==neighbourMark){
            returnValue++;
        }
        return returnValue;
    }
}
