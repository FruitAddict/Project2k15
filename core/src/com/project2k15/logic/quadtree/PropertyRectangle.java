package com.project2k15.logic.quadtree;

import com.badlogic.gdx.math.Rectangle;

/**
 * Custom Rectangle, contains type for collision identification
 */
public class PropertyRectangle extends Rectangle {

    public static final int TERRAIN = 0;
    public static final int MOVING_OBJECT = 1;
    public static final int PROJECTILE = 2;
    public static final int PLAYER = 3;

    private int type;

    public PropertyRectangle(float x, float y, float width, float height, int type) {
        super(x, y, width, height);
        this.type = type;
    }

    public PropertyRectangle(Rectangle rec, int type) {
        super(rec);
        this.type = type;
    }

    public PropertyRectangle(int type) {
        super();
        this.type = type;
    }

    private PropertyRectangle() {
    }

    public int getType() {
        return type;
    }
}
