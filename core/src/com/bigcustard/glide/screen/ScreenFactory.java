package com.bigcustard.glide.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.glide.code.Game;
import com.bigcustard.glide.code.GameStore;
import com.bigcustard.glide.help.Help;

import java.util.function.Consumer;

public class ScreenFactory {
    private Skin skin;
    private Viewport viewport;
    private Consumer<Screen> setScreen;
    private GameStore gameStore;
    private Help help;

    public ScreenFactory(Viewport viewport, Consumer<Screen> setScreen, Skin skin) {
        this.skin = skin;
        this.viewport = viewport;
        this.setScreen = setScreen;
        this.gameStore = new GameStore();
        this.help = new Help();
    }

    public WelcomeScreen createWelcomeScreen() {
        return new WelcomeScreen(gameStore, viewport, setScreen, this, skin);
    }

    public CodingScreen createCodingScreen(Game game) {
        return new CodingScreen(game, gameStore, help, viewport, setScreen, this, skin);
    }
}