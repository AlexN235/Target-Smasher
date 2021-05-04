package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class EndScreen extends ScreenAdapter {

    MainGame game;
    private float textX;
    private float textY ;
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
        this.textX = this.game.viewport.getWorldWidth()/6;
        this.textY = this.game.viewport.getWorldHeight()/1.5f;
        font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float line_difference = game.viewport.getWorldHeight()/10;

        game.batch.begin();
        //this.game.batch.setProjectionMatrix(this.game.camera.combined);
        font.setColor(1, .5f, .5f, 1);
        font.draw(game.batch, "Game Ended", this.game.viewport.getWorldHeight()/2.5f, this.textY);
        font.draw(game.batch, "Click on the screen to play again.", textX, this.textY-line_difference);
        game.batch.end();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
