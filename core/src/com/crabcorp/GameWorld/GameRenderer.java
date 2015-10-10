package com.crabcorp.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crabcorp.cgHelpers.AssetLoader;
import com.crabcorp.gameObjects.Castle;
import com.crabcorp.gameObjects.Knight;
import com.crabcorp.gameObjects.Unit;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class GameRenderer {

    private float screenHeight,screenWidth;

    private GameWorld mWorld;
    private LinkedList<Unit> mAlliesList,mEnemyList;

    private OrthographicCamera cam;
    private SpriteBatch batcher;
    private Stage stage;
    private Button buttonLeft;
    private Button buttonRight;

    public GameRenderer(GameWorld world,float width, float height) {
        mWorld = world;
        screenWidth = width;
        screenHeight = height;
        cam = new OrthographicCamera();
        cam.setToOrtho(true, screenWidth, screenHeight);

        Viewport viewport = new FitViewport(screenWidth,screenHeight,cam);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        initObjects(viewport);
    }

    public void render(float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawBackground();
        stage.draw();
        mAlliesList = mWorld.getAlliesList();
        mEnemyList = mWorld.getEnemyList();
        mAlliesList.getFirst().draw(batcher,runTime);
        mEnemyList.getFirst().draw(batcher,runTime);
        for(int i = 1 ; i < mAlliesList.size();i++){
            mAlliesList.get(i).draw(batcher,runTime);
        }
        for(int i = 1 ; i < mEnemyList.size();i++){
            mEnemyList.get(i).draw(batcher,runTime);
        }
    }


    private void drawBackground() {
        batcher.begin();
        batcher.draw(AssetLoader.backGround,0,0,screenWidth,screenHeight);
        batcher.end();
    }

    public void initObjects(Viewport v){

        mAlliesList = mWorld.getAlliesList();
        mEnemyList = mWorld.getEnemyList();

        initButton(v);
        stage = new Stage(v,batcher);
        stage.addActor(buttonLeft);
        stage.addActor(buttonRight);

    }

    private void initButton(final Viewport viewport) {
        buttonLeft = new Button(AssetLoader.buttonTexture);
        buttonRight = new Button(AssetLoader.buttonTexture);

        buttonRight.setHeight(100);
        buttonRight.setWidth(100);
        buttonRight.setPosition(150, screenHeight - 100);
        buttonRight.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mWorld.spawn(1,2);
                return true;
            }
        });
        buttonLeft.setHeight(100);
        buttonLeft.setWidth(100);
        buttonLeft.setPosition(0, screenHeight - 100);
        buttonLeft.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                mWorld.spawn(1, 1);
                return true;
            }
        });
    }

    public Stage getStage() {
        return this.stage;
    }
}