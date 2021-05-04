package com.mygdx.game.player;

public class PlayerPosition {

    private float posX;
    private float posY;
    private float height = 50;
    private float width = 50;
    private float hitboxHeight;
    private float hitboxWidth;

    // Player Jump Mechanic
    private final float MAX_JUMP_HEIGHT = height / 5;
    private final float MAX_JUMP_TIME = 6.5f;
    private final float INITIAL_JUMP_VELOCITY = 2*MAX_JUMP_TIME;
    private final float MAX_FALL_SPEED = height / 10;
    private float jumpVelocity = 0;

    // Player Running Mechanics
    private final float MAX_RUN_SPEED = 3.5f;
    private final float ACCELERATION_SPEED = 0.5f;
    private final float DECELERATION_SPEED = 0.2f;
    private float runSpeed = 0;

    public PlayerPosition(float modelScale) {
        posX = 0;
        posY = 0;
        hitboxHeight = height*modelScale;
        hitboxWidth = width*modelScale;
    }
    public PlayerPosition(float x, float y, float modelScale) {
        posX = x;
        posY = y;
        hitboxHeight = height*modelScale;
        hitboxWidth = width*modelScale;
    }
    public PlayerPosition(float x, float y, float width, float height,float modelScale) {
        this.posX = x;
        this.posY = y;
        this.height = height;
        this.width = width;
        hitboxHeight = height * modelScale;
        hitboxWidth = width * modelScale;
    }

    // Getters
    public float getPosY() {
        return posY;
    }
    public float getPosX() {
        return posX;
    }
    public float getHeight() {
        return height;
    }
    public float getWidth() {
        return width;
    }
    public float getHitboxHeight() {
        return hitboxHeight;
    }
    public float getHitboxWidth() {
        return hitboxWidth;
    }
    public float getMAX_JUMP_HEIGHT() {
        return MAX_JUMP_HEIGHT;
    }
    public float getMAX_JUMP_TIME() {
        return MAX_JUMP_TIME;
    }

    // Setters
    public void setPosX(float x) { posX = x; }
    public void setPosY(float y) { posY = y; }
    public void setJumpVelocity(float v) { jumpVelocity = v; }

    public void applyLeftMoveToPosition() {
        runSpeed -= ACCELERATION_SPEED;
        if(runSpeed < -MAX_RUN_SPEED)
            runSpeed = -MAX_RUN_SPEED;
    }
    public void applyRightMoveToPosition() {
        runSpeed += ACCELERATION_SPEED;
        if(runSpeed > MAX_RUN_SPEED)
            runSpeed = MAX_RUN_SPEED;
    }
    public void applyJumpToPosition(float gravity) {
        jumpVelocity = INITIAL_JUMP_VELOCITY + gravity;
        posY++;        // Added to treat player an an object in the air.
    }

    public void applyGravity(float g) {
        jumpVelocity += g;
        if(jumpVelocity > MAX_FALL_SPEED)
            posY += MAX_FALL_SPEED;
        else this.posY += jumpVelocity;
    }
    public void calculateAndApplyRunSpeed(boolean changeRunSpeed) { // Applying Deceleration
        if(changeRunSpeed) {
            if (runSpeed > DECELERATION_SPEED)
                runSpeed -= DECELERATION_SPEED;
            else if (runSpeed < -DECELERATION_SPEED)
                runSpeed += DECELERATION_SPEED;
            else
                runSpeed = 0;
        }
        this.posX += runSpeed;
    }

}
