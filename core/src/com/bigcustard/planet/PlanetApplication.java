package com.bigcustard.planet;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.screen.CodingScreen;
import com.bigcustard.planet.screen.ResourceManager;
import com.bigcustard.planet.screen.WelcomeScreen;

import java.util.function.Supplier;

public class PlanetApplication extends com.badlogic.gdx.Game {
    private Viewport viewport;
    private ResourceManager resourceManager;

    @Override
    public void create() {
        viewport = new ScreenViewport();
        resourceManager = new ResourceManager();
        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
        WelcomeScreen welcomeScreen = new WelcomeScreen(viewport, resourceManager);
        configureGameButton(welcomeScreen.getNewGameButton(), Game::create);
        configureGameButton(welcomeScreen.getContinueGameButton(), Game::mostRecent);
        setScreen(welcomeScreen);
    }

    private void showCodingScreen(Supplier<Game> programSupplier) {
        CodingScreen codingScreen = new CodingScreen(programSupplier.get(), viewport, resourceManager, this::showWelcomeScreen);
        setScreen(codingScreen);
    }

    private void configureGameButton(TextButton button, Supplier<Game> programSupplier) {
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showCodingScreen(programSupplier);
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);
        getScreen().resize(width, height);
    }
}
