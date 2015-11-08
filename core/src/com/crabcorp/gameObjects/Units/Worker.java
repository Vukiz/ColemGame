package com.crabcorp.gameObjects.Units;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Евгений on 08.11.2015.
 */
public class Worker extends Actor {
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 targetPos;

    public Worker(float spawnX,float spawnY){
        this.position = new Vector2(spawnX,spawnY);
        this.velocity = new Vector2(0,0);
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        if (this.position != this.targetPos) {
            this.position.add(velocity.cpy().scl(delta));
        }

    }
}
