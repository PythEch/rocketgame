package com.rocketfool.rocketgame;

/**
 * Created by pythech on 07/03/16.
 */
public abstract class Movable {
    protected int x;
    protected int y;
    protected int z;

    public Movable(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Movable(int x, int y) {
        this(x, y, 0);
    }

    public Movable() {
        this(0, 0, 0);
    }

    public void setLocation(int x, int y, int z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public void setLocation(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
