package com.crabcorp.gameObjects;

public class Spawner {
    public static com.crabcorp.gameObjects.Units.Unit spawnKnight(
            float poisitionX,
            float positionY,
            boolean isEnemy){
        return new com.crabcorp.gameObjects.Units.Knight(poisitionX,positionY,isEnemy);
    }
    public static com.crabcorp.gameObjects.Units.Castle spawnCastle(
            boolean isEnemy){
        return new com.crabcorp.gameObjects.Units.Castle(isEnemy);
    }
}
