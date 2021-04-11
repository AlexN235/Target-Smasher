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
the player is taking. It is found combining the playerState with the playerAction.
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
    private PlayerInput input = new PlayerInput();
    private playerState state = playerState.NEUTRAL;
    private playerAnimation currAnimation = playerAnimation.STANDING;
    private playerAnimation prevAnimation = playerAnimation.STANDING;
    private Direction player_direction = Direction.LEFT;
    private float posX;
    private float posY;
    private float height = 50;
    private float width = 50;
    private float hitbox_height;
    private float hitbox_width;
    private int state_time = 0;
    private PlayerAttack att;

    private final float MODEL_SCALE_DIFFERENCE = 0.3f;
    private final int FRAMES_PER_ANIMATION = 6;

    // Player Sprite Animations
    private TextureAtlas playerSprites;
    private final Sprite[] neutralAnimation = new Sprite[1];
    private final Sprite[] jumpAnimation = new Sprite[1];
    private final Sprite[] fallAnimation = new Sprite[1];
    private final Sprite[] walkAnimation = new Sprite[6];
    private final Sprite[] runAnimation = new Sprite[4];
    private final Sprite[] attackAnimation = new Sprite[10];
    private final Sprite[] attackUpAnimation = new Sprite[9];
    private final Sprite[] attackAirAnimation = new Sprite[6];
    private final Sprite[] attackAirUpAnimation = new Sprite[5];

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
    public Player(TextureAtlas sprites) {
        this.posX = 0;
        this.posY = 0;
        this.playerSprites = sprites;
        this.hitbox_height = this.height*MODEL_SCALE_DIFFERENCE;
        this.hitbox_width = this.width*MODEL_SCALE_DIFFERENCE;
        getAnimations();
    }
    public Player(float posX, float posY, TextureAtlas sprites) {
        this.posX = posX;
        this.posY = posY;
        this.playerSprites = sprites;
        this.hitbox_height = this.height*MODEL_SCALE_DIFFERENCE;
        this.hitbox_width = this.width*MODEL_SCALE_DIFFERENCE;
        getAnimations();
    }
    public Player(float posX, float posY, float height, float width, TextureAtlas sprites) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.playerSprites = sprites;
        this.hitbox_height = this.height*MODEL_SCALE_DIFFERENCE;
        this.hitbox_width = this.width*MODEL_SCALE_DIFFERENCE;
        getAnimations();
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

    public void setJumpVelocity(float v) { this.jumpVelocity = v; }
    protected void addToPosX(float x) { this.posX += x; }
    protected void addToPosY(float y) { this.posY += y; }

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

    // Player Actions
    private void doNothing() {
        if(!isInAir() && !isMidGroundAttack() && !isMidAirAttack()) {
            currAnimation = playerAnimation.STANDING;
        }
    }
    private void moveLeft() {
        if(!isInAir() && !isMidAirAttack() && !isMidGroundAttack()) {
            currAnimation = playerAnimation.RUNNING;
            this.runSpeed -= ACCELERATION_SPEED;
            this.player_direction = Direction.LEFT;
            if(this.runSpeed < -this.MAX_RUN_SPEED)
                this.runSpeed = -this.MAX_RUN_SPEED;
        }
    }
    private void moveRight() {
        if(!isInAir() && !isMidAirAttack() && !isMidGroundAttack()) {
            currAnimation = playerAnimation.RUNNING;
            this.runSpeed += ACCELERATION_SPEED;
            this.player_direction = Direction.RIGHT;
            if(this.runSpeed > this.MAX_RUN_SPEED) { this.runSpeed = this.MAX_RUN_SPEED; }
        }
    }
    private void jump(float gravity) {
        if(!isInAir() && !isMidGroundAttack() && !isMidAirAttack()) {
            currAnimation = playerAnimation.JUMPING;
            jumpAction(gravity);
            this.posY++;        // Added to treat player an an object in the air.
        }
    }
    protected void jumpAction(float gravity) {
        this.jumpVelocity = this.INITIAL_JUMP_VELOCITY + gravity;
        this.state = playerState.INAIR;
        return;
    }
    private void attack() {
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

    // Functions to update the player.
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


    private void getAnimations() {
        neutralAnimation[0] = this.playerSprites.createSprite("standing");
        fallAnimation[0] = this.playerSprites.createSprite("falling");
        jumpAnimation[0] = this.playerSprites.createSprite("jumping");
        for(int i=0;i<4;i++) {
            runAnimation[i] = this.playerSprites.createSprite("running" + (i+1));
        }
        for(int i=0;i<6;i++) {
            walkAnimation[i] = this.playerSprites.createSprite("walking" + (i+1));
        }
        for(int i=0;i<10;i++) {
            attackAnimation[i] = this.playerSprites.createSprite("slash" + (i+1));
        }
        for(int i=0;i<9;i++) {
            attackUpAnimation[i] = this.playerSprites.createSprite("upslash" + (i+1));
        }
        for(int i=0;i<6;i++) {
            attackAirAnimation[i] = this.playerSprites.createSprite("airslash" + (i+1));
        }
        for(int i=0;i<5;i++) {
            attackAirUpAnimation[i] = this.playerSprites.createSprite("airupslash" + (i+1));
        }
    }
    private boolean isSameAnimation() {
        if(prevAnimation == currAnimation)
            return true;
        return false;
    }
}
