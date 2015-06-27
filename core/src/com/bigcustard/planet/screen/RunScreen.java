package com.bigcustard.planet.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bigcustard.blurp.bootstrap.BlurpConfiguration;
import com.bigcustard.blurp.bootstrap.BlurpRuntime;
import com.bigcustard.blurp.bootstrap.ScriptCompletionHandler;
import com.bigcustard.blurp.core.BlurpState;
import com.bigcustard.blurp.core.BlurpStore;
import com.bigcustard.blurp.ui.MouseWindowChecker;
import com.bigcustard.planet.code.Game;

import java.util.function.Consumer;

public class RunScreen {
    private BlurpRuntime blurpRuntime;
    private Game game;
    private Consumer<Screen> setScreen;
    private Runnable exit;
    private MouseWindowChecker mouseWindowChecker;
    private ScreenFactory screenFactory;


    RunScreen(Skin skin, Game game, Consumer<Screen> setScreen, Runnable exit, MouseWindowChecker mouseWindowChecker, ScreenFactory screenFactory) {
        this.game = game;
        this.setScreen = setScreen;
        this.exit = exit;
        this.mouseWindowChecker = mouseWindowChecker;
        this.screenFactory = screenFactory;
    }

    public void showScreen() {
        BlurpConfiguration config = new BlurpConfiguration(800, 480);
        ScriptCompletionHandler completionHandler = new ScriptCompletionHandler() {
            @Override
            public void onTerminate() {
                exitGame();
            }
        };
        config.setScriptCompletionHandler(completionHandler);
        String contentRoot = game.folder().path() + "/build";
        config.setContentRoot(contentRoot);

        blurpRuntime = BlurpRuntime.begin(config, mouseWindowChecker);
//        blurpRuntime.onException(e -> {
//            System.err.println(e);
//            e.printStackTrace();
//            game.setRuntimeError(e);
//            exitGame();
//        });
        blurpRuntime.startScript(game.language().scriptEngine(), game.folder().path() + "/" + game.codeFilename());
        setScreen.accept(BlurpStore.blurpScreen);
    }

    private void exitGame() {
        BlurpStore.reset();
        blurpRuntime.end();
        BlurpState.reset();
        exit.run();
    }
}
