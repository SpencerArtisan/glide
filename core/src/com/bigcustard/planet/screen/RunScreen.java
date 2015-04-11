package com.bigcustard.planet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.blurp.bootstrap.BlurpRunnable;
import com.bigcustard.blurp.bootstrap.BlurpRuntime;
import com.bigcustard.blurp.scripting.ScriptEngineBlurpRunnable;
import com.bigcustard.blurp.ui.BlurpScreen;
import com.bigcustard.blurp.ui.RenderListener;
import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.plugin.Plugin;
import com.bigcustard.planet.plugin.groovy.GroovyPlugin;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.io.StringReader;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
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
        blurpRuntime = BlurpRuntime.begin(viewport);
        blurpRuntime.start("Groovy", game.code());
        setScreen.accept(blurpRuntime.getScreen());
        blurpRuntime.getScreen().setRenderListener((batch, delta, eventType) -> {
            if (eventType == RenderListener.EventType.PostFrame) {
                renderStage(batch, delta);
            }
        });
    }

    private void renderStage(Batch batch, float delta) {
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
