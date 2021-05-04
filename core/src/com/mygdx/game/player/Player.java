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
    protected PlayerPosition playerPosition;
    protected PlayerAttack att;
    protected int stateTime = 0;

    protected playerState state = playerState.NEUTRAL;
    protected playerAnimationState currAnimation = playerAnimationState.STANDING;
    protected playerAnimationState prevAnimation = playerAnimationState.STANDING;
    protected direction playerDirection = direction.LEFT;

    protected final float MODEL_SCALE_DIFFERENCE = 0.3f; // For scaling down hitbox from total animation size.
    protected final int FRAMES_PER_ANIMATION = 6;

    // Player Sprite Animations
    protected TextureAtlas playerSprites;
    protected PlayerAnimation playerAnimation;

    // Constructors
    public Player() {
        // Need a default player animation set (game will fail if called).
    }
    public Player(TextureAtlas sprites) {
        playerPosition = new PlayerPosition(MODEL_SCALE_DIFFERENCE);
        playerSprites = sprites;
    }
    public Player(float posX, float posY, TextureAtlas sprites) {
        playerPosition = new PlayerPosition(posX, posY, MODEL_SCALE_DIFFERENCE);
        playerSprites = sprites;

    }
    public Player(float posX, float posY, float height, float width, TextureAtlas sprites) {
        playerPosition = new PlayerPosition(posX, posY, width, height, MODEL_SCALE_DIFFERENCE);
        playerSprites = sprites;
    }

    // Getters
    public float getPosX() { return playerPosition.getPosX(); }
    public float getPosY() { return playerPosition.getPosY(); }
    public float getHeight() { return playerPosition.getHeight(); }
    public float getWidth() { return playerPosition.getWidth(); }
    public float getHitboxHeight() { return playerPosition.getHitboxHeight(); }
    public float getHitboxWidth() { return playerPosition.getHitboxWidth(); }
    public float getJumpHeight() { return playerPosition.getMAX_JUMP_HEIGHT(); }
    public float getJumpTime() { return playerPosition.getMAX_JUMP_TIME(); }
    public float getAnimationDiff() { return MODEL_SCALE_DIFFERENCE; }

    // Setters
    public void setPosX(float x) {
        playerPosition.setPosX(x);
    }
    public void setPosY(float y) {
        playerPosition.setPosY(y);
    }
    public void setJumpVelocity(float v) {
        playerPosition.setJumpVelocity(0);
    }
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

    // Drawing Animations for the player.
    public void draw(Batch batch) {
        // Check to restart or continue animation.
        if(isSameAnimation())
            stateTime++;
        else
            stateTime = 0;

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
        boolean flip = (playerDirection == direction.RIGHT);
        int numOfFrames = animationInfo.length*FRAMES_PER_ANIMATION;
        int frame = stateTime % numOfFrames;
        batch.draw(animationInfo[frame/FRAMES_PER_ANIMATION],
                flip ? getPosX() + playerPosition.getWidth() : getPosX(),
                getPosY(),
                flip ? -playerPosition.getWidth() : playerPosition.getWidth(),
                playerPosition.getHeight());
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
            playerDirection = direction.LEFT;
            playerPosition.applyLeftMoveToPosition();
        }
    }
    private void moveRight() {
        // Called when player moves right.
        if(!isInAir() && !isMidAirAttack() && !isMidGroundAttack()) {
            currAnimation = playerAnimationState.RUNNING;
            playerDirection = direction.RIGHT;
            playerPosition.applyRightMoveToPosition();
        }
    }
    private void jump(float gravity) {
        // Called when player jumps.
        if(!isInAir() && !isMidGroundAttack() && !isMidAirAttack()) {
            currAnimation = playerAnimationState.JUMPING;
            state = playerState.INAIR;
            playerPosition.applyJumpToPosition(gravity);
        }
    }
    private void attack() {
        // Called when player attacks.
        if(!isMidGroundAttack() && !isMidAirAttack()) {
            if(isNeutral()) {
                currAnimation = playerAnimationState.ATTACKBASIC;
                att = new PlayerAttack(Attack.Basic, playerPosition.getWidth(), playerPosition.getHeight());
                state = playerState.ATTACKGROUND;
            }
            else if(isInAir()) {
                currAnimation = playerAnimationState.ATTACKAIR;
                att = new PlayerAttack(Attack.BasicAir, playerPosition.getWidth(), playerPosition.getHeight());
                state = playerState.ATTACKAIR;
            }
        }
    }
    private void attackUp() {
        // Called when player attacks upwards.
        if(!isMidGroundAttack() && !isMidAirAttack()) {
            if(isNeutral()) {
                currAnimation = playerAnimationState.ATTACKBASICUP;
                att = new PlayerAttack(Attack.BasicUp, playerPosition.getWidth(), playerPosition.getHeight());
                state = playerState.ATTACKGROUND;
            }
            else if(isInAir()) {
                currAnimation = playerAnimationState.ATTACKAIRUP;
                att = new PlayerAttack(Attack.UpAir, playerPosition.getWidth(), playerPosition.getHeight());
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
        boolean facingRight = playerDirection == playerDirection.RIGHT;
        AttackFrame hitboxData = att.getNext(playerPosition.getWidth(), facingRight);
        return hitboxData;
    }

    /* ###########################  Functions to update the player. ############################  */
    public void applyFalling(float newY) {
        if(newY > getPosY() && isInAir()) {
            currAnimation = playerAnimationState.FALLING;
        }
    }
    public void applyGravity(float g) {
        playerPosition.applyGravity(g);
    }
    public void applyMomentum() { // Applying Deceleration
        boolean changeRunSpeed = state!=Player.playerState.INAIR && state!=Player.playerState.ATTACKAIR;
        playerPosition.calculateAndApplyRunSpeed(changeRunSpeed);
    }

}