package com.rocketfool.rocketgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rocketfool.rocketgame.RocketGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = RocketGame.TITLE + " v" + RocketGame.VERSION;
		config.width = RocketGame.WIDTH;
		config.height = RocketGame.HEIGHT;
		new LwjglApplication(new RocketGame(), config);
	}
}
