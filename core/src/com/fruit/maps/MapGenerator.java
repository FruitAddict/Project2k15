package com.fruit.maps;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fruit.logic.objects.entities.enemies.MindlessWalker;
import com.fruit.utilities.MapObjectParser;
import com.fruit.utilities.Utils;
import com.fruit.visual.Assets;

/**
 * @Author FruitAddict
 */
public class MapGenerator {

    public static Map generateMap(MapManager mapManager,int circleLevel){
        Map map = new Map(mapManager);
        Boolean[][] layout = obtainLayout(10);
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout.length; j++) {
                if(layout[i][j]){
                   String roomType = "";
                    if(j-1 > 0 && layout[i][j-1]){
                        roomType+="N";
                    }
                    if(i+1 < layout.length && layout[i+1][j]){
                        roomType+="E";
                    }
                    if(j+1 < layout.length && layout[i][j+1]){
                        roomType+="S";
                    }
                    if(i-1 > 0 && layout[i-1][j]){
                        roomType+="W";
                    }
                }
            }
        }
        return map;
    }

    public static Boolean[][] obtainLayout(int maxRooms){
        int targetNumOfRooms = 10;
        int currentNumOfRooms = 1;

        Boolean[][] roomLayout = new Boolean[9][9];
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
            int random = Utils.randomGenerator.nextInt(candidates.size);

            //make a room on a random free direction
            Vector2 chosenRoom = candidates.get(random).cpy();

            //clear the candidates array

            candidates.clear();
            //fill it again with candidates for room spawning
            if(chosenRoom.y-1 > 0 && !roomLayout[(int) chosenRoom.x][(int) chosenRoom.y - 1]){
                candidates.add(new Vector2(chosenRoom.x, chosenRoom.y-1));
            }
            if(chosenRoom.y+1<roomLayout.length && !roomLayout[(int) chosenRoom.x][(int) chosenRoom.y + 1]){
                candidates.add(new Vector2(chosenRoom.x, chosenRoom.y+1));
            }
            if(chosenRoom.x-1 > 0 && !roomLayout[(int) chosenRoom.x - 1][(int) chosenRoom.y]){
                candidates.add(new Vector2(chosenRoom.x-1, chosenRoom.y));
            }
            if(chosenRoom.x+1<roomLayout.length && !roomLayout[(int) chosenRoom.x + 1][(int) chosenRoom.y]){
                candidates.add(new Vector2(chosenRoom.x+1, chosenRoom.y));
            }
            //if no candidates found, repeat the whole process
            if(candidates.size==0){
                continue;
            }
            //pick a random candidate
            int randomCand = Utils.randomGenerator.nextInt(candidates.size);

            //turn it into a room
            roomLayout[(int)candidates.get(randomCand).x][(int)candidates.get(randomCand).y] = true;
            currentNumOfRooms++;

        }

        Utils.print2dArray(roomLayout);
        return roomLayout;
    }

    public static Map generateTestLevel(MapManager mapManager){
        Map map = new Map(mapManager);
        Room firstRoom = new Room((TiledMap) Assets.getAsset("maps//Room1.tmx", TiledMap.class));
        Room secondRoom = new Room((TiledMap)Assets.getAsset("maps//Room2.tmx",TiledMap.class));
        Room thirdRoom = new Room((TiledMap)Assets.getAsset("maps//Room3.tmx",TiledMap.class));
        Room fourthRoom = new Room((TiledMap)Assets.getAsset("maps//Room4.tmx",TiledMap.class));
        map.addRoom(firstRoom);
        map.addRoom(secondRoom);
        map.addRoom(thirdRoom);
        map.addRoom(fourthRoom);

        firstRoom.setLinkedRoomEast(secondRoom);
        secondRoom.setLinkedRoomWest(firstRoom);
        secondRoom.setLinkedRoomEast(thirdRoom);
        thirdRoom.setLinkedRoomWest(secondRoom);
        firstRoom.setLinkedRoomNorth(fourthRoom);
        fourthRoom.setLinkedRoomSouth(firstRoom);

        firstRoom.addGameObject(new MindlessWalker(mapManager.getWorldUpdater().getObjectManager(),4,4));

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
        int targetNumOfRooms = 10;
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
                if (getNumberOfNeighbours(roomLayout,'R',(int)candidates.get(i).x,(int)candidates.get(i).y) > 1){
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
