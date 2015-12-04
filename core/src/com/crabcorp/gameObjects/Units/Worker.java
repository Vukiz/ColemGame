package com.crabcorp.gameObjects.Units;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.crabcorp.cgHelpers.AssetLoader;

/**
 * Created by Евгений on 08.11.2015.
 */
public class Worker extends Actor {
    private int velocity;

    public Worker(float spawnX,float spawnY){
        this.velocity = 100;
        this.setBounds(spawnX,spawnY,50,50);

    }

    public void setTargetPos(Vector2 targetPos) {
        this.addAction(Actions.moveTo(targetPos.x,targetPos.y,
                (float)(Math.sqrt(Math.pow(this.getX()-targetPos.x,2) + Math.pow(this.getX()-targetPos.x,2))
                        /this.velocity)));
    }

    @Override

    public void act(float delta) {
        super.act(delta);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(AssetLoader.knightStand,this.getX(),this.getY(),this.getWidth(),this.getWidth());
    }
}
