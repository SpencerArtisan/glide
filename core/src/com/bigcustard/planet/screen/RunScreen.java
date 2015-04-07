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
        AtomicBoolean alive = new AtomicBoolean(true);

        blurpRuntime.getScreen().setRenderListener((batch, delta, eventType) -> {
            if (!alive.get()) return;

            if (eventType == RenderListener.EventType.PostRender) {
                batch.setColor(1,1,1,1);
                int crossX = viewport.getScreenWidth() - 40;
                int crossY = viewport.getScreenHeight() - 40;

                Drawable closeIcon = skin.getDrawable("close");
                closeIcon.draw(batch, crossX, crossY, closeIcon.getMinWidth(), closeIcon.getMinHeight());

                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && clickedOnClose(viewport, crossX, crossY)) {
                    alive.set(false);
                    System.out.println("END");
                    blurpRuntime.end();
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
