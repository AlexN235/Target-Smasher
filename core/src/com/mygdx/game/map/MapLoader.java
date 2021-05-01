package com.mygdx.game.map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.map.blocks.Block;
import com.mygdx.game.map.entity.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MapLoader {
    public ArrayList<Block> blocks = new ArrayList<Block>();
    public ArrayList<Target> targets = new ArrayList<Target>();
    public HashMap<String, Sprite> mapSprites = new HashMap<String, Sprite>();

    public MapLoader(String filename, TextureAtlas sprites) throws FileNotFoundException {
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
                this.targets.add(new Target(height,8, x, y));
            else
                this.blocks.add(new Block(x, y, blockType, height, width));

            // save sprite names
            if(!texture.contains(blockType))
                texture.add(blockType);
        }

        for(String spriteName : texture) {
            this.mapSprites.put(spriteName, sprites.createSprite(spriteName));
        }
    }

    private static String[] getBlockInfo(String line)
    {
        return line.split(" ");
    }
}
