package com.bigcustard.planet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.blurp.core.BlurpRuntime;
import com.bigcustard.blurp.model.BlurpMain;
import com.bigcustard.blurp.model.ImageSprite;
import com.bigcustard.blurp.ui.BlurpScreen;
import com.bigcustard.blurp.ui.RenderListener;
import com.bigcustard.planet.code.Game;

import java.util.function.Consumer;

public class RunScreen {

    public static void showRunScreen(Viewport viewport, Skin skin, Game game, Consumer<Screen> setScreen, Runnable exit) {

        BlurpRuntime runtime = BlurpRuntime.begin(viewport);
        BlurpMain script = new BlurpMain() {
            @Override
            public void run() {
                new ImageSprite("games/" + game.name() + "/build/" + game.imageModel().images().get(0).filename(), 300, 150);
            }
        };
        runtime.start(script);
        BlurpScreen blurpScreen = runtime.getScreen();
        setScreen.accept(blurpScreen);
        blurpScreen.setRenderListener((batch, delta, eventType) -> {
            if (eventType == RenderListener.EventType.PostRender) {
                int crossX = viewport.getScreenWidth() - 40;
                int crossY = viewport.getScreenHeight() - 40;

                Drawable closeIcon = skin.getDrawable("close");
                closeIcon.draw(batch, crossX, crossY, 32, 32);

                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && clickedOnClose(viewport, crossX, crossY)) {
                    runtime.end();
                    exit.run();
                }
            }
        });
    }

    private static boolean clickedOnClose(Viewport viewport, int crossX, int crossY) {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        Vector3 world = viewport.getCamera().unproject(new Vector3(x, y, 0));
        return world.x > crossX && world.x < crossX + 32 &&
                world.y > crossY && world.y < crossY + 32;
    }
}
