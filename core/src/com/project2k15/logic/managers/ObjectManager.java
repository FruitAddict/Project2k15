
package com.project2k15.logic.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
     * ObjectList stores entities that are not room-specific (like projectiles etc).
     * passRectangleList is reusable list that each entity uses to obtain its neighbours from the quadtree
     * collisionObjects is list of terrain rectangles that are injected into the quadtree.
     * QuadTree is just a regular quad tree data structure
     */
    private Array<Entity> objectList = new Array<Entity>();
    private Array<PropertyRectangle> passRectangleList;
    private Quadtree quadtree;
    private Controller controller;

    public void setController(Controller c){
        controller=c;
    }

    public ObjectManager(){
        passRectangleList = new Array<PropertyRectangle>();
    }

    public void onRoomChanged(){
        /**
         * onRoomChanged method is used to change maps/rooms. Recreates the quadtree
         * with new map width and height and changes the terrain collision objects to new one.
         * Renderer is automatically working on the new map so no need to call it.
         */
        objectList.clear();
        objectList.addAll(controller.getMapManager().getCurrentMap().getCurrentRoom().getGameObjectList());
        float mapWidth = controller.getMapManager().getCurrentMap().getCurrentRoom().getWidth();
        float mapHeight = controller.getMapManager().getCurrentMap().getCurrentRoom().getHeight();
        quadtree = new Quadtree(0,new QuadRectangle(0,0,(int)mapWidth,(int)mapHeight));
        controller.getWorldRenderer().onMapChanged();
        System.out.println("Number of objects in room: "+ controller.getMapManager().getCurrentMap().getCurrentRoom().getGameObjectList().size);
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
        for(int i =0; i< controller.getMapManager().getCurrentMap().getCurrentRoom().getPortalRecs().size;i++){
            quadtree.insert(controller.getMapManager().getCurrentMap().getCurrentRoom().getPortalRecs().get(i));
        }

        for(int i =0;i<controller.getMapManager().getCurrentMap().getCurrentRoom().getGameObjectList().size;i++){
            quadtree.insert(controller.getMapManager().getCurrentMap().getCurrentRoom().getGameObjectList().get(i).getCollisionRectangle());
        }

        for(PropertyRectangle rec : controller.getMapManager().getCurrentMap().getCurrentRoom().getTerrainCollisionRectangles()){
            quadtree.insert(rec);
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
        addObject(controller.getPlayer());
        objectList.addAll(controller.getMapManager().getCurrentMap().getCurrentRoom().getGameObjectList());
    }

    public Quadtree getTree() {
        return quadtree;
    }

    public boolean addObject(Entity obj) {
        if (objectList.size < 250) {
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
