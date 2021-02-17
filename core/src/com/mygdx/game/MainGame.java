package com.mygdx.game;

// gdx imports
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// local class imports
import com.mygdx.game.map.Map;
import com.mygdx.game.map.blocks.Block;
import com.mygdx.game.player.Player;

// regular imports
import java.io.FileNotFoundException;
import java.lang.Math;
import java.util.Random;

public class MainGame extends ApplicationAdapter {
	Random rand = new Random();
	int randDir;

	ShapeRenderer shapeRenderer;
	com.mygdx.game.player.Player player = new Player(300, 300, 30, 15);

	final float X_SPEED = 200;
	final float Y_SPEED = 400;

	final float GRAVITY_SPEED = (-2*player.getJumpHeight()) / (float)Math.pow(player.getJumpTime() , 2) ;

	String dir = System.getProperty("user.dir");
	String path = "/core/src/com/mygdx/game/map/maps/";
	String mapName = "testmap1.txt";
	final String mapPath = dir.substring(0, dir.length()-15) + path + mapName;

	com.mygdx.game.map.Map myMap;
	{
		try {
			myMap = new Map(mapPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
	}



	@Override
	public void render () {
		player.setGravity(GRAVITY_SPEED); // figure out how to move this to outside.
		Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Keep track of the original position
		float preRenderPosX, preRenderPosY, newPlayerPosX, newPlayerPosY;
		preRenderPosX = player.getPosX();
		preRenderPosY = player.getPosY();
		newPlayerPosX = player.getPosX();
		newPlayerPosY = player.getPosY();

		// Gravity
		player.applyGravity(GRAVITY_SPEED);

		// Player Input
		boolean[] input = new boolean[8];
		if (Gdx.input.isKeyPressed(Input.Keys.W)){
			/*
			if(!player.isInAir()) {
				player.jump(GRAVITY_SPEED);
				newPlayerPosY++;        // Added to treat player an an object in the air.
			}
			*/
			input[4] = true;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			//newPlayerPosY--;
			input[5] = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)){
			//newPlayerPosX += -3;
			input[6] = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)){
			//newPlayerPosX += 3;
			input[7] = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.L)){
			//newPlayerPosX += 3;
			input[3] = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.K)){
			//newPlayerPosX += 3;
			input[2] = true;
		}

		player.doAction(input);

		// Checks if player hits a wall.
		for(Block blk : myMap.getMap()) {
			if(hitWallTop(preRenderPosX, preRenderPosY, player.getPosX(), player.getPosY(), blk)) {
				player.setPosY(blk.getY()+blk.getBlockHeight());
			}
			if(hitWallTop(preRenderPosX, preRenderPosY, player.getPosX(), player.getPosY(), blk)) {
				player.setPosY(blk.getY()-player.getHeight());
			}
			if(hitWallTop(preRenderPosX, preRenderPosY, player.getPosX(), player.getPosY(), blk)) {
				player.setPosX(blk.getX()-player.getWidth());
			}
			if(hitWallTop(preRenderPosX, preRenderPosY, player.getPosX(), player.getPosY(), blk)) {
				player.setPosX(blk.getX()+blk.getBlockWidth());
			}
		}

		// Check if player is on the ground.
		for(Block blk : myMap.getMap()) {
			if (playerInRange(player.getPosX(), player.getPosX() + player.getWidth(), blk.getX(), blk.getX() + blk.getBlockWidth())) {
				if (player.getPosY() == blk.getY() + blk.getBlockHeight()) {
					player.setInAir(false);
					player.setJumpVelocity(0);
				}
			}
		}

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, .5f);
		shapeRenderer.rect(player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());

		// Render Map
		for(Block blk : myMap.getMap()) {
			shapeRenderer.rect(blk.getX(), blk.getY(), blk.getBlockWidth(), blk.getBlockHeight());
		}
		//shapeRenderer.rect(wallX, wallY, wallWidth, wallHeight);
		shapeRenderer.end();
	}

	// Helper function : Check if player hits a wall
	private boolean hitWallTop(float oldX, float oldY, float newX, float newY, Block block) {
		float wallTop = block.getY()+block.getBlockHeight();
		if(playerInRange(newX , newX+player.getWidth(), block.getX(), block.getX()+block.getBlockWidth())) {
			return oldY >= wallTop && newY < wallTop;
		}
		return false;
	}
	private boolean hitWallBot(float oldX, float oldY, float newX, float newY, Block block) {
		float playerTopOld = oldY + player.getHeight();
		float playerTopNew = newY + player.getHeight();
		float wallBot = block.getY();
		if(playerInRange(newX , newX+player.getWidth(), block.getX(), block.getX()+block.getBlockWidth())) {
			return playerTopOld <= wallBot && playerTopNew > wallBot;
		}
		return false;
	}
	private boolean hitWallLeft(float oldX, float oldY, float newX, float newY, Block block) {
		float playerFrontOld = oldX + player.getWidth();
		float playerFrontNew = newX + player.getWidth();
		float wallLeft = block.getX();
		if(playerInRange(newY , newY+player.getHeight(), block.getY(), block.getY()+block.getBlockHeight())) {
			return playerFrontOld <= wallLeft && playerFrontNew > wallLeft;
		}
		return false;
	}
	private boolean hitWallRight(float oldX, float oldY, float newX, float newY, Block block) {
		float wallRight = block.getX() + block.getBlockWidth();
		if(playerInRange(newY , newY+player.getHeight(), block.getY(), block.getY()+block.getBlockHeight())) {
			return oldX >= wallRight && newX < wallRight;
		}
		return false;
	}

	private float applyGravity() {
		return GRAVITY_SPEED;
	}

	private boolean playerInRange(float playerSideOne, float playerSideTwo, float sideOne, float sideTwo) {
		boolean inRange = playerSideTwo > sideOne && playerSideOne < sideTwo;
		return inRange;
	}

	// Helper function : Check if location is outside of the boundary
	private boolean outOfBoundary(float x, float y) {
		if(x > Gdx.graphics.getWidth() || x > Gdx.graphics.getHeight()) return true;
		else return x < 0 || y < 0;
	}

	// Helper function : Get a direction to move.
	private float[] getDir(){
		float x = 0;
		float y = 0;
		randDir = rand.nextInt(4);

		if(randDir==0){
			x = 1*X_SPEED*Gdx.graphics.getDeltaTime();
		}
		else if(randDir==1){
			y = 1*Y_SPEED*Gdx.graphics.getDeltaTime();
		}
		else if(randDir==2){
			x = -1*X_SPEED*Gdx.graphics.getDeltaTime();
		}
		else{
			y = -1*Y_SPEED*Gdx.graphics.getDeltaTime();
		}
		return new float[] {x, y};
	}

	@Override
	public void dispose () {
		shapeRenderer.dispose();
		//batch.dispose();
		//font.dispose();
	}
}
