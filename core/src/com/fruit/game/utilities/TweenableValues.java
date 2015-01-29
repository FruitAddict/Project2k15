package com.fruit.game.utilities;

/**
 * @author FruitAddict
 * Class with values and setters/getters. Used to tween things that needs
 * a dynamic base. (so tweened values can be used as an offset, for example
 * text being tweened ontop of the mobs)
 */
public class TweenableValues{
    private float x;
    private float y;
    private float z;
    private float alpha;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public String toString(){
        return x+" "+y+" "+z+" "+alpha;
    }
}
