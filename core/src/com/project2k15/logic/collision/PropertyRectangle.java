package com.project2k15.logic.collision;

import com.badlogic.gdx.math.Rectangle;
import com.project2k15.logic.entities.abstracted.Entity;

/**
 * Custom Rectangle, contains type for collision identification and owner
 */
public class PropertyRectangle extends Rectangle {

    public static final int NO_TYPE = 0;
    public static final int TERRAIN = 1;
    public static final int MOVING_OBJECT = 2;
    public static final int PROJECTILE = 3;
    public static final int CHARACTER = 4;
    public static final int PLAYER = 5;
    private Entity owner;

    private int type;

    public PropertyRectangle(float x, float y, float width, float height, int type) {
        super(x, y, width, height);
        this.type = type;
    }

    public PropertyRectangle(float x, float y, float width, float height, Entity owner, int type) {
        super(x, y, width, height);
        this.owner = owner;
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

    public Entity getOwner() {
        return owner;
    }
}
