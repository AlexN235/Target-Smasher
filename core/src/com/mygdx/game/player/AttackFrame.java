package com.mygdx.game.player;

public class AttackFrame {
    private float posX;
    private float posY;
    private float size = 10; // default at size 10.
    private boolean emptyFrame = false;

    public AttackFrame() {
        emptyFrame = true;
    }

    public AttackFrame(float x, float y) {
        this.posX = x;
        this.posY = y;
    }
    public AttackFrame(float x, float y, float hitboxSize) {
        this.posX = x;
        this.posY = y;
        size = hitboxSize;
    }

    // Getter
    public float getPosX() {
        return posX;
    }
    public float getPosY() {
        return posY;
    }
    public float getSize() {
        return size;
    }

    // Setter
    public void setX(float x) {
        this.posX = x;
    }

    public boolean isEmptyFrame() {
        return this.emptyFrame;
    }
}
