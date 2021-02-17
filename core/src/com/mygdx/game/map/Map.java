package com.mygdx.game.map;

/*
####################################################################################################
TODO:
 - Find a way to get the relative path (Andriod studios doesn't use it)


####################################################################################################
*/

import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import com.mygdx.game.map.blocks.Block;
import java.util.Scanner;
import java.io.File;
import java.nio.file.*;
//import com.mygdx.game.map.maps.*;

public class Map {

    public static void main(String[] args) throws FileNotFoundException {
        // for testing.
        String dir = System.getProperty("user.dir");
        String path = "/core/src/com/mygdx/game/map/maps/";
        String name = "testmap1.txt";
        Map myMap = new Map(dir+path+name);

        System.out.println(myMap.blocks.get(5).getX());
    }
    // Potential have different types of blocks or blocks within a certain location to save time searching/rendering.
    private ArrayList<Block> blocks = new ArrayList<Block>();

    public Map(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scan = new Scanner(file);
        while(scan.hasNextLine()) {
            String[] blockInfo = getBlockInfo(scan.nextLine());

            float x = Float.parseFloat(blockInfo[0]);
            float y = Float.parseFloat(blockInfo[1]);
            String blockType = blockInfo[2];
            blocks.add(new Block(x, y, blockType));
        }
    }

    public ArrayList<Block> getMap() {
        return blocks;
    }

    public int getSize() {
        return blocks.size();
    }

    // Helper function
    private String[] getBlockInfo(String line)
    {
        return line.split(" ");
    }
}
