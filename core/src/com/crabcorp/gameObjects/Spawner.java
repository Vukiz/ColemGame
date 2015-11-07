package com.crabcorp.gameObjects;

public class Spawner {
    public static com.crabcorp.gameObjects.Units.Unit spawnKnight(float poisitionX,float positionY, boolean isEnemy){
        return new com.crabcorp.gameObjects.Units.Knight(poisitionX,positionY,isEnemy);
    }
    public static com.crabcorp.gameObjects.Units.Castle spawnCastle(float positionX,float positionY,int width,int height){
        return new com.crabcorp.gameObjects.Units.Castle(positionX,positionY,width,height);
    }
}
