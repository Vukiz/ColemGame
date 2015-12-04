package com.crabcorp.gameObjects.Units;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.crabcorp.cgHelpers.AssetLoader;

/**
 * Created by Евгений on 08.11.2015.
 */
public class Mine extends Actor {
    int value = 20;
    int goldPerIt = 5;
    int width = 50;
    int height = 50;
    public Mine(int x,int y){
        this.setBounds(x,y,width,height);
    }
    public int giveGold() {
        if (value >= goldPerIt) {
            value -= goldPerIt;
            return goldPerIt;
        }
        return 0;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(AssetLoader.knightStand,this.getX(),this.getY(),this.getWidth(),this.getWidth());
    }
}
