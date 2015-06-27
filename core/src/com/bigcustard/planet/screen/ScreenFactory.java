package com.bigcustard.planet.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.blurp.ui.MouseWindowChecker;
import com.bigcustard.planet.code.Game;

import java.util.function.Consumer;

public class ScreenFactory {
    private Skin skin;
    private Viewport viewport;
    private Consumer<Screen> setScreen;
    private MouseWindowChecker mouseWindowChecker;

    public ScreenFactory(Skin skin, Viewport viewport, Consumer<Screen> setScreen, MouseWindowChecker mouseWindowChecker) {
        this.skin = skin;
        this.viewport = viewport;
        this.setScreen = setScreen;
        this.mouseWindowChecker = mouseWindowChecker;
    }

    public RunScreen createRunScreen(Game game, Runnable exit) {
        return new RunScreen(skin, game, setScreen, exit, mouseWindowChecker, this);
    }

    public WelcomeScreen createWelcomeScreen() {
        return new WelcomeScreen(viewport, skin, setScreen, mouseWindowChecker, this);
    }

    public CodingScreen createCodingScreen(Game game) {
        return new CodingScreen(game, viewport, skin, setScreen, this);
    }
}