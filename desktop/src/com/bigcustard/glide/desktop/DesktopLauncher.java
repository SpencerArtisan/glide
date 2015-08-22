package com.bigcustard.glide.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglInput;
import com.bigcustard.glide.PlanetApplication;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglInput.keyRepeatInitialTime = 0.2f;
		LwjglInput.keyRepeatTime = 0.04f;
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "General Language Integrated Development Environment";
        config.width = 1024;
        config.height = 768;
		new LwjglApplication(new PlanetApplication(new LwjglMouseWindowChecker()), config);
	}
}
