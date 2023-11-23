package com.pedagoquest.game;

import com.badlogic.gdx.math.Rectangle;

public class Wall {
    private int x;
    private int y;
    private int width;
    private int height;
    private Rectangle bounds;


    public Wall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public Rectangle getBounds() {
        return bounds;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean contains(int x, int y) {
        return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
    }
}
