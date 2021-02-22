package com.mygdx.game.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PlayerAttack {
    protected enum Attack {
        Basic,
        BasicUp
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

        float[] val = {10, 15};
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
                System.out.println("Hello1");
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
        this.totalAttackFrame = 10;

        float[] val = {playerWidth, playerHeight*0.7f};
        float[] valZero = {0, 0};
        attTimeInfo.put(0 , val);
        attTimeInfo.put(1 , val);
        attTimeInfo.put(2 , val);
        attTimeInfo.put(3 , val);
        attTimeInfo.put(4 , val);
        attTimeInfo.put(5 , val);
        for(int i=5; i<totalAttackFrame+1;i++) {
            attTimeInfo.put(i, valZero);
        }
    }
    private void basicUp(float playerWidth, float playerHeight) {
        this.attackFrame = 0;
        this.hitboxSize = 10;
        this.totalAttackFrame = 15;

        float[] val = {0, playerHeight*1.3f};
        float[] valZero = {0, 0};
        attTimeInfo.put(0 , val);
        attTimeInfo.put(1 , val);
        attTimeInfo.put(2 , val);
        attTimeInfo.put(3 , val);
        attTimeInfo.put(4 , val);
        attTimeInfo.put(5 , val);
        for(int i=5; i<totalAttackFrame+1;i++) {
            attTimeInfo.put(i, valZero);
        }
    }
}
