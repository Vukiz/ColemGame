package com.crabcorp.colemgame;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.crabcorp.cgHelpers.AssetLoader;
import com.crabcorp.screens.GameScreen;

public class ColemGame extends Game{

	@Override
	public void create() {
		Gdx.app.log("ColemGame", "Created");
		AssetLoader.load();
		setScreen(new GameScreen());


	}

	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}
}
