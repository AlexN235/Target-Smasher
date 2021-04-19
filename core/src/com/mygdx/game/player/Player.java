package com.mygdx.game.player;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.player.PlayerInput.playerActions;
import com.mygdx.game.player.PlayerAttack.Attack;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import javax.xml.soap.Text;

/*
####################################################################################################
TODO:
- Sound
####################################################################################################
*/

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
    private enum playerAnimation {
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
    private enum Direction {
        LEFT,
        RIGHT
    }

    // Player Information
    protected PlayerInput input = new PlayerInput();
    protected playerState state = playerState.NEUTRAL;
    protected playerAnimation currAnimation = playerAnimation.STANDING;
    protected playerAnimation prevAnimation = playerAnimation.STANDING;
    protected Direction player_direction = Direction.LEFT;
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
    protected Sprite[] neutralAnimation;
    protected Sprite[] jumpAnimation;
    protected Sprite[] fallAnimation;
    protected Sprite[] walkAnimation;
    protected Sprite[] runAnimation;
    protected Sprite[] attackAnimation;
    protected Sprite[] attackUpAnimation;
    protected Sprite[] attackAirAnimation;
    protected Sprite[] attackAirUpAnimation;

    // Player Jump Mechanic
    private final float MAX_JUMP_HEIGHT = this.height / 5;
    private final float MAX_JUMP_TIME = 6.5f;
    private final float INITIAL_JUMP_VELOCITY = 2*MAX_JUMP_TIME;
    private final float MAX_FALL_SPEED = this.height / 10;
    private float jumpVelocity = 0;

    // Player Running Mechanics
    private final float MAX_RUN_SPEED = 3.5f;
    private final float ACCELERATION_SPEED = 0.5f;
    private final float DECELERATION_SPEED = 0.2f;
    private float runSpeed = 0;

    // Constructors
    public Player() {
        // Need a default player animation set (game will fail if called).
        this.posX = 0;
        this.posY = 0;
        this.hitbox_height = this.height*MODEL_SCALE_DIFFERENCE;
        this.hitbox_width = this.width*MODEL_SCALE_DIFFERENCE;
    }
    public Player(TextureAtlas sprites) {
        this.posX = 0;
        this.posY = 0;
        this.playerSprites = sprites;
        this.hitbox_height = this.height*MODEL_SCALE_DIFFERENCE;
        this.hitbox_width = this.width*MODEL_SCALE_DIFFERENCE;
    }
    public Player(float posX, float posY, TextureAtlas sprites) {
        this.posX = posX;
        this.posY = posY;
        this.playerSprites = sprites;
        this.hitbox_height = this.height*MODEL_SCALE_DIFFERENCE;
        this.hitbox_width = this.width*MODEL_SCALE_DIFFERENCE;
    }
    public Player(float posX, float posY, float height, float width, TextureAtlas sprites) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.playerSprites = sprites;
        this.hitbox_height = this.height*MODEL_SCALE_DIFFERENCE;
        this.hitbox_width = this.width*MODEL_SCALE_DIFFERENCE;
    }

    // Getters
    public float getPosX() { return this.posX; }
    public float getPosY() { return this.posY; }
    public float getHeight() { return this.height; }
    public float getWidth() { return this.width; }
    public float getHitboxHeight() { return hitbox_height; }
    public float getHitboxWidth() { return hitbox_width; }
    public float getJumpHeight() { return MAX_JUMP_HEIGHT; }
    public float getJumpTime() { return MAX_JUMP_TIME; }
    public float getAnimationDiff() { return MODEL_SCALE_DIFFERENCE; }

    // Setters
    public void setPosX(float x) { this.posX = x; }
    public void setPosY(float y) { this.posY = y; }
    public void setJumpVelocity(float v) { this.jumpVelocity = v; }
    public void setInAir(boolean bool) {
        if(isMidGroundAttack())
            return;
        else if(bool == false)
            this.state = playerState.NEUTRAL;
        else if(isMidAirAttack())
            return;
        else
            this.state = playerState.INAIR;
    }

    // Checking player states
    public boolean isInAir() { return (state == playerState.INAIR); }
    public boolean isNeutral() { return (state == playerState.NEUTRAL); }
    public boolean isMidGroundAttack() { return (state == playerState.ATTACKGROUND); }
    public boolean isMidAirAttack() { return (state == playerState.ATTACKAIR); }

    private void addToPosX(float x) { this.posX += x; }
    private void addToPosY(float y) { this.posY += y; }

    // Drawing Animations for the player.
    public void draw(Batch batch) {
        // Check to restart or continue animation.
        if(isSameAnimation())
            this.state_time++;
        else
            state_time = 0;

        // Get the correct animation for the action.
        Sprite[] animationInfo;
        switch(currAnimation) {
            case ATTACKBASIC:
                animationInfo = attackAnimation;
                break;
            case ATTACKBASICUP:
                animationInfo = attackUpAnimation;
                break;
            case ATTACKAIR:
                animationInfo = attackAirAnimation;
                break;
            case ATTACKAIRUP:
                animationInfo = attackAirUpAnimation;
                break;
            case RUNNING:
                animationInfo = runAnimation;
                break;
            case JUMPING:
                animationInfo = jumpAnimation;
                break;
            case FALLING:
                animationInfo = fallAnimation;
                break;
            default:
                animationInfo = neutralAnimation;
        }

        // Draw with correct frames.
        boolean flip = (player_direction == Direction.RIGHT);
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
        playerActions action = this.input.getAction(in);
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
            currAnimation = playerAnimation.STANDING;
        }
    }
    private void moveLeft() {
        // Called when player moves left.
        if(!isInAir() && !isMidAirAttack() && !isMidGroundAttack()) {
            currAnimation = playerAnimation.RUNNING;
            this.runSpeed -= ACCELERATION_SPEED;
            this.player_direction = Direction.LEFT;
            if(this.runSpeed < -this.MAX_RUN_SPEED)
                this.runSpeed = -this.MAX_RUN_SPEED;
        }
    }
    private void moveRight() {
        // Called when player moves right.
        if(!isInAir() && !isMidAirAttack() && !isMidGroundAttack()) {
            currAnimation = playerAnimation.RUNNING;
            this.runSpeed += ACCELERATION_SPEED;
            this.player_direction = Direction.RIGHT;
            if(this.runSpeed > this.MAX_RUN_SPEED) { this.runSpeed = this.MAX_RUN_SPEED; }
        }
    }
    private void jump(float gravity) {
        // Called when player jumps.
        if(!isInAir() && !isMidGroundAttack() && !isMidAirAttack()) {
            currAnimation = playerAnimation.JUMPING;
            this.jumpVelocity = this.INITIAL_JUMP_VELOCITY + gravity;
            this.state = playerState.INAIR;
            this.posY++;        // Added to treat player an an object in the air.
        }
    }
    private void attack() {
        // Called when player attacks.
        if(!isMidGroundAttack() && !isMidAirAttack()) {
            if(isNeutral()) {
                currAnimation = playerAnimation.ATTACKBASIC;
                this.att = new PlayerAttack(Attack.Basic, this.width, this.height);
                this.state = playerState.ATTACKGROUND;
            }
            else if(isInAir()) {
                currAnimation = playerAnimation.ATTACKAIR;
                this.att = new PlayerAttack(Attack.BasicAir, this.width, this.height);
                this.state = playerState.ATTACKAIR;
            }
        }
    }
    private void attackUp() {
        // Called when player attacks upwards.
        if(!isMidGroundAttack() && !isMidAirAttack()) {
            if(isNeutral()) {
                currAnimation = playerAnimation.ATTACKBASICUP;
                this.att = new PlayerAttack(Attack.BasicUp, this.width, this.height);
                this.state = playerState.ATTACKGROUND;
            }
            else if(isInAir()) {
                currAnimation = playerAnimation.ATTACKAIRUP;
                this.att = new PlayerAttack(Attack.UpAir, this.width, this.height);
                this.state = playerState.ATTACKAIR;
            }
        }
    }

    public boolean canAttack() {
        if(!isMidAirAttack() && !isMidGroundAttack()) {
            return false;
        }
        else if(att.isFinalFrame() == true) {
            this.att = null;
            this.state = playerState.INAIR;
            return false;
        }
        return true;
    }
    public AttackFrame getAttackFrame() {
        boolean facingRight = this.player_direction == player_direction.RIGHT;
        AttackFrame hitboxData = this.att.getNext(this.width, facingRight);
        return hitboxData;
    }

    /* ###########################  Functions to update the player. ############################  */
    public void applyFalling(float newY) {
        if(newY > getPosY() && isInAir()) {
            currAnimation = playerAnimation.FALLING;
        }
    }
    public void applyGravity(float g) {
        this.jumpVelocity += g;

        if(this.jumpVelocity > this.MAX_FALL_SPEED)
            this.posY += this.MAX_FALL_SPEED;
        else addToPosY(this.jumpVelocity);
    }
    public void applyMomentum() { // Applying Deceleration
        if(this.state != playerState.INAIR && this.state != playerState.ATTACKAIR ) {
            if (this.runSpeed > this.DECELERATION_SPEED)
                this.runSpeed -= this.DECELERATION_SPEED;
            else if (this.runSpeed < -this.DECELERATION_SPEED)
                this.runSpeed += this.DECELERATION_SPEED;
            else
                this.runSpeed = 0;
        }
        addToPosX(this.runSpeed);
    }


}