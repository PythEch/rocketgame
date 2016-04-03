package com.rocketfool.rocketgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rocketfool.rocketgame.external.RocketGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "RocketGame v0.1";
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new RocketGame(), config);
	}
}
