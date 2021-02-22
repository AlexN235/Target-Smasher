package com.mygdx.game.player;

import com.mygdx.game.player.PlayerInput.playerActions;
import com.mygdx.game.player.PlayerAttack.Attack;

/*
####################################################################################################
TODO:
 Movement
 - Run
    - Walk.
 - Jump
    - double jump?
 - Attack
####################################################################################################
*/

public class Player {

    private enum playerState {      // A list of states the player could be in.
        NEUTRAL,
        INAIR,
        MIDATTACK
    }

    private enum Direction {
        LEFT,
        RIGHT
    }

    // Player Position and Dimensions
    private PlayerInput playerIn = new PlayerInput();
    private playerState state = playerState.NEUTRAL;
    private float posX;
    private float posY;
    private float height = 30;
    private float width = 15;
    private Direction playerDirection = Direction.RIGHT;

    // Player Jump Mechanic
    private final float MAX_JUMP_HEIGHT = 10;
    private final float MAX_JUMP_TIME = 6.5f;
    private final float INITIAL_JUMP_VELOCITY = 2*MAX_JUMP_TIME;
    private final float MAX_FALL_SPEED = 5;
    private float jumpVelocity = 0;
    private float gravity;

    // Player Running Mechanics
    private final float MAX_RUN_SPEED = 3.5f;
    private float runSpeed = 0;
    private final float ACCELERATION_SPEED = 0.5f;
    private final float DECELERATION_SPEED = 0.2f;

    private PlayerAttack att;

    // Constructors
    public Player() {
        this.posX = 0;
        this.posY = 0;
    }
    public Player(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
    }
    public Player(float posX, float posY, float height, float width) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
    }

    // Getters
    public float getPosX() { return this.posX; }
    public float getPosY() { return this.posY; }
    public float getHeight() { return this.height; }
    public float getWidth() { return this.width; }
    public float getJumpHeight() { return MAX_JUMP_HEIGHT; }
    public float getJumpTime() {return MAX_JUMP_TIME; }

    // Setters
    public void setPosX(float x) { this.posX = x; }
    public void setPosY(float y) { this.posY = y; }
    public void setInAir(boolean bool) {
        if (this.state == playerState.MIDATTACK)
            return;
        else if (bool == true) this.state = playerState.INAIR;
        else this.state = playerState.NEUTRAL;
    }
    public void setJumpVelocity(float v) { this.jumpVelocity = v; }
    public void setGravity(float g) { this.gravity = g; }

    protected void addToPosX(float x) {
        this.posX += x;
    }
    protected void addToPosY(float y) { this.posY += y; }

    // Checking player states
    public boolean isInAir() { return (state == playerState.INAIR); }
    public boolean isNeutral() { return (state == playerState.NEUTRAL); }
    public boolean isMidAttack() { return (state == playerState.MIDATTACK); }

    public void jump(float gravity) {
        // Need to check if jumping is a valiable option here OR in the playerAction function.
        this.jumpVelocity = this.INITIAL_JUMP_VELOCITY + gravity;
        this.state = playerState.INAIR;
        return;
    }

    public void doAction(boolean[] in) {
        playerActions action = this.playerIn.getAction(in);
        switch(action) {
            case DONOTHING:
                return;
            case JUMP:
                jump();
                break;
            case MOVELEFT:
                moveLeft();
                break;
            case MOVERIGHT:
                moveRight();
                break;
            case ATTACK:
                attack();
                break;
            case ATTACKUP:
                attackUp();
                break;
            default:
                return;
        }
        return;
    }

    // Player Actions
    private void moveLeft() {
        if(!isInAir() && !isMidAttack()) {
            this.runSpeed -= ACCELERATION_SPEED;
            this.playerDirection = Direction.LEFT;
            if(this.runSpeed < -this.MAX_RUN_SPEED) { this.runSpeed = -this.MAX_RUN_SPEED; }
        }
    }
    private void moveRight() {
        if(!isInAir() && !isMidAttack()) {
            this.runSpeed += ACCELERATION_SPEED;
            this.playerDirection = Direction.RIGHT;
            if(this.runSpeed > this.MAX_RUN_SPEED) { this.runSpeed = this.MAX_RUN_SPEED; }
        }
    }
    private void jump() {
        if(!this.isInAir() && !isMidAttack()) {
            jump(this.gravity);
            posY++;        // Added to treat player an an object in the air.
        }
    }
    private void attack() {
        if(this.state != playerState.MIDATTACK) {
            this.att = new PlayerAttack(Attack.Basic, this.width, this.height);
            this.state = playerState.MIDATTACK;
        }

    }
    private void attackUp() {
        if(this.state != playerState.MIDATTACK) {
            this.att = new PlayerAttack(Attack.BasicUp, this.width, this.height);
            this.state = playerState.MIDATTACK;
        }
    }

    // Functions to update the player.
    public void applyGravity(float g) {
        this.jumpVelocity += g;

        if(this.jumpVelocity > this.MAX_FALL_SPEED) this.posY += this.MAX_FALL_SPEED;
        else addToPosY(this.jumpVelocity);
    }
    public void applyMomentum() { // Applying Deceleration
        if(this.state != playerState.INAIR && this.state != playerState.MIDATTACK ) {
            if (this.runSpeed > this.DECELERATION_SPEED) { this.runSpeed -= this.DECELERATION_SPEED; }
            else if (this.runSpeed < -this.DECELERATION_SPEED) { this.runSpeed += this.DECELERATION_SPEED; }
            else { this.runSpeed = 0; }
        }

        addToPosX(this.runSpeed);
    }
    public boolean applyAttack() {
        //System.out.println(this.state);
        if(this.state != playerState.MIDATTACK)
            return false;
        else if(att.isFinalFrame() == true) {
            this.att = null;
            this.state = playerState.INAIR;
            return false;

        }
        return true;
    }
    public float[] getAttackFrame() {
        float[] hitboxData = this.att.getNext();
        hitboxData[0] = this.posX
                + ((this.playerDirection == playerDirection.RIGHT)
                ? (1*hitboxData[0])+this.width : (-1*hitboxData[0]));
        hitboxData[1] += this.posY;
        return hitboxData;
    }

}
