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
import com.crabcorp.cgHelpers.AssetLoader;
import com.crabcorp.gameObjects.Castle;
import com.crabcorp.gameObjects.Knight;
import com.crabcorp.gameObjects.Spawner;
import com.crabcorp.gameObjects.Unit;

import java.util.Iterator;
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

    private Button buttonLeft;
    private Button buttonRight;

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

        initObjects(viewport);

        alliesList = new LinkedList<Unit>();
        enemyList = new LinkedList<Unit>();
        dieing = new LinkedList<Unit>();

        castleMine = Spawner.spawnCastle(castleMinePosX,castleMinePosY);
        castleEnemy = Spawner.spawnCastle(castleEnemyPosX,castleEnemyPosY);

        alliesList.add(castleMine);
        enemyList.add(castleEnemy);

        currentAlliesFront = alliesList.getFirst();
        currentEnemyFront = enemyList.getFirst();
    }
    //dependecy injection inversion of control
    //buttons class

    public void update(float delta){
        setFrontline();
        for (Iterator<Unit> i = alliesList.iterator(); i.hasNext();) {
            i.next().update(delta,currentEnemyFront);
        }
        for (Iterator<Unit> i = enemyList.iterator(); i.hasNext();) {
            i.next().update(delta,currentAlliesFront);
        }
        if(!dieing.isEmpty()){
            for (Iterator<Unit> i = dieing.iterator(); i.hasNext();) {
                i.next().update(delta,null);
            }
        }
    }
    private void setFrontline() {
        if(alliesList.isEmpty()){
            currentAlliesFront = castleMine; //TODO GAMEOVER
        }
        else {
            if (currentAlliesFront.isDead()) {
                dieing.add(currentAlliesFront);
                alliesList.remove(currentAlliesFront);                      //Is first element deleted????
                currentAlliesFront = alliesList.getFirst();
            }
        }
        if(enemyList.isEmpty()) {
            currentEnemyFront = castleEnemy;    //TODO GAMEOVER
        }
        else {
            if (currentEnemyFront.isDead()) {
                dieing.add(currentEnemyFront);
                enemyList.remove(currentEnemyFront);                      //Is first element deleted????
                currentEnemyFront = enemyList.getFirst();
            }
        }
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
            default:break;

        }
    }



    //rendering
    public void render(float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawBackground();

        stage.draw();
        alliesList.getFirst().draw(batcher,runTime);
        enemyList.getFirst().draw(batcher, runTime);
        for(int i = 1 ; i < alliesList.size();i++){
            alliesList.get(i).draw(batcher, runTime);
        }
        for(int i = 1 ; i < enemyList.size();i++){
            enemyList.get(i).draw(batcher, runTime);
        }
    }
    private void drawBackground() {
        batcher.begin();

        batcher.draw(AssetLoader.background, 0, 0, screenWidth, screenHeight);

        AssetLoader.shadow.draw(batcher, "My Gold Amount", (screenWidth / 2)- 100, 50);
        AssetLoader.font.draw(batcher, "My Gold Amount", (screenWidth / 2)- 100, 50);

        batcher.end();
    }
    public void initObjects(Viewport v){
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
                spawn(1,2);
                return true;
            }
        });
        buttonLeft.setHeight(100);
        buttonLeft.setWidth(100);
        buttonLeft.setPosition(0, screenHeight - 100);
        buttonLeft.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                spawn(1, 1);
                return true;
            }
        });
    }

    public Stage getStage() {
        return this.stage;
    }




}
