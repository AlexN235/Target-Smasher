package com.mygdx.game.player;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Cless extends Player {

    public Cless() {
        super();
    }
    public Cless(TextureAtlas sprites) {
        super(sprites);
        setAnimationFrames(sprites, getClessAnimationFrameSizes());
    }
    public Cless(float posX, float posY, TextureAtlas sprites) {
        super(posX, posY, sprites);
        setAnimationFrames(sprites, getClessAnimationFrameSizes());
    }
    public Cless(float posX, float posY, float height, float width, TextureAtlas sprites) {
        super(posX, posY, height, width, sprites);
        setAnimationFrames(sprites, getClessAnimationFrameSizes());
    }

    public void setAnimationFrames(TextureAtlas sprite, ArrayList animationFrameSize) {
        // Player Sprite Animations
        Iterator<Integer> it = animationFrameSize.iterator();
        this.playerAnimation = new PlayerAnimation(sprite , it);
    }

    // Helper function
    private ArrayList<Integer> getClessAnimationFrameSizes() {
        ArrayList<Integer> frameSizes = new ArrayList<Integer>(Arrays.asList(1, 1, 1, 6, 4, 10, 9, 6, 5));
        return frameSizes;
    }
}
