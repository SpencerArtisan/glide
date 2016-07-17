package com.bigcustard.scene2dplus.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class ErrorDialog extends Dialog {
    private String message;
    private Runnable onClosed;

    public ErrorDialog(String message, Skin skin) {
        super("", skin);
        this.message = message;
        layoutControls();
    }

    public ErrorDialog(Throwable exception, Runnable onClosed, Skin skin) {
        super("", skin);
        this.message = exception.getMessage();
        this.onClosed = onClosed;
        layoutControls();
    }

    @Override
    protected void result(Object object) {
        super.result(object);
        if (onClosed != null) onClosed.run();
    }

    private void layoutControls() {
        Table contentTable = getContentTable();
        contentTable.padTop(20).padLeft(40).padRight(40);
        Label label = new Label(message, getSkin());
        label.setAlignment(Align.center);
        text(label);
        contentTable.row();
        button("Got it");
        getButtonTable().pad(25);
    }
}
