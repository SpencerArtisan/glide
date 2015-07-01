package com.bigcustard.planet.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglInput;
import com.bigcustard.planet.PlanetApplication;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Planet Burpl";
        config.width = 1024;
        config.height = 768;
		new LwjglApplication(new PlanetApplication(new LwjglMouseWindowChecker()), config);
	}
}
