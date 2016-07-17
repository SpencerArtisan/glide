package com.bigcustard.scene2dplus.button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.bigcustard.scene2dplus.dialog.ErrorDialog;

class ErrorHandler {
    private static String MESSAGE = "Oops.  Something has gone wrong.\r\n\r\nWhatever you did, don't do it again!\r\n\r\nThe guy who wrote this program has been\r\nautomatically electrocuted and ordered\r\nto fix the problem!";

    public static void onClick(Button button, Runnable callback) {
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    callback.run();
                } catch (Exception e) {
                    ErrorDialog errorDialog = new ErrorDialog(MESSAGE, button.getSkin());
                    errorDialog.show(button.getStage());
                }
            }
        });
    }
}
