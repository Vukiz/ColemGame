package com.crabcorp.gameObjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.crabcorp.cgHelpers.AssetLoader;

public class Castle implements Unit {
    private Vector2 position;
    private int width = 200;
    private int height = 200;
    private boolean dead = false;
    private int health = 2000;

    public Castle(float x, float y){
        this.position = new Vector2(x,y);

    }

    @Override
    public void hit(int damage) {
        this.health -= damage;
        if(this.health <= 0) this.die();
    }

    @Override
    public void die() {
        this.dead = true;
    }

    public void update(float delta,Unit target){};
    public float getX(){
        return this.position.x;
    }
    public float getY(){
        return this.position.y;
    }
    public int getHeight() {
        return this.height;
    }
    public int getWidth() {
        return this.width;
    }

    @Override
    public boolean isDieing() {
        return this.dead;
    }

    @Override
    public boolean isDead() {
        return this.dead;
    }

    public void draw(Batch batcher,float runTime){
        batcher.begin();
        batcher.draw(AssetLoader.castle, this.position.x, this.position.y + 20, this.width, this.height); // ВЫРАВНИВАЮ С ФОНОМ
        batcher.end();
    }


}
