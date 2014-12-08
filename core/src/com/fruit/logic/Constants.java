package com.fruit.logic;

public interface Constants {
    //converter constant. 64 pixels in the rendered world represent 1 meter in the logic module
    public final int PIXELS_TO_METERS = 64;
    //category bits for use with object filtering
    public final int TERRAIN_BIT = 0b1;
    public final int PLAYER_BIT = 0b10;
    public final int ENEMY_BIT = 0b100;
    public final int COLLECTIBLE_BIT = 0b1000;
    public final int CLUTTER_BIT = 0b10000;
    public final int PROJECTILE_BIT = 0b100000;
    public final int PORTAL_BIT = 0b1000000;
    //directions
    public final int NORTH_DIR = 1001;
    public final int EAST_DIR = 1002;
    public final int SOUTH_DIR = 1003;
    public final int WEST_DIR = 1004;
    //GroupID's for fixture-independent object filtering in room storage
    public final int NO_GROUP = 10000;
    public final int ENEMIES_GROUP = 10001;
    public final int CLUTTER_GROUP = 10002;
    public final int ITEM_GROUP = 10003;

    //object types for use with rendering, object renderer will use designated rendering pack
    //and obtain more information needed to draw given object from the casted object itself
    public final int PLAYER_TYPE = 101;
    public final int WALKER_TYPE = 102;
    public final int PROJECTILE_TYPE = 103;
    public final int CLUTTER_TYPE = 104;
    public final int DUMMY_TYPE = 105;
    public final int ITEM_TYPE = 106;
}
