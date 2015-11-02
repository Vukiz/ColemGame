package com.crabcorp.gameObjects;

import com.badlogic.gdx.Gdx;

public class Gold {
    private int value ;
    private int income ;
    private final int IncomeCost = 30;
    private final int incomeIncrement = 5;
    private final float incomeTime = 4;
    private float timeSinceLastIncome = 0;
    private int currentGoldChange;
    public Gold(){
        this.value = 30;
        this.income = 10;
        this.currentGoldChange = 0;
    }

    public void tryToIncrease(){
        if(this.value >= IncomeCost ){
            this.increaseGold(-IncomeCost);
            this.increaseIncome(incomeIncrement);

        }
    }
    public int getIncome(){
        return this.income;
    }
    public int getValue(){
        return this.value;
    }
    public void increaseIncome(int value){
        this.income += value;
        Gdx.app.log("ColemGame-GOLD", "Income Increased");
    }
    public void increaseGold(int gold){
        this.value += gold;
        this.currentGoldChange += gold;
    }
    public void goldIncome(float d) {
        timeSinceLastIncome += d;
        while (timeSinceLastIncome >= incomeTime) {
            this.increaseGold(this.income);
            timeSinceLastIncome -= incomeTime;
        }
    }
    public void resetChange(){
        this.currentGoldChange = 0;
    }
    public int getChange() {
        return this.currentGoldChange;
    }
}
