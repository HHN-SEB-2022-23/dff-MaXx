package de.hhn.lib;

public class Vector2D {
    int x;
    int y;

    public Vector2D (int x, int y) {
        this.y = y;
        this.x = x;

    }

    public int getX () {
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
}
