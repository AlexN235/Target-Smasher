package com.mygdx.game.player;

import com.mygdx.game.player.PlayerInput;
import com.mygdx.game.player.PlayerInput.playerActions;

/*
####################################################################################################
TODO:
 Movement
 - Run
    - Walk.
    - Slide.
    - Jump horizontal momentum.
 - Jump
    - No movement actions taken during jump (correlates to momentum)
    - double jump?
 - Attack
####################################################################################################
*/

public class Player {
    // Player Position and Dimensions
    private enum playerState {      // A list of states the player could be in.
        NEUTRAL,
        INAIR,
        KNOCKBACK,
        MIDATTACK
    }

    private PlayerInput playerIn = new PlayerInput();
    private playerState state = playerState.NEUTRAL;
    private float posX;
    private float posY;
    private float height = 30;
    private float width = 15;

    //private boolean inAir;    // Keeps track if the player is in the air.
    private float airTime = 0;  // Keeps track of the time a player has been in the air.

    // Player Jump Mechanic
    private final float MAX_JUMP_HEIGHT = 10;
    private final float MAX_JUMP_TIME = 6.5f;
    private final float INITIAL_JUMP_VELOCITY = 2*MAX_JUMP_TIME;
    private final float MAX_FALL_SPEED = 5;
    private float jumpVelocity = 0;
    private float gravity;

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
    public boolean isInAir() { return (state == playerState.INAIR); }
    public float getJumpHeight() { return MAX_JUMP_HEIGHT; }
    public float getJumpTime() {return MAX_JUMP_TIME; }

    public float getAirTime() { return this.airTime; }
    public float getJumpVelocity() { return jumpVelocity; }
    public float getInitialJumpVelocity() { return INITIAL_JUMP_VELOCITY; }


    // Setters
    public void setPosX(float x) { this.posX = x; }
    public void setPosY(float y) { this.posY = y; }
    public void setInAir(boolean bool) {
        if (bool == true) this.state = playerState.INAIR;
        else this.state = playerState.NEUTRAL;
    }
    public void setJumpVelocity(float v) { this.jumpVelocity = v; }
    public void setGravity(float g) { this.gravity = g; }
    private void setHeight(float height) { this.height = height; }
    private void setWidth(float width) { this.width = width; }
    public void setZeroAirTime() { this.airTime = 0; }
    public void increaseAirTime() { this.airTime++; }

    protected void addToPosX(float x) {
        this.posX += x;
    }
    protected void addToPosY(float y) {
        this.posY += y;
    }

    public void jump(float gravity) {
        // Need to check if jumping is a valiable option here OR in the playerAction function.
        this.jumpVelocity = this.INITIAL_JUMP_VELOCITY + gravity;
        this.state = playerState.INAIR;
        return;
    }

    public void printAction(boolean[] in) {     // Function for testing.
        System.out.println(this.playerIn.getAction(in));
    }

    public void doAction(boolean[] in) {
        playerActions action = this.playerIn.getAction(in);
        switch(action) {
            case DONOTHING:
                return;
            case JUMP:
                if(!this.isInAir()) {
                    jump(this.gravity);
                    posY++;        // Added to treat player an an object in the air.
                }
                break;
            case MOVELEFT:
                if(!this.isInAir()) {
                    this.posX += -3;
                }
                break;
            case MOVERIGHT:
                if(!this.isInAir()) {
                    this.posX += 3;
                }
                break;
            case ATTACK:
                break;
            case ATTACKUP:
                break;
            default:
                return;
        }
        return;
    }

    public void applyGravity(float g) {
        this.jumpVelocity = this.jumpVelocity + g;

        if(this.jumpVelocity > this.MAX_FALL_SPEED) this.posY += this.MAX_FALL_SPEED;
        else this.posY += this.jumpVelocity;
    }

    public void applyMomentum(float speed) {

    }



}
