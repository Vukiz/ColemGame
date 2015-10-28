package com.crabcorp.cgHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class AssetLoader  {
    public static Texture knightTexture;
    public static Texture backTextures;

    public static Animation knightAnimationMoving,knightAnimationAttack,knightAnimationDieing;

    public static TextureRegion knightStand,knightMove1,knightMove2,knightMove3,castle;
    public static TextureRegion knightAttack,knightAttack2;
    public static TextureRegion background;
    public static TextureRegion dead,fall;


    public static TextureRegionDrawable buttonIncome;
    public static TextureRegionDrawable buttonRestart;
    public static TextureRegionDrawable buttonTexture;

    public static BitmapFont font;
    public static BitmapFont shadow;

    public static void load(){
        knightTexture = new Texture(Gdx.files.internal("KnightPack.png"));
        backTextures = new Texture(Gdx.files.internal("backgroundTexture.png"));


        knightTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        backTextures.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        font = new BitmapFont(Gdx.files.internal("text.fnt"));
        shadow = new BitmapFont(Gdx.files.internal("shadow.fnt"));

        font.getData().setScale(0.6f,-0.6f);
        shadow.getData().setScale(0.6f, -0.6f);


        dead = new TextureRegion(knightTexture,303, 605, 300, 300);//left
        dead.flip(false, true);


        fall = new TextureRegion(knightTexture,1,1,300,300);//left
        fall.flip(false, true);

        knightStand = new TextureRegion(knightTexture,303,303,300,300);
        knightStand.flip(false,true);

        knightMove1 = new TextureRegion(knightTexture,605,605,300,300);//left
        knightMove1.flip(false, true);

        knightMove2 = new TextureRegion(knightTexture,303,1,300,300);// both
        knightMove2.flip(false,true);

        knightMove3 = new TextureRegion(knightTexture,605,303,300,300);//right
        knightMove3.flip(false, true);

        knightAttack = new TextureRegion(knightTexture,1, 605, 300, 300);//hop
        knightAttack.flip(false, true);

        knightAttack2 = new TextureRegion(knightTexture,1, 303, 300, 300);//bam
        knightAttack2.flip(false, true);

        background = new TextureRegion(backTextures,1,103,480,320);

        buttonTexture = new TextureRegionDrawable(new TextureRegion(backTextures,1,1,100,100));
        buttonRestart = new TextureRegionDrawable(new TextureRegion(backTextures,103, 1,100, 100));
        buttonIncome = new TextureRegionDrawable(new TextureRegion(backTextures,785,323,100,100));

        TextureRegion knightMoving[] = {knightMove1,knightMove2,knightMove3};
        TextureRegion knightDieing[] = {fall,dead};

        knightAnimationDieing = new Animation(1.0f,knightDieing);
        knightAnimationDieing.setPlayMode(Animation.PlayMode.NORMAL);

        knightAnimationMoving = new Animation(0.2f,knightMoving);
        knightAnimationMoving.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        TextureRegion knightAttacking[] = {knightAttack,knightAttack2};

        //TODO attack animation fix

        knightAnimationAttack = new Animation(0.5f,knightAttacking);
        knightAnimationAttack.setPlayMode(Animation.PlayMode.LOOP);

        castle = new TextureRegion(backTextures,483,123,300,300);
        castle.flip(false, true);


    }
    public static void dispose(){
        font.dispose();
        knightTexture.dispose();
        backTextures.dispose();
    }
}
