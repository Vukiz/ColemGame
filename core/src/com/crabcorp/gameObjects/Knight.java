package com.crabcorp.gameObjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.crabcorp.GameWorld.GameWorld;
import com.crabcorp.cgHelpers.AssetLoader;

public class Knight implements Unit  {
    private Vector2 position;
    private Vector2 velocity;
    public enum DEST  {LEFT,RIGHT};
    public enum STATE {MOVE,ATTACK,DIE,STAY}
    public boolean dead = false;
    public boolean dieing = false;

    private final int width = 80;
    private final int height = 100;
    private final int DMG = 200;
    private final int attackRange = 60;
    public float attackSpeed = 0.5f;

    private int speed = 100;
    private int health = 1000;
    private DEST destination = DEST.RIGHT;
    private STATE currentState = STATE.MOVE;
    private Unit target = null;
    private float attackCooldown = 0;
    private float startTime = 0,currentTimeElapsed = 0,fadeTime = 0;



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
        if(this.target == null){
            this.currentState = STATE.STAY;
        }
        switch (this.currentState){
            case MOVE:
                actionMOVE(delta);
                break;
            case ATTACK:
                actionATTACK(delta);
                break;

        }
    }

    public boolean isDieing(){
        return this.dieing;
    }
    private void actionATTACK(float delta) {
        this.attackCooldown += delta;
        if(this.attackCooldown >= this.attackSpeed) {
            this.target.hit(this.DMG);
            while(this.attackCooldown >= this.attackSpeed){
                this.attackCooldown -= this.attackSpeed;
            }
        }
        if(target.isDead()){
            this.currentState = STATE.MOVE;
            if(this.destination == DEST.RIGHT)this.velocity = new Vector2(100,0);
            else this.velocity = new Vector2(-100,0);
            attackCooldown = 0;
        }
    }

    private void actionMOVE(float delta) {
        //TODO unit synchronization
        if (this.destination == DEST.RIGHT ?
                (this.position.x < target.getX() - this.attackRange) :
                (this.position.x > target.getX() + this.attackRange)) {
            this.position.add(velocity.cpy().scl(delta));
        } else {
            this.velocity = new Vector2(0, 0);
            this.currentState = STATE.ATTACK;
        }
    }
    @Override
    public synchronized void  hit(int damage) {
        this.health -= damage;
        if(this.health <= 0){
            this.die();
            this.currentState = STATE.DIE;
        }
    }

    @Override
    public void die() {
        this.dead = true;
        this.dieing = true;
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

            case ATTACK:
                batch.draw(AssetLoader.knightAnimationAttack.getKeyFrame(runTime),
                        this.destination == DEST.RIGHT ? this.position.x : this.position.x + this.width,
                        this.position.y,
                        this.destination == DEST.RIGHT ? this.width : -this.width,
                        this.height);
                break;
            case DIE:
                if(startTime == 0) startTime = runTime;
                currentTimeElapsed = runTime;

                batch.draw(AssetLoader.dieing.getKeyFrame(runTime),
                        this.destination == DEST.RIGHT ? this.position.x : this.position.x + this.width,
                        this.position.y,
                        this.destination == DEST.RIGHT ? this.width : -this.width,
                        this.height);
                fadeTime += currentTimeElapsed - startTime;
                if(fadeTime >= 5)this.dieing = false;
                startTime = runTime;
                break;
            case STAY:
                batch.draw(AssetLoader.knightStand,
                        this.position.x,
                        this.position.y,
                        this.width,
                        this.height);
        }
        batch.end();
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
    @Override
    public boolean isDead() {
        return this.dead;
    }
    public DEST getDestination() {
        return this.destination;
    }
}
