package com.bigcustard.scene2dplus.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ErrorDialog extends Dialog {
    private Exception exception;
    private Runnable onClosed;

    public ErrorDialog(Skin skin, Exception exception, Runnable onClosed) {
        super("", skin);
        this.exception = exception;
        this.onClosed = onClosed;
        layoutControls();
    }

    private void layoutControls() {
        Table contentTable = getContentTable();
        contentTable.padTop(20).padLeft(40).padRight(40);
        text(exception.getMessage());
        contentTable.row();
        button("Got it");
        getButtonTable().pad(25);
    }

    @Override
    protected void result(Object object) {
        super.result(object);
        onClosed.run();
    }
}
