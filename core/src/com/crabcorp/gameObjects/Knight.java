package com.crabcorp.gameObjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.crabcorp.GameWorld.GameWorld;
import com.crabcorp.cgHelpers.AssetLoader;

public class Knight implements Unit  {
    private Vector2 position;
    private Vector2 velocity;

    public enum DEST  {
        LEFT,RIGHT
    }
    public enum STATE {
        MOVE,ATTACK,DIE,STAY
    }
    private boolean dead = false;  // для проверки мертв ли target
    private boolean dieing = false; //для отрисовки анимации смерти

    private boolean isPaused = false;

    public static final int cost = 5; // стоимость рыцаря

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
        this.setState();
        switch (this.currentState){
            case MOVE:
                actionMOVE(delta);
                break;
            case ATTACK:
                actionATTACK(delta);
                break;
        }
    }
    public void setState() {
        if(this.isPaused){
            this.setStayState();
            return;
        }
        if (this.health <= 0) {
            this.setDieState();
            return;
        }
        if (this.destination == DEST.RIGHT ?
                this.position.x < target.getX() - this.attackRange :
                this.position.x > target.getX() + this.attackRange) {
            this.setMoveState();
            return;
        }
        this.setAttackState();
        return;

    }

    @Override
    public void pause() {
        this.isPaused = true;
    }


    public void setStayState() {
        this.velocity = new Vector2(0, 0);
        this.currentState = STATE.STAY;
    }
    public void setMoveState(){
        this.currentState = STATE.MOVE;
        if(this.destination == DEST.RIGHT)this.velocity = new Vector2(100,0);
        else this.velocity = new Vector2(-100,0);
    }
    public void setAttackState(){
        this.currentState = STATE.ATTACK;
        this.velocity = new Vector2(0, 0);
    }
    public void setDieState() {
        this.dead = true;
        this.dieing = true;
        this.currentState = STATE.DIE;
    }

    private void actionATTACK(float delta) {
        this.attackCooldown += delta;
        if(this.attackCooldown >= this.attackSpeed) {
            this.target.hit(this.DMG);
            while(this.attackCooldown >= this.attackSpeed){
                this.attackCooldown -= this.attackSpeed;
            }
            //TODO ATTACK QUEUE??? if they will gather more than (target.health / this.attack) units they will oneshot them
        }
    }

    private void actionMOVE(float delta) {
        this.position.add(velocity.cpy().scl(delta));
    }
    public void draw (Batch batch, float runTime){
        batch.begin();
        switch (this.currentState){
            case MOVE:
                drawTexture(batch, AssetLoader.knightAnimationMoving.getKeyFrame(runTime), runTime);
                break;

            case ATTACK:
                drawTexture(batch,AssetLoader.knightAnimationAttack.getKeyFrame(runTime),runTime);
                break;
            case DIE:
                if(startTime == 0){
                    startTime = runTime;
                }
                drawTexture(batch, AssetLoader.knightAnimationDieing.getKeyFrame(runTime), runTime);

                currentTimeElapsed = runTime;
                fadeTime += currentTimeElapsed - startTime;
                if(fadeTime >= 4){
                    this.dieing = false;
                    startTime = 0;
                }
                startTime = runTime;
                break;
            case STAY:
                drawTexture(batch, AssetLoader.knightStand, 0);
        }
        batch.end();
    }
    private void drawTexture(Batch batcher, TextureRegion keyFrame,float runTime) {
        batcher.draw(keyFrame,
                this.destination == DEST.RIGHT ? this.position.x : this.position.x + this.width,
                this.position.y,
                this.destination == DEST.RIGHT ? this.width : -this.width,
                this.height);
    }
    public boolean isDieing(){
        return this.dieing;
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
    public DEST getDestination() {
        return this.destination;
    }

    @Override
    public boolean isDead() {
        return this.dead;
    }
    @Override
     public synchronized void  hit(int damage) {
        this.health -= damage;
    }
    @Override
    public void die() {}
}
