package com.project2k15.logic.entities;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.entities.abstracted.MovableObject;

/**
 * Created by FruitAddict on 2014-11-17.
 */
public class CollisionResolver {

    public static boolean[] getCollisionTable(Array<PropertyRectangle> collisionRects, MovableObject obj, int type) {
        /**
         * This method checks for physical collisions
         * between this entity's collision rectangles and those passed as arguement.
         * returns a boolean table in the form [right,top,left,bottom], each indicating
         * if collision on this side happened.
         *
         */
        boolean[] result = new boolean[4];
        for (int i = 0; i < 4; i++) {
            result[i] = false;
        }

        if (obj.getCollisionRectangles().size > 0) {
            for (PropertyRectangle r : obj.getCollisionRectangles()) {
                for (PropertyRectangle cR : collisionRects) {
                    if (cR.getType() == type) {
                        Rectangle intersection = new Rectangle();
                        Intersector.intersectRectangles(r, cR, intersection);
                        if (intersection.width > 0 || intersection.height > 0) {
                            if (intersection.x > r.x) {
                                //Intersects with right side
                                result[0] = true;
                            }
                            if (intersection.y > r.y) {
                                //Intersects with top side
                                result[1] = true;
                            }
                            if (intersection.x + intersection.width < r.x + r.width) {
                                //Intersects with left side
                                result[2] = true;
                            }
                            if (intersection.y + intersection.height < r.y + r.height) {
                                //Intersects with bottom side
                                result[3] = true;
                            }
                        }
                    }

                }
            }
        }
        return result;
    }

    public static boolean resolveCollisionsWithTerrain(float delta, Array<PropertyRectangle> checkRectangles, MovableObject obj) {
        /**
         * Terrain Collision resolver method, takes delta time (time between frames) and array of rectangles to check
         * collisions with. Scales the velocity with delta time, copies the position of the player, moves the player
         * by the scaled velocity and then
         * determines how to resolve all the possible collision cases if any happened (by setting the players position to the
         * old position.)
         */
        Vector2 newVeloc = obj.getVelocity().cpy().scl(delta);
        Vector2 oldPosition = obj.getPosition().cpy();
        obj.getPosition().add(newVeloc);
        obj.getCollisionRectangles().get(0).setPosition(obj.getPosition().x, obj.getPosition().y);
        boolean[] collisions = getCollisionTable(checkRectangles, obj, PropertyRectangle.TERRAIN);

        boolean returnValue = false;
        for (int i = 0; i < collisions.length; i++) {
            if (collisions[i]) {
                returnValue = true;
            }
        }

        if (collisions[0] && collisions[1]) {
            //right-top
            if (obj.getVelocity().x > 0 && obj.getVelocity().y < 0) {
                obj.getVelocity().x = 0;
                obj.getPosition().x = oldPosition.x;
            } else if (obj.getVelocity().y > 0 && obj.getVelocity().x < 0) {
                obj.getVelocity().y = 0;
                obj.getPosition().y = oldPosition.y;
            } else {
                obj.getVelocity().x = 0;
                obj.getVelocity().y = 0;
                obj.setPosition(oldPosition);
            }
        } else if (collisions[0] && collisions[3]) {
            //right-bottom
            if (obj.getVelocity().x > 0 && obj.getVelocity().y > 0) {
                obj.getVelocity().x = 0;
                obj.getPosition().x = oldPosition.x;
            } else if (obj.getVelocity().y < 0 && obj.getVelocity().x < 0) {
                obj.getVelocity().y = 0;
                obj.getPosition().y = oldPosition.y;
            } else {
                obj.getVelocity().x = 0;
                obj.getVelocity().y = 0;
                obj.setPosition(oldPosition);
            }

        } else if (collisions[2] && collisions[1]) {
            //left-top
            if (obj.getVelocity().x < 0 && obj.getVelocity().y < 0) {
                obj.getVelocity().x = 0;
                obj.getPosition().x = oldPosition.x;
            } else if (obj.getVelocity().y > 0 && obj.getVelocity().x > 0) {
                obj.getVelocity().y = 0;
                obj.getPosition().y = oldPosition.y;
            } else {
                obj.getVelocity().x = 0;
                obj.getVelocity().y = 0;
                obj.setPosition(oldPosition);
            }
        } else if (collisions[2] && collisions[3]) {
            //left-bottom
            if (obj.getVelocity().x < 0 && obj.getVelocity().y > 0) {
                obj.getVelocity().x = 0;
                obj.getPosition().x = oldPosition.x;
            } else if (obj.getVelocity().y < 0 && obj.getVelocity().x > 0) {
                obj.getVelocity().y = 0;
                obj.getPosition().y = oldPosition.y;
            } else {
                obj.getVelocity().x = 0;
                obj.getVelocity().y = 0;
                obj.setPosition(oldPosition);
            }

        } else {
            if (collisions[0]) {
                //right
                obj.getPosition().x = oldPosition.x;
                obj.getVelocity().x = 0;
            }
            if (collisions[1]) {
                //top
                obj.getPosition().y = oldPosition.y;
                obj.getVelocity().y = 0;
            }
            if (collisions[2]) {
                //left
                obj.getPosition().x = oldPosition.x;
                obj.getVelocity().x = 0;
            }
            if (collisions[3]) {
                //bottom
                obj.getPosition().y = oldPosition.y;
                obj.getVelocity().y = 0;
            }
        }
        obj.getVelocity().scl(obj.getClamping());

        return returnValue;
    }

    public static boolean resolveCollisionsWithTerrainSimple(Array<PropertyRectangle> checkRectangles, MovableObject obj) {
        for (PropertyRectangle rec : checkRectangles) {
            if (rec.getType() == PropertyRectangle.TERRAIN) {
                if (obj.getCollisionRectangles().get(0).overlaps(rec)) {
                    return true;
                }
            }
        }
        return false;
    }
}
