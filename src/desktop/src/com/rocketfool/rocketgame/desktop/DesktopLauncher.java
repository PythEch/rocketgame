package com.rocketfool.rocketgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rocketfool.rocketgame.external.RocketGame;
import com.rocketfool.rocketgame.util.Constants;

/**
 *  Configures and launches the game for desktop. This is the main class to run.
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "RocketGame v1.0";
		config.width = Constants.GAME_WIDTH;
		config.height = Constants.GAME_HEIGHT;
		config.vSyncEnabled = true;
		new LwjglApplication(new RocketGame(), config);
	}
}
