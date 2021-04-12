package com.mygdx.game;

// gdx imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

// local class imports
import com.mygdx.game.map.Map;
import com.mygdx.game.map.blocks.Block;
import com.mygdx.game.map.entity.Target;
import com.mygdx.game.player.Player;
import com.mygdx.game.player.AttackFrame;

// regular imports
import java.io.FileNotFoundException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.Text;

public class GameScreen extends ScreenAdapter {
    MainGame game;

    private com.mygdx.game.map.Map map;
    private com.mygdx.game.player.Player player;
    private float map_gravity;

    private float map_height;
    private float map_width;

    private Texture background1;

    public GameScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        String map_directory = this.game.getMapDirectory("map1.txt");
        try {
            map = new Map(map_directory, this.game.map_texture);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Player
        player = new Player(300, 300, this.game.player_texture);
        map_gravity = (-2*player.getJumpHeight()) / (float)Math.pow(player.getJumpTime() , 2);

        // Background images
        background1 = new Texture(this.game.getSpriteDirectory("background1.png"));

        Gdx.graphics.setWindowedMode(800,640);
        map_height = this.game.viewport.getScreenHeight();
        map_width = this.game.viewport.getScreenWidth();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.57f, .77f, .85f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Keep track of the previous position
        float preRenderPosX, preRenderPosY;
        preRenderPosX = player.getPosX();
        preRenderPosY = player.getPosY();

        // Update player (gravity, momentum, air state)
        player.applyGravity(map_gravity);
        player.applyMomentum();
        player.applyFalling(preRenderPosY);

        // Player Input
        boolean[] input = new boolean[8];
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            input[4] = true;
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            input[5] = true;
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            input[6] = true;
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            input[7] = true;
        if (Gdx.input.isKeyPressed(Input.Keys.L))
            input[3] = true;
        if (Gdx.input.isKeyPressed(Input.Keys.K))
            input[2] = true;
        player.doAction(input, map_gravity);

        // Checks/Update player's action/affects on environment
        updatePlayerWallCollision(preRenderPosX, preRenderPosY);
        updatePlayerOnGround();
        updatePlayerAttacks();

        // Render Map/Player/Entities

        this.game.batch.begin();
        this.game.batch.draw(background1, 0.0f,0.0f, 800, 640);
        player.draw(this.game.batch);
        map.draw(this.game.batch);
        this.game.batch.end();
        this.game.shape_renderer.end();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    // Helper function : Check if location is outside of the boundary
    private boolean outOfBoundary(float x, float y) {
        if(x > Gdx.graphics.getWidth() || x > Gdx.graphics.getHeight()) return true;
        else return x < 0 || y < 0;
    }

    // Helper function : Checks if player is in range to be affected by a block/entity.
    private boolean playerInRange(float playerSideOne, float playerSideTwo, float sideOne, float sideTwo) {
        boolean inRange = (playerSideTwo > sideOne) && (playerSideOne < sideTwo);
        return inRange;
    }


    // Helper function : Check if player hits a wall
    private boolean hitTopWall(float oldY, float newX, float newY, Block block) {
        float wallTop = block.getTopSideLocation() - player.getHitboxHeight();
        float block_left_side = block.getLeftSideLocation() + player.getHitboxWidth();
        float block_right_side = block.getRightSideLocation() - player.getHitboxWidth();
        if(playerInRange(newX, newX+player.getWidth(), block_left_side, block_right_side)) {
            return oldY >= wallTop && newY < wallTop;
        }
        return false;
    }
    private boolean hitBotWall(float oldY, float newX, float newY, Block block) {
        float block_left_side = block.getLeftSideLocation() + player.getHitboxWidth();
        float block_right_side = block.getRightSideLocation() - player.getHitboxWidth();
        float wallBot = block.getY()-player.getHeight()+player.getHitboxHeight();
        if(playerInRange(newX , newX+player.getWidth(), block_left_side, block_right_side)) {
            return oldY <= wallBot && newY > wallBot;
        }
        return false;
    }
    private boolean hitLeftWall(float oldX, float newX, float newY, Block block) {
        float block_top = block.getTopSideLocation() - player.getHitboxHeight();
        float block_bot = block.getBotSideLocation() + player.getHitboxHeight();
        float wallLeft = block.getLeftSideLocation() - player.getWidth() + player.getHitboxWidth();
        if(playerInRange(newY, newY+player.getHeight(), block_bot, block_top)) {

            return oldX <= wallLeft && newX > wallLeft;
        }
        return false;
    }
    private boolean hitRightWall(float oldX, float newX, float newY, Block block) {
        float block_top = block.getTopSideLocation() - player.getHitboxHeight();
        float block_bot = block.getBotSideLocation() + player.getHitboxHeight();
        float wallRight = block.getRightSideLocation() - player.getHitboxWidth();
        if(playerInRange(newY, newY+player.getHeight(), block_bot, block_top)) {
            return oldX >= wallRight && newX < wallRight;
        }
        return false;
    }


    // Helper Function : update player based on wall collision
    private void updatePlayerWallCollision(float newX, float newY) {
        for(Block blk : map.getMap()) {
            if(hitTopWall(newY, player.getPosX(), player.getPosY(), blk)) {
                player.setPosY(blk.getTopSideLocation() - player.getHitboxHeight());
            }
            if(hitBotWall(newY, player.getPosX(), player.getPosY(), blk)) {
                player.setPosY(blk.getBotSideLocation()-player.getHeight()+player.getHitboxHeight());
            }
            if(hitLeftWall(newX, player.getPosX(), player.getPosY(), blk)) {
                player.setPosX(blk.getLeftSideLocation()-player.getWidth()+player.getHitboxWidth());
            }
            if(hitRightWall(newX, player.getPosX(), player.getPosY(), blk)) {
                player.setPosX(blk.getRightSideLocation() - player.getHitboxWidth());
            }
        }
    }


    // Helper Function : update player based on whether player is on the ground or air.
    private void updatePlayerOnGround() {
        float player_hitboxX = player.getPosX();
        float player_hitboxY = player.getPosY() + player.getHitboxHeight();
        for(Block blk : map.getMap()) {
            if (playerInRange(player_hitboxX, player_hitboxX + player.getWidth(), blk.getLeftSideLocation(), blk.getRightSideLocation())) {
                if (player_hitboxY == blk.getTopSideLocation()) {
                    player.setInAir(false);
                    player.setJumpVelocity(0);
                    break;
                }
                else
                    player.setInAir(true);
            }
        }
    }


    // Helper Function : update player/map based on player attack actions.
    private void updatePlayerAttacks() {
        float hitboxX, hitboxY, hitboxSize;
        AttackFrame playerAttData = null;
        if(player.canAttack())
            playerAttData = player.getAttackFrame();
        if(playerAttData != null) {
            if(!playerAttData.isEmptyFrame()) {
                hitboxX = player.getPosX() + playerAttData.getPosX();
                hitboxY = player.getPosY() + playerAttData.getPosY()*player.getAnimationDiff();
                hitboxSize = playerAttData.getSize();

                List<Target> toRemove = new ArrayList<Target>();
                for(Target target : map.getTargets()) {
                    if(target.targetHit(hitboxX, hitboxY, hitboxSize))
                        toRemove.add(target);
                }
                for(Target target : toRemove)
                    map.destroyTarget(target);
            }
        }
    }
}
