package com.mygdx.game.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PlayerAttack {
    private enum Attack {
        Basic
    }

    private HashMap<Integer, float[]> attTimeInfo = new HashMap<Integer, float[]>();
    private Attack attack;
    private float hitboxSize;
    private float attackFrame;
    private float totalAttackFrame;
    private float hitboxPosX, hitboxPosY;

    public PlayerAttack() {
        this.attack = Attack.Basic;
        this.attackFrame = 0;
        this.hitboxSize = 10;
        this.totalAttackFrame = 5;

        float[] val = {5, 15};
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
}
