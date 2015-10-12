package com.crabcorp.gameObjects;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Created by Евгений on 08.10.2015.
 */
public interface Unit  {
    public void draw (Batch batch, float runTime);
    public float getX();
    public float getY();
    public int getHeight();
    public int getWidth();
    public boolean isDead();
    public void update(float delta,Unit target);
    public void hit(int damage);

    public void die();
    public boolean isDieing();
}
    //TODO get rid of shitty dieing system -_-
    //TODO Randomize attack system
    //TODO actionbuttons???
