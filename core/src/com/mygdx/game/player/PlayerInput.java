package com.mygdx.game.player;

public class PlayerInput {

    public static void main(String[] args) {
        boolean[] in = new boolean[8];

        in[7] = true;

        PlayerInput p = new PlayerInput();
        System.out.println(p.boolToByte(in));
        System.out.println(p.getAction(in));
    }
    public enum playerActions {    // A list of all actions the players could take.
        DONOTHING,
        MOVELEFT,
        MOVERIGHT,
        JUMP,
        ATTACK,
        ATTACKLEFT,
        ATTACKRIGHT,
        ATTACKUP
    }

    public PlayerInput() {}

    private byte boolToByte(boolean[] input) {
        byte result = 0;
        for(int i=0; i<input.length; i++) {
            if(input[i])
                result |= (128 >> i);
        }
        return result;
    }

    public playerActions getAction(boolean[] bool) {
        byte bits = boolToByte(bool);
        if(bits >= 32) {        // player actions is attacking
            if((bits & 44) == 40)
                return playerActions.ATTACKUP;
            else
                return playerActions.ATTACK;
        }
        else if(bits >= 16) {   // player action is jumping
            return playerActions.JUMP;
        }
        else {                  // movement only actions
            if((bits & 3) == 2)
                return playerActions.MOVELEFT;
            else if((bits & 3) == 1)
                return playerActions.MOVERIGHT;
            else
                return playerActions.DONOTHING;
        }
    }
}
