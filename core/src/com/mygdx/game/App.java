package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.code.Game;
import com.mygdx.game.screens.CodingScreen;
import com.mygdx.game.screens.WelcomeScreen;

import java.util.function.Supplier;

public class App extends com.badlogic.gdx.Game {
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
