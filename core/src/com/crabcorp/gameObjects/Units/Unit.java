package com.crabcorp.gameObjects.Units;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Евгений on 08.10.2015.
 */
public interface Unit {
    void draw(Batch batch, float runTime);

    float getX();

    float getY();

    boolean isDead();

    void update(float delta, Unit target);

    void hit(int damage);

    void die();

    boolean isDieing();

    void pause();
}
    //TODO Randomize attack system
