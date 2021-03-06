package com.mygdx.game.player;

import java.util.HashMap;

public class PlayerAttack {

    protected enum Attack {
        Basic,
        BasicUp,
        BasicAir,
        UpAir
    }

    private HashMap<Integer, AttackFrame> attTimeInfo = new HashMap<Integer, AttackFrame>();
    private Attack attack;
    private int totalAttackFrame;
    private int attackFrame;

    public PlayerAttack() {
        this.attack = Attack.Basic;
        this.attackFrame = 0;
        this.totalAttackFrame = 5;

        attTimeInfo.put(0 , new AttackFrame());
        attTimeInfo.put(1 , new AttackFrame());
        attTimeInfo.put(2 , new AttackFrame());
        attTimeInfo.put(3 , new AttackFrame());
        attTimeInfo.put(4 , new AttackFrame());
        attTimeInfo.put(5 , new AttackFrame());
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

    public AttackFrame getNext(float playerWidth, boolean facingRight) {
        this.attackFrame++;
        AttackFrame frameInfo = this.attTimeInfo.get(attackFrame);
        frameInfo.setX(facingRight ? frameInfo.getPosX()+playerWidth : -frameInfo.getPosX());
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
        this.totalAttackFrame = 30;

        for(int i=0;i<=totalAttackFrame;i++) {
            if(i<17)
                attTimeInfo.put(i, new AttackFrame());
            if(i>=17 && i<19)
                attTimeInfo.put(i , new AttackFrame(-0.2f*playerWidth, 1.4f*playerHeight, playerWidth*0.0001f));
            else
                attTimeInfo.put(i, new AttackFrame());
        }
    }
    private void basicUp(float playerWidth, float playerHeight) {
        this.attackFrame = 0;
        this.totalAttackFrame = 54;

        for(int i=0; i<=totalAttackFrame;i++) {
            if(i<13)
                attTimeInfo.put(i, new AttackFrame());
            else if(i>=13 &&i<25)
                attTimeInfo.put(i, new AttackFrame(-0.15f*playerWidth, playerHeight*2.0f, playerWidth*0.0001f));
            else
                attTimeInfo.put(i, new AttackFrame());
        }
    }

    private void basicAir(float playerWidth, float playerHeight) {
        this.attackFrame = 0;
        this.totalAttackFrame = 36;

        for(int i=0; i<=totalAttackFrame;i++) {
            if(i<18)
                attTimeInfo.put(i, new AttackFrame());
            else if(i>=18 && i<21)
                attTimeInfo.put(i, new AttackFrame(-0.3f*playerWidth, 0.6f*playerHeight, playerWidth*0.0001f));
            else
                attTimeInfo.put(i, new AttackFrame());
        }
    }

    private void upAir(float playerWidth, float playerHeight) {
        this.attackFrame = 0;
        this.totalAttackFrame = 30;

        for(int i=0; i<=totalAttackFrame;i++) {
            if(i<15)
                attTimeInfo.put(i, new AttackFrame());
            else if(i>=15 && i<18)
                attTimeInfo.put(i, new AttackFrame(-0.5f*playerWidth, 2.0f*playerHeight, playerWidth*0.0001f));
            else
                attTimeInfo.put(i, new AttackFrame());
        }
    }
}
