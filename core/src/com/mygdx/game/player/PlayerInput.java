package com.mygdx.game.player;

public class PlayerInput {

    protected enum playerActions {    // A list of all actions the players could take.
        DONOTHING,
        MOVELEFT,
        MOVERIGHT,
        JUMP,
        ATTACK,
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
