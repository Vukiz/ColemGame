package com.crabcorp.GameWorld;


import com.crabcorp.gameObjects.Castle;
import com.crabcorp.gameObjects.Knight;
import com.crabcorp.gameObjects.Unit;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Евгений on 29.09.2015.
 */
public class GameWorld {
    private Castle castleMine,castleEnemy;
    private LinkedList<Unit> alliesList,enemyList;
    private float screenWidth,screenHeight;
    private Unit currentAlliesFront,currentEnemyFront;


    public GameWorld(float width,float height){
        screenWidth = width;
        screenHeight = height;

        alliesList = new LinkedList<Unit>();
        enemyList = new LinkedList<Unit>();

        castleMine = new Castle(0,screenHeight-300);
        castleEnemy = new Castle(screenWidth-200,screenHeight-300);

        alliesList.add(castleMine);
        enemyList.add(castleEnemy);

        currentAlliesFront = alliesList.getFirst();
        currentEnemyFront = enemyList.getFirst();
    }

    public void update(float delta){
        setFrontline();
        for (Iterator<Unit> i = alliesList.iterator(); i.hasNext();) {
            Unit currentAllie = i.next();
            currentAllie.update(delta,currentEnemyFront);
        }
        for (Iterator<Unit> i = enemyList.iterator(); i.hasNext();) {
            Unit currentEnemy = i.next();
            currentEnemy.update(delta,currentAlliesFront);
        }
    }

    private void setFrontline() {
        if(alliesList.isEmpty()){
            currentAlliesFront = castleMine; //TODO GAMEOVER
        }
        else {
            if (currentAlliesFront.isDead()) {
                alliesList.removeFirst();                      //Is first element deleted????
                currentAlliesFront = alliesList.getFirst();
            }
        }
        if(enemyList.isEmpty()) {
            currentEnemyFront = castleEnemy;    //TODO GAMEOVER
        }
        else {
            if (currentEnemyFront.isDead()) {
                enemyList.removeFirst();                      //Is first element deleted????
                currentEnemyFront = enemyList.getFirst();
            }
        }
    }

    public LinkedList<Unit> getAlliesList() {
        return alliesList;
    }
    public LinkedList<Unit> getEnemyList() {
        return enemyList;
    }

    public void spawn(int spawnType,int spawnSide){
        switch (spawnSide){
            case 1:
                alliesList.add(new Knight(castleMine.getX()+castleMine.getWidth(),castleMine.getY()+100,false)); //false == ally
                break;
            case 2:
                enemyList.add(new Knight(castleEnemy.getX() ,castleEnemy.getY()+100,true)); //true == enemy
            default:break;

        }
    }

    public Castle getCastleMine() {
        return castleMine;
    }
    public Castle getCastleEnemy() {
        return castleEnemy;
    }
}
