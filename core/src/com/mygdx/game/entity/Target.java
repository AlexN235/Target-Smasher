package com.mygdx.game.entity;

import java.lang.Math;

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

    public boolean targetHit(float hitPosX, float hitPosY, float size) {
        double radicand = Math.pow((hitPosX-this.x), 2) + Math.pow((hitPosY-this.y), 2);
        float distance = (float) Math.sqrt(radicand);

        if(distance < (size + this.size))
            return true;
        return false;
    }

}
