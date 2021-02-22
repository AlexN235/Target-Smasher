package com.mygdx.game.map.blocks;

public class Block {
    private float x;
    private float y;
    private String type;

    final private float blockHeight = 10;
    final private float blockWidth = 10;

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

    // Getters
    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public String getType() {return this.type; }
    public float getBlockHeight() { return this.blockHeight; }
    public float getBlockWidth() { return this.blockWidth; }

    /*
    // Used to determine if player/entity could possibly interact/collide with the block.
    public boolean isEntityAtX(float xLeftSide, float xRightSide) {
        boolean playerInBlockRange = (xLeftSide <= this.x+this.blockWidth) && (xRightSide >= this.x);
        return playerInBlockRange;
    }
    public boolean isEntityAtY(float yBotSide, float yTopSide) {
        boolean playerInBlockRange = (yBotSide <= this.y+this.blockHeight) && (yTopSide >= this.y);
        return playerInBlockRange;
    }
    public boolean collideWithLeftSide(float currPos, float nextPos) {
        // Assumes the entity's Y position is in range of the block -- isEntityAtY is true.
        boolean collides = (currPos <= this.x) && (nextPos >= this.x);
        return collides;
    }
    public boolean collideWithRightSide(float currPos, float nextPos) {
        // Assumes the entity's Y position is in range of the block -- isEntityAtY is true.
        float blockRightSide = this.x+this.blockWidth;
        boolean collides = (currPos >= blockRightSide) && (nextPos < blockRightSide);
        return collides;
    }
    public boolean collideWithTopSide(float currPos, float nextPos) {
        // Assumes the entity's X position is in range of the block -- isEntityX is true.
        float blockTopSide = this.y+this.blockHeight;
        boolean collides = (currPos >= blockTopSide) && (nextPos < blockTopSide);
        return collides;
    }
    public boolean collideWithBotSide(float currPos, float nextPos) {
        // Assumes the entity's X position is in range of the block -- isEntityX is true.
        boolean collides = (currPos <= this.y) && (nextPos > this.y);
        return collides;
    }
    */
}
