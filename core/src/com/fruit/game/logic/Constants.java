package com.fruit.game.logic;

public interface Constants {
    public final int UNSPECIFIED = -1;
    //converter constant. 64 pixels in the rendered world represent 1 meter in the logic module
    public final int PIXELS_TO_UNITS = 64;
    //category bits for use with object filtering
    public final int TERRAIN_BIT = 0b1;
    public final int PLAYER_BIT = 0b10;
    public final int ENEMY_BIT = 0b100;
    public final int COLLECTIBLE_BIT = 0b1000;
    public final int CLUTTER_BIT = 0b10000;
    public final int PROJECTILE_BIT = 0b100000;
    public final int PORTAL_BIT = 0b1000000;
    public final int ITEM_BIT = 0b10000000;
    public final int TREASURE_BIT = 0b100000000;
    public final int PLAYER_PROJECTILE_BIT = 0b1000000000;
    public final int DETECTOR_BIT = 0b10000000000;
    public final int AREA_OF_EFFECT_BIT = 0b100000000000;
    //directions
    public final int NORTH_DIR = 1001;
    public final int EAST_DIR = 1002;
    public final int SOUTH_DIR = 1003;
    public final int WEST_DIR = 1004;
    //Room storage filtering ID's (whether a game object shold be saved in a room after leaving it or not)
    public final int DONT_SAVE = 10001;
    public final int DO_SAVE = 10002;

}
