package com.mygdx.game.player;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import java.util.Iterator;

public class PlayerAnimation {

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

    public PlayerAnimation(TextureAtlas sprite) {
        playerSprites = sprite;
    };

    public PlayerAnimation(TextureAtlas sprite, Iterator<Integer> it) {
        playerSprites = sprite;
        neutralAnimation = new Sprite[it.next()];
        jumpAnimation = new Sprite[it.next()];
        fallAnimation = new Sprite[it.next()];
        walkAnimation = new Sprite[it.next()];
        runAnimation = new Sprite[it.next()];
        attackAnimation = new Sprite[it.next()];
        attackUpAnimation = new Sprite[it.next()];
        attackAirAnimation = new Sprite[it.next()];
        attackAirUpAnimation = new Sprite[it.next()];
        getAnimations();
    }

    // Getters
    public Sprite[] getNeutralAnimation() {
        return neutralAnimation;
    }
    public Sprite[] getJumpAnimation() {
        return jumpAnimation;
    }
    public Sprite[] getFallAnimation() {
        return fallAnimation;
    }
    public Sprite[] getWalkAnimation() {
        return walkAnimation;
    }
    public Sprite[] getRunAnimation() {
        return runAnimation;
    }
    public Sprite[] getAttackAnimation() {
        return attackAnimation;
    }
    public Sprite[] getAttackUpAnimation() {
        return attackUpAnimation;
    }
    public Sprite[] getAttackAirAnimation() {
        return attackAirAnimation;
    }
    public Sprite[] getAttackAirUpAnimation() {
        return attackAirUpAnimation;
    }

    private void getAnimations() {
        neutralAnimation[0] = playerSprites.createSprite("standing");
        fallAnimation[0] = playerSprites.createSprite("falling");
        jumpAnimation[0] = playerSprites.createSprite("jumping");
        for(int i=0;i<4;i++) {
            runAnimation[i] = playerSprites.createSprite("running" + (i+1));
        }
        for(int i=0;i<6;i++) {
            walkAnimation[i] = playerSprites.createSprite("walking" + (i+1));
        }
        for(int i=0;i<10;i++) {
            attackAnimation[i] = playerSprites.createSprite("slash" + (i+1));
        }
        for(int i=0;i<9;i++) {
            attackUpAnimation[i] = playerSprites.createSprite("upslash" + (i+1));
        }
        for(int i=0;i<6;i++) {
            attackAirAnimation[i] = playerSprites.createSprite("airslash" + (i+1));
        }
        for(int i=0;i<5;i++) {
            attackAirUpAnimation[i] = playerSprites.createSprite("airupslash" + (i+1));
        }
    }
}
