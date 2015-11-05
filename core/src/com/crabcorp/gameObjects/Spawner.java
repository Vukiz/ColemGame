package com.crabcorp.gameObjects;

public class Spawner {
    public static com.crabcorp.gameObjects.Units.Unit spawnKnight(float poisitionX,float positionY, boolean isEnemy){
       // Gdx.app.log("ColemGame-Spawner","Knight Spawned");
        return new com.crabcorp.gameObjects.Units.Knight(poisitionX,positionY,isEnemy);
    }
    public static com.crabcorp.gameObjects.Units.Castle spawnCastle(float positionX,float positionY,int width,int height){
     //   Gdx.app.log("ColemGame-Spawner","Castle Spawned");
        return new com.crabcorp.gameObjects.Units.Castle(positionX,positionY,width,height);
    }
}
