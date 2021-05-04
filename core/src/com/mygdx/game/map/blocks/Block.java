package com.mygdx.game.map.blocks;

public class Block {
    private float x;
    private float y;
    private String type;
    private float blockHeight = 50;
    private float blockWidth = 50;

    Block() {}

    Block(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Block(float x, float y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Block(float x, float y, String type, float height, float width) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.blockHeight = height;
        this.blockWidth = width;
    }

    // Getters
    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public String getType() {return this.type; }
    public float getBlockHeight() { return this.blockHeight; }
    public float getBlockWidth() { return this.blockWidth; }
    public float getLeftSideLocation() { return getX(); }
    public float getRightSideLocation() { return getX() + getBlockWidth(); }
    public float getTopSideLocation() { return getY() + getBlockHeight(); }
    public float getBotSideLocation() { return getY(); }

}
