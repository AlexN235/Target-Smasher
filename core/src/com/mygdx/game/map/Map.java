package com.mygdx.game.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.map.entity.Target;

/*
####################################################################################################
TODO:
 - Find a way to get the relative path (Andriod studios doesn't use it)


####################################################################################################
*/

import java.io.FileNotFoundException;
import java.util.ArrayList;
import com.mygdx.game.map.blocks.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.io.File;
//import com.mygdx.game.map.maps.*;

public class Map {
    // Potential have different types of blocks or blocks within a certain location to save time searching/rendering.
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Target> targets = new ArrayList<Target>();
    private HashMap<String, Sprite> mapSprites = new HashMap<String, Sprite>();

    private float sizeX;
    private float sizeY;

    public Map(String filename, TextureAtlas sprites) throws FileNotFoundException {
        loadMapResources(filename, sprites);
    }

    private void loadMapResources(String filename, TextureAtlas sprites) throws FileNotFoundException {
        // load block textures for the map.
        File file = new File(filename);
        Scanner scan = new Scanner(file);
        List<String> texture = new ArrayList<String>();
        while(scan.hasNextLine()) {
            // Save Blocks
            String[] blockInfo = getBlockInfo(scan.nextLine());
            float x = Float.parseFloat(blockInfo[0]);
            float y = Float.parseFloat(blockInfo[1]);
            float height = Float.parseFloat(blockInfo[2]);
            float width = Float.parseFloat(blockInfo[3]);
            String blockType = blockInfo[4];

            if(blockType.equals("target"))
                this.targets.add(new Target(height,8, x-200, y-30));
            else
                this.blocks.add(new Block(x, y, blockType, height, width));

            // save sprite names
            if(!texture.contains(blockType))
                texture.add(blockType);
        }

        // load sprites textures for the map.
        for(String spriteName : texture) {
            this.mapSprites.put(spriteName, sprites.createSprite(spriteName));
        }
    }

    public ArrayList<Block> getMap() {
        return blocks;
    }
    public ArrayList<Target> getTargets() { return targets; }
    public int getSize() {
        return blocks.size();
    }
    public void destroyTarget(Target target) {
        targets.remove(target);
    }

    public void draw(Batch batch) {
        drawBlocks(batch);
        drawEntity(batch);
    }
    private void drawBlocks(Batch batch) {
        for(Block blk : this.blocks) {
            Sprite sprite = getSprite(blk.getType());
            batch.draw(sprite, blk.getX(), blk.getY(), blk.getBlockWidth(), blk.getBlockHeight());
        }
    }
    private void drawEntity(Batch batch) {
        for(Target target : this.targets) {
            Sprite sprite = getSprite("target");
            batch.draw(sprite, target.getX(), target.getY(), target.getSize(), target.getSize());
        }
    }

    private Sprite getSprite(String name) {
        return mapSprites.get(name);
    }
    // Helper function
    private String[] getBlockInfo(String line)
    {
        return line.split(" ");
    }
}
