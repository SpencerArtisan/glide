package com.bigcustard.planet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bigcustard.blurp.bootstrap.BlurpConfiguration;
import com.bigcustard.blurp.bootstrap.BlurpRuntime;
import com.bigcustard.blurp.bootstrap.ScriptCompletionHandler;
import com.bigcustard.blurp.core.BlurpState;
import com.bigcustard.blurp.core.BlurpStore;
import com.bigcustard.blurp.ui.MouseWindowChecker;
import com.bigcustard.blurp.ui.RenderListener;
import com.bigcustard.planet.code.Game;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class RunScreen {
    private BlurpRuntime blurpRuntime;
    private Game game;
    private Consumer<Screen> setScreen;
    private Runnable exit;
    private MouseWindowChecker mouseWindowChecker;


    public RunScreen(Skin skin, Game game, Consumer<Screen> setScreen, Runnable exit, MouseWindowChecker mouseWindowChecker) {
        this.game = game;
        this.setScreen = setScreen;
        this.exit = exit;
        this.mouseWindowChecker = mouseWindowChecker;
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
        blurpRuntime.startScript(game.language().scriptEngine(), game.code(), game.name().replace(" ", "_"));
        setScreen.accept(BlurpStore.blurpScreen);
    }

    private void exitGame() {
        BlurpStore.reset();
        blurpRuntime.end();
        BlurpState.reset();
        exit.run();
    }
}
