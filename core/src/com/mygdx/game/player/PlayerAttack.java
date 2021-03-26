package com.mygdx.game.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PlayerAttack {
    protected enum Attack {
        Basic,
        BasicUp,
        BasicAir,
        UpAir
    }

    private HashMap<Integer, float[]> attTimeInfo = new HashMap<Integer, float[]>();
    private Attack attack;
    private float hitboxSize;
    private int attackFrame;
    private int totalAttackFrame;
    private float hitboxPosX, hitboxPosY;

    public PlayerAttack() {
        this.attack = Attack.Basic;
        this.attackFrame = 0;
        this.hitboxSize = 10;
        this.totalAttackFrame = 5;

        float[] val = {0, 0};
        attTimeInfo.put(0 , val);
        attTimeInfo.put(1 , val);
        attTimeInfo.put(2 , val);
        attTimeInfo.put(3 , val);
        attTimeInfo.put(4 , val);
        attTimeInfo.put(5 , val);

        float[] hitboxCord = attTimeInfo.get(0);
        this.hitboxPosX = hitboxCord[0];
        this.hitboxPosY = hitboxCord[1];
    }

    public PlayerAttack(Attack attackType, float playerWidth, float playerHeight) {
        this.attack = attackType;
        switch(attackType) {
            case Basic:
                basic(playerWidth, playerHeight);
                break;
            case BasicUp:
                basicUp(playerWidth, playerHeight);
                break;
            case BasicAir:
                basicAir(playerWidth, playerHeight);
                break;
            case UpAir:
                upAir(playerWidth, playerHeight);
                break;
            default:
                this.attack = null;
                return;
        }
    }

    // Getters
    public float getHitboxSize() { return this.hitboxSize; }
    public float getHitboxPosX() { return this.hitboxPosX; }
    public float getHitboxPosY() { return this.hitboxPosY; }

    public float[] getNext() {
        this.attackFrame++;
        float[] frameInfo = {this.attTimeInfo.get(attackFrame)[0],
                            this.attTimeInfo.get(attackFrame)[1],
                            this.hitboxSize};
        return frameInfo;
    }
    public boolean isFinalFrame() {
        return this.attackFrame >= this.totalAttackFrame;
    }

    // Attack Information
    /* #############################################################################################
    # Functions that are called when a certain attack is called. They contain "frame" specific data
    # for each different attack.
    # May be better to put this data in a seperate file.
    ################################################################################################
    */
    private void basic(float playerWidth, float playerHeight) {
        this.attackFrame = 0;
        this.hitboxSize = 10;
        this.totalAttackFrame = 30;

        float[] val = {-0.3f*playerWidth, 0.5f*playerHeight};
        float[] valZero = {0, 0};

        attTimeInfo.put(5 , val);
        attTimeInfo.put(6 , val);
        attTimeInfo.put(7 , val);
        attTimeInfo.put(8 , val);
        attTimeInfo.put(9 , val);
        attTimeInfo.put(10 , val);
        attTimeInfo.put(11 , val);
        attTimeInfo.put(12 , val);
        attTimeInfo.put(13 , val);
        attTimeInfo.put(14 , val);
        attTimeInfo.put(15 , val);
        attTimeInfo.put(16 , val);
        for(int i=0; i<5;i++) {
            attTimeInfo.put(i, valZero);
        }
        for(int i=16; i<totalAttackFrame+1;i++) {
            attTimeInfo.put(i, valZero);
        }
    }
    private void basicUp(float playerWidth, float playerHeight) {
        this.attackFrame = 0;
        this.hitboxSize = 10;
        this.totalAttackFrame = 54;

        float[] val = {0, playerHeight*1.3f};
        float[] valZero = {0, 0};
        attTimeInfo.put(0 , val);
        attTimeInfo.put(1 , val);
        attTimeInfo.put(2 , val);
        attTimeInfo.put(3 , val);
        attTimeInfo.put(4 , val);
        attTimeInfo.put(5 , val);
        attTimeInfo.put(6 , val);
        attTimeInfo.put(7 , val);
        attTimeInfo.put(8 , val);
        attTimeInfo.put(9 , val);
        attTimeInfo.put(10 , val);
        attTimeInfo.put(11 , val);
        for(int i=5; i<totalAttackFrame+1;i++) {
            attTimeInfo.put(i, valZero);
        }
    }

    private void basicAir(float playerWidth, float playerHeight) {
        this.attackFrame = 0;
        this.hitboxSize = 10;
        this.totalAttackFrame = 36;

        float[] val1 = {-0.3f*playerWidth, 0.5f*playerHeight};
        float[] val2 = {-0.3f*playerWidth, 0.3f*playerHeight};
        float[] valZero = {0, 0};
        attTimeInfo.put(10 , val1);
        attTimeInfo.put(11 , val1);
        attTimeInfo.put(12 , val1);
        attTimeInfo.put(13 , val1);
        attTimeInfo.put(14 , val1);
        attTimeInfo.put(15 , val1);
        attTimeInfo.put(16 , val1);
        attTimeInfo.put(17 , val1);
        attTimeInfo.put(18 , val2);
        attTimeInfo.put(19 , val2);
        attTimeInfo.put(20 , val2);
        attTimeInfo.put(21 , val2);
        attTimeInfo.put(22 , val2);
        attTimeInfo.put(23 , val2);
        attTimeInfo.put(24 , val2);
        for(int i=0; i<10+1;i++) {
            attTimeInfo.put(i, valZero);
        }
        for(int i=24; i<totalAttackFrame+1;i++) {
            attTimeInfo.put(i, valZero);
        }
    }

    private void upAir(float playerWidth, float playerHeight) {
        this.attackFrame = 0;
        this.hitboxSize = 10;
        this.totalAttackFrame = 30;

        float[] val = {-0.5f*playerWidth, 0.8f*playerHeight};
        float[] valZero = {0, 0};
        attTimeInfo.put(10 , val);
        attTimeInfo.put(11 , val);
        attTimeInfo.put(12 , val);
        attTimeInfo.put(13 , val);
        attTimeInfo.put(14 , val);
        attTimeInfo.put(15 , val);
        attTimeInfo.put(16 , val);
        attTimeInfo.put(17 , val);
        attTimeInfo.put(18 , val);
        attTimeInfo.put(19 , val);
        attTimeInfo.put(20 , val);
        attTimeInfo.put(21 , val);
        attTimeInfo.put(22 , val);
        attTimeInfo.put(23 , val);
        attTimeInfo.put(24 , val);
        for(int i=0; i<10;i++) {
            attTimeInfo.put(i, valZero);
        }
        for(int i=24; i<totalAttackFrame+1;i++) {
            attTimeInfo.put(i, valZero);
        }
    }
}
