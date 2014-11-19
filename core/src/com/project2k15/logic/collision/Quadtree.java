package com.project2k15.logic.collision;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.project2k15.rendering.Assets;

/**
 * Quad tree to use with collision detection algorithm
 */
public class Quadtree {
    private int MAX_OBJECTS = 5;
    private int MAX_LEVELS = 10;

    private int level;
    private Array<PropertyRectangle> objects;
    private QuadRectangle bounds;
    private Quadtree[] nodes;

    public Quadtree(int pLevel, QuadRectangle pBounds) {
        level = pLevel;
        objects = new Array<PropertyRectangle>();
        bounds = pBounds;
        nodes = new Quadtree[4];
    }

    public void clear() {
        objects.clear();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    public void split() {
        int subWidth = bounds.getWidth() / 2;
        int subHeight = bounds.getHeight() / 2;
        int x = bounds.getX();
        int y = bounds.getY();
        //bottom left
        nodes[0] = new Quadtree(level + 1, new QuadRectangle(x, y, subWidth, subHeight));
        //bottom right
        nodes[1] = new Quadtree(level + 1, new QuadRectangle(x + subWidth, y, subWidth, subHeight));
        //top left
        nodes[2] = new Quadtree(level + 1, new QuadRectangle(x, y + subHeight, subWidth, subHeight));
        //top right
        nodes[3] = new Quadtree(level + 1, new QuadRectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    private int getIndex(Rectangle pRect) {
        int index = -1;
        double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

        // Object can completely fit within the top quadrants
        boolean topQuadrant = (pRect.getY() > horizontalMidpoint);
        // Object can completely fit within the bottom quadrants
        boolean bottomQuadrant = (pRect.getY() < horizontalMidpoint && pRect.getY() + pRect.getHeight() < horizontalMidpoint);

        // Object can completely fit within the left quadrants
        if (pRect.getX() < verticalMidpoint && pRect.getX() + pRect.getWidth() < verticalMidpoint) {
            if (topQuadrant) {
                index = 2;
            } else if (bottomQuadrant) {
                index = 0;
            }
        }
        // Object can completely fit within the right quadrants
        else if (pRect.getX() > verticalMidpoint) {
            if (topQuadrant) {
                index = 3;
            } else if (bottomQuadrant) {
                index = 1;
            }
        }

        return index;
    }

    public void insert(PropertyRectangle pRect) {
        if (nodes[0] != null) {
            int index = getIndex(pRect);

            if (index != -1) {
                nodes[index].insert(pRect);
                return;
            }
        }

        objects.add(pRect);

        if (objects.size > MAX_OBJECTS && level < MAX_LEVELS) {
            if (nodes[0] == null) {
                split();
            }
            int i = 0;
            while (i < objects.size) {
                int index = getIndex(objects.get(i));
                if (index != -1) {
                    nodes[index].insert(objects.removeIndex(i));
                } else {
                    i++;
                }
            }
        }
    }

    public Array<PropertyRectangle> retrieveByType(Array<PropertyRectangle> returnObjects, PropertyRectangle pRect, int type) {
        int index = getIndex(pRect);
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieveByType(returnObjects, pRect, type);
        }
        for (PropertyRectangle obj : objects) {
            if (obj.getType() == type) {
                returnObjects.add(obj);
            }
        }
        return returnObjects;
    }

    public Array<PropertyRectangle> retrieve(Array<PropertyRectangle> returnObjects, PropertyRectangle pRect) {
        int index = getIndex(pRect);
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieve(returnObjects, pRect);
        }
        returnObjects.addAll(objects);
        return returnObjects;
    }

    public void debugDraw(SpriteBatch renderer) {
        renderer.draw((Texture) Assets.manager.get("playersheet.png"), bounds.getX(), bounds.getY(), bounds.getWidth(), 1);
        renderer.draw((Texture) Assets.manager.get("playersheet.png"), bounds.getX(), bounds.getY(), 1, bounds.getY());
        renderer.draw((Texture) Assets.manager.get("playersheet.png"), bounds.getX(), bounds.getHeight() - 1, bounds.getX(), 1);
        renderer.draw((Texture) Assets.manager.get("playersheet.png"), bounds.getWidth() - 1, bounds.getY(), 1, bounds.getHeight());
        if (nodes[0] != null) {
            for (int i = 0; i < 4; i++) {
                nodes[i].debugDraw(renderer);
            }
        }
    }

}