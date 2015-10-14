package com.crabcorp.buttons;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.crabcorp.cgHelpers.AssetLoader;

/**
 * Created by Евгений on 13.10.2015.
 */
public class ColemButtons  {
    public static Button createButton(float x,float y,float width,float height,TextureRegionDrawable drawable) {
        Button currentBtn = new Button(drawable);
        currentBtn.setBounds(x, y, width, height);
        return currentBtn;
    }
}
