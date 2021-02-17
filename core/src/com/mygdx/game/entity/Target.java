package com.mygdx.game.entity;

public class Target {
    private float size, x, y, hitboxRadius;

    public Target(float size, float posX, float posY) {
        this.size = size;
        this.hitboxRadius = size;
        this.x = posX;
        this.y = posY;
    }

    public Target(float size, float hitbox, float posX, float posY) {
        this.size = size;
        this.hitboxRadius = hitbox;
        this.x = posX;
        this.y = posY;
    }

    // Getters
    public float getSize() { return size;}
    public float getHitboxRadius() { return hitboxRadius; }
    public float getX() { return x; }
    public float getY() { return y; }

    // Setters
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

}
