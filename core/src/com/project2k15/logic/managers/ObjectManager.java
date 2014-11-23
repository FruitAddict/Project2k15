
package com.project2k15.logic.managers;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.collision.QuadRectangle;
import com.project2k15.logic.collision.Quadtree;
import com.project2k15.logic.collision.RectangleTypes;
import com.project2k15.logic.entities.Player;
import com.project2k15.logic.entities.abstracted.Entity;
import com.project2k15.logic.maps.Map;
import com.project2k15.rendering.WorldRenderer;

/**
 * Object Manager
 */
public class ObjectManager implements RectangleTypes {
    /**
     * ObjectList stores entities (not terrain)
     * passRectangleList is reusable list that each entity uses to obtain its neighbours from the quadtree
     * collisionObjects is list of terrain rectangles that are injected into the quadtree.
     * QuadTree is just a regular quad tree data structure
     * Player is stored to re-add him after clearing the objectList.
     * Map references the map currently stored in the mapManager
     * WorldRenderer used to inform the map renderer about map/room change.
     */
    private Array<Entity> objectList = new Array<Entity>();
    private Array<PropertyRectangle> passRectangleList;
    private MapObjects collisionObjects;
    private Quadtree quadtree;
    private Player player;
    private Map storedMap;
    private WorldRenderer worldRenderer;

    public ObjectManager(){
        passRectangleList = new Array<PropertyRectangle>();
    }

    public void setMap(Map map){
        storedMap = map;
    }

    public void setWorldRenderer(WorldRenderer worldRenderer){
        this.worldRenderer = worldRenderer;
    }

    public void onRoomChanged(){
        /**
         * onRoomChanged method is used to change maps/rooms. Recreates the quadtree
         * with new map width and height and changes the terrain collision objects to new one.
         * Renderer is automatically working on the new map so no need to call it.
         */
        objectList.clear();
        collisionObjects = storedMap.getCurrentRoom().getTiledMap().getLayers().get("collisionObjects").getObjects();
        float mapWidth = storedMap.getCurrentRoom().getWidth();
        float mapHeight = storedMap.getCurrentRoom().getHeight();
        quadtree = new Quadtree(0,new QuadRectangle(0,0,(int)mapWidth,(int)mapHeight));
        worldRenderer.onMapChanged();
    }

    public void setPlayer(Player p) {
        /**
         * Changes the player reference and adds it to the list
         */
        objectList.removeValue(player,true);
        player = p;
        objectList.add(player);
    }


    public void update(float delta) {
        /**
         * ObjectManager update method
         * Clears the passRectangleList (just in case) and the quadtree
         * reinserts terrain collision rectangles into the quadtree with TERRAIN type
         * inserts all objects from objectlist (they store their own types already)
         * and finnaly updates all the entities on object list ( they draw themselves too atm so
         * object overlapping is determined by their position in the objectList).
         */
        passRectangleList.clear();
        quadtree.clear();

        for(int i =0; i< storedMap.getCurrentRoom().getPortalRecs().size;i++){
            quadtree.insert(storedMap.getCurrentRoom().getPortalRecs().get(i));
        }

        for (int i = 0; i < collisionObjects.getCount(); i++) {
            PropertyRectangle obj = new PropertyRectangle(((RectangleMapObject) collisionObjects.get(i)).getRectangle(), TERRAIN);
            quadtree.insert(obj);
        }

        for (int i = 0; i < objectList.size; i++) {
            quadtree.insert(objectList.get(i).getCollisionRectangle());
        }

        if (objectList.size > 0) {
            for (Entity o : objectList) {
                passRectangleList.clear();
                o.update(delta, quadtree.retrieve(passRectangleList, o.getCollisionRectangle()));
            }
        }
    }

    public void clear() {
        objectList.clear();
        addObject(player);
    }

    public Quadtree getTree() {
        return quadtree;
    }

    public boolean addObject(Entity obj) {
        if (objectList.size < 200) {
            objectList.add(obj);
            return true;
        }
        return false;
    }

    public void removeObject(Entity obj) {
        objectList.removeValue(obj, true);
    }

    public int getNumberOfObjects() {
        return objectList.size;
    }

}
