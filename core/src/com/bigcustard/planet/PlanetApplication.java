package com.bigcustard.planet;

import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.blurp.ui.MouseWindowChecker;
import com.bigcustard.planet.code.GameStore;
import com.bigcustard.planet.screen.ResourceManager;
import com.bigcustard.planet.screen.RuntimeFacade;
import com.bigcustard.planet.screen.ScreenFactory;
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
        RuntimeFacade.INSTANCE.initialise(new GameStore(), mouseWindowChecker, this::setScreen);
        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
        ScreenFactory screenFactory = new ScreenFactory(viewport, this::setScreen, resourceManager.getSkin());
        WelcomeScreen welcomeScreen = screenFactory.createWelcomeScreen();
        setScreen(welcomeScreen);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (!RuntimeFacade.INSTANCE.isRuntimeActive()) viewport.update(width, height, true);
    }
}
