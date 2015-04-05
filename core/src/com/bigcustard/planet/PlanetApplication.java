package com.bigcustard.planet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
    private Viewport viewport;
    private ResourceManager resourceManager;

    @Override
    public void create() {
        viewport = new ScreenViewport();
        resourceManager = new ResourceManager();
        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
        new WelcomeScreen(viewport, resourceManager.getSkin(), this::setScreen).showWelcomeScreen();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);
        getScreen().resize(width, height);
    }
}
