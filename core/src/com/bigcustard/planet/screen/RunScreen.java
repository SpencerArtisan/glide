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
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.blurp.bootstrap.BlurpConfiguration;
import com.bigcustard.blurp.bootstrap.BlurpRuntime;
import com.bigcustard.blurp.ui.RenderListener;
import com.bigcustard.planet.code.Game;

import java.util.function.Consumer;

public class RunScreen {
    private Skin skin;
    private Stage stage;
    private Viewport viewport;
    private BlurpRuntime blurpRuntime;
    private ImageButton closeButton;
    private Game game;
    private Consumer<Screen> setScreen;
    private Runnable exit;

    public RunScreen(Viewport viewport, Skin skin, Game game, Consumer<Screen> setScreen, Runnable exit) {
        this.game = game;
        this.viewport = viewport;
        this.setScreen = setScreen;
        this.exit = exit;
        this.skin = skin;
    }

    public void showScreen() {
        BlurpConfiguration config = new BlurpConfiguration(viewport);

        blurpRuntime = BlurpRuntime.begin(config);
        blurpRuntime.start("Groovy", game.code());
        setScreen.accept(blurpRuntime.getScreen());
        blurpRuntime.onRenderEvent(new RenderListener() {
            @Override
            public void handlePreRenderEvent(float v) {

            }

            @Override
            public void handlePostRenderEvent(Batch batch, float delta) {
                renderStage(batch, delta);
            }
        });
    }

    private void renderStage(Batch batch, float delta) {
        if (batch.isDrawing()) batch.end();
        getStage(batch).act(Math.min(delta, 1 / 60f));
        getStage(batch).draw();

    }

    private Stage getStage(Batch batch) {
        if (stage == null) {
            stage = new Stage(viewport, batch);
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
                blurpRuntime.end();
                exit.run();
            }
        });
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
