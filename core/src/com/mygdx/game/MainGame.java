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
	ShapeRenderer shape_renderer;
	SpriteBatch batch;
	TextureAtlas map_texture;
	TextureAtlas player_texture;

	// Camera
	OrthographicCamera camera;
	StretchViewport viewport;

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
	protected String getSpriteDirectory(String sprite_file) {
		return System.getProperty("user.dir") + "\\" + sprite_file;
	}
	protected String getMapDirectory(String map_file) {
		String dir = System.getProperty("user.dir");
		String path = "/core/src/com/mygdx/game/map/maps/";
		return (dir.substring(0, dir.length()-15) + path + map_file);
	}

}
