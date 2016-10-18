package com.bigcustard.glide;

import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.blurp.ui.MouseWindowChecker;
import com.bigcustard.glide.code.GameStore;
import com.bigcustard.glide.screen.ResourceManager;
import com.bigcustard.glide.screen.RuntimeFacade;
import com.bigcustard.glide.screen.ScreenFactory;
import com.bigcustard.glide.screen.WelcomeScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlideApplication extends com.badlogic.gdx.Game {
    private static Logger logger = LoggerFactory.getLogger(GlideApplication.class);

    private Viewport viewport;
    private ResourceManager resourceManager;
    private MouseWindowChecker mouseWindowChecker;

    public GlideApplication(MouseWindowChecker mouseWindowChecker) {
        this.mouseWindowChecker = mouseWindowChecker;
    }

    @Override
    public void create() {
        logger.info("Someone is running Glide!");
        viewport = new ScreenViewport();
        resourceManager = new ResourceManager();
        RuntimeFacade.INSTANCE.initialise(new GameStore(), mouseWindowChecker, this::setScreen);
        showWelcomeScreen();
    }

    @Override
    public void dispose() {
        logger.info("Someone is closing Glide!");
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
