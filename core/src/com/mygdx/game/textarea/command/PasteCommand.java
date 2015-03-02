package com.mygdx.game.textarea.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import com.google.common.base.Strings;
import com.mygdx.game.textarea.TextAreaModel;

public class PasteCommand extends AbstractCommand {
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
