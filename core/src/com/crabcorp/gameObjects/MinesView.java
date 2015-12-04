package com.crabcorp.gameObjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.crabcorp.cgHelpers.AssetLoader;

public class MinesView extends Table {
    Vector2 targetpos;

    public void setTargetpos(float x,float y) {
        this.targetpos = new Vector2(x,y);
    }

    public MinesView(float X,float Y, float gWidth,float gHeight){
        this.setBounds(X, Y, gWidth, gHeight);
        this.setBackground(AssetLoader.minesTexture);
    }

    public Vector2 getTargetpos() {
        return targetpos;
    }
}
