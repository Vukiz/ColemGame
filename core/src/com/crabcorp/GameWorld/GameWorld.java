package com.crabcorp.GameWorld;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crabcorp.buttons.ColemButtons;
import com.crabcorp.cgHelpers.AssetLoader;
import com.crabcorp.colemgame.ColemGame;
import com.crabcorp.gameObjects.Castle;
import com.crabcorp.gameObjects.Spawner;
import com.crabcorp.gameObjects.Unit;
import com.crabcorp.screens.GameScreen;

import java.util.LinkedList;

/**
 * Created by Евгений on 29.09.2015.
 */
public class GameWorld {
    //screen
    private float screenWidth;
    private float screenHeight;

    private OrthographicCamera cam;
    private SpriteBatch batcher;
    private Stage stage;

    private Button buttonSpawnLeft;
    private Button buttonSpawnRight;
    private Button buttonRestart;
    //units
    private LinkedList<Unit> alliesList;
    private LinkedList<Unit> enemyList;
    private LinkedList<Unit> dieing;
    private Unit currentAlliesFront;
    private Unit currentEnemyFront;
    private Castle castleMine;
    private Castle castleEnemy;
    private final float castleMinePosX;
    private final float castleMinePosY;
    private final float castleEnemyPosX;
    private final float castleEnemyPosY;
    private int castleWidth;
    private int castleHeight;
    //time & income
    private final float incomeTime = 4;
    private float timeSinceLastIncome = 0;
    private float AImoveCD = 0;
    private long btnCooldown = 0;

    private int allyGold = 10;
    private int enemyGold = 10;
    private int allyIncome = 10;
    private int enemyIncome = 10;


    private String output;
    private boolean gameisOn = true;


    public GameWorld(float width, float height) {
        screenWidth = width;
        screenHeight = height;
        //TODO SCALE UNITS SIZE BY PERCENTAGES
        castleWidth = (int)screenWidth / 10;
        castleHeight = (int)screenHeight / 4;
        castleMinePosX = castleWidth;
        castleMinePosY = screenHeight - 2*castleHeight;
        castleEnemyPosX = screenWidth - castleWidth;
        castleEnemyPosY = screenHeight - 2*castleHeight;

        cam = new OrthographicCamera();
        cam.setToOrtho(true, screenWidth, screenHeight);
        Viewport viewport = new FitViewport(screenWidth, screenHeight, cam);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        initObjects(viewport);

        StartGame();

    }

    //dependecy injection inversion of control
    public void StartGame() {

        alliesList = new LinkedList<Unit>();
        enemyList = new LinkedList<Unit>();
        dieing = new LinkedList<Unit>();

        castleMine = Spawner.spawnCastle(castleMinePosX, castleMinePosY,castleWidth,castleHeight);
        castleEnemy = Spawner.spawnCastle(castleEnemyPosX, castleEnemyPosY,castleWidth,castleHeight);

        alliesList.add(castleMine);
        enemyList.add(castleEnemy);

        currentAlliesFront = castleMine;     //target = castle
        currentEnemyFront = castleEnemy;

        allyGold = 30;
        enemyGold = 30;

        allyIncome = 10;
        enemyIncome = 10;

        gameisOn = true;
        output = new String("Game On");
    }

    private void gameOver() {
        buttonSpawnLeft.setTouchable(Touchable.disabled);
        buttonSpawnRight.setTouchable(Touchable.disabled);
        gameisOn = false;
    }

    public void update(float delta) {
        if (gameisOn) {
            AIMove(delta);
            goldIncome(delta);

            setFrontline();
            for (int i = 0; i < alliesList.size(); i++) {
                alliesList.get(i).update(delta, currentEnemyFront);
            }
            for (int i = 0; i < enemyList.size(); i++) {
                enemyList.get(i).update(delta, currentAlliesFront);
            }

            if (!dieing.isEmpty()) {
                for (int i = 0; i < dieing.size(); i++) {
                    if (!dieing.get(i).isDieing()) {
                        dieing.remove(i);
                    }
                }
            }
        }

    }
    //TODO create ally ai? like merging incincrease,spawns,updates,renders

    private void AIMove(float delta) {

        AImoveCD += delta;
        if (AImoveCD >= 1 && enemyGold >= 4*enemyIncome) {
            increaseIncomeEn();
            AImoveCD = 0;
            Gdx.app.log("ColemGame-EnemyAI", "Income Increased");
        }
        if (AImoveCD >= 1 && enemyGold >= 10 && enemyList.size() <= alliesList.size()) {
            Gdx.app.log("ColemGame-EnemyAI", "Knight called");
            spawn(1, 2);
            enemyGold -= 10;
            AImoveCD = 0;
        }
    }

    private void setFrontline() {
        if (currentAlliesFront != null && currentEnemyFront != null) {       //вообще сомнительно, подразумевает, что в начале игры таргеты обязательно кастлы.
            if (castleMine.isDead()) {
                output = "You lose :C";
                gameOver();
            } else {
                if (castleEnemy.isDead()) {
                    output = "You win! :)";
                    gameOver();
                } else {
                    if(currentAlliesFront.isDead())currentAlliesFront = castleMine;
                    if(currentEnemyFront.isDead())currentEnemyFront = castleEnemy;
                    for (int i = 0; i < alliesList.size(); i++) {
                        Unit current = alliesList.get(i);
                        if (current.isDead()) {
                            dieing.add(current);
                        } else {
                            if (current.getX() > currentAlliesFront.getX()) {
                                currentAlliesFront = current;
                            }
                        }
                    }
                    for (int i = 0; i < enemyList.size(); i++) {
                        Unit current = enemyList.get(i);
                        if (current.isDead()) {
                            dieing.add(current);
                        } else {
                            if (current.getX() < currentEnemyFront.getX()) {
                                currentEnemyFront = current;
                            }
                        }
                    }
                    for (int i = 0; i < dieing.size(); i++) {
                        Unit current = dieing.get(i);
                        alliesList.remove(current);
                        enemyList.remove(current);
                    }
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
    private void increaseIncomeAl(){
        if(allyGold >= 30){
            allyGold -= 30;
            allyIncome += 5;
        }
    }
    private void increaseIncomeEn(){
        if(enemyGold >= 30) {
            enemyGold -= 30;
            enemyIncome += 5;
        }
    }
    public void spawn(int spawnType,int spawnSide){
        switch (spawnSide){
            case 1:
                alliesList.add(Spawner.spawnKnight(castleMinePosX,castleMinePosY + castleHeight/2 + 10,false));   // castle size == 200.200
                if(currentAlliesFront == castleMine){
                    currentAlliesFront = alliesList.getLast();
                }
                break;
            case 2:
                enemyList.add(Spawner.spawnKnight(castleEnemyPosX,castleEnemyPosY + castleHeight/2 + 10,true)); //true == enemy
                if(currentEnemyFront == castleEnemy){
                    currentEnemyFront = enemyList.getLast();
                }
                break;
            default:break;

        }
    }



    //rendering
    public void render(float runTime,boolean isPaused) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawBackground();                               //BG and text

        stage.draw();                                   //BUTTONS
        for(int i = 0; i< dieing.size();i++){
            dieing.get(i).draw(batcher, runTime);
        }
        for(int i = 0 ; i < alliesList.size();i++){  //UNITS FURTHER
            alliesList.get(i).draw(batcher, runTime);
        }
        for(int i = 0    ; i < enemyList.size();i++){
            enemyList.get(i).draw(batcher, runTime);
        }
        if(isPaused){

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
            AssetLoader.shadow.draw(batcher, "" + currentEnemyFront.getX(), (screenWidth) - 150, 150);
            AssetLoader.font.draw(batcher, "" + currentEnemyFront.getX(), (screenWidth) - 150, 150);
        }
        if(currentAlliesFront != null) {
            AssetLoader.shadow.draw(batcher, "" + currentAlliesFront.getX(), 100, 150);
            AssetLoader.font.draw(batcher, "" + currentAlliesFront.getX(), 100, 150);
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
        buttonSpawnRight = ColemButtons.createButton(150, screenHeight - 100, 100, 100,AssetLoader.buttonIncome);
        buttonRestart = ColemButtons.createButton(screenWidth/5,100,100,100,AssetLoader.buttonRestart);

        buttonRestart.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                StartGame();

                buttonSpawnLeft.setTouchable(Touchable.enabled);
                buttonSpawnRight.setTouchable(Touchable.enabled);
                return true;
            }
        });

        buttonSpawnRight.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                increaseIncomeAl();
                return true;
            }
        });
        buttonSpawnLeft.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(allyGold >= 10 && System.currentTimeMillis() - btnCooldown > 1000 ) {
                    btnCooldown = System.currentTimeMillis();
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
