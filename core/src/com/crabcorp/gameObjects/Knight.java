package com.crabcorp.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.crabcorp.cgHelpers.AssetLoader;

public class Knight implements Unit  {
    private Vector2 position;
    private Vector2 velocity;
    public enum DEST  {LEFT,RIGHT};
    public enum STATE {MOVE,ATTACK,STAY,DIE}
    public boolean dead = false;


    private final int width = 80;
    private final int height = 100;
    private final int DMG = 250;
    private final int attackRange = 20;
    public int attackSpeed = 200;
    private int speed = 100;
    private int health = 1000;
    private DEST destination = DEST.RIGHT;
    private STATE currentState = STATE.MOVE;
    private Unit target = null;



    public Knight(float x, float y, boolean isEnemy){
        this.position = new Vector2(x,y);
        this.velocity = new Vector2(this.speed,0);

        if(isEnemy){
            this.velocity.rotate(180);
            this.destination = DEST.LEFT;
        }

    }


    public void update(float delta,Unit gTarget){
        this.target = gTarget;
        if(this.health<= 0){
            this.currentState = STATE.DIE;
        }
        switch (this.currentState){
            case MOVE:
                actionMOVE(delta);
                break;
            case STAY:
                this.velocity = new Vector2(0,0);
                break;
            case ATTACK:
                actionATTACK(delta);
                break;
            case DIE:
                actionDIE();
                break;
        }
    }

    private void actionDIE() {

        this.velocity = new Vector2(0,0);
    }

    private void actionATTACK(float delta) {
        //target.hit(this.DMG);
    }

    private void actionMOVE(float delta) {
        if (this.destination == DEST.RIGHT) {
            if (this.position.x < target.getX() + this.attackRange) {
                this.position.add(velocity.cpy().scl(delta));
            } else {
                this.velocity = new Vector2(0, 0);
                this.currentState = STATE.ATTACK;
            }
        }
        else {
            if (position.x > target.getX() - this.attackRange) {
                this.position.add(velocity.cpy().scl(delta));
            } else {
                this.velocity = new Vector2(0, 0);
                this.currentState = STATE.ATTACK;
            }
        }
    }
    @Override
    public void hit(int damage) {
        this.health -= damage;
    }

    public void draw (Batch batch, float runTime){
        batch.begin();
        switch (this.currentState){
            case MOVE:
                batch.draw(AssetLoader.knightAnimationMoving.getKeyFrame(runTime),
                        this.destination == DEST.RIGHT ? this.position.x : this.position.x + this.width,
                        this.position.y,
                        this.destination == DEST.RIGHT ? this.width : -this.width,
                        this.height);
                break;
            case STAY:
                batch.draw(AssetLoader.knightStand,
                        this.position.x,
                        this.position.y,
                        this.destination == DEST.RIGHT ? this.width : -this.width,
                        this.height);
                break;
            case ATTACK:
                batch.draw(AssetLoader.knightAnimationAttack.getKeyFrame(runTime),
                        this.position.x,
                        this.position.y,
                        this.destination == DEST.RIGHT ? this.width : -this.width,
                        this.height);
                break;
        }


        batch.end();
    }
    public void setPositionX(float x){
        this.position.x = x;
    }
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
    public int getHealth() {
        return this.health;
    }
    @Override
    public boolean isDead() {
        return this.dead;
    }
    public DEST getDestination() {
        return this.destination;
    }
}
