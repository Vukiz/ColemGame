package com.crabcorp.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.crabcorp.GameWorld.GameRenderer;
import com.crabcorp.GameWorld.GameWorld;

public class GameScreen implements Screen {

    private GameWorld world;
    private GameRenderer renderer;

    private float runTime = 0;

    public GameScreen() {
        Gdx.app.log("GameScreen", "Attached");

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeigth = Gdx.graphics.getHeight();
        float gameWidth = screenWidth;
        float gameHeight = screenHeigth;


        world = new GameWorld(gameWidth,gameHeight);
        renderer = new GameRenderer(world,gameWidth,gameHeight);

        Gdx.input.setInputProcessor(renderer.getStage());
    }

    @Override
    public void render(float delta) {
        runTime +=delta;
        world.update(delta);
        renderer.render(runTime);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen", "show called");
    }

    @Override
    public void hide() {
        Gdx.app.log("GameScreen", "hide called");
    }

    @Override
    public void pause() {
        Gdx.app.log("GameScreen", "pause called");
    }

    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "resume called");
    }

    @Override
    public void dispose() {
        // Оставьте пустым
    }
}
