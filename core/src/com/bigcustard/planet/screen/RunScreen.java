package com.bigcustard.planet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bigcustard.blurp.bootstrap.BlurpRunnable;
import com.bigcustard.blurp.bootstrap.BlurpRuntime;
import com.bigcustard.blurp.scripting.ScriptEngineBlurpRunnable;
import com.bigcustard.blurp.ui.BlurpScreen;
import com.bigcustard.blurp.ui.RenderListener;
import com.bigcustard.planet.code.Game;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.io.StringReader;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class RunScreen {

    public static void showRunScreen(Viewport viewport, Skin skin, Game game, Consumer<Screen> setScreen, Runnable exit) {
        BlurpRuntime blurpRuntime = BlurpRuntime.begin(viewport);
        blurpRuntime.start("Groovy", game.code());
        setScreen.accept(blurpRuntime.getScreen());

        blurpRuntime.getScreen().setRenderListener((batch, delta, eventType) -> {
            int crossX = viewport.getScreenWidth() - 40;
            int crossY = viewport.getScreenHeight() - 40;

            Drawable closeIcon = skin.getDrawable("close");

            if (eventType == RenderListener.EventType.PostRender) {
                closeIcon.draw(batch, crossX, crossY, closeIcon.getMinWidth(), closeIcon.getMinHeight());
            }

            if (eventType == RenderListener.EventType.PostFrame) {
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) &&
                        clickedOnClose(viewport, crossX, crossY, closeIcon.getMinWidth(), closeIcon.getMinHeight())) {
                    blurpRuntime.end();
                    exit.run();
                }
            }
        });
    }

    private static boolean clickedOnClose(Viewport viewport, int crossX, int crossY, float width, float height) {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        Vector3 world = viewport.getCamera().unproject(new Vector3(x, y, 0));
        return world.x > crossX && world.x < crossX + width &&
                world.y > crossY && world.y < crossY + height;
    }
}
