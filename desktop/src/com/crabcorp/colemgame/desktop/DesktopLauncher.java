package com.crabcorp.colemgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.crabcorp.colemgame.ColemGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "ColemGame";
		config.width = 480;
		config.height = 960;
		new LwjglApplication(new ColemGame(), config);
	}
}
