package com.crabcorp.gameObjects;

import com.badlogic.gdx.Gdx;

public class Spawner {
    public static Unit spawnKnight(float poisitionX,float positionY, boolean isEnemy){
        Gdx.app.log("ColemGame-Spawner","Knight Spawned");
        return new Knight(poisitionX,positionY,isEnemy);
    }
    public static Castle spawnCastle(float positionX,float positionY,int width,int height){
        Gdx.app.log("ColemGame-Spawner","Castle Spawned");
        return new Castle(positionX,positionY,width,height);
    }
}
