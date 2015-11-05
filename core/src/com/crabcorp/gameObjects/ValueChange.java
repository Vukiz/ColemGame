package com.crabcorp.gameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.crabcorp.cgHelpers.AssetLoader;

/**
 * Created by Евгений on 05.11.2015.
 */
public class ValueChange {
    private final float speed = 100;
    private Vector2 position;
    private Vector2 velocity;
    private int valueToDraw;
    private float removeBorder = 300;

    public ValueChange(int value,float x, float y){
        this.position = new Vector2(x,y);
        this.velocity = new Vector2(0,this.speed);
        this.valueToDraw = value;
    }
    public boolean mustBeRemoved(){
        if (this.position.y > this.removeBorder){
            return true;
        }
        return false;
    }
    public void draw(Batch batcher) {
        batcher.begin();
        AssetLoader.shadow.draw(batcher, "" + valueToDraw,
                this.position.x, this.position.y);
        if(valueToDraw > 0)
        AssetLoader.font.setColor(Color.GREEN);
        else
        AssetLoader.font.setColor(Color.RED);
        AssetLoader.font.draw(batcher, "" + valueToDraw,
                this.position.x, this.position.y);
        AssetLoader.font.setColor(Color.WHITE);
        //TODO UNEFFICIENT>??????????
        batcher.end();
    }
    public void update(float delta) {
        this.position.add(velocity.cpy().scl(delta));
    }
}
