package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class TitleScreen extends ScreenAdapter {
    MainGame game;
    private final Vector2 mouse_position = new Vector2();
    private final float button_height = 50;
    private final float button_width = 100;
    private final float button_x = 275;
    private final float button_y  = 275;

    public TitleScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    game.setScreen(new GameScreen(game));
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.57f, .77f, .85f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mouse_position.x = Gdx.input.getX();
        mouse_position.y = Gdx.input.getY();
        float world_height = this.game.viewport.getScreenHeight();
        boolean check_mouse_x = mouse_position.x > button_x && mouse_position.x < button_x+button_width;
        boolean check_mouse_y = (world_height-mouse_position.y) > button_y && (world_height-mouse_position.y) < button_y+button_height;
        if(check_mouse_x && check_mouse_y) {
            this.game.shape_renderer.begin(ShapeRenderer.ShapeType.Filled);
            this.game.shape_renderer.rect(button_x, button_y, button_width, button_height);
        }
        this.game.shape_renderer.end();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
