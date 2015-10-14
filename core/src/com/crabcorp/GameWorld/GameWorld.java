package com.crabcorp.GameWorld;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crabcorp.buttons.ColemButtons;
import com.crabcorp.cgHelpers.AssetLoader;
import com.crabcorp.gameObjects.Castle;
import com.crabcorp.gameObjects.Spawner;
import com.crabcorp.gameObjects.Unit;
import com.crabcorp.screens.GameScreen;

import java.util.LinkedList;

/**
 * Created by Евгений on 29.09.2015.
 */
public class GameWorld {
    private float screenWidth;
    private float screenHeight;

    private OrthographicCamera cam;
    private SpriteBatch batcher;
    private Stage stage;

    private Button buttonSpawnLeft;
    private Button buttonSpawnRight;
    private Button buttonRestart;

    private LinkedList<Unit> alliesList;
    private LinkedList<Unit> enemyList;
    private LinkedList<Unit> dieing;

    private Unit currentAlliesFront;
    private Unit currentEnemyFront;

    private Castle castleMine;
    private Castle castleEnemy;
    private float castleMinePosX;
    private float castleMinePosY;
    private float castleEnemyPosX;
    private float castleEnemyPosY;
    private String  output;

    private final float incomeTime = 5;
    private float timeSinceLastIncome = 0;

    private int allyGold = 10;
    private int enemyGold = 10;
    private int allyIncome = 10;
    private int enemyIncome = 10;

    public GameWorld(float width,float height){
        screenWidth = width;
        screenHeight = height;

        castleMinePosX = 0;
        castleMinePosY = screenHeight - 300;
        castleEnemyPosY = screenHeight - 300;
        castleEnemyPosX = screenWidth - 200;

        cam = new OrthographicCamera();
        cam.setToOrtho(true, screenWidth, screenHeight);
        Viewport viewport = new FitViewport(screenWidth,screenHeight,cam);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        StartGame(viewport);

    }
    //dependecy injection inversion of control
    //buttons class
    public void StartGame(Viewport viewport){
        initObjects(viewport);

        alliesList = new LinkedList<Unit>();
        enemyList = new LinkedList<Unit>();
        dieing = new LinkedList<Unit>();

        castleMine = Spawner.spawnCastle(castleMinePosX,castleMinePosY);
        castleEnemy = Spawner.spawnCastle(castleEnemyPosX,castleEnemyPosY);

        alliesList.add(castleMine);
        enemyList.add(castleEnemy);

        currentAlliesFront = alliesList.getFirst();     //target = castle
        currentEnemyFront = enemyList.getFirst();

        allyGold = 0;
        enemyGold = 0;

        output = new String("Game On");
    }
    public void update(float delta){
        goldIncome(delta);
        setFrontline();
        for (int i = 0; i<alliesList.size();i++) {
            alliesList.get(i).update(delta, currentEnemyFront);
        }
        for (int i = 0; i < enemyList.size();i++) {
            enemyList.get(i).update(delta, currentAlliesFront);
        }
        if(!dieing.isEmpty()){
            for (int i = 0; i < dieing.size();i++) {
                if(!dieing.get(i).isDieing()){
                    dieing.remove(i);
                }
            }
        }
    }

    private void goldIncome(float d) {
        timeSinceLastIncome +=d;

        while(timeSinceLastIncome>=incomeTime){
            allyGold += allyIncome;
            enemyGold += enemyIncome;
            timeSinceLastIncome -= incomeTime;
        }
    }

    private void setFrontline() {
        if (alliesList.isEmpty() || enemyList.isEmpty()) {
            output = "Victory";
            gameOver();
        }
        else {
            if (currentAlliesFront.isDead()) {
                dieing.add(currentAlliesFront);

                alliesList.remove(currentAlliesFront);
                if (!alliesList.isEmpty()) {
                    currentAlliesFront = (alliesList.size() == 1) ?
                            alliesList.get(0) : alliesList.get(1);
                } else {
                    currentAlliesFront = null;
                }
            }

            if (currentEnemyFront.isDead()) {
                dieing.add(currentEnemyFront);
                enemyList.remove(currentEnemyFront);
                if (!enemyList.isEmpty()) {
                    currentEnemyFront = enemyList.size() == 1?
                            enemyList.get(0):enemyList.get(1);
                } else {
                    currentEnemyFront = null;
                }
            }
        }
    }
    private void gameOver() {
        buttonSpawnLeft.clearListeners();
        buttonSpawnRight.clearListeners();

    }
    public void spawn(int spawnType,int spawnSide){
        switch (spawnSide){
            case 1:
                alliesList.add(Spawner.spawnKnight(castleMinePosX,castleMinePosY,false));   // castle size == 200.200
                if(currentAlliesFront == castleMine){
                    currentAlliesFront = alliesList.getLast();
                }
                break;
            case 2:
                enemyList.add(Spawner.spawnKnight(castleEnemyPosX,castleEnemyPosY,true)); //true == enemy
                if(currentEnemyFront == castleEnemy){
                    currentEnemyFront = enemyList.getLast();
                }
                break;
            default:break;

        }
    }



    //rendering
    public void render(float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawBackground();                               //BG and text

        stage.draw();                                   //BUTTONS
        for(int i = 0; i< dieing.size();i++){
            dieing.get(i).draw(batcher, runTime);
        }
        castleMine.draw(batcher, runTime);               //Castles
        castleEnemy.draw(batcher,runTime);
        for(int i = 1 ; i < alliesList.size();i++){  //UNITS FURTHER
            alliesList.get(i).draw(batcher, runTime);
        }
        for(int i = 1 ; i < enemyList.size();i++){
            enemyList.get(i).draw(batcher, runTime);
        }

    }
    private void drawBackground() {
        batcher.begin();

        batcher.draw(AssetLoader.background, 0, 0, screenWidth, screenHeight);

        AssetLoader.shadow.draw(batcher, "" + output, (screenWidth / 2)- 100, 50);
        AssetLoader.font.draw(batcher, "" + output, (screenWidth / 2) - 100, 50);

        AssetLoader.shadow.draw(batcher, "" + enemyGold, (screenWidth) - 150, 50);
        AssetLoader.font.draw(batcher, "" + enemyGold, (screenWidth) - 150, 50);

        AssetLoader.shadow.draw(batcher, "" + allyGold, 100, 50);
        AssetLoader.font.draw(batcher, "" + allyGold, 100, 50);

        /*

        if(currentEnemyFront != null) {
        AssetLoader.shadow.draw(batcher, "" + currentEnemyFront.getX(), (screenWidth) - 150, 50);
        AssetLoader.font.draw(batcher, "" + currentEnemyFront.getX(), (screenWidth) - 150, 50);
        }
        if(currentAlliesFront != null) {
        AssetLoader.shadow.draw(batcher, "" + currentAlliesFront.getX(), 100, 50);
        AssetLoader.font.draw(batcher, "" + currentAlliesFront.getX(), 100, 50);
        }

        *///Draw targets position
        batcher.end();
    }
    public void initObjects(Viewport v){
        initButton(v);
        stage = new Stage(v,batcher);
        stage.addActor(buttonSpawnLeft);
        stage.addActor(buttonSpawnRight);
        stage.addActor(buttonRestart);

    }
    private void initButton(final Viewport viewport) {
        buttonSpawnLeft = ColemButtons.createButton(0, screenHeight - 100, 100, 100,AssetLoader.buttonTexture);
        buttonSpawnRight = ColemButtons.createButton(150, screenHeight - 100, 100, 100,AssetLoader.buttonTexture);
        buttonRestart = ColemButtons.createButton(screenWidth/5,100,100,100,AssetLoader.buttonRestart);

        buttonRestart.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                StartGame(viewport);
                return true;
            }
        });

        buttonSpawnRight.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (enemyGold >= 10) {
                    spawn(1, 2);
                    enemyGold -= 10;                                             //TODO unit cost
                }
                return true;
            }
        });
        buttonSpawnLeft.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(allyGold >= 10) {
                    spawn(1, 1);
                    allyGold -= 10;
                }
                return true;
            }
        });
    }

    public Stage getStage() {
        return this.stage;
    }
}
