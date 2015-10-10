package com.crabcorp.gameObjects;

public class Spawner {
    public static Unit spawnKnight(float poisitionX,float positionY, boolean isEnemy){
        return new Knight(isEnemy?poisitionX-50:poisitionX+180,positionY + 100,isEnemy);
    }
    public static Castle spawnCastle(float positionX,float positionY){
        return new Castle(positionX,positionY);
    }
}
