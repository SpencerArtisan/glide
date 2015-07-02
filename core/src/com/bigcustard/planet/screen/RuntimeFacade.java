package com.bigcustard.planet.screen;

import com.badlogic.gdx.Screen;
import com.bigcustard.blurp.bootstrap.BlurpConfiguration;
import com.bigcustard.blurp.bootstrap.BlurpRuntime;
import com.bigcustard.blurp.bootstrap.ScriptCompletionHandler;
import com.bigcustard.blurp.bootstrap.languages.SupportedLanguage;
import com.bigcustard.blurp.bootstrap.languages.SupportedLanguages;
import com.bigcustard.blurp.core.BlurpState;
import com.bigcustard.blurp.core.BlurpStore;
import com.bigcustard.blurp.ui.MouseWindowChecker;
import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.GameStore;

import java.util.function.Consumer;

public class RuntimeFacade {
    public static RuntimeFacade INSTANCE = new RuntimeFacade();
    private GameStore gameStore;
    private Consumer<Screen> setScreen;
    private BlurpRuntime blurpRuntime;

    private RuntimeFacade() {
    }

    public void initialise(GameStore gameStore, MouseWindowChecker mouseWindowChecker, Consumer<Screen> setScreen) {
        this.gameStore = gameStore;
        this.setScreen = setScreen;
        BlurpConfiguration config = new BlurpConfiguration(800, 480);
        blurpRuntime = BlurpRuntime.begin(config, mouseWindowChecker);
    }


    public void run(Game game, Runnable onExit) {
        saveGame(game);
        updateBlurpConfig(game, onExit);
        updateBlurpExceptionHandler(game);
        startScript(game);
        showBlurpScreen();
    }

    private void saveGame(Game game) {
        gameStore.save(game);
    }

    private void updateBlurpConfig(Game game, final Runnable onExit) {
        BlurpConfiguration config = BlurpStore.configuration;
        config.setContentRoot(gameStore.buildFolder(game).path());
        config.setScriptCompletionHandler(new ScriptCompletionHandler() {
            public void onTerminate() {
                BlurpStore.reset();
                onExit.run();
            }
        });
    }

    private void updateBlurpExceptionHandler(Game game) {
        blurpRuntime.onException(e -> {
            System.err.println(e);
            e.printStackTrace();
            game.runtimeError(e);
        });
    }

    private void startScript(Game game) {
        String fileName = gameStore.codePathname(game);
        SupportedLanguage language = SupportedLanguages.forFile(fileName);
        blurpRuntime.startScriptFile(language, fileName);
    }

    private void showBlurpScreen() {
        setScreen.accept(BlurpStore.blurpScreen);
    }
}
