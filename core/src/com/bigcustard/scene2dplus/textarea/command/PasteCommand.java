package com.bigcustard.scene2dplus.textarea.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.google.common.base.Strings;

public class PasteCommand extends AbstractTextAreaCommand {
    public PasteCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public boolean canExecute() {
        return !Strings.isNullOrEmpty(getClipboard().getContents());
    }

    @Override
    public void execute() {
        if (canExecute()) {
            model.insert(getClipboard().getContents());
        }
    }

    public Clipboard getClipboard() {
        return Gdx.app.getClipboard();
    }
}
