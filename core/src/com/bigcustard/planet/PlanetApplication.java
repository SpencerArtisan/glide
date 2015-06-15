package com.bigcustard.planet;

import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.blurp.ui.MouseWindowChecker;
import com.bigcustard.planet.screen.ResourceManager;
import com.bigcustard.planet.screen.WelcomeScreen;

public class PlanetApplication extends com.badlogic.gdx.Game {
    private Viewport viewport;
    private ResourceManager resourceManager;
    private MouseWindowChecker mouseWindowChecker;

    public PlanetApplication(MouseWindowChecker mouseWindowChecker) {
        this.mouseWindowChecker = mouseWindowChecker;
    }

    @Override
    public void create() {
        viewport = new ScreenViewport();
        resourceManager = new ResourceManager();
        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
        new WelcomeScreen(viewport, resourceManager.getSkin(), this::setScreen, mouseWindowChecker).showWelcomeScreen();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);
    }
}
