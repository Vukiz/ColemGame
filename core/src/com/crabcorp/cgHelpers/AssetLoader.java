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

    public static Animation knightAnimationMoving,knightAnimationAttack,dieing;

    public static TextureRegion knightStand,knightMove1,knightMove2,knightMove3,castle;
    public static TextureRegion knightAttack,knightAttack2;
    public static TextureRegion background;
    public static TextureRegion dead,fall;


    public static TextureRegionDrawable buttonTexture;
    public static TextureRegionDrawable healthBar;
    public static TextureRegionDrawable healthBaroff;

    public static BitmapFont font;
    public static BitmapFont shadow;

    public static void load(){
        knightTexture = new Texture(Gdx.files.internal("KnightPack.png"));
        backTextures = new Texture(Gdx.files.internal("backgroundpack.png"));


        knightTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        backTextures.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        font = new BitmapFont(Gdx.files.internal("text.fnt"));
        shadow = new BitmapFont(Gdx.files.internal("shadow.fnt"));

        font.getData().setScale(0.6f,-0.6f);
        shadow.getData().setScale(0.6f, -0.6f);

        healthBar = new TextureRegionDrawable(new TextureRegion(backTextures,480,210,100,10));
        healthBaroff = new TextureRegionDrawable(new TextureRegion(backTextures,680,410,100,10));

        dead = new TextureRegion(knightTexture,600,0,300,300);//left
        dead.flip(false, true);


        fall = new TextureRegion(knightTexture,300,73,300,218);//left
        fall.flip(false, true);

        knightStand = new TextureRegion(knightTexture,600, 759,300, 233);
        knightStand.flip(false,true);

        knightMove1 = new TextureRegion(knightTexture,300,291,300,231);//left
        knightMove1.flip(false, true);

        knightMove2 = new TextureRegion(knightTexture,300,755,300,231);// both
        knightMove2.flip(false,true);

        knightMove3 = new TextureRegion(knightTexture,600,528,300,231);//right
        knightMove3.flip(false, true);

        knightAttack = new TextureRegion(knightTexture,0,467,300,258);//hop
        knightAttack.flip(false, true);

        knightAttack2 = new TextureRegion(knightTexture,300,522,300,233);//bam
        knightAttack2.flip(false, true);

        background = new TextureRegion(backTextures,0,100,480,320);

        buttonTexture = new TextureRegionDrawable(new TextureRegion(backTextures,0,0,99,100));

        TextureRegion knightMoving[] = {knightMove1,knightMove2,knightMove3};
        TextureRegion knightDieing[] = {fall,dead};

        dieing = new Animation(9,knightDieing);
        dieing.setPlayMode(Animation.PlayMode.NORMAL);

        knightAnimationMoving = new Animation(0.2f,knightMoving);
        knightAnimationMoving.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        TextureRegion knightAttacking[] = {knightAttack,knightAttack2};

        //TODO attack animation fix

        knightAnimationAttack = new Animation(0.5f,knightAttacking);
        knightAnimationAttack.setPlayMode(Animation.PlayMode.LOOP);

        castle = new TextureRegion(backTextures,480,220,200,200);
        castle.flip(false, true);


    }
    public static void dispose(){
        font.dispose();
        knightTexture.dispose();
        backTextures.dispose();
    }
}
