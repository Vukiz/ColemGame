package com.crabcorp.GameWorld;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crabcorp.buttons.ColemButtons;
import com.crabcorp.cgHelpers.AssetLoader;
import com.crabcorp.gameObjects.Castle;
import com.crabcorp.gameObjects.Spawner;
import com.crabcorp.gameObjects.Unit;

import java.util.LinkedList;

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
    private Button buttonUnpause;
    private Button buttonMenu;
    private Table menuPopup;
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
    private GameState currentState;
    private EventListener StartGameListener;

    public enum GameState {
        PAUSE, GAMEOVER, GAMEON, READY
    }

    public GameWorld(float width, float height) {
        screenWidth = width;
        screenHeight = height;
        //TODO SCALE UNITS SIZE BY PERCENTAGES
        castleWidth = (int) screenWidth / 10;
        castleHeight = (int) screenHeight / 4;
        castleMinePosX = castleWidth;
        castleMinePosY = screenHeight - 2 * castleHeight + screenHeight / 20;
        castleEnemyPosX = screenWidth - castleWidth;
        castleEnemyPosY = screenHeight - 2 * castleHeight + screenHeight / 20;

        cam = new OrthographicCamera();
        cam.setToOrtho(true, screenWidth, screenHeight);
        Viewport viewport = new FitViewport(screenWidth, screenHeight, cam);

        StartGameListener = new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setState(GameState.GAMEON);
                return true;
            }
        };
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        initObjects(viewport);

        output = new String("Tap to start");
        StartGame();

    }

    //dependecy injection inversion of control
    public void StartGame() {

        alliesList = new LinkedList<Unit>();
        enemyList = new LinkedList<Unit>();
        dieing = new LinkedList<Unit>();

        castleMine = Spawner.spawnCastle(castleMinePosX, castleMinePosY, castleWidth, castleHeight);
        castleEnemy = Spawner.spawnCastle(castleEnemyPosX, castleEnemyPosY, castleWidth, castleHeight);

        alliesList.add(castleMine);
        enemyList.add(castleEnemy);

        currentAlliesFront = castleMine;     //target = castle
        currentEnemyFront = castleEnemy;

        allyGold = 30;
        enemyGold = 30;

        allyIncome = 10;
        enemyIncome = 10;

        setState(GameState.READY);

    }

    public void setState(GameState state) {

        switch (state) {
            case PAUSE:
                if (this.currentState != GameState.GAMEOVER) {
                    this.currentState = state;
                    Gdx.app.log("ColemGame-StateChanger", "State changed to PAUSE");
                    output = "Paused";

                    buttonSpawnLeft.setTouchable(Touchable.disabled);
                    buttonSpawnRight.setTouchable(Touchable.disabled);
                    Gdx.app.log("ColemGame-StateChanger", "Buttons : left,right,restart - disabled");
                }
                break;
            case READY:

                this.currentState = state;
                Gdx.app.log("ColemGame-StateChanger", "State changed to READY");
                output = "Tap to start";

                stage.addListener(StartGameListener);
                break;
            case GAMEOVER:
                this.currentState = state;
                Gdx.app.log("ColemGame-StateChanger", "State changed to GAMEOVER");
                output = "Gameover";

                for (Unit i : alliesList) {
                    i.pause();
                }
                for (Unit i : enemyList) {
                    i.pause();
                }
                buttonSpawnLeft.setTouchable(Touchable.disabled);
                buttonSpawnRight.setTouchable(Touchable.disabled);
                break;
            case GAMEON:
                this.currentState = state;
                Gdx.app.log("ColemGame-StateChanger", "State changed to GAMEON");
                output = "Game on";

                stage.removeListener(StartGameListener);
                buttonSpawnLeft.setTouchable(Touchable.enabled);
                buttonSpawnRight.setTouchable(Touchable.enabled);
                break;
        }
    }

    public void update(float delta) {
        switch (currentState) {
            case GAMEON:
                AIMove(delta);
                goldIncome(delta);

                setFrontline();
                for (Unit i : alliesList) {
                    i.update(delta, currentEnemyFront);
                }
                for (Unit i : enemyList) {
                    i.update(delta, currentAlliesFront);
                }

                if (!dieing.isEmpty())
                    for (int i = 0; i < dieing.size(); i++) {
                        if (!dieing.get(i).isDieing()) {
                            dieing.remove(i);
                        }
                    }

                break;
            case PAUSE:
                break;
            case GAMEOVER:
                break;
            case READY:
                break;
        }

    }

    private void AIMove(float delta) {

        AImoveCD += delta;
        if (enemyIncome >= 30) {
            if (AImoveCD >= 1 && enemyGold >= 10) {
                Gdx.app.log("ColemGame-EnemyAI", "Knight called");
                spawn(1, 2);
                enemyGold -= 10;
                AImoveCD = 0;
            }
        } else {
            if (AImoveCD >= 1 && enemyGold >= 10 && enemyList.size() <= alliesList.size()) {
                Gdx.app.log("ColemGame-EnemyAI", "Knight called");
                spawn(1, 2);
                enemyGold -= 10;
                AImoveCD = 0;
            }
            if (AImoveCD >= 1) {
                increaseIncomeEn();
                AImoveCD = 0;
            }
        }
    }

    private void setFrontline() {
        if (currentAlliesFront != null && currentEnemyFront != null) {       //вообще сомнительно, подразумевает, что в начале игры таргеты обязательно кастлы.
            if (castleMine.isDead()) {
                output = "You lose :C";
                setState(GameState.GAMEOVER);
                return;
            }
            if (castleEnemy.isDead()) {
                output = "You win! :)";
                setState(GameState.GAMEOVER);
                return;
            }

            if (currentAlliesFront.isDead()) currentAlliesFront = castleMine;
            if (currentEnemyFront.isDead()) currentEnemyFront = castleEnemy;

            for (Unit i : alliesList) {
                if (i.isDead()) {
                    dieing.add(i);
                } else {
                    if (i.getX() > currentAlliesFront.getX()) {
                        currentAlliesFront = i;
                    }
                }
            }
            for (Unit i : enemyList) {
                if (i.isDead()) {
                    dieing.add(i);
                } else {
                    if (i.getX() < currentEnemyFront.getX()) {
                        currentEnemyFront = i;
                    }
                }
            }
            for (Unit i : dieing) {
                alliesList.remove(i);
                enemyList.remove(i);
            }
        }
    }

    private void goldIncome(float d) {
        timeSinceLastIncome += d;
        while (timeSinceLastIncome >= incomeTime) {
            allyGold += allyIncome;
            enemyGold += enemyIncome;
            timeSinceLastIncome -= incomeTime;
        }
    }

    private void increaseIncomeAl() {
        if (allyGold >= 30) {
            allyGold -= 30;
            allyIncome += 5;
        }
    }

    private void increaseIncomeEn() {
        if (enemyGold >= 30) {
            Gdx.app.log("ColemGame-EnemyAI", "Income Increased");
            enemyGold -= 30;
            enemyIncome += 5;
        }
    }

    public void spawn(int spawnType, int spawnSide) {
        switch (spawnSide) {
            case 1:
                alliesList.add(Spawner.spawnKnight(castleMinePosX, castleMinePosY + castleHeight / 2 + castleHeight / 5, false));   // castle size == 200.200
                if (currentAlliesFront == castleMine) {
                    currentAlliesFront = alliesList.getLast();
                }
                break;
            case 2:
                enemyList.add(Spawner.spawnKnight(castleEnemyPosX, castleEnemyPosY + castleHeight / 2 + castleHeight / 5, true)); //true == enemy
                if (currentEnemyFront == castleEnemy) {
                    currentEnemyFront = enemyList.getLast();
                }
                break;
            default:
                break;

        }
    }

    //rendering
    public void render(float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawBackground();                              //BG and text
        stage.draw();
        switch (currentState) {
            case GAMEON:
            case READY:
            case GAMEOVER:
                for (Unit i : alliesList) {
                    i.draw(batcher, runTime);
                }
                for (Unit i : enemyList) {
                    i.draw(batcher, runTime);
                }
                for (Unit i : dieing) {
                    i.draw(batcher, runTime);
                }
                break;
            case PAUSE:
                for (Unit i : alliesList) {
                    i.draw(batcher, 0);
                }
                for (Unit i : enemyList) {
                    i.draw(batcher, 0);
                }
                for (Unit i : dieing) {
                    i.draw(batcher, 0);
                }
                break;
        }
    }

    private void drawBackground() {
        batcher.begin();

        batcher.draw(AssetLoader.background, 0, 0, screenWidth, screenHeight);

        AssetLoader.shadow.draw(batcher, "" + output, (screenWidth / 2) - 100, 50);
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

    public void initObjects(Viewport v) {

        menuPopup = new Table(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.draw(AssetLoader.menuLayout,
                        menuPopup.getX(), menuPopup.getY(),
                        menuPopup.getWidth(), menuPopup.getHeight());
                super.draw(batch, parentAlpha);

            }
        };
        menuPopup.setBounds(screenWidth / 2 - 150, screenHeight / 2 - 150, 300, 150);

        initButton();
        stage = new Stage(v, batcher);
        stage.addActor(buttonSpawnLeft);
        stage.addActor(buttonSpawnRight);
        stage.addActor(buttonMenu);

        menuPopup.addActor(buttonUnpause);
        menuPopup.addActor(buttonRestart);

    }

    private void initButton() {
        buttonSpawnLeft = ColemButtons.createButton(0, screenHeight - 100, 100, 100, AssetLoader.buttonSpawn);
        buttonSpawnRight = ColemButtons.createButton(150, screenHeight - 100, 100, 100, AssetLoader.buttonIncome);

        buttonMenu = ColemButtons.createButton(screenWidth - 200, 100, 100, 100, AssetLoader.buttonMenu);
        buttonRestart = ColemButtons.createButton(150, 20, 100, 100, AssetLoader.buttonRestart);
        buttonUnpause = ColemButtons.createButton(20,20, 100, 100, AssetLoader.buttonUnpause);
        buttonRestart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ColemGame-Listeners","  restart clicked");
                menuPopup.remove();
                StartGame();
            }
        });
        buttonSpawnRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                increaseIncomeAl();
            }
        });
        buttonSpawnLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (allyGold >= 10 && System.currentTimeMillis() - btnCooldown > 1000) {
                    btnCooldown = System.currentTimeMillis();
                    spawn(1, 1);
                    allyGold -= 10;
                }
            }
        });

        buttonUnpause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ColemGame-Listeners","  unpause clicked");
                setState(GameState.GAMEON);
                menuPopup.remove();
            }
        });
        buttonMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addActor(menuPopup);
                setState(GameState.PAUSE);
            }
        });
    }

        public Stage getStage() {
            return this.stage;
        }


}

