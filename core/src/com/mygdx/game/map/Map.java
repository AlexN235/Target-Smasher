package com.mygdx.game.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.map.entity.Target;
import com.mygdx.game.map.blocks.Block;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Map {

    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Target> targets = new ArrayList<Target>();
    private HashMap<String, Sprite> mapSprites = new HashMap<String, Sprite>();
    private ArrayList<Block> boundary = new ArrayList<Block>();

    public Map(String filename, TextureAtlas sprites) throws FileNotFoundException {
        MapLoader load = new MapLoader(filename, sprites);
        blocks = load.blocks;
        targets = load.targets;
        mapSprites = load.mapSprites;
    }

    public void setBoundary(float map_width, float map_height) {
        Block left_boundary = new Block(0, 0, "boundary", map_height, 1);
        Block right_boundary = new Block(map_width, 0, "boundary", map_height, 1);
        boundary.add(left_boundary);
        boundary.add(right_boundary);
    }

    public ArrayList<Block> getMap() {
        return blocks;
    }
    public ArrayList<Block> getBoundary() { return boundary; }
    public ArrayList<Target> getTargets() { return targets; }
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
    public boolean noTargetsLeft() {
        return this.targets.isEmpty();
    }
}
