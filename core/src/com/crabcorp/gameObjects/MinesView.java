package com.crabcorp.gameObjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.crabcorp.cgHelpers.AssetLoader;

/**
 * Created by Евгений on 07.11.2015.
 */
public class MinesView extends Table {
    float posX;
    float posY;
    float width;
    float height;
    public MinesView(float X,float Y, float gWidth,float gHeight){
        this.posX = X;
        this.posY = Y;
        this.width = gWidth;
        this.height = gHeight;
        this.setBounds(posX,posY,width,height);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(AssetLoader.minesTexture,this.posX,this.posY,this.width,this.height);
        super.draw(batch, parentAlpha);
    }
}
