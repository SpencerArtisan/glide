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
import com.bigcustard.blurp.core.BlurpStore;
import com.bigcustard.blurp.ui.RenderListener;
import com.bigcustard.planet.code.Game;

import java.util.function.Consumer;

public class RunScreen {
    private Skin skin;
    private Stage stage;
    private BlurpRuntime blurpRuntime;
    private ImageButton closeButton;
    private Game game;
    private Consumer<Screen> setScreen;
    private Runnable exit;
    private FitViewport viewport;

    public RunScreen(Skin skin, Game game, Consumer<Screen> setScreen, Runnable exit) {
        this.game = game;
        this.setScreen = setScreen;
        this.exit = exit;
        this.skin = skin;
    }

    public void showScreen() {
        viewport = new FitViewport(800, 480);
        BlurpConfiguration config = new BlurpConfiguration(viewport);
        String contentRoot = game.folder().path() + "/build";
        config.setContentRoot(contentRoot);

        blurpRuntime = BlurpRuntime.begin(config);
        blurpRuntime.start(game.language().scriptEngine(), game.code(), game.name().replace(" ", "_"));
        setScreen.accept(BlurpStore.blurpScreen);
        blurpRuntime.onRenderEvent(new RenderListener() {
            @Override
            public void handlePreRenderEvent(float v) {
            }

            @Override
            public void handlePostRenderEvent(Batch batch, float delta) {
                renderStage(batch, delta);
            }
        });
        blurpRuntime.onException(e -> {
            System.err.println(e);
            e.printStackTrace();
            game.setRuntimeError(e);
            exit.run();
        });
    }

    private void renderStage(Batch batch, float delta) {
        if (batch.isDrawing()) batch.end();
        getStage(batch).act(Math.min(delta, 1 / 60f));
        getStage(batch).draw();
    }

    private Stage getStage(Batch batch) {
        if (stage == null) {
            stage = new Stage(new FitViewport(800, 480), batch);
            createCloseButton();
            layoutScreen();
        }
        return stage;
    }

    private void createCloseButton() {
        closeButton = new ImageButton(skin, "close-button");
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitGame();
            }
        });
    }

    private void exitGame() {
        blurpRuntime.end();
        exit.run();
    }

    private void layoutScreen() {
        Table table = new Table();
        table.add(closeButton);
        table.setFillParent(true);
        table.pack();
        table.right().top();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }
}
