package com.bigcustard.glide.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.glide.code.Game;
import com.bigcustard.glide.code.GameStore;

import java.util.function.Consumer;

public class ScreenFactory {
    private Skin skin;
    private Viewport viewport;
    private Consumer<Screen> setScreen;
    private GameStore gameStore;

    public ScreenFactory(Viewport viewport, Consumer<Screen> setScreen, Skin skin) {
        this.skin = skin;
        this.viewport = viewport;
        this.setScreen = setScreen;
        this.gameStore = new GameStore();
    }

    public WelcomeScreen createWelcomeScreen() {
        return new WelcomeScreen(gameStore, viewport, setScreen, this, skin);
    }

    public CodingScreen createCodingScreen(Game game) {
        return new CodingScreen(game, gameStore, viewport, setScreen, this, skin);
    }
}