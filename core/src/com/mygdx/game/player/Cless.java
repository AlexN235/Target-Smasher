package com.mygdx.game.player;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Cless extends Player {
    Cless() {
        super();
    }
    public Cless(TextureAtlas sprites) {
        super(sprites);
        setAnimationFrames();
        getAnimations();
    }
    public Cless(float posX, float posY, TextureAtlas sprites) {
        super(posX, posY, sprites);
        setAnimationFrames();
        getAnimations();
    }
    public Cless(float posX, float posY, float height, float width, TextureAtlas sprites) {
        super(posX, posY, height, width, sprites);
        setAnimationFrames();
        getAnimations();
    }

    public void setAnimationFrames() {
        // Player Sprite Animations
        neutralAnimation = new Sprite[1];
        jumpAnimation = new Sprite[1];
        fallAnimation = new Sprite[1];
        walkAnimation = new Sprite[6];
        runAnimation = new Sprite[4];
        attackAnimation = new Sprite[10];
        attackUpAnimation = new Sprite[9];
        attackAirAnimation = new Sprite[6];
        attackAirUpAnimation = new Sprite[5];
    }

    protected void getAnimations() {
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
}
