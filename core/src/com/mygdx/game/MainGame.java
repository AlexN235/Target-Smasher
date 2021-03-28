package com.mygdx.game;

// gdx imports
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

// local class imports
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.blocks.Block;
import com.mygdx.game.entity.Target;
import com.mygdx.game.player.Player;
import com.mygdx.game.player.AttackFrame;

// regular imports
import java.io.FileNotFoundException;
import java.lang.Math;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class MainGame extends ApplicationAdapter {
	// Shape Renderer
	ShapeRenderer shapeRenderer;
	com.mygdx.game.player.Player player;

	// Sprite Renderer
	TextureAtlas mapTexture;
	TextureAtlas playerTexture;

	Sprite banana;
	Sprite standing;
	Sprite crate;
	Sprite orange;
	Sprite smashTarget;

	SpriteBatch batch;

	// Camera
	OrthographicCamera camera;
	ExtendViewport viewport;

	Random rand = new Random();
	int randDir;

	float GRAVITY_SPEED;

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

		// Camera
		camera = new OrthographicCamera(100, 100);
		viewport = new ExtendViewport(400, 400, camera);

		// Textures
		String dir = System.getProperty("user.dir") + "/";
		mapTexture = new TextureAtlas(dir+"sprites.txt");
		playerTexture = new TextureAtlas(dir+"playersprites.txt");
		crate = mapTexture.createSprite("crate");
		orange = mapTexture.createSprite("orange");
		smashTarget = mapTexture.createSprite("target");
		standing = playerTexture.createSprite("standing");
		standing.flip(true, false);
		batch = new SpriteBatch();

		player = new Player(300, 300, playerTexture);
		GRAVITY_SPEED = (-2*player.getJumpHeight()) / (float)Math.pow(player.getJumpTime() , 2);
	}


	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}


	@Override
	public void render () {
		player.setGravity(GRAVITY_SPEED); // figure out how to move this to outside.
		Gdx.gl.glClearColor(.57f, .77f, .85f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Keep track of the original position
		float preRenderPosX, preRenderPosY;
		preRenderPosX = player.getPosX();
		preRenderPosY = player.getPosY();

		// Gravity
		player.applyGravity(GRAVITY_SPEED);

		// Momentum
		player.applyMomentum();

		// Player Attack
		AttackFrame playerAttData = null;
		//System.out.println(player.applyAttack());
		if(player.applyAttack()) {
			playerAttData = player.getAttackFrame();
		}

		// Player Input
		boolean[] input = new boolean[8];
		if (Gdx.input.isKeyPressed(Input.Keys.W)){
			input[4] = true;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			input[5] = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)){
			input[6] = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)){
			input[7] = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.L)){
			input[3] = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.K)){
			input[2] = true;
		}

		player.doAction(input);
		player.applyFalling(preRenderPosY);

		// Checks if player hits a wall.
		for(Block blk : myMap.getMap()) {
			if(hitWallTop(preRenderPosX, preRenderPosY, player.getPosX(), player.getPosY(), blk)) {
				player.setPosY(blk.getY()+blk.getBlockHeight());
			}
			if(hitWallBot(preRenderPosX, preRenderPosY, player.getPosX(), player.getPosY(), blk)) {
				player.setPosY(blk.getY()-player.getHeight());
			}
			if(hitWallLeft(preRenderPosX, preRenderPosY, player.getPosX(), player.getPosY(), blk)) {
				player.setPosX(blk.getX()-player.getWidth());
			}
			if(hitWallRight(preRenderPosX, preRenderPosY, player.getPosX(), player.getPosY(), blk)) {
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
				else
					player.setInAir(true);
			}
		}

		// Render Player
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(1, 1, 1, .5f);

		batch.begin();
		// Player Attack
		if(playerAttData != null) {
			if(!playerAttData.isEmptyFrame()) {
				float hitboxX, hitboxY, hitboxSize;
				hitboxX = player.getPosX() + playerAttData.getPosX();
				hitboxY = player.getPosY() + playerAttData.getPosY();
				hitboxSize = playerAttData.getSize();

				List<Target> toRemove = new ArrayList<Target>();
				for(Target target : myMap.getTargets()) {
					if(target.targetHit(hitboxX, hitboxY, hitboxSize))
						toRemove.add(target);
				}
				for(Target target : toRemove) {
					myMap.destroyTarget(target);
				}
				batch.draw(orange, hitboxX, hitboxY, hitboxSize, hitboxSize);
			}
		}

		// Render Entities
		for(Target target : myMap.getTargets()) {
			shapeRenderer.setColor(1.0f, 0, 0, .5f);
			shapeRenderer.circle(target.getX(), target.getY(), target.getSize());
			batch.draw(smashTarget, target.getX(), target.getY(), target.getSize(), target.getSize());
		}

		// Render Map
		player.draw(batch);

		for(Block blk : myMap.getMap()) {
			batch.draw(crate, blk.getX(), blk.getY(), blk.getBlockWidth(), blk.getBlockHeight());
		}
		//shapeRenderer.rect(wallX, wallY, wallWidth, wallHeight);
		batch.end();
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
	private boolean playerInRange(float playerSideOne, float playerSideTwo, float sideOne, float sideTwo) {
		boolean inRange = playerSideTwo > sideOne && playerSideOne < sideTwo;
		return inRange;
	}


	// Helper function : Check if location is outside of the boundary
	private boolean outOfBoundary(float x, float y) {
		if(x > Gdx.graphics.getWidth() || x > Gdx.graphics.getHeight()) return true;
		else return x < 0 || y < 0;
	}

	@Override
	public void dispose () {
		shapeRenderer.dispose();
		mapTexture.dispose();
		playerTexture.dispose();
		//batch.dispose();
		//font.dispose();
	}
}
