package com.bigcustard.planet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.blurp.core.BlurpRuntime;
import com.bigcustard.blurp.model.BlurpMain;
import com.bigcustard.blurp.model.ImageSprite;
import com.bigcustard.blurp.ui.BlurpScreen;
import com.bigcustard.blurp.ui.RenderListener;
import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.plugin.Plugin;
import com.bigcustard.planet.plugin.groovy.GroovyPlugin;
import com.bigcustard.planet.screen.CodingScreen;
import com.bigcustard.planet.screen.GameLibraryDialog;
import com.bigcustard.planet.screen.ResourceManager;
import com.bigcustard.planet.screen.WelcomeScreen;
import com.bigcustard.scene2dplus.dialog.ErrorDialog;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.util.function.Supplier;

public class PlanetApplication extends com.badlogic.gdx.Game {
    private static Plugin PLUGIN = new GroovyPlugin();
    private Viewport viewport;
    private ResourceManager resourceManager;

    @Override
    public void create() {
        viewport = new ScreenViewport();
        resourceManager = new ResourceManager();
        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
        WelcomeScreen welcomeScreen = new WelcomeScreen(viewport, resourceManager);
        configureGameButton(welcomeScreen, welcomeScreen.getNewGameButton(), Game::create);
        configureGameButton(welcomeScreen, welcomeScreen.getContinueGameButton(), Game::mostRecent);
        configureGameLibraryButton(welcomeScreen);
        configureQuitButton(welcomeScreen);
        setScreen(welcomeScreen);
    }

    private void showRunScreen(Game game) {
        BlurpRuntime runtime = BlurpRuntime.begin(viewport);
        BlurpMain script = new BlurpMain() {
            @Override
            public void run() {
                new ImageSprite("games/" + game.name() + "/build/" + game.imageModel().images().get(0).filename(), 300, 150);
            }
        };
        runtime.start(script);
        BlurpScreen blurpScreen = runtime.getScreen();
        setScreen(blurpScreen);
        blurpScreen.setRenderListener((batch, delta, eventType) -> {
            if (eventType == RenderListener.EventType.PostRender) {
                int crossX = viewport.getScreenWidth() - 40;
                int crossY = viewport.getScreenHeight() - 40;

                Drawable closeIcon = resourceManager.getSkin().getDrawable("close");
                closeIcon.draw(batch, crossX, crossY, 32, 32);

                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && clickedOnClose(crossX, crossY)) {
                    runtime.end();
                    showCodingScreen(() -> game);
                }
            }
        });
    }

    private boolean clickedOnClose(int crossX, int crossY) {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        Vector3 world = viewport.getCamera().unproject(new Vector3(x, y, 0));
        return world.x > crossX && world.x < crossX + 32 &&
                world.y > crossY && world.y < crossY + 32;
    }

    private void configureGameLibraryButton(WelcomeScreen welcomeScreen) {
        welcomeScreen.getGameLibraryButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                welcomeScreen.getTable().setVisible(false);
                GameLibraryDialog dialog = new GameLibraryDialog(resourceManager.getSkin());
                dialog.show(welcomeScreen.getStage());
                Futures.addCallback(dialog.getFutureGame(), new FutureCallback<FileHandle>() {
                    @Override
                    public void onSuccess(FileHandle gameFolder) {
                        try {
                            showCodingScreen(() -> Game.from(gameFolder));
                        } catch (Exception e) {
                            new ErrorDialog(resourceManager.getSkin(), e, () ->
                                    welcomeScreen.getTable().setVisible(true)).show(welcomeScreen.getStage());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        System.out.println("Error saving game " + t);
                    }
                });
            }
        });
    }

    private void configureQuitButton(WelcomeScreen welcomeScreen) {
        welcomeScreen.getQuitButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        });
    }

    private void configureGameButton(WelcomeScreen welcomeScreen, TextButton button, Supplier<Game> programSupplier) {
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    showCodingScreen(programSupplier);
                } catch (Exception e) {
                    new ErrorDialog(resourceManager.getSkin(), e, () ->
                            welcomeScreen.getTable().setVisible(true)).show(welcomeScreen.getStage());
                }
            }
        });
    }

    private void showCodingScreen(Supplier<Game> programSupplier) {
        CodingScreen codingScreen = new CodingScreen(
                programSupplier.get(),
                viewport,
                resourceManager,
                this::showWelcomeScreen,
                this::showRunScreen,
                PLUGIN.syntax());
        setScreen(codingScreen);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);
        getScreen().resize(width, height);
    }
}
