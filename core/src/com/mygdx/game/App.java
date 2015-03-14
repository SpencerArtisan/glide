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

        WelcomeScreen welcomeScreen = new WelcomeScreen(viewport, resourceManager);
        configure(welcomeScreen.getNewGameButton(), Game::create);
        configure(welcomeScreen.getContinueGameButton(), Game::mostRecent);

        setScreen(welcomeScreen);
    }

    private void configure(TextButton button, Supplier<Game> programSupplier) {
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(new CodingScreen(programSupplier.get(), viewport, resourceManager));
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
