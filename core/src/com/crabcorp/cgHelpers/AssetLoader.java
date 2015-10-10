package com.crabcorp.cgHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class AssetLoader  {
    public static Texture texture,knightTexture;

    public static Animation knightAnimationMoving,knightAnimationAttack;
    public static TextureRegion knightStand,knightMove1,knightMove2,knightMove3,castle;
    public static TextureRegion knightAttack,knightAttack2;
    public static TextureRegionDrawable buttonTexture;
    public static Texture backGround;
    //TODO put all images in one atlas

    public static void load(){
        texture = new Texture(Gdx.files.internal("colemgamepack.png"));
        knightTexture = new Texture(Gdx.files.internal("knighttexture.png"));
        backGround = new Texture(Gdx.files.internal("background.png"));

        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        knightTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        backGround.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        knightStand = new TextureRegion(knightTexture,300,300,300,300);
        knightStand.flip(false, true);

        knightMove1 = new TextureRegion(knightTexture,600,300,300,300);//left
        knightMove1.flip(false, true);

        knightMove2 = new TextureRegion(knightTexture,300,0,300,300);// both
        knightMove2.flip(false,true);

        knightMove3 = new TextureRegion(knightTexture,600,600,300,300);//right
        knightMove3.flip(false, true);

        knightAttack = new TextureRegion(knightTexture,0,600,300,300);//hop
        knightAttack.flip(false, true);

        knightAttack2 = new TextureRegion(knightTexture,600,0,300,300);//bam
        knightAttack2.flip(false, true);

        TextureRegion knightMoving[] = {knightMove1,knightMove2,knightMove3};

        knightAnimationMoving = new Animation(0.2f,knightMoving);
        knightAnimationMoving.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        TextureRegion knightAttacking[] = {knightAttack,knightAttack2};
        //TODO attack animation fix
        knightAnimationAttack = new Animation(0.5f,knightAttacking);
        knightAnimationAttack.setPlayMode(Animation.PlayMode.LOOP);

        castle = new TextureRegion(texture,0,0,200,200);
        castle.flip(false, true);

        buttonTexture = new TextureRegionDrawable(new TextureRegion(texture,200,100,100,100));

    }
    public static void dispose(){
        texture.dispose();
    }
}
