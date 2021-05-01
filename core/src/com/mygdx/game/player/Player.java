package com.mygdx.game.player;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.player.PlayerInput.playerActions;
import com.mygdx.game.player.PlayerAttack.Attack;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/*
####################################################################################################
General Info :
Two types of "states", playerState and playerAnimation. playerState represents the state of the
player and restricts certain actions based on the state. playerAnimation represents the animation
the player is taking.
####################################################################################################
*/

public class Player {
    private enum playerState {
        NEUTRAL,
        INAIR,
        ATTACKGROUND,
        ATTACKAIR
    }
    private enum playerAnimationState {
        STANDING,
        WALKING,
        RUNNING,
        ATTACKBASIC,
        ATTACKAIR,
        ATTACKBASICUP,
        ATTACKAIRUP,
        JUMPING,
        FALLING
    }
    private enum direction {
        LEFT,
        RIGHT
    }

    // Player Information
    protected PlayerInput input = new PlayerInput();
    protected playerState state = playerState.NEUTRAL;
    protected playerAnimationState currAnimation = playerAnimationState.STANDING;
    protected playerAnimationState prevAnimation = playerAnimationState.STANDING;
    protected direction player_direction = direction.LEFT;
    protected float posX;
    protected float posY;
    protected float height = 50;
    protected float width = 50;
    protected float hitbox_height;
    protected float hitbox_width;
    protected int state_time = 0;
    protected PlayerAttack att;

    protected final float MODEL_SCALE_DIFFERENCE = 0.3f; // For scaling down hitbox from total animation size.
    protected final int FRAMES_PER_ANIMATION = 6;

    // Player Sprite Animations
    protected TextureAtlas playerSprites;
    protected PlayerAnimation playerAnimation;

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

    // Constructors
    public Player() {
        // Need a default player animation set (game will fail if called).
        posX = 0;
        posY = 0;
        hitbox_height = height*MODEL_SCALE_DIFFERENCE;
        hitbox_width = width*MODEL_SCALE_DIFFERENCE;
    }
    public Player(TextureAtlas sprites) {
        posX = 0;
        posY = 0;
        playerSprites = sprites;
        hitbox_height = height*MODEL_SCALE_DIFFERENCE;
        hitbox_width = width*MODEL_SCALE_DIFFERENCE;
    }
    public Player(float posX, float posY, TextureAtlas sprites) {
        this.posX = posX;
        this.posY = posY;
        playerSprites = sprites;
        hitbox_height = height*MODEL_SCALE_DIFFERENCE;
        hitbox_width = width*MODEL_SCALE_DIFFERENCE;
    }
    public Player(float posX, float posY, float height, float width, TextureAtlas sprites) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        playerSprites = sprites;
        hitbox_height = height*MODEL_SCALE_DIFFERENCE;
        hitbox_width = width*MODEL_SCALE_DIFFERENCE;
    }

    // Getters
    public float getPosX() { return posX; }
    public float getPosY() { return posY; }
    public float getHeight() { return height; }
    public float getWidth() { return width; }
    public float getHitboxHeight() { return hitbox_height; }
    public float getHitboxWidth() { return hitbox_width; }
    public float getJumpHeight() { return MAX_JUMP_HEIGHT; }
    public float getJumpTime() { return MAX_JUMP_TIME; }
    public float getAnimationDiff() { return MODEL_SCALE_DIFFERENCE; }

    // Setters
    public void setPosX(float x) { posX = x; }
    public void setPosY(float y) { posY = y; }
    public void setJumpVelocity(float v) { jumpVelocity = v; }
    public void setInAir(boolean bool) {
        if(isMidGroundAttack())
            return;
        else if(bool == false)
            state = playerState.NEUTRAL;
        else if(isMidAirAttack())
            return;
        else
            state = playerState.INAIR;
    }

    // Checking player states
    public boolean isInAir() { return (state == playerState.INAIR); }
    public boolean isNeutral() { return (state == playerState.NEUTRAL); }
    public boolean isMidGroundAttack() { return (state == playerState.ATTACKGROUND); }
    public boolean isMidAirAttack() { return (state == playerState.ATTACKAIR); }

    private void addToPosX(float x) { posX += x; }
    private void addToPosY(float y) { posY += y; }

    // Drawing Animations for the player.
    public void draw(Batch batch) {
        // Check to restart or continue animation.
        if(isSameAnimation())
            state_time++;
        else
            state_time = 0;

        // Get the correct animation for the action.
        Sprite[] animationInfo;
        switch(currAnimation) {
            case ATTACKBASIC:
                animationInfo = playerAnimation.attackAnimation;
                break;
            case ATTACKBASICUP:
                animationInfo = playerAnimation.attackUpAnimation;
                break;
            case ATTACKAIR:
                animationInfo = playerAnimation.attackAirAnimation;
                break;
            case ATTACKAIRUP:
                animationInfo = playerAnimation.attackAirUpAnimation;
                break;
            case RUNNING:
                animationInfo = playerAnimation.runAnimation;
                break;
            case JUMPING:
                animationInfo = playerAnimation.jumpAnimation;
                break;
            case FALLING:
                animationInfo = playerAnimation.fallAnimation;
                break;
            default:
                animationInfo = playerAnimation.neutralAnimation;
        }

        // Draw with correct frames.
        boolean flip = (player_direction == direction.RIGHT);
        int numOfFrames = animationInfo.length*FRAMES_PER_ANIMATION;
        int frame = state_time % numOfFrames;
        batch.draw(animationInfo[frame/FRAMES_PER_ANIMATION],
                flip ? getPosX() + width : getPosX(),
                getPosY(),
                flip ? -width : width,
                height);
        prevAnimation = currAnimation;
    }
    private boolean isSameAnimation() {
        if(prevAnimation == currAnimation)
            return true;
        return false;
    }

    /* #########################  Functions related to player attacks. ########################## */
    public void doAction(boolean[] in, float gravity) {
        playerActions action = input.getAction(in);
        switch(action) {
            case DONOTHING:
                doNothing();
                break;
            case JUMP:
                jump(gravity);
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
    private void doNothing() {
        // Called when player does no action.
        if(!isInAir() && !isMidGroundAttack() && !isMidAirAttack()) {
            currAnimation = playerAnimationState.STANDING;
        }
    }
    private void moveLeft() {
        // Called when player moves left.
        if(!isInAir() && !isMidAirAttack() && !isMidGroundAttack()) {
            currAnimation = playerAnimationState.RUNNING;
            runSpeed -= ACCELERATION_SPEED;
            player_direction = direction.LEFT;
            if(runSpeed < -MAX_RUN_SPEED)
                runSpeed = -MAX_RUN_SPEED;
        }
    }
    private void moveRight() {
        // Called when player moves right.
        if(!isInAir() && !isMidAirAttack() && !isMidGroundAttack()) {
            currAnimation = playerAnimationState.RUNNING;
            runSpeed += ACCELERATION_SPEED;
            player_direction = direction.RIGHT;
            if(runSpeed > MAX_RUN_SPEED) { runSpeed = MAX_RUN_SPEED; }
        }
    }
    private void jump(float gravity) {
        // Called when player jumps.
        if(!isInAir() && !isMidGroundAttack() && !isMidAirAttack()) {
            currAnimation = playerAnimationState.JUMPING;
            jumpVelocity = INITIAL_JUMP_VELOCITY + gravity;
            state = playerState.INAIR;
            posY++;        // Added to treat player an an object in the air.
        }
    }
    private void attack() {
        // Called when player attacks.
        if(!isMidGroundAttack() && !isMidAirAttack()) {
            if(isNeutral()) {
                currAnimation = playerAnimationState.ATTACKBASIC;
                att = new PlayerAttack(Attack.Basic, width, height);
                state = playerState.ATTACKGROUND;
            }
            else if(isInAir()) {
                currAnimation = playerAnimationState.ATTACKAIR;
                att = new PlayerAttack(Attack.BasicAir, width, height);
                state = playerState.ATTACKAIR;
            }
        }
    }
    private void attackUp() {
        // Called when player attacks upwards.
        if(!isMidGroundAttack() && !isMidAirAttack()) {
            if(isNeutral()) {
                currAnimation = playerAnimationState.ATTACKBASICUP;
                att = new PlayerAttack(Attack.BasicUp, width, height);
                state = playerState.ATTACKGROUND;
            }
            else if(isInAir()) {
                currAnimation = playerAnimationState.ATTACKAIRUP;
                att = new PlayerAttack(Attack.UpAir, width, height);
                state = playerState.ATTACKAIR;
            }
        }
    }

    public boolean canAttack() {
        if(!isMidAirAttack() && !isMidGroundAttack()) {
            return false;
        }
        else if(att.isFinalFrame() == true) {
            att = null;
            state = playerState.INAIR;
            return false;
        }
        return true;
    }
    public AttackFrame getAttackFrame() {
        boolean facingRight = player_direction == player_direction.RIGHT;
        AttackFrame hitboxData = att.getNext(width, facingRight);
        return hitboxData;
    }

    /* ###########################  Functions to update the player. ############################  */
    public void applyFalling(float newY) {
        if(newY > getPosY() && isInAir()) {
            currAnimation = playerAnimationState.FALLING;
        }
    }
    public void applyGravity(float g) {
        jumpVelocity += g;
        if(jumpVelocity > MAX_FALL_SPEED)
            posY += MAX_FALL_SPEED;
        else addToPosY(jumpVelocity);
    }
    public void applyMomentum() { // Applying Deceleration
        if(state != playerState.INAIR && state != playerState.ATTACKAIR ) {
            if (runSpeed > DECELERATION_SPEED)
                runSpeed -= DECELERATION_SPEED;
            else if (runSpeed < -DECELERATION_SPEED)
                runSpeed += DECELERATION_SPEED;
            else
                runSpeed = 0;
        }
        addToPosX(runSpeed);
    }

}