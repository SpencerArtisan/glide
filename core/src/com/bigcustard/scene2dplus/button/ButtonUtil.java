package com.bigcustard.scene2dplus.button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ButtonUtil {
    public static void onClick(Button button, Runnable process) {
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                process.run();
            }
        });
    }
}
