package com.bigcustard.planet.screen;

import com.badlogic.gdx.Screen;
import com.bigcustard.blurp.bootstrap.BlurpConfiguration;
import com.bigcustard.blurp.bootstrap.BlurpRuntime;
import com.bigcustard.blurp.bootstrap.ScriptCompletionHandler;
import com.bigcustard.blurp.core.BlurpState;
import com.bigcustard.blurp.core.BlurpStore;
import com.bigcustard.blurp.ui.MouseWindowChecker;
import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.GameStore;

import java.util.function.Consumer;

public class RunScreen {
    private BlurpRuntime blurpRuntime;
    private Game game;
    private GameStore gameStore;
    private Consumer<Screen> setScreen;
    private Runnable exit;
    private MouseWindowChecker mouseWindowChecker;

    RunScreen(Game game, GameStore gameStore, Consumer<Screen> setScreen, Runnable exit, MouseWindowChecker mouseWindowChecker) {
        this.game = game;
        this.gameStore = gameStore;
        this.setScreen = setScreen;
        this.exit = exit;
        this.mouseWindowChecker = mouseWindowChecker;
    }

    public void showScreen() {
        gameStore.save(game);
        BlurpConfiguration config = new BlurpConfiguration(800, 480);
        ScriptCompletionHandler completionHandler = new ScriptCompletionHandler() {
            @Override
            public void onTerminate() {
                exitGame();
            }
        };
        config.setScriptCompletionHandler(completionHandler);
        String contentRoot = gameStore.buildFolder(game).path();
        config.setContentRoot(contentRoot);

        blurpRuntime = BlurpRuntime.begin(config, mouseWindowChecker);
//        blurpRuntime.onException(e -> {
//            System.err.println(e);
//            e.printStackTrace();
//            game.runtimeError(e);
//            exitGame();
//        });
        blurpRuntime.startScript(game.language().scriptEngine(), new GameStore().codePathname(game));
        setScreen.accept(BlurpStore.blurpScreen);
    }

    private void exitGame() {
        // TODO - Work with Phil to sort this out!
        BlurpStore.reset();
        blurpRuntime.end();
        BlurpState.reset();
        exit.run();
    }
}
