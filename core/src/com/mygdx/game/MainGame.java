package com.mygdx.game;

// gdx imports
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.Game;

// local class imports
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MainGame extends Game {

	// Renderer
	protected ShapeRenderer shape_renderer;
	protected SpriteBatch batch;
	protected TextureAtlas map_texture;
	protected TextureAtlas player_texture;

	// Camera
	protected OrthographicCamera camera;
	protected StretchViewport viewport;

	@Override
	public void create () {
		shape_renderer = new ShapeRenderer();
		batch = new SpriteBatch();

		// Map/Textures
		map_texture = new TextureAtlas(getSpriteDirectory("sprites.txt"));
		player_texture = new TextureAtlas(getSpriteDirectory("playersprites.txt"));

		// Camera
		camera = new OrthographicCamera(600, 600);
		viewport = new StretchViewport(600, 600, camera);
		setScreen(new TitleScreen(this));
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void dispose () {
		shape_renderer.dispose();
		map_texture.dispose();
		player_texture.dispose();
	}

	// Helper function : Get directory for files used by game.
	protected String getSpriteDirectory(String filename) {
		return System.getProperty("user.dir") + "\\" + filename;
	}
	protected String getMapDirectory(String filename) {
		String dir = System.getProperty("user.dir");
		String path = "/core/src/com/mygdx/game/map/maps/";
		return (dir.substring(0, dir.length()-15) + path + filename);
	}

}
