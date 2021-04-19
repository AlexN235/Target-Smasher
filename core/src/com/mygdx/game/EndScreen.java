package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class EndScreen extends ScreenAdapter {

    MainGame game;
    private float text_x;
    private float text_y ;
    private BitmapFont font;

    public EndScreen(MainGame game) {
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
        this.text_x = this.game.viewport.getWorldWidth()/6;
        this.text_y = this.game.viewport.getWorldHeight()/1.5f;
        font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float line_difference = this.game.viewport.getWorldHeight()/10;

        this.game.batch.begin();
        //this.game.batch.setProjectionMatrix(this.game.camera.combined);
        font.setColor(1, .5f, .5f, 1);
        font.draw(this.game.batch, "Game Ended", this.game.viewport.getWorldHeight()/2.5f, this.text_y);
        font.draw(this.game.batch, "Click on screen the to play again.", this.text_x, this.text_y-line_difference);
        this.game.batch.end();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
